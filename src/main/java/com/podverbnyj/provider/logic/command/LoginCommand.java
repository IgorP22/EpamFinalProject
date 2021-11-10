package com.podverbnyj.provider.logic.command;

import javax.servlet.http.*;

import com.podverbnyj.provider.DAO.UserDAO;
import com.podverbnyj.provider.DAO.db.DBException;
import com.podverbnyj.provider.DAO.db.entity.User;
import com.podverbnyj.provider.DAO.db.entity.constant.Language;
import com.podverbnyj.provider.DAO.db.entity.constant.Role;
import com.podverbnyj.provider.DAO.db.entity.constant.Status;
import com.podverbnyj.provider.utils.HashPassword;
import org.apache.logging.log4j.*;

import static com.podverbnyj.provider.utils.HashPassword.securePassword;

public class LoginCommand implements Command {

    private static final Logger log = LogManager.getLogger(LoginCommand.class);
    private static final UserDAO userDAO = UserDAO.getInstance();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");
        User currentUser = new User.UserBuilder(login, securePassword(password)).build();
        log.trace("login ==> " + login);


        User user = userDAO.getByName(login);
        log.trace("Current user ==>" + currentUser);
        log.trace("User from DB ==>" + user);

//        user.setLogin("user2");
//        user.setPassword("user2");
//        user.setEmail("email@email.email");
//        user.setName("Ivan");
//        user.setSurname("Ivanov");
//        user.setBalance(50);
//        user.setLanguage(Language.RU);
//        user.setNotification(false);
//        user.setPhone("+3805076543210");
//        user.setRole(Role.USER);
//        user.setStatus(Status.BLOCKED);
//        userDAO.update(user);


        if (currentUser.equals(user)) {
            log.trace("Logged successfully as " + login);
            req.getSession().setAttribute("login", login);
            log.trace("Login stored in session");
            log.trace("User role ==> " + user.getRole());
            if (user.getRole().equals(Role.ADMIN)) {
                return "admin.jsp";
            } else {
                return "user.jsp";
            }
        }
        log.trace("Login failed, username and password don't matches");
        return "error.jsp";
    }
}
