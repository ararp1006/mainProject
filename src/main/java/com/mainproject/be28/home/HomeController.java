package com.mainproject.be28.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(){
        return "home";
    }

    @GetMapping("/itemUpload")
    public String itemImageUpload(){
        return "itemUpload";
    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/signIn")
    public String signin(){
        return "signIn";
    }
}
