package com.podverbnyj.provider;

import com.podverbnyj.provider.DAO.ServiceDAO;
import com.podverbnyj.provider.DAO.TariffDAO;
import com.podverbnyj.provider.DAO.UserDAO;
import com.podverbnyj.provider.DAO.db.DBException;
import com.podverbnyj.provider.DAO.db.entity.Service;
import com.podverbnyj.provider.DAO.db.entity.Tariff;
import org.apache.logging.log4j.*;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.List;

public class MyHttpSessionListener implements HttpSessionListener {



    @Override
    public void sessionCreated(HttpSessionEvent session) {

        System.out.println("Session started" + session.getSession().getId());
        List<Service> listOfServices;
        List<Tariff> listOfTariffs;

        try {
            listOfServices = ServiceDAO.getInstance().findAll();
            listOfTariffs = TariffDAO.getInstance().findAll();
        } catch (DBException e) {
            System.out.println("Can't receive list of services" + session.getSession().getId());
            return;
        }

        session.getSession().setAttribute("ListOfServices", listOfServices);
        session.getSession().setAttribute("ListOfTariffs", listOfTariffs);
        session.getSession().setAttribute("language", "ru");
        session.getSession().setAttribute("servicesIsSorted", false);
        session.getSession().setAttribute("tariffsIsSortedByName", false);
        session.getSession().setAttribute("sortedByPrice", false);


    }


    @Override
    public void sessionDestroyed(HttpSessionEvent session) {
        System.out.println("Session stopped" + session.getSession().getId());

    }

}
