package com.podverbnyj.provider.utils.createFile.fileCreator;

import com.podverbnyj.provider.utils.createFile.factories.FileFactory;
import com.podverbnyj.provider.utils.createFile.files.File;

public class FileCreator {
    private File file;

    public FileCreator(FileFactory factory) {
        file = factory.createFile();
    }

    public void createFile () {
        file.create();
    }
}
