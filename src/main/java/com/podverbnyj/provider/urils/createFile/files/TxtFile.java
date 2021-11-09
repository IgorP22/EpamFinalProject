package com.podverbnyj.provider.urils.createFile.files;

public class TxtFile implements File{
    @Override
    public void create() {
        System.out.println("Text file will be created there");
    }
}
