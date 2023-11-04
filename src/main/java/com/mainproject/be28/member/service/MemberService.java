package com.mainproject.be28.member.service;


import com.mainproject.be28.auth.jwt.JwtTokenizer;
import com.mainproject.be28.auth.utils.CustomAuthorityUtils;
import com.mainproject.be28.exception.BusinessLogicException;
import com.mainproject.be28.exception.ExceptionCode;
import com.mainproject.be28.member.entity.Member;
import com.mainproject.be28.member.excption.MemberException;
import com.mainproject.be28.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
@Component
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthorityUtils authorityUtils;
    private final JwtTokenizer jwtTokenizer;


    public Member createMember(Member member) {
        verifyExistEmail(member.getEmail());
        member.setName(verifyExistName(member.getName()));   //중복되는 이름 확인 후 중복되는 이름이 있을 시 뒤에 0~9999까지 번호를 붙여서 이름 저장

        // (3) 추가: Password 암호화
        String encryptedPassword = passwordEncoder.encode(member.getPassword());
        member.setPassword(encryptedPassword);

        // (4) 추가: DB에 User Role 저장
        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);

        Member savedMember = memberRepository.save(member);

        return savedMember;
    }

   /* public Member uploadImage(long memberId, MultipartFile imageFile) {
        Member findMember = findVerifiedMember(memberId);
        String fileUrl = fileStorageService.storeFile(imageFile);
        findMember.setImage(fileUrl);

        return memberRepository.save(findMember);
    }*/

    public Member createMemberOAuth2(Member member) {

        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);
        String newName = verifyExistName(member.getName());
        member.setName(newName);

        return memberRepository.save(member);
    }

    public Member findMember(long memberId) {
        return findVerifiedMember(memberId);
    }

    public Page<Member> findMembers(int page, int size) {
        return memberRepository.findAll(PageRequest.of(page, size,
                Sort.by("memberId").descending()));
    }


    @Transactional
    public Member updateMember(Long loginId, Member member) {

        verifyPermission(loginId, member.getMemberId());

        Member findMember = findVerifiedMember(member.getMemberId());
        if (member.getName() != findMember.getName()) {                   //수정하려는 이름과 기존 이름이 다를 경우 수정하는 이름이 중복되는지 체크후 중복시 추가숫자를 덧붙여 이름수정
            findMember.setName(verifyExistName(member.getName()));
        }

        return findMember;
    }

    public Member updateActiveStatus(long memberId) {
        Member findMember = findVerifiedMember(memberId);
        findMember.setStatus(Member.Status.MEMBER_ACTIVE);

        return findMember;
    }

    public Member updateDeleteStatus(long memberId) {
        Member findMember = findVerifiedMember(memberId);

        findMember.setStatus(Member.Status.MEMBER_DELETE);
        return findMember;
    }

    public void deleteMember(Long loginId, long memberId) {
        verifyPermission(loginId, memberId);

        Member findMember = findVerifiedMember(memberId);

        memberRepository.delete(findMember);
    }


    public Member findVerifiedMember(long memberId) {
        Optional<Member> optionalMember = memberRepository.findById(memberId);
        Member findMember = optionalMember.orElseThrow(() ->
                new BusinessLogicException(MemberException.MEMBER_NOT_FOUND));


        return findMember;
    }

    public Member findVerifiedMember(String email) {
        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        Member findMember = optionalMember.orElseThrow(() ->
                new BusinessLogicException(MemberException.MEMBER_NOT_FOUND));


        return findMember;
    }

    private void verifyExistEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent())
            throw new BusinessLogicException(MemberException.MEMBER_EXISTS);
    }

    public Boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    private String verifyExistName(String name) {     // oauth2로 로그인 했을 때 같은 이름이 있을 때 1~1000까지의 랜덤숫자를 붙임
        String newName = name;
        Optional<Member> optionalMember = memberRepository.findByName(name);
        if (optionalMember.isPresent()) {
            Random random = new Random();
            int randomNumber = random.nextInt(10000) + 1;
            newName = name + randomNumber;
        }

        return newName;
    }

    public void verifyPermission(Long loginId, long memeberId) {
        Member findMember = findVerifiedMember(loginId);
        if (!findMember.getRoles().contains("ADMIN")) {
            if (loginId != memeberId) {
                throw new BusinessLogicException(MemberException.NO_PERMISSION_EDITING_POST);
            }
        }
    }

    public String delegateAccessToken(Member member) {//엑세스토큰 생성
        Map<String, Object> claims = new HashMap<>();
        claims.put("memberId", member.getMemberId());
        claims.put("roles", member.getRoles());

        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration, base64EncodedSecretKey);

        return accessToken;
    }


    public String delegateRefreshToken(Member member) {//리프레시 토큰 생성


        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration, base64EncodedSecretKey);

        return refreshToken;
    }
    public Long findTokenMemberId() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new BusinessLogicException(MemberException.MEMBER_NOT_FOUND));
        return member.getMemberId();
    }
    public Member findTokenMember() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return memberRepository.findByEmail(email).orElseThrow(() -> new BusinessLogicException(MemberException.MEMBER_NOT_FOUND)) ;
    }

    //현재 로그인한 회원정보 접근 시 회원 이메일/비밀번호 한번더 검증
    public Member verifyEmailPassword(String email, String password) {
        Member currentMember = findTokenMember();
        String currentEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // 현재 로그인되어 있는 회원의 메일
        Member member = memberRepository.findByEmail(currentEmail).orElseThrow(() -> new BusinessLogicException(MemberException.VERIFY_FAILURE));
        boolean matchMember = member.equals(currentMember);
        boolean matchEmail = email.equals(currentEmail); //실제 이메일과 입력한 이메일이 일치하는지
        boolean matchPassword = passwordEncoder.matches(password, member.getPassword());
        if(!matchMember||!matchEmail||!matchPassword){
            throw new BusinessLogicException(MemberException.VERIFY_FAILURE); // 둘 중하나라도 다르다면 인증 실
        }
        return member;
    }
}
