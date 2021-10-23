package com.example.MVCSpring6.filters

import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletContext
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@WebFilter(
    urlPatterns = ["/api/*", "/app/*"]
)
class AuthFilter : HttpFilter() {
    @Autowired
    private lateinit var context: ServletContext

    override fun init(filterConfig: FilterConfig) {
        this.context = filterConfig.servletContext
    }

        override fun doFilter(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?) {
        val cookies = request!!.cookies
        if (cookies == null)
            response!!.sendRedirect("/login")
        else {
            val authCookie = cookies.find { it.name == "auth" }?.value

            if (authCookie == null) {
                this.context.log("Unauthorized access request to ${request.requestURI}")
                response!!.sendRedirect("/login")
            } else {
                val currentTime = Calendar.getInstance().timeInMillis.toString()
                if (authCookie >= currentTime) {
                    this.context.log("Auth expired")
                    response!!.sendRedirect("/login")
                }
            }
            chain!!.doFilter(request, response)

        }
    }
}