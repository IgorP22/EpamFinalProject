package com.podverbnyj.provider.urils.createFile.factories;

import com.podverbnyj.provider.urils.createFile.files.File;
import com.podverbnyj.provider.urils.createFile.files.TxtFile;

public class TxtFileFactory implements FileFactory{
    @Override
    public File createFile() {
        return new TxtFile();
    }
}
