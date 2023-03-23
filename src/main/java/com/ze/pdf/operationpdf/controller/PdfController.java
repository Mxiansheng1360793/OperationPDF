package com.ze.pdf.operationpdf.controller;

import com.ze.pdf.operationpdf.module.IPdfHandler;
import com.ze.pdf.operationpdf.module.impl.PdfHandler;
import com.ze.pdf.operationpdf.module.utils.ResourceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.ImageFilter;
import java.awt.image.RasterFormatException;
import java.io.*;

/**
 * @Author : ze
 * @Date : 2023/3/22 0:19
 * @Description : TODO
 */
@RestController
@RequestMapping("/pdfProcess")
@Slf4j
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

        log.info("pdf文件:{},img文件:{},scale参数:{}", pdfFile.getName(), imgFile.getName(), scale);

        // 获取各种流对象
        InputStream imgInputStream = null;
        InputStream pdfInputStream = null;
        PDDocument pdDocument = null;
        ByteArrayOutputStream outputStream = null;
        try {
            imgInputStream = imgFile.getInputStream();
            pdfInputStream = pdfFile.getInputStream();
            // 执行处理pdf逻辑
            pdDocument = pdfHandler.stampImgToPdf(imgInputStream, pdfInputStream, scale);
            // 封装处理后数据，返回给客户端
            outputStream = new ByteArrayOutputStream();
            pdDocument.save(new File(ResourceUtil.getResourcePath() + "pdf" + "/" + System.currentTimeMillis() + ".pdf"));
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

    /**
     * 图像不等分：
     * 根据传入scale缩放插入章大小；
     * 根据传入 fistScale、lastScale 首页比例、尾页比例，调整首尾也插入图像比例
     * 根据 accuracyCompensation 精度补偿调整章完整度
     *
     * @param pdfFile              pdf文档
     * @param imgFile              图像章
     * @param scale                图像章缩放比例
     * @param fistScale            首页比例
     * @param lastScale            尾页比例
     * @param accuracyCompensation 精度补偿
     * @return 已处理的pdf文档
     */
    @PostMapping("/stamp/unequal/pdfImgAndParam")
    public byte[] unequalPdfAndImageSize(@RequestParam("pdfFile") MultipartFile pdfFile,
                                         @RequestParam("imgFile") MultipartFile imgFile,
                                         Float scale,
                                         Float fistScale,
                                         Float lastScale,
                                         int accuracyCompensation,
                                         HttpServletResponse response) {

        log.info("pdf文件:{},\nimg文件:{},\nscale参数:{},\nfistScale:{},\nlastScale:{},\naccuracyCompensation:{}",
                pdfFile.getName(), imgFile.getName(), scale, fistScale, lastScale, accuracyCompensation);

        // 获取各种流对象
        InputStream imgInputStream = null;
        InputStream pdfInputStream = null;
        PDDocument pdDocument = null;
        ByteArrayOutputStream outputStream = null;
        try {
            imgInputStream = imgFile.getInputStream();
            pdfInputStream = pdfFile.getInputStream();
            // 执行处理pdf逻辑
            pdDocument = pdfHandler.stampImgToPdf(imgInputStream, pdfInputStream, scale, fistScale, lastScale, accuracyCompensation);
            // 封装处理后数据，返回给客户端
            outputStream = new ByteArrayOutputStream();
            pdDocument.save(new File(ResourceUtil.getResourcePath() + "pdf" + "/test" + System.currentTimeMillis() + ".pdf"));
            return outputStream.toByteArray();
        } catch (RasterFormatException rasterFormatException) {
            rasterFormatException.printStackTrace();
            System.out.println("精度补偿参数有误");
            response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            return new String("精度补偿参数有误").getBytes();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("系统错误");
            response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
            return new String("系统错误").getBytes();
        } finally {
            try {
                outputStream.close();
                pdDocument.close();
                pdfInputStream.close();
                imgInputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
