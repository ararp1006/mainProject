package com.mainproject.be28.member.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
@Service
@Slf4j
@EnableAsync
@Component
public class MailService {
    @Autowired
    private JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public MailService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }
    private void simpleEmailSend(String email) {
        //이메일 작성
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("개발자들입니다..");
        simpleMailMessage.setText("google Oauth로 가입된 회원입니다.");

        //이메일 발신
        mailSender.send(simpleMailMessage);
    }
    //실제 메일 전송
    @Async
    public String sendEmail(String toEmail, String mailKey, Long id)
            throws MessagingException, UnsupportedEncodingException {

        //메일전송에 필요한 정보 설정
        MimeMessage emailForm = createEmailForm(toEmail, mailKey, id);
        //실제 메일 전송
        mailSender.send(emailForm);

        return mailKey;
    }
    //메일 양식 작성
    public MimeMessage createEmailForm(String email, String mailKey, Long id)
            throws MessagingException, UnsupportedEncodingException {

        String setFrom = "${spring.mail.username}"; //email-config에 설정한 자신의 이메일 주소(보내는 사람)
        String toEmail = email; //받는 사람
        String title = "개발자들 인증 메일"; //제목
        String href =
                "https://www.google.co.kr/?hl=ko" + id
                        + "&mailKey=" + mailKey;

        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, email); //보낼 이메일 설정
        message.setSubject(title); //제목 설정
        message.setFrom(setFrom); //보내는 이메일
        message.setText(setContext(href), "utf-8", "html");

        return message;
    }
    //타임리프를 이용한 context 설정
    public String setContext(String href) {
        Context context = new Context();
        context.setVariable("href", href);
        return templateEngine.process("emailAuth", context); //mail.html

    }
}
