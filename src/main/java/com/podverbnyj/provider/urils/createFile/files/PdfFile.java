package com.podverbnyj.provider.urils.createFile.files;

public class PdfFile implements File{
    @Override
    public void create() {
        System.out.println("Pdf file will be created there");
    }
}
