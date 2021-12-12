package com.podverbnyj.provider.logic.command;

import com.podverbnyj.provider.dao.PasswordRecoveryDAO;
import com.podverbnyj.provider.dao.UserDAO;
import com.podverbnyj.provider.dao.db.DBException;
import com.podverbnyj.provider.dao.db.entity.PasswordRecovery;
import com.podverbnyj.provider.dao.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.security.SecureRandom;

import static com.podverbnyj.provider.logic.command.LoginCommand.captchaVerification;
import static com.podverbnyj.provider.utils.EmailSender.emailSender;
import static com.podverbnyj.provider.utils.HashPassword.securePassword;

/**
 * ForgotPasswordCommand class handles requests from unauthorized user of web application to restore forgotten password,
 * check possibility to restore, generate unique password restore link, send it email, and
 * start 5 minute delayed thread to delete this link.
 *
 * Implements Command interface.
 */
public class ForgotPasswordCommand implements Command {

    private static final Logger log = LogManager.getLogger(ForgotPasswordCommand.class);
    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final PasswordRecoveryDAO passwordRecoveryDAO = PasswordRecoveryDAO.getInstance();
    public static final String USER_TO_RECOVER = "userToRecover";


    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {


        // if attributes userToRestore & userNewPassword present, set user's new password in DB
        if ((req.getSession().getAttribute(USER_TO_RECOVER) != null) &&
                (req.getParameter("userNewPassword") != null)) {
            PasswordRecovery ps =
                    (PasswordRecovery) req.getSession().getAttribute(USER_TO_RECOVER);
            User user = userDAO.getById(ps.getUserId());
            user.setPassword(securePassword(req.getParameter("userNewPassword")));
            userDAO.update(user);
            req.getSession().setAttribute(USER_TO_RECOVER, null);
            return "index.jsp#success";
        }

        String errorAddress = captchaVerification(req);
        if (errorAddress != null) return errorAddress;

        // if combination of userName & email isn't present in DB, redirect to error modal window of webapp
        String userLoginToRestore = req.getParameter("userLoginToRestore");
        String emailToRestore = req.getParameter("emailToRestore");
        User user = userDAO.getByLogin(userLoginToRestore);
        if (user == null) {
            return "index.jsp#noSuchRecordInDb";
        }
        if (!emailToRestore.equals(user.getEmail())) {
            return "index.jsp#noSuchRecordInDb";
        }

        // generating restore password link
        SecureRandom random = new SecureRandom();
        String linkToRestore = String.valueOf(System.currentTimeMillis() +
                (random.nextInt(100000)));
        linkToRestore = securePassword(linkToRestore);
        PasswordRecovery pr = new PasswordRecovery();
        pr.setUserId(userDAO.getByLogin(userLoginToRestore).getId());
        pr.setCode(linkToRestore);
        // creating record in DB with userId & linkToRestore
        passwordRecoveryDAO.create(pr);
        log.info("Password record created in BD {}", pr);

        String language = req.getSession().getAttribute("language").toString();
        String restoreUrl = "http://localhost:8080/Final/index.jsp?restoreLink=" + linkToRestore;
        log.info("Password restore link created {}", restoreUrl);

        // starting 5 minute delayed thead to delete link from DB
        String finalLinkToRestore = linkToRestore;
        Thread t2 = new Thread(() -> {
            log.info("Thread for deleting {} after 5 min tarted", finalLinkToRestore);
            try {
                Thread.sleep(300000);
            } catch (InterruptedException e) {
                log.error("Fall thread sleep");
                Thread.currentThread().interrupt();
            }
            try {
                passwordRecoveryDAO.deleteByCode(finalLinkToRestore);
                log.info("Password restore link {} deleted", finalLinkToRestore);
            } catch (DBException e) {
                log.error("Recover password link wasn't deleted from BD {}", finalLinkToRestore);
            }
            log.info("Thread stopped");
        });
        t2.start();


        // generate body and subject for email
        String body;
        String subject;
        if ("ru".equals(language)) {
            subject = "Ссылка на восстановление пароля";
            body = "Здравствуйте, " + user.getLogin() + "!" + System.lineSeparator() +
                    "Ссылка на восстановление пароля будет активна в течении 5 минут." + System.lineSeparator() +
                    restoreUrl;
        } else {
            subject = "Password restore link.";
            body = "Hi " + user.getLogin() + "!" + System.lineSeparator() +
                    "The password recovery link will be active within 5 minutes." + System.lineSeparator() +
                    restoreUrl;
        }

        // send email
        emailSender(emailToRestore, subject, body, null);

        return "index.jsp#success";
    }
}
