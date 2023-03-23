package com.ze.pdf.operationpdf;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author : ze
 * @Date : 2023/3/22 21:00
 * @Description : TODO
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
public class OperationPdfControllerTest {

    @Resource
    ResourceLoader resourceLoader;

    @Autowired
    private MockMvc mockMvc;

    private MockMultipartFile imgMultipartFile;
    private MockMultipartFile pdfMultipartFile;

    @Before
    public void init() throws IOException {
        InputStream imgInput = resourceLoader.getResource("classpath:images/mmexport1679453123060.png").getInputStream();
        InputStream pdfInput = resourceLoader.getResource("classpath:pdf/GetReportFileOfPDF.pdf").getInputStream();
        imgMultipartFile = new MockMultipartFile("imageFile", imgInput);
        pdfMultipartFile = new MockMultipartFile("pdfFile", pdfInput);
    }

    @Test
    public void test01() throws Exception {
         mockMvc.perform(MockMvcRequestBuilders.multipart("/pdfProcess/stamp/halve/pdfImgAndParam")
                        .file(imgMultipartFile)
                        .file(pdfMultipartFile)
                        .param("scale", "0.3"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("File successfully!"));

    }


}
