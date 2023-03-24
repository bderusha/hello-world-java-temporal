package com.b3.helloworld;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
    @GetMapping("/")
    String HelloWorld(){
        String msg = "Hello World";
        return msg;
    }
}
