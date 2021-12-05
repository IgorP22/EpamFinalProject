package com.podverbnyj.provider.logic.command;

import com.itextpdf.text.DocumentException;
import com.podverbnyj.provider.DAO.db.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static com.podverbnyj.provider.utils.EmailSender.emailSender;
import static com.podverbnyj.provider.utils.createFile.CreateTariffsFile.GetFile;

public class EmailCommand implements Command {

    private static final Logger log = LogManager.getLogger(EmailCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {

        String emailAddress = req.getParameter("email");
        String language = req.getSession().getAttribute("language").toString();

        try {
            GetFile(req);
            log.info("File created");
        } catch (IOException | DocumentException e) {
            //TODO catch
            e.printStackTrace();
        }


        String body;
        String subject;
        if ("ru".equals(language)) {
            subject = "Прайс-лист на услуги от Вашего будущего провайдера!";
            body = "Здравствуйте!" +System.lineSeparator()+
                    "Во вложенном файле, находится прайс-лист на услуги от провайдера," +System.lineSeparator()+
                    "который вы запросили на нашем сайте." +System.lineSeparator()+
                    "С наилучшими пожеланиями, Ваш будущий провайдер!) ";
        } else {
            subject = "Price list for services from your future provider!";
            body = "Dear Future Client!" +System.lineSeparator()+
                    "In the attached file, there is a price list for services from provider," +System.lineSeparator()+
                    "which you requested on our site." +System.lineSeparator()+
                    "Best wishes, your future provider!) ";
        }

        emailSender(emailAddress, subject, body, req);

        return "index.jsp#success";
    }
}
