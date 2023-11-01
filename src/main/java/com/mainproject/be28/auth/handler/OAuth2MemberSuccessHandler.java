package com.mainproject.be28.auth.handler;

import com.mainproject.be28.auth.jwt.JwtTokenizer;
import com.mainproject.be28.auth.userdetails.MemberAuthority;
import com.mainproject.be28.member.entity.Member;
import com.mainproject.be28.member.service.MemberService;
import io.jsonwebtoken.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.rmi.ServerException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


