package com.podverbnyj.provider.utils.create_file;


import com.itextpdf.text.DocumentException;
import com.podverbnyj.provider.utils.create_file.file_creator.FileCreator;
import com.podverbnyj.provider.utils.create_file.factories.FileFactory;
import com.podverbnyj.provider.utils.create_file.factories.PdfFileFactory;
import com.podverbnyj.provider.utils.create_file.factories.TxtFileFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * File creation factory
 */
public class CreateTariffsFile {
    private CreateTariffsFile() {
    }

    /**
     * File creator configurator
     *
     * @param fileType type of file to generate
     * @return new factory
     */
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

    /**
     * Entrance to file factory
     *
     * @param req used to get type of created file
     * @throws DocumentException to change low level exception to our own exception
     * @throws IOException in case of file creation error
     */
    public static void getFile(HttpServletRequest req) throws DocumentException, IOException {
        String fileType = req.getParameter("file");

        FileCreator ff = configureFileCreator(fileType);
        ff.createFile(req);
    }
}
