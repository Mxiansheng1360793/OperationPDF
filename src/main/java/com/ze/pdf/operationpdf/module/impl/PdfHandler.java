package com.ze.pdf.operationpdf.module.impl;

import com.ze.pdf.operationpdf.module.BaseHandler;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.awt.RenderingHints;

/**
 * @Author : ze
 * @Date : 2023/3/21 23:52
 * @Description : TODO
 */
@Component
public class PdfHandler extends BaseHandler {

    @Override
    public PDDocument stampImgToPdf(File image, File pdf) throws IOException {
        /**
         * 1、资源准备：
         *  获取图片
         *  获取PDF文件
         */
        // 准备图片和PDF资源
        BufferedImage img = ImageIO.read(image);
        PDDocument pdfDoc = PDDocument.load(pdf);
        // 获取PDF 所有页
        PDPageTree pages = pdfDoc.getPages();
        // 获取PDF 总页码
        int pagesCount = pages.getCount();

        /**
         * 2、根据PDF页码平均切分图片
         */
        List<BufferedImage> bufferedImages = segmentationEqualImg(img, pages, pagesCount);

        /**
         * 3、遍历每页PDF，给每页 PDF 插入指定切分图片
         */
        processPdf(pdfDoc, pages, pagesCount, bufferedImages);

        return pdfDoc;
    }

    @Override
    public PDDocument stampImgToPdf(InputStream imgInputStream, InputStream pdfInputStream) throws IOException {
        /**
         * 1、资源准备：
         *  获取图片
         *  获取PDF文件
         */
        // 准备图片和PDF资源
        BufferedImage img = ImageIO.read(imgInputStream);
        PDDocument pdfDoc = PDDocument.load(pdfInputStream);
        // 获取PDF 所有页
        PDPageTree pages = pdfDoc.getPages();
        // 获取PDF 总页码
        int pagesCount = pages.getCount();

        /**
         * 2、根据PDF页码平均切分图片
         */
        List<BufferedImage> bufferedImages = segmentationEqualImg(img, pages, pagesCount);

        /**
         * 3、遍历每页PDF，给每页 PDF 插入指定切分图片
         */
        processPdf(pdfDoc, pages, pagesCount, bufferedImages);

        return pdfDoc;
    }

    @Override
    public PDDocument stampScaleImgToPdf(InputStream imgInputStream, InputStream pdfInputStream, Float scale) throws IOException {
        /**
         * 1、资源准备：
         *  获取图片
         *  获取PDF文件
         */
        // 准备图片和PDF资源
        BufferedImage img = ImageIO.read(imgInputStream);
        PDDocument pdfDoc = PDDocument.load(pdfInputStream);
        // 获取PDF 所有页
        PDPageTree pages = pdfDoc.getPages();
        // 获取PDF 总页码
        int pagesCount = pages.getCount();

        /**
         * 2、缩放图片
         */
        BufferedImage scaleImage = scaleImage(img, scale);

        /**
         * 3、根据PDF页码平均切分图片
         */
        List<BufferedImage> bufferedImages = segmentationEqualImg(scaleImage, pages, pagesCount);

        /**
         * 4、遍历每页PDF，给每页 PDF 插入指定切分图片
         */
        processPdf(pdfDoc, pages, pagesCount, bufferedImages);

        return pdfDoc;
    }

    @Override
    public PDDocument stampScaleImgToPdf(File imgFile, File pdfFile, Float scale) throws IOException {
        /**
         * 1、资源准备：
         *  获取图片
         *  获取PDF文件
         */
        // 准备图片和PDF资源
        BufferedImage img = ImageIO.read(imgFile);
        PDDocument pdfDoc = PDDocument.load(pdfFile);
        // 获取PDF 所有页
        PDPageTree pages = pdfDoc.getPages();
        // 获取PDF 总页码
        int pagesCount = pages.getCount();

        /**
         * 2、缩放图片
         */
        BufferedImage scaleImage = scaleImage(img, scale);

        /**
         * 3、根据PDF页码平均切分图片
         */
        List<BufferedImage> bufferedImages = segmentationEqualImg(scaleImage, pages, pagesCount);

        /**
         * 4、遍历每页PDF，给每页 PDF 插入指定切分图片
         */
        processPdf(pdfDoc, pages, pagesCount, bufferedImages);

        return pdfDoc;
    }

    @Override
    public PDDocument stampScaleImgToPdf(File imgFile, File pdfFile, Float scale, Float fistScale, Float lastScale) throws IOException {
        /**
         * 1、资源准备：
         *  获取图片
         *  获取PDF文件
         */
        // 准备图片和PDF资源
        BufferedImage img = ImageIO.read(imgFile);
        PDDocument pdfDoc = PDDocument.load(pdfFile);
        // 获取PDF 所有页
        PDPageTree pages = pdfDoc.getPages();
        // 获取PDF 总页码
        int pagesCount = pages.getCount();
        int otherPagesCount = pagesCount - 2;

        /**
         * 2、缩放图片
         */
        BufferedImage scaleImage = scaleImage(img, scale);

        /**
         * 3、根据PDF页码,参数比例切分图片
         */
        List<BufferedImage> bufferedImages = segmentationImg(scaleImage, pages, pagesCount, otherPagesCount,
                fistScale, lastScale);

        /**
         * 4、遍历每页PDF，给每页 PDF 插入指定切分图片
         */
        processPdf(pdfDoc, pages, pagesCount, bufferedImages);

        return pdfDoc;
    }

    @Override
    public PDDocument stampScaleImgToPdf(InputStream imgInput,
                                         InputStream pdfInput,
                                         Float scale,
                                         Float fistScale,
                                         Float lastScale) throws IOException {
        /**
         * 1、资源准备：
         *  获取图片
         *  获取PDF文件
         */
        // 准备图片和PDF资源
        BufferedImage img = ImageIO.read(imgInput);
        PDDocument pdfDoc = PDDocument.load(pdfInput);
        // 获取PDF 所有页
        PDPageTree pages = pdfDoc.getPages();
        // 获取PDF 总页码
        int pagesCount = pages.getCount();
        int otherPagesCount = pagesCount - 2;

        /**
         * 2、缩放图片
         */
        BufferedImage scaleImage = scaleImage(img, scale);

        /**
         * 3、根据PDF页码,参数比例切分图片
         */
        List<BufferedImage> bufferedImages = segmentationImg(scaleImage, pages, pagesCount, otherPagesCount,
                fistScale, lastScale);

        /**
         * 4、遍历每页PDF，给每页 PDF 插入指定切分图片
         */
        processPdf(pdfDoc, pages, pagesCount, bufferedImages);

        return pdfDoc;
    }
}