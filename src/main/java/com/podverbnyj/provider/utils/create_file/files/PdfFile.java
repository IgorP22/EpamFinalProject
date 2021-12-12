package com.podverbnyj.provider.utils.create_file.files;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.podverbnyj.provider.dao.db.entity.Service;
import com.podverbnyj.provider.dao.db.entity.Tariff;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * .pdf file creator
 */
public class PdfFile implements File {
    private static final Logger log = LogManager.getLogger(PdfFile.class);


    /**
     * Generate .pdf file
     *
     * @param req used to get language of created file
     * @throws DocumentException to change low level exception to our own exception
     * @throws IOException in case of file creation error
     */
    @Override
    public void create(HttpServletRequest req) throws IOException, DocumentException {

        String language = req.getSession().getAttribute("language").toString();

        Document document = new Document();

        PdfWriter.getInstance(document, new FileOutputStream(req.getServletContext().getRealPath("/") + "price_list.pdf"));


        document.open();
        BaseFont unicode =
                BaseFont.createFont("g:/windows/fonts/arialuni.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
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

        createTable(req, language, unicode, table);

        document.add(table);

        document.close();

        log.info("Price list created: {}price_list.pdf", req.getServletContext().getRealPath("/"));
    }

    /**
     * Generate table for  file
     *
     * @param req used to get lists of services and tariffs
     * @param language file language
     * @param unicode set file codding
     * @param table table object
     */
    private void createTable(HttpServletRequest req, String language, BaseFont unicode, PdfPTable table) {
        Paragraph paragraph;
        List<Service> serviceList = (List<Service>) req.getSession().getAttribute("ListOfServices");
        List<Tariff> tariffList = (List<Tariff>) req.getSession().getAttribute("ListOfTariffs");

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
    }
}
