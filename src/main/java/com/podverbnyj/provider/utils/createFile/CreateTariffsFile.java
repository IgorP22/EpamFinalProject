package com.podverbnyj.provider.utils.createFile;


import com.itextpdf.text.DocumentException;
import com.podverbnyj.provider.utils.createFile.fileCreator.FileCreator;
import com.podverbnyj.provider.utils.createFile.factories.FileFactory;
import com.podverbnyj.provider.utils.createFile.factories.PdfFileFactory;
import com.podverbnyj.provider.utils.createFile.factories.TxtFileFactory;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.IOException;

public class CreateTariffsFile {

    private static FileCreator configureFileCreator(String fileType) {
        FileCreator app;
        FileFactory factory;

        if (fileType.contains("txt")) {
            factory = new TxtFileFactory();
        } else {
            factory = new PdfFileFactory();
        }
        app = new FileCreator(factory);
        return app;
    }

    public static void GetFile(HttpServletRequest req) throws DocumentException, IOException {
        String fileType = req.getParameter("file");

        FileCreator ff = configureFileCreator(fileType);
        ff.createFile(req);
    }
}
