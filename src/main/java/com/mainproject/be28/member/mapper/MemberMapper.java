package com.mainproject.be28.member.mapper;

import com.mainproject.be28.image.entity.MemberImage;
import com.mainproject.be28.member.dto.AuthLoginDto;
import com.mainproject.be28.member.dto.MemberDto;
import com.mainproject.be28.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {

    Member postToMember(MemberDto.PostDto postDto);


    Member patchToMember(MemberDto.PatchDto patchDto);

    MemberDto.ResponseDto memberToResponse(Member member);

    List<MemberDto.ResponseDto> membersToResponses(List<Member> members);

    Member AuthLoginDtoMember(AuthLoginDto authLoginDto);
}
