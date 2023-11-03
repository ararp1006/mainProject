package com.mainproject.be28.member.controller;

import com.mainproject.be28.auth.jwt.JwtTokenizer;
import com.mainproject.be28.auth.loginResolver.LoginMemberId;
import com.mainproject.be28.member.dto.AuthLoginDto;
import com.mainproject.be28.member.dto.MemberDto;
import com.mainproject.be28.member.mapper.MemberMapper;
import com.mainproject.be28.member.service.MemberService;
import com.mainproject.be28.response.DataResponseDto;
import com.mainproject.be28.response.MultiResponseDto;
import com.mainproject.be28.response.MultiResponseDtos;
import com.mainproject.be28.response.SingleResponseDto;
import com.mainproject.be28.utils.UriCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.mainproject.be28.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import org.springframework.data.domain.Page;


@RestController
@RequestMapping("/members")
@Validated
@Slf4j
@RequiredArgsConstructor
public class MemberController {
    private final static String MEMBER_DEFAULT_URL = "/members";
    private final MemberService memberService;
    private final MemberMapper mapper;

    private final JwtTokenizer jwtTokenizer;


    @PostMapping("/signup")
    public ResponseEntity postMember(@Valid @RequestBody MemberDto.PostDto requestBody) {
        Member member = mapper.postToMember(requestBody);

        Member createdMember = memberService.createMember(member);
        URI location = UriCreator.createUri(MEMBER_DEFAULT_URL, createdMember.getMemberId());

        return ResponseEntity.created(location).build();
    }
    //  # 프론트엔드에서 네이버유저의 아이디와 비번을 알려주면 그걸 이용해 회원가입을 하고 토큰발급하는 방식
    @PostMapping("/oauth/signup/naver")
    public ResponseEntity oAuth2LoginNaver(@RequestBody @Valid AuthLoginDto requesBody) {
        log.info("### oauth2 login start! ###");
        String accessToken = "";
        String refreshToken = "";
        String memberId = "";
        Member member = mapper.AuthLoginDtoMember(requesBody);
        member.setEmail(member.getEmail()+"2");   //Naver 유저인 경우 이메일 뒤에 2를 붙임

        if(!memberService.existsByEmail(member.getEmail())) {
            member = memberService.createMemberOAuth2(member);
        } else {
            member = memberService.findVerifiedMember(member.getEmail());
        }

        accessToken = memberService.delegateAccessToken(member);
        refreshToken = memberService.delegateRefreshToken(member);
        memberId = String.valueOf(member.getMemberId());
        return ResponseEntity.ok().header("Authorization", "Bearer " + accessToken)
                .header("Refresh", refreshToken)
                .header("MemberId", memberId).build();
    }

    @PostMapping("/oauth/signup/kakao")
    public ResponseEntity oAuth2LoginKakao(@RequestBody @Valid AuthLoginDto requesBody) {
        log.info("### oauth2 login start! ###");
        String accessToken = "";
        String refreshToken = "";
        String memberId = "";
        Member member = mapper.AuthLoginDtoMember(requesBody);
        member.setEmail(member.getEmail()+"3");
        if(!memberService.existsByEmail(member.getEmail())) {
            member = memberService.createMemberOAuth2(member);
        } else {
            member = memberService.findVerifiedMember(member.getEmail());
        }

        accessToken = memberService.delegateAccessToken(member);
        refreshToken = memberService.delegateRefreshToken(member);
        memberId = String.valueOf(member.getMemberId());
        return ResponseEntity.ok().header("Authorization", "Bearer " + accessToken)
                .header("Refresh", refreshToken)
                .header("MemberId", memberId).build();
    }

    @GetMapping("/info")
    public ResponseEntity getMemberInfo(@LoginMemberId Long memberId){
        Member findMember = memberService.findMember(memberId);
        MemberDto.ResponseDto response = mapper.memberToResponse(findMember);

        return new ResponseEntity<>(
                new DataResponseDto<>(response), HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String jws = authorizationHeader.substring(7);    // "Bearer " 이후의 토큰 문자열 추출

        jwtTokenizer.addToTokenBlackList(jws);     //블랙리스트에 jws 추가, 접근 막음

        return ResponseEntity.ok().body("Successfully logged out");
    }

    /*@PostMapping("/image")
    public ResponseEntity postImage(@LoginMemberId Long memberId,
                                    @RequestParam("imageFile") MultipartFile imageFile) {
        Member savedMember = memberService.uploadImage(memberId, imageFile);
        MemberDto.ResponseDto response = mapper.memberToResponse(savedMember);

        return new ResponseEntity<>((response),HttpStatus.OK);
    }*/


    @GetMapping("/{member-id}")
    public ResponseEntity getMember(@PathVariable("member-id") @Positive long memberId){
        Member findMember = memberService.findMember(memberId);
        MemberDto.ResponseDto response = mapper.memberToResponse(findMember);
        return new ResponseEntity<>(
                new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getMembers(@Positive @RequestParam(value = "page", defaultValue = "1") int page,
                                     @Positive @RequestParam(value = "size", defaultValue = "20") int size){
        Page<Member> pageMember = memberService.findMembers(page -1, size);
        List<Member> members = pageMember.getContent();
        List<MemberDto.ResponseDto> response = mapper.membersToResponses(members);

        return new ResponseEntity<>(
                new MultiResponseDtos<>(response, pageMember), HttpStatus.OK);
    }


    @PatchMapping("/{member-id}")
    public ResponseEntity patchMember(@PathVariable("member-id") @Positive long memberId,
                                      @LoginMemberId Long loginId,
                                      @Valid @RequestBody MemberDto.PatchDto requestBody){

        Member member = mapper.patchToMember(requestBody);
        member.setMemberId(memberId);
        Member updatedMember = memberService.updateMember(loginId,member); //검증을 위한 메서드포함, loginId가 관리자가 아닌이상 memeberId와 다를 경우 권한없다는 오류 발생
        MemberDto.ResponseDto response = mapper.memberToResponse(updatedMember);

        return new ResponseEntity<>(
                new SingleResponseDto<>(response), HttpStatus.OK);
    }

    @PatchMapping("/status/active/{member-id}")     // Status를 Active로 변경
    public ResponseEntity patchStatusActive(@PathVariable("member-id") @Positive long memberId){
        Member activeMember =memberService.updateActiveStatus(memberId);

        MemberDto.ResponseDto response = mapper.memberToResponse(activeMember);

        return new ResponseEntity<>((response), HttpStatus.OK);
    }

    @PatchMapping("/status/delete/{member-id}")   // Status를 delete로 변경(로그인 못하게 막음)
    public ResponseEntity patchStatusDelete(@PathVariable("member-id") @Positive long memberId){

        Member deleteMember  = memberService.updateDeleteStatus(memberId);

        MemberDto.ResponseDto response = mapper.memberToResponse(deleteMember);

        return new ResponseEntity<>((response), HttpStatus.OK);
    }

    @DeleteMapping("/{member-id}")    //Member 삭제
    public ResponseEntity deleteMember(@PathVariable("member-id") @Positive long memberId,
                                       @LoginMemberId Long loginId){


        memberService.deleteMember(loginId,memberId); //검증을 위한 메서드포함, loginId가 관리자가 아닌이상 memeberId와 다를 경우 권한없다는 오류 발생

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}