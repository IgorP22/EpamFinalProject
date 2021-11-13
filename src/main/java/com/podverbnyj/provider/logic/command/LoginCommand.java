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

        List<Service> services = serviceDAO.findAll();
        System.out.println(services);
//        Sorter.sortServicesByName(services);
//        log.debug("List of services ==> "+services);

        List<Tariff> tariffs = tariffDAO.findAll();
        System.out.println(tariffs);
//        log.debug("List of tariffs ==> "+tariffs);

//        ArrayList<User> users = userDAO.findAll();
//        log.debug("List of users ==> " + users);
//
//        Tariff tariff = tariffDAO.getById(1);
//        Service service = serviceDAO.getById(1);
//
//        tariff.setId(9);
//        tariff.setNameRu("IPTV7");
//        tariff.setNameEn("IPTV7");
//        tariff.setPrice(150);
//        tariff.setService_id(2);
//        tariffDAO.delete(tariff);
//
//        service.setId(5);
//        service.setTitleRu("Новый 545");
//        service.setTitleEn("New 5454");
//        serviceDAO.delete(service);






//        log.debug("Tariff by id ==> " + tariff);
//        log.debug("Service by id ==> " + service);




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
            req.getSession().setAttribute("role", user.getRole());
            if (user.getRole().equals(Role.ADMIN)) {
                return "admin.jsp";
            }
                return "user.jsp";
        }
        log.trace("Login failed, username and password don't matches");
        return "index.jsp";
    }
}
