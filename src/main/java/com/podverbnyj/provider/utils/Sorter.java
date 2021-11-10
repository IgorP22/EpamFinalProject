package com.podverbnyj.provider.utils;

import com.podverbnyj.provider.DAO.ServiceDAO;
import com.podverbnyj.provider.DAO.TariffDAO;
import com.podverbnyj.provider.DAO.db.entity.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Sorter {

    private Sorter (){}

    private static final ServiceDAO serviceDAO = ServiceDAO.getInstance();
    private static final TariffDAO tariffDAO = TariffDAO.getInstance();


    public static void sortServicesByName(ArrayList<Service> services) {
        services.sort(Comparator.comparing(Service::getTitleEn));
    }



}
