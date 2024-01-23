package com.mainproject.be28.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mainproject.be28.auth.dto.LoginDto;
import com.mainproject.be28.auth.dto.PrincipalDto;
import com.mainproject.be28.auth.jwt.JwtTokenizer;
import com.mainproject.be28.member.entity.Member;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenizer jwtTokenizer;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JwtTokenizer jwtTokenizer) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenizer = jwtTokenizer;

    }

    // 메서드 내부에서 인증을 시도하는 로직
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            LoginDto loginDto = objectMapper.readValue(request.getInputStream(), LoginDto.class);

            if (loginDto == null) {
                // 클라이언트에서 유효한 JSON 데이터가 아닌 경우에 대한 처리
                throw new AuthenticationServiceException("Invalid JSON data in the request");
            }

            // UsernamePasswordAuthenticationToken을 생성하고 PrincipalDetails를 설정
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());

            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 인증 객체에서 PrincipalDetails를 가져와서 SecurityContextHolder에 설정
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return authentication;
        } catch (IOException e) {
            throw new AuthenticationServiceException("Error reading JSON data from the request", e);
        }
    }


    // 클라이언트의 인증 정보를 이용해 인증에 성공할 경우 호출
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        Member member = (Member) authResult.getPrincipal();

        String accessToken = delegateAccessToken(member);
        String refreshToken = delegateRefreshToken(member);


//    헤더에 토큰 실어서 보내기
        response.setHeader("Authorization", "Bearer " + accessToken);
        response.setHeader("Refresh", refreshToken);

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        this.getSuccessHandler()
                .onAuthenticationSuccess(request, response, authResult); // 핸들러 불러옴 (실패 핸들러는 자동호출됨)
    }


    private String delegateAccessToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
        PrincipalDto principal = PrincipalDto.builder().id(member.getMemberId()).email(member.getEmail())
                .name(member.getName()).roles(String.join(",", member.getRoles())) // 리스트를 쉼표로 구분된 문자열로 결합
                .build();
        claims.put("email", member.getEmail());
        claims.put("name",member.getName());
        claims.put("roles", member.getRoles());
        claims.put("principal", principal);
        log.info("###### principal = {} ", principal);


        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(
                jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration,
                base64EncodedSecretKey);

        return accessToken;
    }


    private String delegateRefreshToken(Member member) {
        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(
                jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration,
                base64EncodedSecretKey);

        return refreshToken;
    }

}