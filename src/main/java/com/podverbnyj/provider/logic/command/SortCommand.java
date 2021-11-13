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
        String language = (String) req.getSession().getAttribute("language");
        boolean servicesIsSorted = (boolean)req.getSession().getAttribute("servicesIsSorted");
        boolean tariffsIsSortedByName = (boolean)req.getSession().getAttribute("tariffsIsSortedByName");
        boolean sortedByPrice = (boolean)req.getSession().getAttribute("sortedByPrice");



        if (sort.equals("Sort services")) {
            List<Service> services = (List<Service>) req.getSession().getAttribute("ListOfServices");
            if (servicesIsSorted) {
                Sorter.sortServicesByNameReverseOrder(services,language);
                req.getSession().setAttribute("servicesIsSorted", false);
            } else {
                Sorter.sortServicesByName(services,language);
                req.getSession().setAttribute("servicesIsSorted", true);
            }
            return req.getHeader("referer");
        }

        if (sort.equals("Sort tariffs by name")) {
            List<Tariff> tariffs = (List<Tariff>) req.getSession().getAttribute("ListOfTariffs");
            if (tariffsIsSortedByName) {
                Sorter.sortTariffsByNameReverseOrder(tariffs,language);
                req.getSession().setAttribute("tariffsIsSortedByName", false);
            } else {
                Sorter.sortTariffsByName(tariffs,language);
                req.getSession().setAttribute("tariffsIsSortedByName", true);
            }
            return req.getHeader("referer");
        }

        if (sort.equals("Sort tariffs by price")) {
            List<Tariff> tariffs = (List<Tariff>) req.getSession().getAttribute("ListOfTariffs");
            if (sortedByPrice) {
                Sorter.sortTariffsByPriceReverseOrder(tariffs);
                req.getSession().setAttribute("sortedByPrice", false);
            } else {
                Sorter.sortTariffsByPrice(tariffs);
                req.getSession().setAttribute("sortedByPrice", true);
            }
        }

        return req.getHeader("referer");
    }
}
