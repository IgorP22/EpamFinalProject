package com.podverbnyj.provider.web;

import com.podverbnyj.provider.DAO.PasswordRecoveryDAO;
import com.podverbnyj.provider.DAO.UserDAO;
import com.podverbnyj.provider.DAO.db.DBException;
import com.podverbnyj.provider.DAO.db.entity.PasswordRecovery;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(urlPatterns = {"/index.jsp"})
public class PasswordRecoveryFilter implements Filter {
    private static final PasswordRecoveryDAO passwordRecoveryDAO = PasswordRecoveryDAO.getInstance();
    private static final UserDAO userDAO = UserDAO.getInstance();

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response, FilterChain chain)
            throws IOException, ServletException {


        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String code = (request.getParameter("restoreLink"));

        if (code != null) {
            PasswordRecovery ps = new PasswordRecovery();



            System.out.println(code);

            try {
                ps = passwordRecoveryDAO.getPasswordRecovery(code);

            } catch (DBException e) {
                e.printStackTrace();
            }
            String userLoginToRecover = null;
            try {
                userLoginToRecover = userDAO.getById(ps.getUserId()).getLogin();
            } catch (DBException e) {
                e.printStackTrace();
            }
            System.out.println(userLoginToRecover);

            req.getSession().setAttribute("userToRecover", ps);
            req.getSession().setAttribute("userLoginToRecover", userLoginToRecover);
            //todo BD get code
            resp.sendRedirect(req.getContextPath() + "/index.jsp#passwordRecovery");
        } else {

            chain.doFilter(req, response);
        }
    }
}
