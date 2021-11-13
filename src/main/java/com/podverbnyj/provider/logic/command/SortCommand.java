package com.podverbnyj.provider.logic.command;

import com.podverbnyj.provider.DAO.ServiceDAO;
import com.podverbnyj.provider.DAO.TariffDAO;
import com.podverbnyj.provider.DAO.UserDAO;
import com.podverbnyj.provider.DAO.db.DBException;
import com.podverbnyj.provider.DAO.db.entity.Service;
import com.podverbnyj.provider.DAO.db.entity.Tariff;
import com.podverbnyj.provider.DAO.db.entity.User;
import com.podverbnyj.provider.DAO.db.entity.constant.Role;
import com.podverbnyj.provider.utils.Sorter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static com.podverbnyj.provider.utils.HashPassword.securePassword;

public class SortCommand implements Command {

    private static final Logger log = LogManager.getLogger(SortCommand.class);
    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final ServiceDAO serviceDAO = ServiceDAO.getInstance();
    private static final TariffDAO tariffDAO = TariffDAO.getInstance();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {

        String sort = req.getParameter("sort");
        System.out.println(sort);


        req.getSession().getAttribute("ListOfTariffs");


        if (sort.equals("Sort services")) {
            List<Service> services = new ArrayList<>();
            services = (List<Service>) req.getSession().getAttribute("ListOfServices");

            Sorter.sortServicesByName(services);
        }

        if (sort.equals("Sort tariffs by name")) {
            List<Tariff> tariffs = new ArrayList<>();
            tariffs = (List<Tariff>) req.getSession().getAttribute("ListOfTariffs");

            Sorter.sortTariffsByName(tariffs);
        }

        if (sort.equals("Sort tariffs by price")) {
            List<Tariff> tariffs = new ArrayList<>();
            tariffs = (List<Tariff>) req.getSession().getAttribute("ListOfTariffs");

            Sorter.sortTariffsByPrice(tariffs);
        }
//        if (sort.equals("Sort tariffs by name")) {
//            Sorter.sortServicesByName(services);
//        }
//        if (sort.equals("Sort tariffs by price")) {
//            Sorter.sortServicesByName(services);
//        }


//        User currentUser = new User.UserBuilder(login, securePassword(password)).build();
//        log.trace("login ==> " + login);


//        User user = userDAO.getByName(login);
//        log.trace("Current user ==>" + currentUser);
//        log.trace("User from DB ==>" + user);

//        List<Service> services = serviceDAO.findAll();
//        System.out.println(services);
//        Sorter.sortServicesByName(services);
//        log.debug("List of services ==> "+services);

//        List<Tariff> tariffs = tariffDAO.findAll();
//        System.out.println(tariffs);


//        if (currentUser.equals(user)) {
//            log.trace("Logged successfully as " + login);
//            req.getSession().setAttribute("login", login);
//            log.trace("Login stored in session");
//            log.trace("User role ==> " + user.getRole());
//            req.getSession().setAttribute("role", user.getRole());
//            if (user.getRole().equals(Role.ADMIN)) {
//                return "admin.jsp";
//            }
//                return "user.jsp";
//        }
//        log.trace("Login failed, username and password don't matches");
        System.out.println(req.getHeader("referer"));
        return req.getHeader("referer");
    }
}
