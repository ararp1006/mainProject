package com.mainproject.be28.auth.details;

import com.mainproject.be28.auth.userdetails.PrincipalDetails;
import com.mainproject.be28.auth.utils.CustomAuthorityUtils;
import com.mainproject.be28.exception.BusinessLogicException;
import com.mainproject.be28.member.entity.Member;
import com.mainproject.be28.member.excption.MemberException;
import com.mainproject.be28.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CustomMemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final CustomAuthorityUtils authorityUtils;

    public CustomMemberDetailsService(MemberRepository memberRepository,
                                    CustomAuthorityUtils authorityUtils) {
        this.memberRepository = memberRepository;
        this.authorityUtils = authorityUtils;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalUser = memberRepository.findByEmail(username);
        Member findUser = optionalUser.orElseThrow(() -> new BusinessLogicException(MemberException.MEMBER_NOT_FOUND));

        return new PrincipalDetails(findUser,authorityUtils);
    }


}

