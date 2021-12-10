package com.podverbnyj.provider.logic.command;

import com.podverbnyj.provider.utils.create_file.factories.PdfFileFactory;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileWriter;

public class EmailCommandTest {


    @Test
    public void emailCommandTest() throws Exception {
//        HttpServletRequest req = mock(HttpServletRequest.class);
//        HttpServletResponse resp = mock(HttpServletResponse.class);
//        HttpSession sessionMock = mock(HttpSession.class);
//        when(req.getSession()).thenReturn(sessionMock);
//        when(req.getSession().getAttribute("language")).thenReturn("ru");
//        when(req.getParameter("file")).thenReturn("txt");
//        FileWriter mock = mock(FileWriter.class);

// call the method under test and pass in the mock

// verify the intended behaviour
//        verify(mock).write("the expected text");
//        verify(mock).close();

        //When
//        pdfFactory.create(mockPdf, mockReserveOrder);

        //Then
//        verify(pdfFactory.getPdfWriter());






//        assertEquals("index.jsp#success", new EmailCommand().execute(req, resp));
    }
}