package com.devesta.blogify.controller;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hello")
public class T {

    @GetMapping
    public ResponseEntity<String> e() {
        return ResponseEntity.ok("hello all");
    }

    @GetMapping("/t")
    public ResponseEntity<String> h() {
        return ResponseEntity.ok("hello auth");
    }

    @GetMapping("/t1")
    public ResponseEntity<String> g() {
        return ResponseEntity.ok("hello user");
    }

    @GetMapping("/t2")
    public ResponseEntity<String> f() {
        return ResponseEntity.ok("hello admin");
    }

    @GetMapping("/accessDenied")
    public ResponseEntity<String> i() {
        return ResponseEntity.ok("Access Denied so you are here ^_^");
    }

}
