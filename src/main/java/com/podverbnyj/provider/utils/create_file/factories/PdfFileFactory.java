package com.podverbnyj.provider.utils.create_file.factories;

import com.podverbnyj.provider.utils.create_file.files.File;
import com.podverbnyj.provider.utils.create_file.files.PdfFile;

public class PdfFileFactory implements FileFactory{
    @Override
    public File createFile() {
        return new PdfFile();
    }
}
