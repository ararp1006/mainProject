package com.mainproject.be28.member.data;

import lombok.Getter;

public enum MemberRole {
    ROLE_USER(1, "일반 회원"), ROLE_ADMIN(2, "관리자");

    @Getter
    private int index;
    @Getter
    private String desc;

    MemberRole(int index, String desc) {
        this.index = index;
        this.desc = desc;
    }
}

