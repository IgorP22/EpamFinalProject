package com.podverbnyj.provider.web;

import com.podverbnyj.provider.dao.UserDAO;
import com.podverbnyj.provider.dao.db.DBException;
import org.apache.logging.log4j.*;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Validation servlet for checking free login when edit or create user
 */

@WebServlet(urlPatterns = {"/ValidateLoginServlet",})
public class ValidateLoginServlet extends HttpServlet {

    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final Logger log = LogManager.getLogger(ValidateLoginServlet.class);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String login = request.getParameter("login");
        response.setContentType("text/html");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            log.error("Can't check existing users for login {}", login);
        }

        try {
            if ((login != null) && (userDAO.getByLogin(login)) != null) {
                if (out != null) {
                    out.println(1);
                }
            } else {
                if (out != null) {
                    out.println(2);
                }
            }
        } catch (DBException ex) {
            log.error("Can't receive login to compare with  ==> {}", login);
        }

    }
}

