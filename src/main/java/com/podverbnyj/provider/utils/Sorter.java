package com.podverbnyj.provider.utils;

import com.podverbnyj.provider.dao.db.entity.Service;
import com.podverbnyj.provider.dao.db.entity.Tariff;
import com.podverbnyj.provider.dao.db.entity.User;

import java.util.*;

/**
 * Sorting util, all method have title according to sorting type.
 * Field to sort depends on language from parameters.
 */
public class Sorter {

    private Sorter() {
    }

    public static void sortServicesByName(List<Service> services, String lang) {
        if (lang.equals("ru")) {
            services.sort(Comparator.comparing(Service::getTitleRu));
            return;
        }
        services.sort(Comparator.comparing(Service::getTitleEn));
    }

    public static void sortServicesByNameReverseOrder(List<Service> services, String lang) {
        if (lang.equals("ru")) {
            services.sort(Comparator.comparing(Service::getTitleRu).reversed());
            return;
        }
        services.sort(Comparator.comparing(Service::getTitleEn).reversed());
    }

    public static void sortTariffsByName(List<Tariff> tariffs, String lang) {
        if (lang.equals("ru")) {
            tariffs.sort(Comparator.comparing(Tariff::getNameRu));
            return;
        }
        tariffs.sort(Comparator.comparing(Tariff::getNameEn));
    }

    public static void sortTariffsByNameReverseOrder(List<Tariff> tariffs, String lang) {
        if (lang.equals("ru")) {
            tariffs.sort(Comparator.comparing(Tariff::getNameRu).reversed());
            return;
        }
        tariffs.sort(Comparator.comparing(Tariff::getNameEn).reversed());
    }

    public static void sortTariffsByPrice(List<Tariff> tariffs) {
        tariffs.sort(Comparator.comparing(Tariff::getPrice));
    }

    public static void sortTariffsByPriceReverseOrder(List<Tariff> tariffs) {
        tariffs.sort(Comparator.comparing(Tariff::getPrice).reversed());
    }

    public static void sortUsersByLogin(List<User> users) {
        users.sort(Comparator.comparing(User::getLogin));
    }

    public static void sortUsersByLoginReverseOrder(List<User> users) {
        users.sort(Comparator.comparing(User::getLogin).reversed());
    }


}
