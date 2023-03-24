package com.ze.pdf.operationpdf;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.io.InputStream;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource("classpath:pdfConfig.properties")
class OperationPdfApplicationTests {

    @Value("${local.image.stamp}")
    String path;

    @Autowired
    ResourceLoader resourceLoader;

    @Test
    void contextLoads() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:images/" + path);
    }

    @Test
    void test01() throws IOException {

    }

    @Test
    public void test02() throws IOException {

    }
}
