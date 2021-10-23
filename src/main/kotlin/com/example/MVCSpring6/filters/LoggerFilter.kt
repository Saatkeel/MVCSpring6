package com.example.MVCSpring6.filters

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
@Order(1)
class LoggerFilter : Filter {
    @Autowired
    private lateinit var context: ServletContext

    override fun init(filterConfig: FilterConfig) {
        this.context = filterConfig.servletContext
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain
    ) {
        val req = request as HttpServletRequest
        val res = response as HttpServletResponse
        this.context.log("Logging Request with method ${req.method} and request URI ${req.requestURI}")
        this.context.log("Logging Response : with code ${res.status}")

        chain.doFilter(request, response)
    }
}
