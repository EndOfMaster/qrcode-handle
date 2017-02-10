package com.nisharp.web.service;

import com.google.zxing.WriterException;
import com.nisharp.web.infrastructure.util.QrcodeUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author ZM.Wang
 */
@Service
public class QrcodeService {

    public void getQrcode(String content, int width, HttpServletResponse response) throws WriterException, IOException {
        response.setContentType("image/png");
        response.setHeader("Content-Disposition", "inline; filename=" + DigestUtils.md5Hex(content)+".png");
        BufferedImage image = QrcodeUtils.createQrcode(content, width);
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            try (ByteArrayOutputStream file = QrcodeUtils.imageToStream(image)) {
                response.setContentLength(file.toByteArray().length);
                outputStream.write(file.toByteArray());
            }
        }

    }

}
