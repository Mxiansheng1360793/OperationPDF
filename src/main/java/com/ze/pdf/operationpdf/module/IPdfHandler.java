package com.ze.pdf.operationpdf.module;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author : ze
 * @Date : 2023/3/21 23:53
 * @Description : TODO
 */
public interface IPdfHandler {

    /**
     * 自动向PDF中每一个插入骑缝章
     *
     * @param img 插入图片File对象
     * @param pdf pdf文件File对象
     * @return
     */
    public PDDocument stampImgToPdf(File img, File pdf) throws IOException;


    /**
     * 自动向PDF中每一个插入骑缝章 - 重载
     *
     * @param imgInputStream 插入图片输入流
     * @param pdfInputStream pdf文件输入流
     * @return
     */
    public PDDocument stampImgToPdf(InputStream imgInputStream, InputStream pdfInputStream) throws IOException;


    /**
     * 自动向PDF中每一个插入骑缝章,根据缩放比例,调整插入章大小 - 重载
     *
     * @param imgInputStream 插入图片输入流
     * @param pdfInputStream pdf文件输入流
     * @param scale          插入章缩放比例
     * @return
     * @throws IOException
     */
    public PDDocument stampScaleImgToPdf(InputStream imgInputStream, InputStream pdfInputStream, Float scale) throws IOException;


    /**
     * 自动向PDF中每一个插入骑缝章,根据缩放比例,调整插入章大小 - 重载
     *
     * @param imgFile 插入图片输入流
     * @param pdfFile pdf文件输入流
     * @param scale   插入章缩放比例
     * @return
     * @throws IOException
     */
    public PDDocument stampScaleImgToPdf(File imgFile, File pdfFile, Float scale) throws IOException;

    /**
     * 自动向PDF中每一个插入骑缝章
     * 根据缩放比例,调整插入章大小
     * 根据传入的首位两页比列分配章 - 重载
     *
     * @param imgFile              插入图片文件
     * @param pdfFile              pdf文件
     * @param scale                插入章缩放比例
     * @param fistScale            首页插入章比例
     * @param lastScale            尾页插入章比例
     * @return
     * @throws IOException
     */
    public PDDocument stampScaleImgToPdf(File imgFile,
                                    File pdfFile,
                                    Float scale,
                                    Float fistScale,
                                    Float lastScale) throws IOException;

    /**
     * 自动向PDF中每一个插入骑缝章
     * 根据缩放比例,调整插入章大小
     * 根据传入的首位两页比列分配章 - 重载
     *
     * @param imgInput              插入图片输入流
     * @param pdfInput              pdf文件输入流
     * @param scale                插入章缩放比例
     * @param fistScale            首页插入章比例
     * @param lastScale            尾页插入章比例
     * @return
     * @throws IOException
     */
    public PDDocument stampScaleImgToPdf(InputStream imgInput,
                                    InputStream pdfInput,
                                    Float scale,
                                    Float fistScale,
                                    Float lastScale) throws IOException;

}
