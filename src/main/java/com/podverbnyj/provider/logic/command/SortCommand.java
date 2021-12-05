package com.podverbnyj.provider.logic.command;

import com.podverbnyj.provider.DAO.db.DBException;
import com.podverbnyj.provider.DAO.db.entity.Service;
import com.podverbnyj.provider.DAO.db.entity.Tariff;
import com.podverbnyj.provider.DAO.db.entity.User;
import com.podverbnyj.provider.utils.Sorter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class SortCommand implements Command {

    private static final Logger log = LogManager.getLogger(SortCommand.class);
    public static final String SERVICES_IS_SORTED = "servicesIsSorted";
    public static final String TARIFFS_IS_SORTED_BY_NAME = "tariffsIsSortedByName";
    public static final String SORTED_BY_PRICE = "sortedByPrice";
    public static final String SORTED_BY_LOGIN = "sortedByLogin";
    public static final String REFERER = "referer";

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {

        String sort = req.getParameter("sort");
        String language = (String) req.getSession().getAttribute("language");
        boolean servicesIsSorted = (boolean) req.getSession().getAttribute(SERVICES_IS_SORTED);
        boolean tariffsIsSortedByName = (boolean) req.getSession().getAttribute(TARIFFS_IS_SORTED_BY_NAME);
        boolean sortedByPrice = (boolean) req.getSession().getAttribute(SORTED_BY_PRICE);
        boolean sortedByLogin = (boolean) req.getSession().getAttribute(SORTED_BY_LOGIN);

        String sortServices = "Sort services";
        String sortTariffsByName = "Sort tariffs by name";
        String sortTariffsByPrice = "Sort tariffs by price";
        String sortUsersByLogin = "Sort users by login";


        if (sortServices.equals(sort)) {
            List<Service> services = (List<Service>) req.getSession().getAttribute("ListOfServices");
            if (servicesIsSorted) {
                Sorter.sortServicesByNameReverseOrder(services, language);
                req.getSession().setAttribute(SERVICES_IS_SORTED, false);
            } else {
                Sorter.sortServicesByName(services, language);
                req.getSession().setAttribute(SERVICES_IS_SORTED, true);
            }
            log.info("List of services sorted.");
            return req.getHeader(REFERER);
        }

        if (sortTariffsByName.equals(sort)) {
            List<Tariff> tariffs = (List<Tariff>) req.getSession().getAttribute("ListOfTariffs");
            if (tariffsIsSortedByName) {
                Sorter.sortTariffsByNameReverseOrder(tariffs, language);
                req.getSession().setAttribute(TARIFFS_IS_SORTED_BY_NAME, false);
            } else {
                Sorter.sortTariffsByName(tariffs, language);
                req.getSession().setAttribute(TARIFFS_IS_SORTED_BY_NAME, true);
            }
            log.info("List of tariffs sorted by name.");
            return req.getHeader(REFERER);
        }

        if (sortTariffsByPrice.equals(sort)) {
            List<Tariff> tariffs = (List<Tariff>) req.getSession().getAttribute("ListOfTariffs");
            if (sortedByPrice) {
                Sorter.sortTariffsByPriceReverseOrder(tariffs);
                req.getSession().setAttribute(SORTED_BY_PRICE, false);
            } else {
                Sorter.sortTariffsByPrice(tariffs);
                req.getSession().setAttribute(SORTED_BY_PRICE, true);
            }
            log.info("List of tariffs sorted by price.");
            return req.getHeader(REFERER);
        }

        if (sortUsersByLogin.equals(sort)) {
            List<User> users = (List<User>) req.getSession().getAttribute("ListOfUsers");
            if (sortedByLogin) {
                Sorter.sortUsersByLoginReverseOrder(users);
                req.getSession().setAttribute(SORTED_BY_LOGIN, false);
            } else {
                Sorter.sortUsersByLogin(users);
                req.getSession().setAttribute(SORTED_BY_LOGIN, true);
            }
            log.info("List of users sorted.");
        }
        return req.getHeader(REFERER);
    }
}
