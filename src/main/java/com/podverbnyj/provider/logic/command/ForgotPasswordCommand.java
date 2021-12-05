package com.podverbnyj.provider.logic.command;

import com.podverbnyj.provider.DAO.PasswordRecoveryDAO;
import com.podverbnyj.provider.DAO.UserDAO;
import com.podverbnyj.provider.DAO.db.DBException;
import com.podverbnyj.provider.DAO.db.entity.PasswordRecovery;
import com.podverbnyj.provider.DAO.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.podverbnyj.provider.utils.EmailSender.emailSender;
import static com.podverbnyj.provider.utils.HashPassword.securePassword;

public class ForgotPasswordCommand implements Command {

    private static final Logger log = LogManager.getLogger(ForgotPasswordCommand.class);
    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final PasswordRecoveryDAO passwordRecoveryDAO = PasswordRecoveryDAO.getInstance();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {

        if ((req.getSession().getAttribute("userToRecover") != null) &&
                (req.getParameter("userNewPassword") != null)) {
            PasswordRecovery ps =
                    (PasswordRecovery) req.getSession().getAttribute("userToRecover");
            User user = userDAO.getById(ps.getUserId());
            user.setPassword(securePassword(req.getParameter("userNewPassword")));
            userDAO.update(user);
            req.getSession().setAttribute("userToRecover", null);
            return "index.jsp#success";
        }

        String userLoginToRestore = req.getParameter("userLoginToRestore");
        String emailToRestore = req.getParameter("emailToRestore");
        User user = userDAO.getByLogin(userLoginToRestore);
        if (user == null) {
            return "index.jsp#noSuchRecordInDb";
        }
        if (!emailToRestore.equals(user.getEmail())) {
            return "index.jsp#noSuchRecordInDb";
        }

        String linkToRestore = String.valueOf(System.currentTimeMillis() +
                ((int) (Math.random() * 100000)));
        linkToRestore = securePassword(linkToRestore);
        PasswordRecovery pr = new PasswordRecovery();
        pr.setUserId(userDAO.getByLogin(userLoginToRestore).getId());
        pr.setCode(linkToRestore);
        passwordRecoveryDAO.create(pr);
        log.info("Password record created in BD {}", pr);

        String language = req.getSession().getAttribute("language").toString();
        String restoreUrl = "http://localhost:8080/Final/index.jsp?restoreLink=" + linkToRestore;
        log.info("Password restore link created {}", restoreUrl);

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


        String body;
        String subject;

        if ("ru".equals(language)) {
            subject = "Ссылка на восстановление пароля";
            body = "Здравствуйте, " + user.getLogin() + "!" + System.lineSeparator() +
                    "Ссылка на восстановление пароля будет активна в течении 5 минут." + System.lineSeparator() +
                    restoreUrl;
        } else {
            subject = "Password restore link.";
            body = "Hi" + user.getLogin() + "!" + System.lineSeparator() +
                    "The password recovery link will be active within 5 minutes." + System.lineSeparator() +
                    restoreUrl;
        }

        emailSender(emailToRestore, subject, body, null);

        return "index.jsp#success";
    }
}
