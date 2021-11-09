package com.podverbnyj.provider.urils.createFile.factories;

import com.podverbnyj.provider.urils.createFile.files.File;
import com.podverbnyj.provider.urils.createFile.files.PdfFile;

public class PdfFileFactory implements FileFactory{
    @Override
    public File createFile() {
        return new PdfFile();
    }
}
