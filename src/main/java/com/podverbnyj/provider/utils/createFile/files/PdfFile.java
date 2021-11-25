package com.podverbnyj.provider.utils.createFile.files;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.podverbnyj.provider.DAO.db.entity.Service;
import com.podverbnyj.provider.DAO.db.entity.Tariff;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import javax.servlet.http.HttpServletRequest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.itextpdf.text.xml.xmp.XmpWriter.UTF8;

public class PdfFile implements File{
    private static final Logger log = LogManager.getLogger(PdfFile.class);

    @Override
    public void create(HttpServletRequest req) throws IOException, DocumentException {
        List<Service> serviceList = (List<Service>) req.getSession().getAttribute("ListOfServices");
        List<Tariff> tariffList = (List<Tariff>) req.getSession().getAttribute("ListOfTariffs");
        System.out.println(serviceList);
        System.out.println(tariffList);
        String language = req.getSession().getAttribute("language").toString();

        Document document = new Document();

            PdfWriter.getInstance(document, new FileOutputStream(req.getServletContext().getRealPath("/")+"price_list.pdf"));


        document.open();
        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, BaseColor.BLACK);
        Paragraph paragraph;

        if (language.equals("ru")) {
            paragraph = new Paragraph("Прайс-лист" + System.lineSeparator(),font);
        } else {
            paragraph = new Paragraph("Our prices" + System.lineSeparator());
        }
        document.add(paragraph);
        paragraph = new Paragraph("------------------------------" + System.lineSeparator());
        document.add(paragraph);
        if (language.equals("ru")) {
            paragraph = new Paragraph("Наименование                Описание             Цена" + System.lineSeparator(),font);
        } else {
            paragraph = new Paragraph("Name                Description             Price" + System.lineSeparator());
        }
        document.add(paragraph);
        paragraph = new Paragraph("------------------------------" + System.lineSeparator());
        document.add(paragraph);

        for (Service service : serviceList) {
            if (language.equals("ru")) {
                paragraph = new Paragraph(service.getTitleRu() + System.lineSeparator(),font);
            } else {
                paragraph = new Paragraph(service.getTitleEn() + System.lineSeparator());
            }
            document.add(paragraph);
            paragraph = new Paragraph("------------------------------" + System.lineSeparator());
            document.add(paragraph);
            for (Tariff tariff : tariffList) {
                if (service.getId() == tariff.getServiceId()) {
                    if (language.equals("ru")) {
                        paragraph = new Paragraph("==> " + tariff.getNameRu() + " ==> " + tariff.getDescriptionRu() + " ==> "
                                + tariff.getPrice() + System.lineSeparator());
                        document.add(paragraph);
                    } else {
                        paragraph = new Paragraph("==> " + tariff.getNameEn() + " ==> " + tariff.getDescriptionEn() + " ==> "
                                + tariff.getPrice() + System.lineSeparator());
                        document.add(paragraph);
                    }
                }
            }
        }



        document.close();



        System.out.println("Pdf file will be created there");




    }
}
