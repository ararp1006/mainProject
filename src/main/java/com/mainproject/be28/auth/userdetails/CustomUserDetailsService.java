package com.mainproject.be28.auth.userdetails;

import com.mainproject.be28.auth.utils.CustomAuthorityUtils;
import com.mainproject.be28.exception.BusinessLogicException;
import com.mainproject.be28.exception.ExceptionCode;
import com.mainproject.be28.member.entity.Member;
import com.mainproject.be28.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository userRepository;
    private final CustomAuthorityUtils authorityUtils;

    public CustomUserDetailsService(MemberRepository memebrRepository,
                                    CustomAuthorityUtils authorityUtils) {
        this.userRepository = memebrRepository;
        this.authorityUtils = authorityUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalUser = userRepository.findByEmail(username);
        Member findUser = optionalUser.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));

        return new PrincipalDetails(findUser,authorityUtils);
    }


}