package com.ze.pdf.operationpdf.controller;

import com.ze.pdf.operationpdf.module.IPdfHandler;
import com.ze.pdf.operationpdf.service.IPdfService;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
    IPdfService pdfService;

    /**
     * 图像等分：处理传入pdf，插入本地图片骑缝章
     * @param pdfFile pdf文档
     * @return 已处理的pdf文档
     * @throws IOException
     */
    @PostMapping("/local/halve/scaleStampToPdf")
    public ResponseEntity<byte[]> localHalveStampPdf(@RequestParam("pdfFile") MultipartFile pdfFile, Float scale) {

        HttpHeaders headers = new HttpHeaders();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             // 等分图片，执行PDF 处理逻辑
             PDDocument pdfDoc = pdfService.halveScaleLocalStampPdf(pdfFile, scale))
        {
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment","example.pdf");
            // 输出pdf
            pdfDoc.save(outputStream);
            return new ResponseEntity(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            headers.add("message","系统错误");
            return new ResponseEntity<>(null,headers,HttpStatus.BAD_GATEWAY);
        }
    }

    /**
     * 图像比例切分：处理传入pdf，插入本地图片骑缝章
     * @param pdfFile pdf文档
     * @return 已处理的pdf文档
     * @throws IOException
     */
    @PostMapping("/local/unequal/scaleStampToPdf")
    public ResponseEntity<byte[]> localUnequalStampPdf(@RequestParam("pdfFile") MultipartFile pdfFile,
                                       Float scale,
                                       Float fistScale,
                                       Float lastScale) {
        HttpHeaders headers = new HttpHeaders();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             // 等分图片，执行PDF 处理逻辑
             PDDocument pdfDoc = pdfService.unequalScaleLocalStampPdf(pdfFile, scale,fistScale,lastScale))
        {
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment","example.pdf");
            // 输出pdf
            pdfDoc.save(outputStream);
            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 图像等分：处理传入pdf和图像章
     * @param pdfFile pdf文档
     * @param imgFile 图像章
     * @return 已处理的pdf文档
     * @throws IOException
     */
    @PostMapping("/upload/halve/scaleStampToPdf")
    public ResponseEntity<byte[]> uploadHalveStampPdf(@RequestParam("pdfFile") MultipartFile pdfFile,
                                 @RequestParam("imgFile") MultipartFile imgFile,
                                 Float scale) {
        HttpHeaders headers = new HttpHeaders();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             // 等分图片，执行PDF 处理逻辑
             PDDocument pdfDoc = pdfService.halveScaleUploadStampPdf(pdfFile,imgFile,scale))
        {
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment","example.pdf");
            // 输出pdf
            pdfDoc.save(outputStream);
            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 图像比例切分：处理传入pdf和图像章
     *
     * @param pdfFile pdf文档
     * @param imgFile 图像章
     * @param scale   图像章缩放比例
     * @return 已处理的pdf文档
     */
    @PostMapping("/upload/unequal/scaleStampToPdf")
    public ResponseEntity<byte[]> uploadUnequalStampPdf(@RequestParam("pdfFile") MultipartFile pdfFile,
                                       @RequestParam("imgFile") MultipartFile imgFile,
                                       Float scale,
                                       Float fistScale,
                                       Float lastScale) {

//        log.info("pdf文件:{},img文件:{},scale参数:{},fistScale参数:{},lastScale:{}", pdfFile.getName(), imgFile.getName(), scale,fistScale,lastScale);
        HttpHeaders headers = new HttpHeaders();
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             // 等分图片，执行PDF 处理逻辑
             PDDocument pdfDoc = pdfService.unequalScaleUploadStampPdf(pdfFile,imgFile, scale,fistScale,lastScale))
        {
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment","example.pdf");
            // 输出pdf
            pdfDoc.save(outputStream);
            return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
