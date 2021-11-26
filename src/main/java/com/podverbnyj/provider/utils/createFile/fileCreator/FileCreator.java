package com.podverbnyj.provider.utils.createFile.fileCreator;

import com.itextpdf.text.DocumentException;
import com.podverbnyj.provider.utils.createFile.factories.FileFactory;
import com.podverbnyj.provider.utils.createFile.files.File;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class FileCreator {
    private File file;

    public FileCreator(FileFactory factory) {
        file = factory.createFile();
    }

    public void createFile(HttpServletRequest req) throws IOException, DocumentException {
        file.create(req);
    }
}
