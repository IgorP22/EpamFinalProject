package com.podverbnyj.provider.logic.command;

import com.podverbnyj.provider.DAO.ServiceDAO;
import com.podverbnyj.provider.DAO.TariffDAO;
import com.podverbnyj.provider.DAO.UserDAO;
import com.podverbnyj.provider.DAO.db.DBException;
import com.podverbnyj.provider.DAO.db.entity.User;
import com.podverbnyj.provider.DAO.db.entity.constant.Language;
import com.podverbnyj.provider.DAO.db.entity.constant.Role;
import com.podverbnyj.provider.DAO.db.entity.constant.Status;
import org.apache.logging.log4j.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

import static com.podverbnyj.provider.utils.HashPassword.securePassword;

public class UserRequestCommand implements Command {

    private static final Logger log = LogManager.getLogger(UserRequestCommand.class);
    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final ServiceDAO serviceDAO = ServiceDAO.getInstance();
    private static final TariffDAO tariffDAO = TariffDAO.getInstance();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException, SQLException {
        String userRequest = req.getParameter("userRequest");
        System.out.println(userRequest);

        String getServices = "Choice of services";
        String editProfile = "Edit profile";
        String editBalance = "Edit balance";

        if (editBalance.equals(userRequest)) {
            int userID = Integer.parseInt(req.getParameter("userToEditId"));
            User user = userDAO.getById(userID);
            System.out.println(req.getParameter("sum"));
            user.setBalance((user.getBalance()+Double.parseDouble(req.getParameter("sum"))));

            userDAO.update(user);

            req.getSession().setAttribute("currentUser", user);
            return "user.jsp#success";
        }

        if (editProfile.equals(userRequest)) {
            System.out.println("~~~~~");
            String address = addOrEditUser(req);
            if (address != null) return address;
        }

        return req.getHeader("referer");
    }

    private String addOrEditUser(HttpServletRequest req) throws DBException {

        int idToEdit = Integer.parseInt(req.getParameter("userToEditId"));
        User user;
        user = getUser(req);
        user.setId(idToEdit);
        String password = userDAO.getById(idToEdit).getPassword();
        if (password.equals(req.getParameter("userPassword"))) {
            user.setPassword(password);
        }
        userDAO.update(user);
        req.getSession().setAttribute("currentUser", user);
        req.getSession().setAttribute("userToEdit", null);
        return "user.jsp#success";
    }

    private User getUser(HttpServletRequest req) {
        User user;
        user = new User.UserBuilder(
                req.getParameter("userLogin"),
                securePassword(req.getParameter("userPassword")))
                .setEmail(req.getParameter("userEmail"))
                .setName(req.getParameter("userName"))
                .setSurname(req.getParameter("userSurname"))
                .setPhone(req.getParameter("userPhone"))
                .setLanguage(Language.valueOf(req.getParameter("userLanguage")))
                .setRole(Role.valueOf(req.getParameter("userRole")))
                .setNotification(Boolean.parseBoolean(req.getParameter("userNotification")))
                .setStatus(Status.valueOf(req.getParameter("userStatus")))
                .build();
        return user;
    }

}
