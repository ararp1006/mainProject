package com.mainproject.be28.member.data;

import lombok.Getter;

public enum MemberStatus {
    MEMBER_TMP(1, "임시 회원"), MEMBER_ACTIVE(2, "활동 회원"),
    MEMBER_DELETE(3, "탈퇴 회원"), MEMBER_GOOGLE(4, "구글 회원"),
    MEMBER_ADMIN(5,"관리자");

    @Getter
    private int index;
    @Getter
    private String status;

    MemberStatus(int index, String status) {
        this.index = index;
        this.status = status;
    }
}