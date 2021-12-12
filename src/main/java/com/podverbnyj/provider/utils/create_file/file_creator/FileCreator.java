package com.podverbnyj.provider.utils.create_file.file_creator;

import com.itextpdf.text.DocumentException;
import com.podverbnyj.provider.utils.create_file.factories.FileFactory;
import com.podverbnyj.provider.utils.create_file.files.File;

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
