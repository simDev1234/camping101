package com.camping101.beta.global.exception;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionController {

    @GetMapping("/error")
    public void error() {
        throw new RuntimeException();
    }


}
