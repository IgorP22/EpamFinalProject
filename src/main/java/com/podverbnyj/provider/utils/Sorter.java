package com.podverbnyj.provider.utils;

import com.podverbnyj.provider.DAO.ServiceDAO;
import com.podverbnyj.provider.DAO.TariffDAO;
import com.podverbnyj.provider.DAO.db.entity.Service;

import java.util.*;

public class Sorter {

    private Sorter (){}

    private static final ServiceDAO serviceDAO = ServiceDAO.getInstance();
    private static final TariffDAO tariffDAO = TariffDAO.getInstance();


    public static void sortServicesByName(List<Service> services) {
        services.sort(Comparator.comparing(Service::getTitleEn));
    }



}
