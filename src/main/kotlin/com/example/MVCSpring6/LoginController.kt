package com.example.MVCSpring6

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class LoginController {

    @GetMapping("/")
    fun redirectToLogin(): String {
        return "redirect:/login"
    }

}