package com.podverbnyj.provider.utils.create_file.factories;

import com.podverbnyj.provider.utils.create_file.files.File;
import com.podverbnyj.provider.utils.create_file.files.TxtFile;

public class TxtFileFactory implements FileFactory{
    @Override
    public File createFile() {
        return new TxtFile();
    }
}
