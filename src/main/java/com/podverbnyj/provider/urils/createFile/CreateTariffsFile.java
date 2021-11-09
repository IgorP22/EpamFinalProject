package com.podverbnyj.provider.urils.createFile;

import com.podverbnyj.provider.urils.createFile.fileCreator.FileCreator;
import com.podverbnyj.provider.urils.createFile.factories.FileFactory;
import com.podverbnyj.provider.urils.createFile.factories.PdfFileFactory;
import com.podverbnyj.provider.urils.createFile.factories.TxtFileFactory;

import javax.servlet.http.HttpSession;

public class CreateTariffsFile {

    private static FileCreator configureFileCreator(String fileFormat) {
        FileCreator app;
        FileFactory factory;

        if (fileFormat.contains("txt")) {
            factory = new TxtFileFactory();
            app = new FileCreator(factory);
        } else {
            factory = new PdfFileFactory();
            app = new FileCreator(factory);
        }
        return app;
    }
    public static void GetFile(String format){
        FileCreator ff = configureFileCreator(format);
        ff.createFile();
    }
}
