package com.podverbnyj.provider.utils.createFile.factories;

import com.podverbnyj.provider.utils.createFile.files.File;
import com.podverbnyj.provider.utils.createFile.files.TxtFile;

public class TxtFileFactory implements FileFactory{
    @Override
    public File createFile() {
        return new TxtFile();
    }
}
