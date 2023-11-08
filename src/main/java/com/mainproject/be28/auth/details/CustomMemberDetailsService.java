package com.mainproject.be28.auth.details;

import com.mainproject.be28.auth.utils.CustomAuthorityUtils;
import com.mainproject.be28.exception.BusinessLogicException;
import com.mainproject.be28.member.data.MemberStatus;
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
        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        Member findMember = optionalMember.orElseThrow(() -> new BusinessLogicException(MemberException.MEMBER_NOT_FOUND));

        // 멤버 상태가 MEMBER_TMP이면 BusinessLogicException을 발생시켜 로그인을 막습니다.
        if (findMember.getStatus() == MemberStatus.MEMBER_TMP) {
            throw new BusinessLogicException(MemberException.NOT_YET_AUTHENTICATE_EMAIL);
        }

        return new PrincipalDetails(findMember,authorityUtils);
    }
}




