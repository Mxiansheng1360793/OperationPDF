package com.ze.pdf.operationpdf.controller;

import com.ze.pdf.impl.PdfHandler;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Author : ze
 * @Date : 2023/3/22 0:19
 * @Description : TODO
 */
@RestController
@RequestMapping("/pdf")
public class PdfController {
    @Resource
    ResourceLoader resourceLoader;

    @PostMapping("/stamp")
    public byte[] stamp(@RequestParam("pdfFile") MultipartFile pdfFile,HttpServletResponse response) throws IOException {
        InputStream imgInputStream = resourceLoader.getResource("classpath:images/水利章.png").getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        if (!pdfFile.isEmpty()) {
            InputStream pdfFileInputStream = pdfFile.getInputStream();
            PDDocument pdDocument = new PdfHandler().stampImgToPdf(imgInputStream, pdfFileInputStream);
            imgInputStream.close();
            pdfFileInputStream.close();
            pdDocument.save(outputStream);
            pdDocument.close();
        }
        return outputStream.toByteArray();
    }
}
