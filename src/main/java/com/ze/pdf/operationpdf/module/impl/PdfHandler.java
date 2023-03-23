package com.ze.pdf.operationpdf.module.impl;

import com.ze.pdf.operationpdf.module.IPdfHandler;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
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
public class PdfHandler implements IPdfHandler {

    @Override
    public PDDocument stampImgToPdf(File image, File pdf) throws IOException {
        /**
         * 1、资源准备：
         *  获取图片
         *  获取PDF文件
         */
        // 读取待插入图片
        BufferedImage img = ImageIO.read(image);
        // 读取pdf文件
        PDDocument pdfDoc = PDDocument.load(pdf);

        // 获取pdf所有页
        PDPageTree pages = pdfDoc.getPages();
        // 获取pdf页码
        int pagesCount = pages.getCount();

        // 获取插入图片的宽度和高度
        float imageWidth = img.getWidth();
        float imageHeight = img.getHeight();

        /**
         * 2、根据 PDF 页码切分图片
         */
        // 计算等分宽度
        float blockWidth = imageWidth / pagesCount;
        List<BufferedImage> blockImages = new ArrayList<>();
        // 根据等分宽度切分插入图片，并保存到切分图片集合中
        for (int i = 0; i < pagesCount; i++) {
            float posX = i * blockWidth;
            float posY = imageHeight;
            BufferedImage block = img.getSubimage((int) posX, 0, (int) blockWidth, (int) imageHeight);
            blockImages.add(block);
        }

        /**
         * 3、遍历每页PDF，给每页 PDF 插入指定切分图片
         */
        // 循环遍历 pdfPages,向pdf每页指定位置插入图片
        for (int i = 0; i < pages.getCount(); i++) {
            // 获取每页的pdf
            PDPage pdPage = pdfDoc.getPage(i);
            // 获取每页要插入的等分image图片
            BufferedImage blockImage = blockImages.get(i);
            // 获取页面宽度和高度
            float width = pdPage.getMediaBox().getWidth();
            float height = pdPage.getMediaBox().getHeight();

            // 计算图片缩放比例
            float scaleX = blockImage.getWidth() / width;
            float scaleY = blockImage.getHeight() / height;
            float scale = Math.max(scaleX, scaleY);

            // 计算图片大小和插入坐标位置
            float blockImageWidth = blockImage.getWidth() * 1;
            float blockImageHeight = blockImage.getHeight() * 1;

            float x = width - blockImageWidth;
            float y = (height - blockImageHeight) / 2;

            // 创建内容流
            PDPageContentStream contentStream = new PDPageContentStream(pdfDoc, pdPage, PDPageContentStream.AppendMode.APPEND, true);
            PDImageXObject pdImage = LosslessFactory.createFromImage(pdfDoc, blockImage);
            contentStream.drawImage(pdImage, x, y, blockImageWidth, blockImageHeight);
            // 关闭内容流
            contentStream.close();
        }
        return pdfDoc;
    }

    @Override
    public PDDocument stampImgToPdf(InputStream imgInputStream, InputStream pdfInputStream) throws IOException {
        /**
         * 1、资源准备：
         *  获取图片
         *  获取PDF文件
         */

        // 读取待插入图片
        BufferedImage img = ImageIO.read(imgInputStream);
        // 读取pdf文件
        PDDocument pdfDoc = PDDocument.load(pdfInputStream);

        // 获取pdf所有页
        PDPageTree pages = pdfDoc.getPages();
        // 获取pdf页码
        int pagesCount = pages.getCount();

        // 获取插入图片的宽度和高度
        float imageWidth = img.getWidth();
        float imageHeight = img.getHeight();

        /**
         * 2、根据 PDF 页码切分图片
         */
        // 计算等分宽度
        float blockWidth = imageWidth / pagesCount;
        List<BufferedImage> blockImages = new ArrayList<>();
        // 根据等分宽度切分插入图片，并保存到切分图片集合中
        for (int i = 0; i < pagesCount; i++) {
            float posX = i * blockWidth;
            float posY = imageHeight;
            BufferedImage block = img.getSubimage((int) posX, 0, (int) blockWidth, (int) imageHeight);
            blockImages.add(block);
        }

        /**
         * 3、遍历每页PDF，给每页 PDF 插入指定切分图片
         */
        // 循环遍历 pdfPages,向pdf每页指定位置插入图片
        for (int i = 0; i < pages.getCount(); i++) {
            // 获取每页的pdf
            PDPage pdPage = pdfDoc.getPage(i);
            // 获取每页要插入的等分image图片
            BufferedImage blockImage = blockImages.get(i);
            // 获取页面宽度和高度
            float width = pdPage.getMediaBox().getWidth();
            float height = pdPage.getMediaBox().getHeight();

            // 计算图片缩放比例
            float scaleX = blockImage.getWidth() / width;
            float scaleY = blockImage.getHeight() / height;
            float scale = Math.max(scaleX, scaleY);

            // 计算图片大小和插入坐标位置
            float blockImageWidth = blockImage.getWidth() * 1;
            float blockImageHeight = blockImage.getHeight() * 1;

            float x = width - blockImageWidth;
            float y = (height - blockImageHeight) / 2;

            // 创建内容流
            PDPageContentStream contentStream = new PDPageContentStream(pdfDoc, pdPage, PDPageContentStream.AppendMode.APPEND, true);
            PDImageXObject pdImage = LosslessFactory.createFromImage(pdfDoc, blockImage);
            contentStream.drawImage(pdImage, x, y, blockImageWidth, blockImageHeight);
            // 关闭内容流
            contentStream.close();
        }
        return pdfDoc;
    }

    @Override
    public PDDocument stampImgToPdf(InputStream imgInputStream, InputStream pdfInputStream, Float scale) throws IOException {

        /**
         * 1、资源准备：
         *  获取图片
         *  获取PDF文件
         */
        // 读取待插入图片
        BufferedImage bufferedImage = ImageIO.read(imgInputStream);
        // 读取pdf文件
        PDDocument pdfDoc = PDDocument.load(pdfInputStream);

        // 获取pdf所有页
        PDPageTree pages = pdfDoc.getPages();
        // 获取pdf页码
        int pagesCount = pages.getCount();

        /**
         * 2、根据设定比例缩放图片
         */
        // 根据缩放比例获取插入图像缩放大小
        float imageWidth = bufferedImage.getWidth() * scale;
        float imageHeight = bufferedImage.getHeight() * scale;
        // 缩放图片
        Image image = bufferedImage.getScaledInstance((int) imageWidth, (int) imageHeight, Image.SCALE_SMOOTH);
        // 创建一个空白的bufferImage缓存区，宽高与原图相同
        BufferedImage bufferImg = new BufferedImage(image.getWidth(null), image.getHeight(null), bufferedImage.getType());
        // 获取 Graphics2D 上下文
        Graphics2D g2d = bufferImg.createGraphics();
        // 在bufferImage 上绘制img
        g2d.drawImage(image, 0, 0, null);
        // 释放资源
        g2d.dispose();

        /**
         * 3、根据 PDF 页码切分图片
         */
        // 计算等分宽度
        float blockWidth = imageWidth / pagesCount;
        List<BufferedImage> blockImages = new ArrayList<>();
        // 根据等分宽度切分插入图片，并保存到切分图片集合中
        for (int i = 0; i < pagesCount; i++) {
            float posX = i * blockWidth;
            float posY = imageHeight;
            BufferedImage block = bufferImg.getSubimage((int) posX, 0, (int) blockWidth, (int) imageHeight);
            blockImages.add(block);
        }

        /**
         * 4、遍历每页PDF，给每页 PDF 插入指定切分图片
         */
        // 循环遍历 pdfPages,向pdf每页指定位置插入图片
        for (int i = 0; i < pages.getCount(); i++) {
            // 获取每页的pdf
            PDPage pdPage = pdfDoc.getPage(i);
            // 获取每页要插入的等分image图片
            BufferedImage blockImage = blockImages.get(i);
            // 获取页面宽度和高度
            float width = pdPage.getMediaBox().getWidth();
            float height = pdPage.getMediaBox().getHeight();

            // 计算图片大小和插入坐标位置
            float blockImageWidth = blockImage.getWidth() * 1;
            float blockImageHeight = blockImage.getHeight() * 1;

            float x = width - blockImageWidth;
            float y = (height - blockImageHeight) / 2;

            // 创建内容流
            PDPageContentStream contentStream = new PDPageContentStream(pdfDoc, pdPage, PDPageContentStream.AppendMode.APPEND, true);
            PDImageXObject pdImage = LosslessFactory.createFromImage(pdfDoc, blockImage);
            contentStream.drawImage(pdImage, x, y, blockImageWidth, blockImageHeight);
            // 关闭内容流
            contentStream.close();
        }
        return pdfDoc;
    }

    @Override
    public PDDocument stampImgToPdf(File imgFile, File pdfFile, Float scale) throws IOException {
        /**
         * 1、资源准备：
         *  获取图片
         *  获取PDF文件
         */
        // 读取待插入图片
        BufferedImage bufferedImage = ImageIO.read(imgFile);
        // 读取pdf文件
        PDDocument pdfDoc = PDDocument.load(pdfFile);
        // 获取pdf所有页
        PDPageTree pages = pdfDoc.getPages();
        // 获取pdf页码
        int pagesCount = pages.getCount();

        /**
         * 2、根据设定比例缩放图片
         */
        // 根据缩放比例获取插入图像缩放大小
        float imageWidth = bufferedImage.getWidth() * scale;
        float imageHeight = bufferedImage.getHeight() * scale;
        // 缩放图片
        Image scaledImage = bufferedImage.getScaledInstance((int) imageWidth, (int) imageHeight, Image.SCALE_SMOOTH);
        // 创建一个空白的bufferImage缓存区，宽高与原图相同
        BufferedImage img = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), bufferedImage.getType());
        // 获取 Graphics2D 上下文
        Graphics2D g2d = img.createGraphics();
        // 设置抗锯齿和插值参数 - 保留清晰度
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        // 在bufferImage 上绘制img
        g2d.drawImage(scaledImage, 0, 0, null);
        // 释放资源
        g2d.dispose();

        /**
         * 3、根据 PDF 页码切分图片
         */
        // 计算等分宽度
        float blockWidth = imageWidth / pagesCount;
        List<BufferedImage> blockImages = new ArrayList<>();
        // 根据等分宽度切分插入图片，并保存到切分图片集合中
        for (int i = 0; i < pagesCount; i++) {
            float posX = i * blockWidth;
            BufferedImage block = img.getSubimage((int) posX, 0, (int) blockWidth, (int) imageHeight);
            blockImages.add(block);
        }

        /**
         * 4、遍历每页PDF，给每页 PDF 插入指定切分图片
         */
        // 循环遍历 pdfPages,向pdf每页指定位置插入图片
        for (int i = 0; i < pages.getCount(); i++) {
            // 获取每页的pdf
            PDPage pdPage = pdfDoc.getPage(i);
            // 获取每页要插入的等分image图片
            BufferedImage blockImage = blockImages.get(i);
            // 获取页面宽度和高度
            float width = pdPage.getMediaBox().getWidth();
            float height = pdPage.getMediaBox().getHeight();

            // 计算图片大小和插入坐标位置
            float blockImageWidth = blockImage.getWidth() * 1;
            float blockImageHeight = blockImage.getHeight() * 1;
            float x = width - blockImageWidth;
            float y = (height - blockImageHeight) / 2;

            // 创建内容流
            PDPageContentStream contentStream = new PDPageContentStream(pdfDoc, pdPage, PDPageContentStream.AppendMode.APPEND, true);
            PDImageXObject pdImage = LosslessFactory.createFromImage(pdfDoc, blockImage);
            contentStream.drawImage(pdImage, x, y, blockImageWidth, blockImageHeight);
            // 关闭内容流
            contentStream.close();
        }
        return pdfDoc;
    }

    @Override
    public PDDocument stampImgToPdf(File imgFile, File pdfFile, Float scale, Float fistScale, Float lastScale, Integer accuracyCompensation) throws IOException {
        /**
         * 1、资源准备：
         *  获取图片
         *  获取PDF文件
         */
        // 读取待插入图片
        BufferedImage bufferedImage = ImageIO.read(imgFile);
        // 读取pdf文件
        PDDocument pdfDoc = PDDocument.load(pdfFile);
        // 获取pdf所有页
        PDPageTree pages = pdfDoc.getPages();
        // 获取pdf页码
        int pagesCount = pages.getCount();
        // 获取减去首、尾页的页数
        int otherPages = pagesCount - 2;

        /**
         * 2、根据设定比例缩放图片
         */
        // 根据缩放比例获取插入图像缩放大小
        int imageWidth = (int) (bufferedImage.getWidth() * scale);
        int imageHeight = (int) (bufferedImage.getHeight() * scale);
        // 缩放图片
        Image scaledImage = bufferedImage.getScaledInstance((int) imageWidth, (int) imageHeight, Image.SCALE_SMOOTH);
        // 创建一个空白的bufferImage缓存区，宽高与原图相同
        BufferedImage img = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), bufferedImage.getType());
        // 获取 Graphics2D 上下文
        Graphics2D g2d = img.createGraphics();
        // 设置抗锯齿和插值参数 - 保留清晰度
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        // 在bufferImage 上绘制img
        g2d.drawImage(scaledImage, 0, 0, null);
        // 释放资源
        g2d.dispose();

        /**
         * 3、根据 PDF 页码切分图片
         */
        // 计算等分宽度
        float fullScale = 1f;
        if (fistScale + lastScale > 1) {
            // 如果首页+尾页比例超过1，传入参数有误
            System.out.println("传入的首页、尾页比例错误");
            return null;
        }
        // 计算首页宽度
        int fistWidth = (int) (imageWidth * fistScale);
        // 计算尾页宽度
        int lastWidth = (int) (imageWidth * lastScale);
        // 计算其余页宽度
        int equalWidth = (imageWidth - fistWidth - lastWidth) / otherPages;

        List<BufferedImage> blockImages = new ArrayList<>();
        // 根据等分宽度切分插入图片，并保存到切分图片集合中
        int startX = 0;
        for (int i = 0; i < pagesCount; i++) {
            try {
                if (i == 0) {
                    BufferedImage block = img.getSubimage(startX, 0, (int) fistWidth, imageHeight);
                    blockImages.add(block);
                    startX = fistWidth;
                    continue;
                } else if (i == pagesCount) {
                    try {
                        BufferedImage block = img.getSubimage(startX, 0, imageWidth - startX, imageHeight);
                        blockImages.add(block);
                        continue;
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("精度补偿超出");
                    }
                }
                BufferedImage block = img.getSubimage(startX, 0, equalWidth, imageHeight);
                blockImages.add(block);
                startX += equalWidth + accuracyCompensation;
            } catch (RasterFormatException rasterFormatException) {
                rasterFormatException.printStackTrace();
                System.out.println("精度补偿超出");
            }
        }

        /**
         * 4、遍历每页PDF，给每页 PDF 插入指定切分图片
         */
        // 循环遍历 pdfPages,向pdf每页指定位置插入图片
        for (int i = 0; i < pages.getCount(); i++) {
            // 获取每页的pdf
            PDPage pdPage = pdfDoc.getPage(i);
            // 获取每页要插入的等分image图片
            BufferedImage blockImage = blockImages.get(i);
            // 获取页面宽度和高度
            float width = pdPage.getMediaBox().getWidth();
            float height = pdPage.getMediaBox().getHeight();

            // 计算图片大小和插入坐标位置
            float blockImageWidth = blockImage.getWidth();
            float blockImageHeight = blockImage.getHeight();
            float x = width - blockImageWidth;
            float y = (height - blockImageHeight) / 2;

            // 创建内容流
            PDPageContentStream contentStream = new PDPageContentStream(pdfDoc, pdPage, PDPageContentStream.AppendMode.APPEND, true);
            PDImageXObject pdImage = LosslessFactory.createFromImage(pdfDoc, blockImage);
            contentStream.drawImage(pdImage, x, y, blockImageWidth, blockImageHeight);
            // 关闭内容流
            contentStream.close();
        }
        return pdfDoc;
    }

    @Override
    public PDDocument stampImgToPdf(InputStream imgInput, InputStream pdfInput, Float scale, Float fistScale,
                                    Float lastScale, Integer accuracyCompensation) throws IOException {
        /**
         * 1、资源准备：
         *  获取图片
         *  获取PDF文件
         */
        // 读取待插入图片
        BufferedImage bufferedImage = ImageIO.read(imgInput);
        // 读取pdf文件
        PDDocument pdfDoc = PDDocument.load(pdfInput);
        // 获取pdf所有页
        PDPageTree pages = pdfDoc.getPages();
        // 获取pdf页码
        int pagesCount = pages.getCount();
        // 获取减去首、尾页的页数
        int otherPages = pagesCount - 2;

        /**
         * 2、根据设定比例缩放图片
         */
        // 根据缩放比例获取插入图像缩放大小
        int imageWidth = (int) (bufferedImage.getWidth() * scale);
        int imageHeight = (int) (bufferedImage.getHeight() * scale);
        // 缩放图片
        Image scaledImage = bufferedImage.getScaledInstance((int) imageWidth, (int) imageHeight, Image.SCALE_SMOOTH);
        // 创建一个空白的bufferImage缓存区，宽高与原图相同
        BufferedImage img = new BufferedImage(scaledImage.getWidth(null), scaledImage.getHeight(null), bufferedImage.getType());
        // 获取 Graphics2D 上下文
        Graphics2D g2d = img.createGraphics();
        // 设置抗锯齿和插值参数 - 保留清晰度
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        // 在bufferImage 上绘制img
        g2d.drawImage(scaledImage, 0, 0, null);
        // 释放资源
        g2d.dispose();

        /**
         * 3、根据 PDF 页码切分图片
         */
        // 计算等分宽度
        float fullScale = 1f;
        if (fistScale + lastScale > 1) {
            // 如果首页+尾页比例超过1，传入参数有误
            System.out.println("传入的首页、尾页比例错误");
            return null;
        }
        // 计算首页宽度
        int fistWidth = (int) (imageWidth * fistScale);
        // 计算尾页宽度
        int lastWidth = (int) (imageWidth * lastScale);
        // 计算其余页宽度
        int equalWidth = (imageWidth - fistWidth - lastWidth) / otherPages;

        List<BufferedImage> blockImages = new ArrayList<>();
        // 根据等分宽度切分插入图片，并保存到切分图片集合中
        int startX = 0;
        for (int i = 0; i < pagesCount; i++) {
            if (i == 0) {
                BufferedImage block = img.getSubimage(startX, 0, (int) fistWidth, imageHeight);
                blockImages.add(block);
                startX = fistWidth;
                continue;
            } else if (i == pagesCount) {
                try {
                    BufferedImage block = img.getSubimage(startX, 0, imageWidth - startX, imageHeight);
                    blockImages.add(block);
                    continue;
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("精度补偿超出");
                }
            }
            BufferedImage block = img.getSubimage(startX, 0, equalWidth, imageHeight);
            blockImages.add(block);
            startX += equalWidth + accuracyCompensation;
//            }catch (RasterFormatException rasterFormatException){
//                rasterFormatException.printStackTrace();
//                System.out.println("精度补偿超出");
//            }
        }

        /**
         * 4、遍历每页PDF，给每页 PDF 插入指定切分图片
         */
        // 循环遍历 pdfPages,向pdf每页指定位置插入图片
        for (int i = 0; i < pages.getCount(); i++) {
            // 获取每页的pdf
            PDPage pdPage = pdfDoc.getPage(i);
            // 获取每页要插入的等分image图片
            BufferedImage blockImage = blockImages.get(i);
            // 获取页面宽度和高度
            float width = pdPage.getMediaBox().getWidth();
            float height = pdPage.getMediaBox().getHeight();

            // 计算图片大小和插入坐标位置
            float blockImageWidth = blockImage.getWidth();
            float blockImageHeight = blockImage.getHeight();
            float x = width - blockImageWidth;
            float y = (height - blockImageHeight) / 2;

            // 创建内容流
            PDPageContentStream contentStream = new PDPageContentStream(pdfDoc, pdPage, PDPageContentStream.AppendMode.APPEND, true);
            PDImageXObject pdImage = LosslessFactory.createFromImage(pdfDoc, blockImage);
            contentStream.drawImage(pdImage, x, y, blockImageWidth, blockImageHeight);
            // 关闭内容流
            contentStream.close();
        }
        return pdfDoc;
    }
}