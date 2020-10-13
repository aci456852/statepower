package com.example.statepower.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping("/home")
    public String home(){
        return "home";
    }

    @RequestMapping("/form")
    public String form(){
        return "write";
    }

    @RequestMapping("/export")
    public String export(){
        return "export";
    }
}
