package com.podverbnyj.provider.utils.createFile.files;

import com.podverbnyj.provider.DAO.db.entity.Service;
import com.podverbnyj.provider.DAO.db.entity.Tariff;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TxtFile implements File {
    private static final Logger log = LogManager.getLogger(TxtFile.class);

    @SuppressWarnings("unchecked")
    @Override
    public void create(HttpServletRequest req) throws IOException {
        List<Service> serviceList = (List<Service>) req.getSession().getAttribute("ListOfServices");
        List<Tariff> tariffList = (List<Tariff>) req.getSession().getAttribute("ListOfTariffs");
        String language = req.getSession().getAttribute("language").toString();

        System.out.println(serviceList);
        System.out.println(tariffList);
        FileWriter file = new FileWriter(req.getServletContext().getRealPath("/") + "price_list.txt");

        if (language.equals("ru")) {
            file.write("Прайс-лист" + System.lineSeparator());
        } else {
            file.write("Our prices" + System.lineSeparator());
        }
        file.write("------------------------------" + System.lineSeparator());
        if (language.equals("ru")) {
            file.write("Наименование                Описание             Цена" + System.lineSeparator());
        } else {
            file.write("Name                Description             Price" + System.lineSeparator());
        }

        file.write("------------------------------" + System.lineSeparator());

        for (Service service : serviceList) {
            if (language.equals("ru")) {
                file.write(service.getTitleRu() + System.lineSeparator());
            } else {
                file.write(service.getTitleEn() + System.lineSeparator());
            }
            for (Tariff tariff : tariffList) {
                if (service.getId() == tariff.getServiceId()) {
                    if (language.equals("ru")) {
                        file.write("==> " + tariff.getNameRu() + " ==> " + tariff.getDescriptionRu() + " ==> "
                                + tariff.getPrice() + System.lineSeparator());
                    } else {
                        file.write("==> " + tariff.getNameEn() + " ==> " + tariff.getDescriptionEn() + " ==> "
                                + tariff.getPrice() + System.lineSeparator());
                    }
                }
            }
        }

        file.close();

        log.info("Price list created: " + req.getServletContext().getRealPath("/") + "price_list.txt");
    }
}
