package com.podverbnyj.provider.logic.command;

import com.itextpdf.text.DocumentException;
import com.podverbnyj.provider.DAO.db.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.podverbnyj.provider.utils.createFile.CreateTariffsFile.GetFile;

public class DownloadCommand implements Command {

    private static final Logger log = LogManager.getLogger(DownloadCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException {

        String fileType = req.getParameter("file");
        System.out.println(fileType);

        try {
            GetFile(req);
        } catch (IOException | DocumentException e) {
            //TODO catch
            e.printStackTrace();
        }

        System.out.println(req.getHeader("referer"));
        return "index.jsp";
    }
}
