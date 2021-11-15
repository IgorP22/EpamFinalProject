package com.podverbnyj.provider.logic.command;

import com.podverbnyj.provider.DAO.ServiceDAO;
import com.podverbnyj.provider.DAO.TariffDAO;
import com.podverbnyj.provider.DAO.UserDAO;
import com.podverbnyj.provider.DAO.db.DBException;
import com.podverbnyj.provider.DAO.db.entity.Service;
import com.podverbnyj.provider.DAO.db.entity.Tariff;
import com.podverbnyj.provider.DAO.db.entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class AdminRequestCommand implements Command {

    private static final Logger log = LogManager.getLogger(AdminRequestCommand.class);
    private static final UserDAO userDAO = UserDAO.getInstance();
    private static final ServiceDAO serviceDAO = ServiceDAO.getInstance();
    private static final TariffDAO tariffDAO = TariffDAO.getInstance();

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {
        String adminRequest = req.getParameter("adminRequest");
        String getListOfServicesAndTariff = "List of services and tariffs";
        String addNewService = "Add new service";
        String addNewTariff = "Add new tariff";
        String getUsersList = "List of users";
        String addUser =  "Add new user";
        String adminFlag;


        if (getListOfServicesAndTariff.equals(adminRequest)) {
            List<Service> listOfServices = serviceDAO.findAll();
            List<Tariff> listOfTariffs = tariffDAO.findAll();
            req.getSession().setAttribute("ListOfServices", listOfServices);
            req.getSession().setAttribute("ListOfTariffs", listOfTariffs);
            adminFlag = "price";
            req.getSession().setAttribute("adminFlag", adminFlag);
            System.out.println(adminFlag);
            return req.getHeader("referer");
        }

        if (getUsersList.equals(adminRequest)) {
            List<User> listOfUsers = userDAO.findAll();
            req.getSession().setAttribute("ListOfUsers", listOfUsers);
            adminFlag = "users";
            req.getSession().setAttribute("adminFlag", adminFlag);
            req.getSession().setAttribute("usersIsSorted", false);
            return req.getHeader("referer");
        }




        return req.getHeader("referer");
    }
}
