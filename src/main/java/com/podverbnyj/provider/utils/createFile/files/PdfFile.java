package com.podverbnyj.provider.utils.createFile.files;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.podverbnyj.provider.DAO.db.entity.Service;
import com.podverbnyj.provider.DAO.db.entity.Tariff;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PdfFile implements File {
    private static final Logger log = LogManager.getLogger(PdfFile.class);

    @SuppressWarnings("unchecked")
    @Override
    public void create(HttpServletRequest req) throws IOException, DocumentException {
        List<Service> serviceList = (List<Service>) req.getSession().getAttribute("ListOfServices");
        List<Tariff> tariffList = (List<Tariff>) req.getSession().getAttribute("ListOfTariffs");
        System.out.println(serviceList);
        System.out.println(tariffList);
        String language = req.getSession().getAttribute("language").toString();

        Document document = new Document();

        PdfWriter.getInstance(document, new FileOutputStream(req.getServletContext().getRealPath("/") + "price_list.pdf"));


        document.open();
        BaseFont unicode =
                BaseFont.createFont("g:/windows/fonts/arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
//        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16, BaseColor.BLACK);
        Paragraph paragraph;


        if (language.equals("ru")) {
            paragraph = new Paragraph("Прайс-лист" + System.lineSeparator(), new Font(unicode, 16));
        } else {
            paragraph = new Paragraph("Our prices", new Font(unicode, 16));
        }
        document.add(paragraph);
        paragraph = new Paragraph("  ", new Font(unicode, 16));
        document.add(paragraph);

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        float[] columnWidths = {30, 60, 10};
        table.setWidths(columnWidths);


        if (language.equals("ru")) {
            paragraph = new Paragraph("Наименование", new Font(unicode, 10));
            table.addCell(paragraph);
            paragraph = new Paragraph("Описание", new Font(unicode, 10));
            table.addCell(paragraph);
            paragraph = new Paragraph("Цена", new Font(unicode, 10));
        } else {
            paragraph = new Paragraph("Name", new Font(unicode, 10));
            table.addCell(paragraph);
            paragraph = new Paragraph("Description", new Font(unicode, 10));
            table.addCell(paragraph);
            paragraph = new Paragraph("Price", new Font(unicode, 10));
        }
        table.addCell(paragraph);


        for (Service service : serviceList) {
            PdfPCell cell;
            if (language.equals("ru")) {
                cell = new PdfPCell(new Phrase(service.getTitleRu(), new Font(unicode, 12)));
            } else {
                cell = new PdfPCell(new Phrase(service.getTitleEn(), new Font(unicode, 12)));
            }
            cell.setColspan(3);
            table.addCell(cell);


            for (Tariff tariff : tariffList) {
                if (service.getId() == tariff.getServiceId()) {
                    if (language.equals("ru")) {
                        paragraph = new Paragraph(tariff.getNameRu(), new Font(unicode, 10));
                        table.addCell(paragraph);
                        paragraph = new Paragraph(tariff.getDescriptionRu(), new Font(unicode, 10));
                    } else {
                        paragraph = new Paragraph(tariff.getNameEn(), new Font(unicode, 10));
                        table.addCell(paragraph);
                        paragraph = new Paragraph(tariff.getDescriptionEn(), new Font(unicode, 10));
                    }
                    table.addCell(paragraph);
                    paragraph = new Paragraph(String.valueOf(tariff.getPrice()), new Font(unicode, 10));
                    table.addCell(paragraph);
                }
            }
        }
        document.add(table);

        document.close();

        log.info("Price list created: " + req.getServletContext().getRealPath("/") + "price_list.pdf");
    }
}
