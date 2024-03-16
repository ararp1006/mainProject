package com.mainproject.be28.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.security.NoSuchAlgorithmException;
@RestController
@RequiredArgsConstructor
@RequestMapping("/email")
public class EmailController {

    private final RedisEmailService emailService;

    @GetMapping("/{email_addr}")
    public ResponseEntity<String> sendEmailPath(@PathVariable String email_addr) throws MessagingException {
        if (!emailService.canSendEmail(email_addr)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("이미 전송된 이메일이 너무 많습니다. 잠시 후 다시 시도해주세요.");
        }

        emailService.sendEmail(email_addr);
        return ResponseEntity.ok("이메일을 확인하세요");
    }
}

