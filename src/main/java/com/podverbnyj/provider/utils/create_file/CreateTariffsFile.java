package com.podverbnyj.provider.utils.create_file;


import com.itextpdf.text.DocumentException;
import com.podverbnyj.provider.utils.create_file.file_сreator.FileCreator;
import com.podverbnyj.provider.utils.create_file.factories.FileFactory;
import com.podverbnyj.provider.utils.create_file.factories.PdfFileFactory;
import com.podverbnyj.provider.utils.create_file.factories.TxtFileFactory;

import javax.servlet.http.HttpServletRequest;
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
