package com.podverbnyj.provider.logic.command;

import javax.servlet.http.*;

import com.podverbnyj.provider.DAO.ServiceDAO;
import com.podverbnyj.provider.DAO.TariffDAO;
import com.podverbnyj.provider.DAO.UserDAO;
import com.podverbnyj.provider.DAO.db.DBException;
import com.podverbnyj.provider.DAO.db.entity.Service;
import com.podverbnyj.provider.DAO.db.entity.Tariff;
import com.podverbnyj.provider.DAO.db.entity.User;
import com.podverbnyj.provider.DAO.db.entity.constant.Language;
import com.podverbnyj.provider.DAO.db.entity.constant.Role;
import com.podverbnyj.provider.DAO.db.entity.constant.Status;
import com.podverbnyj.provider.utils.HashPassword;
import com.podverbnyj.provider.utils.Sorter;
import org.apache.logging.log4j.*;

import java.util.ArrayList;
import java.util.List;

import static com.podverbnyj.provider.utils.HashPassword.securePassword;

public class LoginCommand implements Command {

    private static final Logger log = LogManager.getLogger(LoginCommand.class);
    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final ServiceDAO serviceDAO = ServiceDAO.getInstance();
    private static final TariffDAO tariffDAO = TariffDAO.getInstance();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");
        User currentUser = new User.UserBuilder(login, securePassword(password)).build();
        log.trace("login ==> " + login);


        User user = userDAO.getByName(login);
        log.trace("Current user ==>" + currentUser);
        log.trace("User from DB ==>" + user);


        if (currentUser.equals(user)) {
            log.trace("Logged successfully as " + login);
            req.getSession().setAttribute("login", login);
            log.trace("Login stored in session");
            log.trace("User role ==> " + user.getRole());
            req.getSession().setAttribute("role", user.getRole());
            if (user.getRole().equals(Role.ADMIN)) {
                req.getSession().setAttribute("user", user);
                return "admin.jsp";
            }
            req.getSession().setAttribute("currentUser", user);
            return "user.jsp";
        }
        log.trace("Login failed, username and password don't matches");
        return "index.jsp";
    }
}
