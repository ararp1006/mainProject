package com.mainproject.be28.member.mapper;

import com.mainproject.be28.member.dto.AuthLoginDto;
import com.mainproject.be28.member.dto.MemberDto;
import com.mainproject.be28.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {

    Member postToMember(MemberDto.PostDto postDto);


    Member patchToMember(MemberDto.PatchDto patchDto);

    MemberDto.ResponseDto memberToResponse(Member member);

    List<MemberDto.ResponseDto> membersToResponses(List<Member> members);

    @Mapping(source = "profileimg", target = "image")
    Member AuthLoginDtoMember(AuthLoginDto authLoginDto);
}