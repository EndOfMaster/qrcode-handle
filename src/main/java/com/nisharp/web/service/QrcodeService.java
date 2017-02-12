package com.nisharp.web.service;

import cn.payingcloud.commons.rest.exception.BadRequestException;
import com.google.zxing.WriterException;
import com.nisharp.web.infrastructure.util.QrcodeUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author ZM.Wang
 */
@Service
public class QrcodeService {

    public void getQrcode(String content, int width, String paddingStr, HttpServletResponse response) throws WriterException, IOException {
        response.setContentType("image/png");
        response.setHeader("Content-Disposition", "inline; filename=" + DigestUtils.md5Hex(content) + ".png");
        int padding;
        try {
            padding = Integer.valueOf(paddingStr);
        } catch (NumberFormatException e) {
            throw new BadRequestException("数字格式不正确");
        }
        BufferedImage image = QrcodeUtils.createSimpleQrcode(content, width, padding, new Color(255, 255, 255));
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            try (ByteArrayOutputStream file = QrcodeUtils.imageToStream(image)) {
                response.setContentLength(file.toByteArray().length);
                outputStream.write(file.toByteArray());
            }
        }

    }

}
