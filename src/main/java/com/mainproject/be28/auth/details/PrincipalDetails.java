package com.mainproject.be28.auth.details;

import com.mainproject.be28.auth.utils.CustomAuthorityUtils;
import com.mainproject.be28.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Map;

public class PrincipalDetails extends Member implements UserDetails {
    private final CustomAuthorityUtils authorityUtils;
    private Map<String, Object> attributes;

    public PrincipalDetails(Member member, CustomAuthorityUtils authorityUtils) {
        super(member);
        this.authorityUtils = authorityUtils;
    }

    public PrincipalDetails(Member member, CustomAuthorityUtils authorityUtils, Map<String, Object> attributes) {
        super(member);
        this.authorityUtils = authorityUtils;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityUtils.createAuthorities(this.getRoles());
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}