package org.avy.viber2.data;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet(name = "LoginServlette", urlPatterns = "/login")
public class Login extends HttpServlet {
    private static final long serialVersionUID = 4970223239827455229L;

    @Override
    public void init() throws ServletException {
	// Не се ползва
    }

    @Override
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
	String JsonFromClient = request.getParameter("help_me");
    }

    @Override
    public void destroy() {
	// Не се ползва
    }
}