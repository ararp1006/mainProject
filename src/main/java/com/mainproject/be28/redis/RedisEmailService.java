package com.mainproject.be28.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
@RequiredArgsConstructor
public class RedisEmailService {
    @Autowired
    private JavaMailSender mailSender;

    private final RedisUtil redisUtil;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private static final String EMAIL_QUEUE_KEY = "email_queue";
    private static final long MAX_EMAILS_PER_HOUR = 5;
    private static final long EXPIRATION_TIME_SECONDS = 3600; // 1시간
    @PostConstruct
    public void initializeEmailWorker() {
        // 이메일을 백그라운드에서 처리할 작업자를 시작
        Thread emailWorker = new Thread(() -> {
            while (true) {
                try {
                    String email = redisTemplate.opsForList().leftPop(EMAIL_QUEUE_KEY, 10, TimeUnit.SECONDS);
                    if (email != null) {
                        sendEmail(email);
                    }
                } catch (MessagingException e) {
                    log.error("Error processing email: {}", e.getMessage());
                }
            }
        });
        emailWorker.start();
    }
    public boolean canSendEmail(String userEmail) {
        String key = "email_limit:" + userEmail;
        Long emailCount = redisTemplate.opsForValue().increment(key, 1); // 이메일 카운트 증가
        if (emailCount == 1) {
            // 이메일 카운트가 처음 증가한 경우에만 만료 시간 설정
            redisTemplate.expire(key, EXPIRATION_TIME_SECONDS, TimeUnit.SECONDS);
        }
        return emailCount <= MAX_EMAILS_PER_HOUR; // 허용된 최대 이메일 횟수 이내인지 확인
    }
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

    // 실제 메일 전송
    private MimeMessage createEmailForm(String email) throws MessagingException {

        String setFrom = "${spring.mail.username}"; //email-config에 설정한 자신의 이메일 주소(보내는 사람)
        String toEmail = email; //받는 사람
        String title = "개발자들 인증 메일"; //제목
        String context = createCode();

        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email); //보낼 이메일 설정
        message.setSubject(title); //제목 설정
        message.setFrom(setFrom); //보내는 이메일
        message.setText(context, "utf-8", "html");

        return message;
    }
    public void sendEmail(String toEmail) throws MessagingException {
        if (redisUtil.existData(toEmail)) {
            redisUtil.deleteData(toEmail);
        }

        MimeMessage emailForm = createEmailForm(toEmail);

        mailSender.send(emailForm);
    }
    // 이메일을 메시지 큐에 추가
    public void queueEmail(String toEmail) {
        redisTemplate.opsForList().rightPush(EMAIL_QUEUE_KEY, toEmail);
    }
}
