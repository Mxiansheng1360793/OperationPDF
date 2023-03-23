package com.ze.pdf.operationpdf;

import com.ze.pdf.operationpdf.module.utils.ResourceUtil;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.mime.FileBody;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.entity.mime.StringBody;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.*;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.util.Timeout;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author : ze
 * @Date : 2023/3/22 21:20
 * @Description : TODO
 */
public class HttpTest {
    private File imgFile;
    private File pdfFile;

    private HttpClient httpClient;

    private final String URL_ONE = "http://localhost:8999/pdfProcess/stamp/halve/pdfImgAndParam";
    private final String URL_TWO = "http://localhost:8999/pdfProcess/stamp/unequal/pdfImgAndParam";

    @Before
    public void setUp() {
        imgFile = new File("C:\\Users\\MX\\Desktop\\dailyTask\\up-test\\mexport1679453123060.png");
        pdfFile = new File("C:\\Users\\MX\\Desktop\\dailyTask\\up-test\\GetReportFileOfPDF.pdf");

        httpClient = HttpClientBuilder.create().build();
    }

    @Test
    public void test01() throws IOException, ParseException {
        imgFile = new File("C:\\Users\\MX\\Desktop\\dailyTask\\up-test\\mmexport1679453123060.png");
        pdfFile = new File("C:\\Users\\MX\\Desktop\\dailyTask\\up-test\\GetReportFileOfPDF.pdf");
        httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(URL_TWO);
        // 配置请求参数
        RequestConfig requestConfig = RequestConfig.custom()
                // 设置连接超时时间
                .setConnectTimeout(Timeout.of(5000, TimeUnit.MILLISECONDS))
                // 设置响应超时时间
                .setResponseTimeout(10000, TimeUnit.MILLISECONDS)
                // 设置从连接池获取链接的超时时间
                .setConnectionRequestTimeout(5000, TimeUnit.MILLISECONDS)
                .build();
        post.setConfig(requestConfig);

        MultipartEntityBuilder builder = MultipartEntityBuilder.create();

        if (Objects.isNull(imgFile) || Objects.isNull(pdfFile)){
            System.out.println("文件为空");
        }

        builder.addPart("pdfFile", new FileBody(pdfFile, ContentType.APPLICATION_PDF,pdfFile.getName()));
        builder.addPart("imgFile", new FileBody(imgFile, ContentType.IMAGE_PNG,imgFile.getName()));
        builder.addPart("scale", new StringBody("0.1", ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8)));
        builder.addPart("fistScale", new StringBody("0.4", ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8)));
        builder.addPart("lastScale", new StringBody("0.2", ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8)));
        builder.addPart("accuracyCompensation", new StringBody("5", ContentType.TEXT_PLAIN.withCharset(StandardCharsets.UTF_8)));

        HttpEntity httpEntity = builder.build();

        post.setEntity(httpEntity);

        ClassicHttpResponse httpResponse = (ClassicHttpResponse) httpClient.execute(post);

        String path = ResourceUtil.getResourcePath() + "pdf" + "/" + System.currentTimeMillis() + ".pdf";

        if (httpResponse.getCode() == HttpStatus.SC_OK) {
            // 解析响应
            HttpEntity entity = httpResponse.getEntity();
            String contentType = entity.getContentType();
            InputStream resInputStream = entity.getContent();
            if (contentType.equalsIgnoreCase("application/pdf")) {
                FileOutputStream fileOutputStream = new FileOutputStream(new File(path));
                Files.copy(resInputStream, Paths.get(fileOutputStream.toString()));
            }
            EntityUtils.consume(entity);
            System.out.println("pdf文件处理成功");
        } else {
            // 响应状态码不为200
            System.out.println("Server returned " + httpResponse.getCode());
            HttpEntity entity = httpResponse.getEntity();

            String s = EntityUtils.toString(entity);

        }
    }
}
