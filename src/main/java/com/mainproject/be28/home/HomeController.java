package com.mainproject.be28.home;

import com.google.gson.Gson;
import com.mainproject.be28.auth.dto.PrincipalDto;
import com.mainproject.be28.member.dto.MemberDto;
import com.mainproject.be28.member.entity.Member;
import com.mainproject.be28.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;
@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {
    private final MemberService memberService;

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/itemUpload")
    public String itemImageUpload() {
        return "itemUpload";
    }

    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/signIn")
    public String signin() {
        return "signIn";
    }

    @GetMapping("/adminPage")
    public String adminPage() {
        return "adminPage";
    }
    @GetMapping( "/order")
    public String Order(){return "iamPort";

    }
}
