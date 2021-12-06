package com.podverbnyj.provider.utils.create_file.files;

import com.podverbnyj.provider.dao.db.entity.Service;
import com.podverbnyj.provider.dao.db.entity.Tariff;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TxtFile implements File {
    private static final Logger log = LogManager.getLogger(TxtFile.class);
    public static final String DEVIDER = " ==> ";

        @Override
    public void create(HttpServletRequest req) throws IOException {
        List<Service> serviceList = (List<Service>) req.getSession().getAttribute("ListOfServices");
        List<Tariff> tariffList = (List<Tariff>) req.getSession().getAttribute("ListOfTariffs");
        String language = req.getSession().getAttribute("language").toString();

        try (FileWriter file = new FileWriter(req.getServletContext().getRealPath("/") + "price_list.txt")) {

            prepareHeader(language, file);

            for (Service service : serviceList) {
                if (language.equals("ru")) {
                    file.write(service.getTitleRu() + System.lineSeparator());
                } else {
                    file.write(service.getTitleEn() + System.lineSeparator());
                }
                for (Tariff tariff : tariffList) {
                    if (service.getId() == tariff.getServiceId()) {
                        if (language.equals("ru")) {
                            file.write("==> " + tariff.getNameRu() + DEVIDER + tariff.getDescriptionRu() + DEVIDER
                                    + tariff.getPrice() + System.lineSeparator());
                        } else {
                            file.write("==> " + tariff.getNameEn() + DEVIDER + tariff.getDescriptionEn() + DEVIDER
                                    + tariff.getPrice() + System.lineSeparator());
                        }
                    }
                }
            }
        }

        log.info("Price list created: {}}price_list.txt", req.getServletContext().getRealPath("/"));
    }

    private void prepareHeader(String language, FileWriter file) throws IOException {
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
    }
}
