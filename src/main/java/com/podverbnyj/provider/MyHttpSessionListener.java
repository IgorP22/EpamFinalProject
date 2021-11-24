package com.podverbnyj.provider;

import com.podverbnyj.provider.DAO.ServiceDAO;
import com.podverbnyj.provider.DAO.TariffDAO;
import com.podverbnyj.provider.DAO.UserDAO;
import com.podverbnyj.provider.DAO.db.DBException;
import com.podverbnyj.provider.DAO.db.entity.Service;
import com.podverbnyj.provider.DAO.db.entity.Tariff;
import com.podverbnyj.provider.DAO.db.entity.User;
import com.podverbnyj.provider.utils.Sorter;
import org.apache.logging.log4j.*;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.List;

public class MyHttpSessionListener implements HttpSessionListener {
    private static final Logger log = LogManager.getLogger(MyHttpSessionListener.class);



    @Override
    public void sessionCreated(HttpSessionEvent session) {

        log.info("Session started" + session.getSession().getId());
        List<Service> listOfServices;
        List<Tariff> listOfTariffs;
        List <User> listOfUsers;

        try {
            listOfServices = ServiceDAO.getInstance().findAll();
            listOfTariffs = TariffDAO.getInstance().findAll();
            listOfUsers = UserDAO.getInstance().findAll();
        } catch (DBException e) {
            log.error("Can't receive list of services " + session.getSession().getId());
            return;
        }
        Sorter.sortServicesByName(listOfServices,"ru");
        Sorter.sortTariffsByPrice(listOfTariffs);
        Sorter.sortUsersByLogin(listOfUsers);

        session.getSession().setAttribute("ListOfServices", listOfServices);
        session.getSession().setAttribute("ListOfTariffs", listOfTariffs);
        session.getSession().setAttribute("language", "ru");
        session.getSession().setAttribute("servicesIsSorted", true);
        session.getSession().setAttribute("tariffsIsSortedByName", false);
        session.getSession().setAttribute("sortedByPrice", true);
        session.getSession().setAttribute("sortedByLogin", true);
        log.info("Session data initialised " + session.getSession().getId());
    }


    @Override
    public void sessionDestroyed(HttpSessionEvent session) {
        log.info("Session stopped " + session.getSession().getId());

    }

}
