package com.mainproject.be28.member.service;


import com.mainproject.be28.auth.dto.PrincipalDto;
import com.mainproject.be28.auth.jwt.JwtTokenizer;
import com.mainproject.be28.auth.utils.CustomAuthorityUtils;
import com.mainproject.be28.exception.BusinessLogicException;
import com.mainproject.be28.image.entity.MemberImage;
import com.mainproject.be28.image.service.ImageService;
import com.mainproject.be28.member.data.MemberStatus;
import com.mainproject.be28.member.entity.Member;
import com.mainproject.be28.member.excption.MemberException;
import com.mainproject.be28.member.mapper.MemberMapper;
import com.mainproject.be28.member.repository.MemberRepository;
import com.mainproject.be28.cart.entity.Cart;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
;
import org.springframework.data.domain.Page;


@Service
@Slf4j
@EnableAsync
@Component
public class MemberService {

    private final CustomAuthorityUtils authorityUtils;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final ApplicationEventPublisher publisher;
    private final ImageService imageService;
    private final JwtTokenizer jwtTokenizer;
    public  MemberService(CustomAuthorityUtils authorityUtils, PasswordEncoder passwordEncoder,
                          MemberRepository memberRepository,
                       ApplicationEventPublisher publisher, ImageService imageService, JwtTokenizer jwtTokenizer) {
        this.authorityUtils = authorityUtils;
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
        this.publisher = publisher;
        this.imageService = imageService;
        this.jwtTokenizer = jwtTokenizer;
    }

    public Member createMember(Member member) throws MessagingException, UnsupportedEncodingException {

        setDefaultMemberInfo(member);

        member.setCart(Cart.builder().member(member).build());

        Member save = memberRepository.save(member);

        return save;
    }
    public Member createMemberOAuth2(Member member) {

        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);
        String newName = verifyExistName(member.getName());
        member.setName(newName);

        return memberRepository.save(member);
    }
    private String verifyExistName(String name){     // oauth2로 로그인 했을 때 같은 이름이 있을 때 1~1000까지의 랜덤숫자를 붙임
        String newName = name;
        Optional<Member> optionalMember = memberRepository.findByName(name);
        if(optionalMember.isPresent()){
            Random random = new Random();
            int randomNumber = random.nextInt(10000) + 1;
            newName = name + randomNumber;
        }

        return newName;
    }
    public Member findMember(long memberId) {
        return findVerifiedMember(memberId);
    }
    public void deleteMember(String email) {
        Member findMember =memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(MemberException.MEMBER_NOT_FOUND));


        memberRepository.delete(findMember);
    }

    public  Member updatedMember(Member member) {
        Member findMember = checkMemberExist(member.getEmail());
        //검증 성공
        Optional.ofNullable(member.getName()).ifPresent(findMember::setName);
        Optional.ofNullable(member.getAddress()).ifPresent(findMember::setAddress);
        Optional.ofNullable(member.getPhoneNumber()).ifPresent(findMember::setPhoneNumber);
        Optional.ofNullable(member.getDeliveryInformation()).ifPresent(findMember::setDeliveryInformation);

        memberRepository.save(findMember);
        return findMember;
    }

    // 패스워드 변경
    public  Member updatePassword(String email, String password, String afterPassword) {
        // 회원이 존재하는지 검증
        log.info("###회원이 존재하는지 검증합니다!");
        Member findMember = checkMemberExist(email);
        log.info("### 검증 완료!");
        // 비밀번호가 일치하는지 검증
        log.info("### 비밀번호가 일치하는지 검증합니다!");
        if (passwordEncoder.matches(password, findMember.getPassword())) {
            findMember.setPassword(passwordEncoder.encode(afterPassword));
            memberRepository.save(findMember);
        } else {
            throw new BusinessLogicException(MemberException.INCORRECT_PASSWORD);
        }
        return findMember;
    }


    public  Member getMember(Long memberId) {
        Member findMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException( MemberException.MEMBER_NOT_FOUND));

        return findMember;
    }


    // 회원이 존재하는지 검사 , 존재하면 예외
    private void verifyExistsEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new BusinessLogicException( MemberException.MEMBER_EXIST);
        }
    }

    // 회원이 존재하지 않으면 예외발생
    public  Member checkMemberExist(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(MemberException.MEMBER_NOT_FOUND));
    }

    // 회원이 존재하지 않으면 예외발생 by email
    public  Member checkMemberExist(String email) {

        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(MemberException.MEMBER_NOT_FOUND));
    }

    private void setDefaultMemberInfo( Member member) {
        // 패스워드 암호화
        String encryptedPassword = Optional.ofNullable(passwordEncoder.encode(member.getPassword()))
                .get();
        member.setPassword(encryptedPassword);
        member.setMailKey(createCode());
        // db에 유저 role 저장
        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);
        log.info("member encryptedPassword = {}", encryptedPassword);
    }


    //simple email sender

    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0:
                    key.append((char) ((int) random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) ((int) random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append(random.nextInt(9));
                    break;
            }
        }
        return key.toString();
    }

    public void mailKeyAuth(Long id, String mailKey) {
        Member findMember = checkMemberExist(id);
        if (findMember.getMailKey().equals(mailKey)) {
            findMember.setStatus(MemberStatus.MEMBER_ACTIVE);
            memberRepository.save(findMember);
        } else {
            throw new BusinessLogicException(MemberException.MAILKEY_MISMATCH);
        }
    }



    public void postMemberImage(Long id, MultipartFile file) {
        Member member =getMember(id);
        MemberImage memberImage = imageService.uploadMemberImage(file, member);
        member.setMemberImage(memberImage);
        memberRepository.save(member);
    }
    public Member findVerifiedMember(long memberId) {
        Optional<Member> optionalMember =  memberRepository.findById(memberId);
        Member findMember = optionalMember.orElseThrow(() ->
                new BusinessLogicException(MemberException.MEMBER_NOT_FOUND));


        return findMember;
    }
    public void checkActive( Member member) {
        if (member.getStatus() ==  MemberStatus.MEMBER_TMP) {
            throw new BusinessLogicException(MemberException.NOT_YET_AUTHENTICATE_EMAIL);
        } else if (member.getStatus() !=  MemberStatus.MEMBER_ACTIVE
                && member.getStatus() !=  MemberStatus.MEMBER_GOOGLE) {
            throw new BusinessLogicException(MemberException.NOT_ACTIVE_MEMBER);
        }
    }

    //이메일 중복체크
    public Boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    public  Member authMemberSave(Member member) {
        List<String> roles = authorityUtils.createRoles(member.getEmail());
        member.setRoles(roles);
        member.setCart(Cart.builder().member(member).build());
        member.setStatus(MemberStatus. MEMBER_GOOGLE);

        return memberRepository.save(member);
    }

    public Member findVerifiedMember(String email) {
        Optional<Member> optionalMember =  memberRepository.findByEmail(email);
        Member findMember = optionalMember.orElseThrow(() ->
                new BusinessLogicException(MemberException.MEMBER_NOT_FOUND));


        return findMember;
    }

    public String delegateAccessToken(Member member) {
        Map<String, Object> claims = new HashMap<>();
        PrincipalDto principal = PrincipalDto.builder().id(member.getMemberId()).email(member.getEmail())
                .name(member.getName()).build();
        claims.put("username", member.getEmail());
        claims.put("roles", member.getRoles());
        claims.put("principal", principal);
        log.info("###### principal = {} ", principal);

        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(
                jwtTokenizer.getAccessTokenExpirationMinutes());

        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String accessToken = jwtTokenizer.generateAccessToken(claims, subject, expiration,
                base64EncodedSecretKey);

        return accessToken;
    }

    // (6)
    public String delegateRefreshToken(Member member) {
        String subject = member.getEmail();
        Date expiration = jwtTokenizer.getTokenExpiration(
                jwtTokenizer.getRefreshTokenExpirationMinutes());
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());

        String refreshToken = jwtTokenizer.generateRefreshToken(subject, expiration,
                base64EncodedSecretKey);

        return refreshToken;
    }
    public void checkGoogleAuth(Member member) {
        if (!member.getStatus().equals(MemberStatus.MEMBER_GOOGLE)) {
            throw new BusinessLogicException(MemberException.NOT_GOOGLE_MEMBER);
        }
    }

    public void checkNotGoogleAuth(Member member) {
        log.info("### member.getStatus() = " + member.getStatus());
        log.info(MemberStatus.MEMBER_GOOGLE.toString());
        if (member.getStatus().equals(MemberStatus.MEMBER_GOOGLE)) {
            throw new BusinessLogicException(MemberException.GOOGLE_MEMBER);
        }
    }
    public Page<Member> findMembers(int page, int size) {
        return memberRepository.findAll(PageRequest.of(page, size,
                Sort.by("memberId").descending()));
    }


}
