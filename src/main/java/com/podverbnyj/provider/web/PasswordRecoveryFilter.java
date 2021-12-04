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
import org.apache.logging.log4j.*;

@WebFilter(urlPatterns = {"/index.jsp"})
public class PasswordRecoveryFilter implements Filter {

    private static final PasswordRecoveryDAO passwordRecoveryDAO = PasswordRecoveryDAO.getInstance();
    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final Logger log = LogManager.getLogger(PasswordRecoveryFilter.class);

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response, FilterChain chain)
            throws IOException, ServletException {


        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String code = (request.getParameter("restoreLink"));

        if (code != null) {
            PasswordRecovery ps = new PasswordRecovery();

            try {
                ps = passwordRecoveryDAO.getPasswordRecovery(code);

            } catch (DBException ex) {
                log.error("Error to receive password recovery entry with code {}", code, ex);
            }

            String userLoginToRecover = null;


            req.getSession().setAttribute("userToRecover", ps);

            if (code.equals(ps.getCode())) {
                try {
                    userLoginToRecover = userDAO.getById(ps.getUserId()).getLogin();
                } catch (DBException ex) {
                    log.error("Error to receive user login for recovery for code{}", code, ex);
                }
                req.getSession().setAttribute("userLoginToRecover", userLoginToRecover);
                resp.sendRedirect(req.getContextPath() + "/index.jsp#passwordRecovery");
            } else {
                resp.sendRedirect(req.getContextPath() + "/index.jsp#invalidLink");
            }
        } else {
            chain.doFilter(req, response);
        }
    }
}
