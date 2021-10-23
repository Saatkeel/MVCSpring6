package com.example.MVCSpring6

import java.io.IOException
import java.util.*
import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@WebServlet(
    name = "authServlet",
    description = "Servlet for auth",
    urlPatterns = ["/login"]
)
class LoginServlet : HttpServlet() {
    private val username = "admin"
    private val password = "password"

    @Throws(ServletException::class, IOException::class)
    override fun doPost(
        request: HttpServletRequest?,
        response: HttpServletResponse?
    ) {
        val userName = request?.getParameter("username")
        val password = request?.getParameter("password")

        if (userName == "1" && password == "2") {

            response!!.contentType = "text/html"
            val cookie = Cookie("auth", Calendar.getInstance().timeInMillis.toString())
            response.addCookie(cookie)
            response.sendRedirect("/app/list")
        } else {
            val requestDispatcher = servletContext.getRequestDispatcher("/login.html")
            val out = response!!.writer
            out.println(
                "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <title>Title</title>\n" +
                        "    <form method=\"post\">\n" +
                        "        Username: <input type=\"text\" name=\"username\"/> <br/>\n" +
                        "        Password: <input type=\"password\" name=\"password\"/> <br/>\n" +
                        "        <input type=\"submit\" />\n" +
                        "    </form>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "\n" + "<p> Wrong username or password</p>" +
                        "</body>\n" +
                        "</html>"
            )
            requestDispatcher.include(request, response)
        }
    }

    override fun doGet(request: HttpServletRequest?, response: HttpServletResponse?) {
        val out = response!!.writer
        out.println(
            "<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Title</title>\n" +
                    "    <form method=\"post\">\n" +
                    "        Username: <input type=\"text\" name=\"username\"/> <br/>\n" +
                    "        Password: <input type=\"password\" name=\"password\"/> <br/>\n" +
                    "        <input type=\"submit\" />\n" +
                    "    </form>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>"
        )
    }

}