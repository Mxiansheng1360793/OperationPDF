package com.ze.pdf.operationpdf;

import com.ze.pdf.operationpdf.module.IPdfHandler;
import com.ze.pdf.operationpdf.module.utils.ResourceUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Handler;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class OperationPdfApplicationTests {

    @Resource
    IPdfHandler pdfHandler;

    @Resource
    ResourceLoader resourceLoader;

    @Test
    void contextLoads() {
        String path = ResourceUtils.getResourcePath() + "pdf" + "/" + System.currentTimeMillis() + ".png";
        System.out.println("path = " + path);
    }

    @Test
    void test01() throws IOException {

        InputStream imgInput = resourceLoader.getResource("classpath:images/mmexport1679453123060.png").getInputStream();
        InputStream pdfInput = resourceLoader.getResource("classpath:pdf/GetReportFileOfPDF.pdf").getInputStream();
        Float scale = 0.08f;
        Float fistScale = 0.5f;
        Float lastScale = 0.2f;
        Integer accuracyCompensation = 5;
        PDDocument pdDocument = pdfHandler.stampImgToPdf(imgInput, pdfInput, scale, fistScale, lastScale,
                accuracyCompensation);
        pdDocument.save(new File(ResourceUtils.getResourcePath() + "pdf" + "/test" + System.currentTimeMillis() + ".pdf"));
        pdDocument.close();
    }
}
