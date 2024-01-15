package com.mainproject.be28.member.mapper;

import com.mainproject.be28.member.dto.AuthLoginDto;
import com.mainproject.be28.member.dto.MemberDto;
import com.mainproject.be28.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {

    Member memberPostDtoToMember(MemberDto.PostDto postDto);

    default Member memberPatchDtoToMember(MemberDto.PatchDto patchDto) {
        return Member.builder()
                .name(patchDto.getName())
                .phoneNumber(patchDto.getPhoneNumber())
                .address(patchDto.getAddress())
                .deliveryInformation(patchDto.getDeliveryInformation())
                .build();
    }

    MemberDto.ResponseDto memberToMemberResponseDto(Member member);

    List<MemberDto.ResponseDto> membersToResponses(List<Member> members);

    Member AuthLoginDtoMember(AuthLoginDto authLoginDto);
}
