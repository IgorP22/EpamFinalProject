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

    private static FileCreator configureFileCreator(String fileFormat) {
        FileCreator app;
        FileFactory factory;

        if (fileFormat.contains("txt")) {
            factory = new TxtFileFactory();
        } else {
            factory = new PdfFileFactory();
        }
        app = new FileCreator(factory);
        return app;
    }

    public static void GetFile(String format, HttpServletRequest req) throws DocumentException, IOException {
        FileCreator ff = configureFileCreator(format);
        ff.createFile(req);
    }
}
