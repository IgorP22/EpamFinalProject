package com.podverbnyj.provider.utils.createFile.files;


import com.itextpdf.text.DocumentException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface File {
    void create(HttpServletRequest req) throws IOException, DocumentException;
}
