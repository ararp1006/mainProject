package com.mainproject.be28.member.controller;

import com.mainproject.be28.auth.jwt.JwtTokenizer;
import com.mainproject.be28.exception.BusinessLogicException;
import com.mainproject.be28.image.entity.ImageInfo;
import com.mainproject.be28.member.dto.AuthLoginDto;
import com.mainproject.be28.member.dto.MemberDto;
import com.mainproject.be28.member.dto.PasswordPatchDto;
import com.mainproject.be28.member.excption.MemberException;
import com.mainproject.be28.member.mapper.MemberMapper;
import com.mainproject.be28.member.service.MailService;
import com.mainproject.be28.member.service.MemberService;
import com.mainproject.be28.utils.UriCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.mainproject.be28.member.entity.Member;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.Principal;
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
    private final MailService mailService;
    private final JwtTokenizer jwtTokenizer;

    // 회원 가입
    @PostMapping("/signup")
    public ResponseEntity createMEMBER(@RequestBody @Valid MemberDto.PostDto postDto)
            throws MessagingException, UnsupportedEncodingException {
        log.info("##### CREATE MEMBER #####");

        Member member = mapper. memberPostDtoToMember(postDto);
        Member savedMember = memberService.createMember(member);
        URI location = UriCreator.createUri(MEMBER_DEFAULT_URL, savedMember.getMemberId());

        mailService.sendEmail(savedMember.getEmail(), savedMember.getMailKey(), savedMember.getMemberId());

        return ResponseEntity.created(location).build();
    }
    // Oauth 네이버
    @PostMapping("/oauth/signup/naver")
    public ResponseEntity oAuth2LoginNaver(@RequestBody @Valid AuthLoginDto dto) {
        log.info("### oauth2 login start! ###");
        String accessToken = "";
        String refreshToken = "";
        Member member = mapper.AuthLoginDtoMember(dto);

        if(!memberService.existsByEmail(member.getEmail())) {
            member = memberService.createMemberOAuth2(member);
        } else {
            member = memberService.checkMemberExist(member.getEmail());
        }

        accessToken = memberService.delegateAccessToken(member);
        refreshToken = memberService.delegateRefreshToken(member);
        return ResponseEntity.ok().header("Authorization", "Bearer " + accessToken)
                .header("Refresh", refreshToken).build();
    }
    // Oauth 카카오
    @PostMapping("/oauth/signup/kakao")
    public ResponseEntity oAuth2LoginKakao(@RequestBody @Valid AuthLoginDto dto) {
        log.info("### oauth2 login start! ###");
        String accessToken = "";
        String refreshToken = "";

        Member member = mapper.AuthLoginDtoMember(dto);
        if(!memberService.existsByEmail(member.getEmail())) {
            member = memberService.createMemberOAuth2(member);
        } else {
            member = memberService.checkMemberExist(member.getEmail());
        }

        accessToken = memberService.delegateAccessToken(member);
        refreshToken = memberService.delegateRefreshToken(member);
        return ResponseEntity.ok().header("Authorization", "Bearer " + accessToken)
                .header("Refresh", refreshToken).build();
    }
    // Oauth 구글
    @PostMapping("/oauth/signup/gogle")
    public ResponseEntity oAuth2Login(@RequestBody @Valid AuthLoginDto dto) {
        log.info("### oauth2 login start! ###");
        String accessToken = "";
        String refreshToken = "";

        Member member = mapper.AuthLoginDtoMember(dto);
        if (!memberService.existsByEmail(member.getEmail())) {
            member = memberService.authMemberSave(member);
        } else {
            member = memberService.checkMemberExist(member.getEmail());
        }

        accessToken = memberService.delegateAccessToken(member);
        refreshToken =memberService.delegateRefreshToken(member);
        return ResponseEntity.ok().header("Authorization", "Bearer " + accessToken)
                .header("Refresh", refreshToken).build();
    }

    //회원 삭제
    @DeleteMapping
    public ResponseEntity deleteUser(
            Principal principal) {
        log.info("############" + principal.getName());
        String email = principal.getName();
        log.info("##### DELETE USER #####");
        log.info("### MEMBER EMAIL = {}", email);
        memberService.deleteMember(email);

        return ResponseEntity.noContent().build();
    }

    //회원 정보 수정
    @PatchMapping
    public ResponseEntity updateUser(@RequestBody @Valid MemberDto.PatchDto patchDto,
                                     Principal principal) {

        log.info("##### UPDATE USER #####");
        System.out.println(principal.getName());
        Member member = mapper.memberPatchDtoToMember(patchDto);
        member.setEmail(principal.getName());
        Member patchedMember = memberService.updatedMember(member);

        return ResponseEntity.ok().build();
    }

    //회원 상세 정보 조회
    //회원 상세 정보 조회
    @GetMapping
    public ResponseEntity getUser(Principal principal) {
        log.info("##### GET USER #####");
        log.debug("principal: {}", principal);


        Member findMember = memberService.checkMemberExist(principal.getName());
        MemberDto.ResponseDto memberResponseDto = mapper.memberToMemberResponseDto(findMember);

        if (findMember.getMemberImage() != null) {
            ImageInfo imageInfo = findMember.getMemberImage().getImageInfo();
            memberResponseDto.setImagePath(
                    imageInfo.getBaseUrl() + imageInfo.getFilePath() + imageInfo.getImageName());

        }
        return ResponseEntity.ok(memberResponseDto);
    }
    // 비밀번호 변경
    @PatchMapping("/password")
    public ResponseEntity updatePassword(Principal principal,
                                         @RequestBody @Valid PasswordPatchDto passwordPatchDto) {
        log.info("### PW 수정");
        String password = passwordPatchDto.getPassword();
        String afterPassword = passwordPatchDto.getAfterPassword();
        log.info("###PW = " + password + ", AFTER PW = " + afterPassword);
        memberService.updatePassword(principal.getName(), password, afterPassword);

        return ResponseEntity.ok().build();
    }
    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity logout(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        String jws = authorizationHeader.substring(7);    // "Bearer " 이후의 토큰 문자열 추출

        jwtTokenizer.addToTokenBlackList(jws);     //블랙리스트에 jws 추가, 접근 막음

        return ResponseEntity.ok().body("Successfully logged out");
    }
    //이미지 업로드
    @PostMapping("/image")
    public ResponseEntity postUserImage(Principal principal,
                                        @RequestPart("file") MultipartFile file) {
        Member findMember = memberService.checkMemberExist(principal.getName());
        memberService.postMemberImage(findMember.getMemberId(), file);
        return new ResponseEntity(HttpStatus.CREATED);
    }


    //회원리스트
    @GetMapping("/admin")
    public ResponseEntity getMembers(@Positive @RequestParam(value = "page", defaultValue = "1") int page,
                                     @Positive @RequestParam(value = "size", defaultValue = "20") int size){
        Page<Member> pageMember = memberService.findMembers(page -1, size);
        List<Member> members = pageMember.getContent();
        List<MemberDto.ResponseDto> response = mapper.membersToResponses(members);

        return new ResponseEntity(HttpStatus.CREATED);

    }
    // 이메일키 인증
    @GetMapping("/email_auth")
    public ResponseEntity<String> confirmRegistration(@RequestParam("id") Long id, @RequestParam("mailKey") String mailKey) {
        try {
            memberService.mailKeyAuth(id, mailKey);
            // 인증 성공
            return ResponseEntity.ok("이메일 인증이 완료되었습니다. 로그인을 진행해 주세요.");
        } catch (BusinessLogicException e) {
            // 인증 실패
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 인증 접근입니다.");
        }
    }



  /*

    // 리커버리 이메일 샌드
    @PostMapping("/recovery/signup/send")
    public ResponseEntity recoveryEmailSend(@RequestBody @Valid Recovery.RecoveryEmailSendDto dto)
            throws MessagingException, UnsupportedEncodingException {
        String emailSignUp = dto.getEmailSignUp();
        String emailNeedToSend = dto.getEmailNeedToSend();

        memberService.recoveryEmailSend(emailSignUp, emailNeedToSend);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/recovery/password/send")
    public ResponseEntity recoveryPWEmailSend(@RequestBody @Valid Recovery.RecoveryPWEmailSendDto dto)
            throws MessagingException, UnsupportedEncodingException {
        String email = dto.getEmail();

        memberService.recoveryPWEmailSend(email);
        if (memberService.existsByEmail(email)){
            Member findMember = memberService.checkMemberExist(email);
            memberService.checkNotGoogleAuth(findMember);
        }
        return ResponseEntity.ok().build();
    }

    // 리커버리 진행
    @PatchMapping("/recovery")
    public ResponseEntity recovery(@RequestBody @Valid Recovery.RecoveryPasswordPatchDto dto) {
        String email = dto.getEmail();
        String mailKey = dto.getMailKey();
        String afterPassword = dto.getAfterPassword();

        memberService.recovery(email, mailKey, afterPassword);

        return ResponseEntity.ok().build();
    }

    //이메일 인증 다시 보내기
    @GetMapping("/resend")
    public ResponseEntity resend(Principal principal)
            throws MessagingException, UnsupportedEncodingException {
        String email = principal.getName();
        Member findMember = memberService.checkMemberExist(email);
        memberService.sendEmail(email, findMember.getMailKey(), findMember.getMemberId());

        return ResponseEntity.ok().build();
    }*/
}