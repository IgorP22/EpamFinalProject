package com.podverbnyj.provider.urils.createFile.fileCreator;

import com.podverbnyj.provider.urils.createFile.factories.FileFactory;
import com.podverbnyj.provider.urils.createFile.files.File;

public class FileCreator {
    private File file;

    public FileCreator(FileFactory factory) {
        file = factory.createFile();
    }

    public void createFile () {
        file.create();
    }
}
