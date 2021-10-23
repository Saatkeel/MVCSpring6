package com.example.MVCSpring6

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class LoginController {

    @GetMapping("/")
    fun redirectToLogin(): String {
        return "redirect:/login"
    }

}