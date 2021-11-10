package com.podverbnyj.provider.utils.createFile.factories;

import com.podverbnyj.provider.utils.createFile.files.File;
import com.podverbnyj.provider.utils.createFile.files.PdfFile;

public class PdfFileFactory implements FileFactory{
    @Override
    public File createFile() {
        return new PdfFile();
    }
}
