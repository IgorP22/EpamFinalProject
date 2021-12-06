package com.podverbnyj.provider.logic.command;

import javax.servlet.http.*;

import com.podverbnyj.provider.dao.UserDAO;
import com.podverbnyj.provider.dao.UserTariffDAO;
import com.podverbnyj.provider.dao.db.DBException;
import com.podverbnyj.provider.dao.db.entity.User;
import com.podverbnyj.provider.dao.db.entity.constant.Role;
import com.podverbnyj.provider.utils.VerifyRecaptcha;
import org.apache.logging.log4j.*;

import java.io.IOException;

import static com.podverbnyj.provider.utils.HashPassword.securePassword;

public class LoginCommand implements Command {

    private static final Logger log = LogManager.getLogger(LoginCommand.class);
    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final UserTariffDAO userTariffDAO = UserTariffDAO.getInstance();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {


        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String gRecaptchaResponse = req
                .getParameter("g-recaptcha-response");
        boolean verify = false;
        try {
            verify = VerifyRecaptcha.verify(gRecaptchaResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }

        User currentUser = new User.UserBuilder(login, securePassword(password)).build();
        log.trace("User login ==> {}", login);


        User user = userDAO.getByLogin(login);
        log.trace("Current user ==> {}", currentUser);
        log.trace("User from DB ==>{}", user);


        if (currentUser.equals(user)) {
            log.trace("Logged successfully as {}", login);
            req.getSession().setAttribute("login", login);
            log.trace("Login stored in session");
            log.trace("User role ==> {}", user.getRole());
            req.getSession().setAttribute("role", user.getRole());
            if (user.getRole().equals(Role.ADMIN)) {
                req.getSession().setAttribute("user", user);
                req.getSession().setAttribute("currentUser", user);
                if (!verify) {
                    return "index.jsp#wrongCaptcha";
                }
                return "admin.jsp";
            }
            req.getSession().setAttribute("currentUser", user);
            req.getSession().setAttribute("userFlag", null);
            double totalCost = userTariffDAO.getTotalCost(user.getId());
            req.getSession().setAttribute("totalCost", totalCost);
            log.info("Totac price for {} = {}",user,totalCost);
            if (!verify) {
                return "index.jsp#wrongCaptcha";
            }
            return "user.jsp";
        }

        if (user!=null && user.getLogin().equals(currentUser.getLogin())) {
            log.trace("Login failed, username and password don't match");
            return "index.jsp#wrongPassword";
        }

        log.trace("Login failed, no such username in db");
        return "index.jsp#userNotExist";
    }
}
