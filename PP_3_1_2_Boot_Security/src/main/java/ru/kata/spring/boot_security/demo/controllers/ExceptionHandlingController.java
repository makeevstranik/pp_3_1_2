package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ExceptionHandlingController {
    @ExceptionHandler(Exception.class)
    public void handleError(HttpServletRequest req, Exception ex) {
        System.out.println("----------------In handleError(HttpServletRequest req, Exception ex)-----------------");
        ex.printStackTrace();
    }
}

