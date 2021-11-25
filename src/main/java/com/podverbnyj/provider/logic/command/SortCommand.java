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

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {

        String sort = req.getParameter("sort");
        String language = (String) req.getSession().getAttribute("language");
        boolean servicesIsSorted = (boolean) req.getSession().getAttribute("servicesIsSorted");
        boolean tariffsIsSortedByName = (boolean) req.getSession().getAttribute("tariffsIsSortedByName");
        boolean sortedByPrice = (boolean) req.getSession().getAttribute("sortedByPrice");
        boolean sortedByLogin = (boolean) req.getSession().getAttribute("sortedByLogin");

        System.out.println(sort);
        System.out.println(language);
        String s1 = "Sort services";
        String s2 = "Sort tariffs by name";
        String s3 = "Sort tariffs by price";
        String s4 = "Sort users by login";


        if (s1.equals(sort)) {
            List<Service> services = (List<Service>) req.getSession().getAttribute("ListOfServices");
            if (servicesIsSorted) {
                Sorter.sortServicesByNameReverseOrder(services, language);
                req.getSession().setAttribute("servicesIsSorted", false);
            } else {
                Sorter.sortServicesByName(services, language);
                req.getSession().setAttribute("servicesIsSorted", true);
            }
            return req.getHeader("referer");
        }

        if (s2.equals(sort)) {
            List<Tariff> tariffs = (List<Tariff>) req.getSession().getAttribute("ListOfTariffs");
            if (tariffsIsSortedByName) {
                Sorter.sortTariffsByNameReverseOrder(tariffs, language);
                req.getSession().setAttribute("tariffsIsSortedByName", false);
            } else {
                Sorter.sortTariffsByName(tariffs, language);
                req.getSession().setAttribute("tariffsIsSortedByName", true);
            }
            return req.getHeader("referer");
        }

        if (s3.equals(sort)) {
            List<Tariff> tariffs = (List<Tariff>) req.getSession().getAttribute("ListOfTariffs");
            if (sortedByPrice) {
                Sorter.sortTariffsByPriceReverseOrder(tariffs);
                req.getSession().setAttribute("sortedByPrice", false);
            } else {
                Sorter.sortTariffsByPrice(tariffs);
                req.getSession().setAttribute("sortedByPrice", true);
            }
        }

        if (s4.equals(sort)) {
            List<User> users = (List<User>) req.getSession().getAttribute("ListOfUsers");
            if (sortedByLogin) {
                Sorter.sortUsersByLoginReverseOrder(users);
                req.getSession().setAttribute("sortedByLogin", false);
            } else {
                Sorter.sortUsersByLogin(users);
                req.getSession().setAttribute("sortedByLogin", true);
            }
        }
        return req.getHeader("referer");
    }
}
