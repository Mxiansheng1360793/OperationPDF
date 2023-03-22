package com.ze.pdf.operationpdf.controller;

import com.ze.pdf.operationpdf.module.IPdfHandler;
import com.ze.pdf.operationpdf.module.impl.PdfHandler;
import com.ze.pdf.operationpdf.module.utils.ResourceUtils;
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
import java.io.*;

/**
 * @Author : ze
 * @Date : 2023/3/22 0:19
 * @Description : TODO
 */
@RestController
@RequestMapping("/pdfProcess")
public class PdfController {
    @Resource
    ResourceLoader resourceLoader;

    @Resource
    IPdfHandler pdfHandler;

    /**
     * 图像等分：处理传入pdf，插入骑缝章
     *
     * @param pdfFile pdf文档
     * @return 已处理的pdf文档
     * @throws IOException
     */
    @PostMapping("/stamp/halve/pdf")
    public byte[] halvePdf(@RequestParam("pdfFile") MultipartFile pdfFile) throws IOException {
        // 加载本地图像章
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

    /**
     * 图像等分：处理传入pdf和图像章，插入骑缝章
     *
     * @param pdfFile pdf文档
     * @param imgFile 图像章
     * @return 已处理的pdf文档
     * @throws IOException
     */
    @PostMapping("/stamp/halve/pdfAndImg")
    public byte[] halvePdfAndImg(@RequestParam("pdfFile") MultipartFile pdfFile,
                                 @RequestParam("imgFile") MultipartFile imgFile) {
        InputStream imgInputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            imgInputStream = imgFile.getInputStream();
            outputStream = new ByteArrayOutputStream();
            if (!pdfFile.isEmpty()) {
                InputStream pdfFileInputStream = pdfFile.getInputStream();
                PDDocument pdDocument = new PdfHandler().stampImgToPdf(imgInputStream, pdfFileInputStream);
                imgInputStream.close();
                pdfFileInputStream.close();
                pdDocument.save(outputStream);
                pdDocument.close();
            }
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                imgInputStream.close();
                outputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


    /**
     * 图像等分：处理传入pdf和图像章，插入骑缝章，并可以根据传入的 scale 参数大小，按比例缩放图像章
     *
     * @param pdfFile pdf文档
     * @param imgFile 图像章
     * @param scale   图像章缩放比例
     * @return 已处理的pdf文档
     */
    @PostMapping("/stamp/halve/pdfImgAndParam")
    public byte[] halvePdfAndImageSize(@RequestParam("pdfFile") MultipartFile pdfFile,
                                       @RequestParam("imgFile") MultipartFile imgFile,
                                       Float scale) {
        // 获取各种流对象
        InputStream imgInputStream = null;
        InputStream pdfInputStream = null;
        PDDocument pdDocument = null;
        ByteArrayOutputStream outputStream = null;
        try {
            imgInputStream = imgFile.getInputStream();
            pdfInputStream = pdfFile.getInputStream();
            // 执行业务
            pdDocument = pdfHandler.stampImgToPdf(imgInputStream, pdfInputStream, scale);
            outputStream = new ByteArrayOutputStream();
            // 已处理的pdf文档保存到输出流
//            pdDocument.save(outputStream);

//            pdDocument.save(new File(ResourceUtils.getResourcePath() + File.separator + "pdf" + File.separator + System.currentTimeMillis() + ".png"));
            pdDocument.save(new File(ResourceUtils.getResourcePath() + "pdf" + "/" + System.currentTimeMillis() + ".pdf"));
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                imgInputStream.close();
                pdfInputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
