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

/**
 * LoginCommand class get user login, password, captcha and redirecting user depend on user role.
 * Implements Command interface.
 */
public class LoginCommand implements Command {

    private static final Logger log = LogManager.getLogger(LoginCommand.class);
    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final UserTariffDAO userTariffDAO = UserTariffDAO.getInstance();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {


        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String errorAddress = captchaVerification(req);
        if (errorAddress != null) return errorAddress;


        // create currentUser with entered login and password
        User currentUser = new User.UserBuilder(login, securePassword(password)).build();
        log.trace("User login ==> {}", login);

        // get user from DB by login
        User user = userDAO.getByLogin(login);
        log.trace("Current user ==> {}", currentUser);
        log.trace("User from DB ==>{}", user);


        // if user exist in DB redirecting to user or admin page depends on role
        if (currentUser.equals(user)) {
            log.trace("Logged successfully as {}", login);
            req.getSession().setAttribute("login", login);
            log.trace("Login stored in session");
            log.trace("User role ==> {}", user.getRole());
            req.getSession().setAttribute("role", user.getRole());

            // if user role is 'admin', redirecting to admin.jsp
            if (user.getRole().equals(Role.ADMIN)) {
                // set currentUser attribute in session scope
                req.getSession().setAttribute("user", user);
                req.getSession().setAttribute("currentUser", user);
                return "admin.jsp";
            }

            req.getSession().setAttribute("currentUser", user);
            req.getSession().setAttribute("userFlag", null);

            // receive total price of services for user
            double totalCost = userTariffDAO.getTotalCost(user.getId());
            req.getSession().setAttribute("totalCost", totalCost);
            log.info("Total price for {} = {}",user,totalCost);
            // redirecting to user.jsp
            return "user.jsp";
        }

        // error if passwords don't match
        if (user!=null && user.getLogin().equals(currentUser.getLogin())) {
            log.trace("Login failed, username and password don't match");
            return "index.jsp#wrongPassword";
        }

        // error if no such user
        log.trace("Login failed, no such username in db");
        return "index.jsp#userNotExist";
    }

    /**
     * Captcha verification method
     *
     * @param req used to receive 'g-recaptcha-response' parameter
     * @return address to redirect in case of captcha verification error, or 'null' if passed
     * @throws DBException our own exception
     */
    public static String captchaVerification(HttpServletRequest req) throws DBException {
        String gRecaptchaResponse = req
                .getParameter("g-recaptcha-response");
        boolean verify;
        try {
            verify = VerifyRecaptcha.verify(gRecaptchaResponse);
        } catch (IOException e) {
            // bad practice, it's not database exception, but used to throw our own exception if captcha verification error
            throw new DBException("Captcha not verified error");
        }

        // error verification captcha
        if (!verify) {
            return "index.jsp#wrongCaptcha";
        }
        return null;
    }
}
