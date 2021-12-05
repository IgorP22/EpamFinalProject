package com.podverbnyj.provider.web;

import com.podverbnyj.provider.DAO.UserDAO;
import com.podverbnyj.provider.DAO.db.DBException;
import org.apache.logging.log4j.*;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/ValidateLoginServlet",})
public class ValidateLoginServlet extends HttpServlet {

    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final Logger log = LogManager.getLogger(ValidateLoginServlet.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String login = request.getParameter("login");

        try {
            if ((login != null) && (userDAO.getByLogin(login))!=null) {
                out.println(1);
            } else {
                out.println(2);
            }
        } catch (DBException ex) {
            log.error("Can't receive login to compare with  ==> {}", login, ex);
        }

    }
}

