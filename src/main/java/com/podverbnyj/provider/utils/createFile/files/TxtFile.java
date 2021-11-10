package com.podverbnyj.provider.utils.createFile.files;

public class TxtFile implements File{
    @Override
    public void create() {
        System.out.println("Text file will be created there");
    }
}
