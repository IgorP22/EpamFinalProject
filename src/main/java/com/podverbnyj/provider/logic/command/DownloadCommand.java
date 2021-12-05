package com.podverbnyj.provider.logic.command;

import com.itextpdf.text.DocumentException;
import com.podverbnyj.provider.DAO.db.DBException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.io.File;

import static com.podverbnyj.provider.utils.create_file.CreateTariffsFile.getFile;


public class DownloadCommand implements Command {

    private static final Logger log = LogManager.getLogger(DownloadCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) throws DBException, IOException {

        String fileType = req.getParameter("file");
        try {
            getFile(req);
        } catch (IOException | DocumentException e) {

            log.error("File price_list.{}} can't be created to user", fileType);
        }

        String fileName = req.getServletContext().getRealPath("/") + "price_list."+fileType;
        File file = new File(fileName);

        resp.setContentType("application/octet-stream");
        resp.setContentLength((int)file.length());
        resp.setHeader("Content-Disposition", String.format("attachment; filename=\"%s\"", "price_list."+fileType));

        OutputStream out = resp.getOutputStream();
        try (FileInputStream in = new FileInputStream(file)) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
        out.flush();
        log.info("File price_list.{} sent to user", fileType);
        return "referer";
    }
}
