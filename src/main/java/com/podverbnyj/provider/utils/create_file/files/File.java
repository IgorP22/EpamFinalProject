package com.podverbnyj.provider.utils.create_file.files;


import com.itextpdf.text.DocumentException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * File creator interface
 */
public interface File {
    void create(HttpServletRequest req) throws IOException, DocumentException;
}
