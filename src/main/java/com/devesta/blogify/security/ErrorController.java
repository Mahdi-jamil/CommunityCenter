package com.devesta.blogify.security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
@RequestMapping("/api")
public class ErrorController {

    @GetMapping("/accessDenied")
    public String errorPage() {
        return "error";
    }
}
