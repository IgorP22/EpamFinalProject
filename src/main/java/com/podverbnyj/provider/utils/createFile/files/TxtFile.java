package com.podverbnyj.provider.utils.createFile.files;

import com.itextpdf.text.Chunk;
import com.podverbnyj.provider.DAO.db.entity.Service;
import com.podverbnyj.provider.DAO.db.entity.Tariff;

import javax.servlet.http.HttpServletRequest;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TxtFile implements File {
    @Override
    public void create(HttpServletRequest req) throws IOException {
        List<Service> serviceList = (List<Service>) req.getSession().getAttribute("ListOfServices");
        List<Tariff> tariffList = (List<Tariff>) req.getSession().getAttribute("ListOfTariffs");
        String language = req.getSession().getAttribute("language").toString();

        System.out.println(serviceList);
        System.out.println(tariffList);
        FileWriter file = new FileWriter(req.getServletContext().getRealPath("/")+"price_list.txt");

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


        System.out.println("Text file with price_list created there");

        try(FileReader reader = new FileReader(req.getServletContext().getRealPath("/")+"price_list.txt"))
        {
            // читаем посимвольно
            int c;
            while((c=reader.read())!=-1){

                System.out.print((char)c);
            }
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }

        String filePath = req.getServletContext().getRealPath("/");
        System.out.println(filePath);


    }
}
