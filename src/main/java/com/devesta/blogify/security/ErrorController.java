package com.devesta.blogify.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class ErrorController {

    @GetMapping("/error")
    public String errorPage() {
        return "accessDenied";
    }
}
