package com.nisharp.web.infrastructure.qrcodw;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

import java.util.Map;

/**
 * Created by ZM.Wang
 */
public final class QRCodeWriter implements Writer {

    @Override
    public BitMatrix encode(String contents, BarcodeFormat format, int width, int height)
            throws WriterException {

        return encode(contents, format, width, height, null);
    }

    @Override
    public BitMatrix encode(String contents,
                            BarcodeFormat format,
                            int width,
                            int height,
                            Map<EncodeHintType, ?> hints) throws WriterException {

        QRCode code = getQrCode(contents, format, width, height, hints);
        return renderResult(code, width, height, 1);
    }

    public BitMatrix encode(String contents,
                            BarcodeFormat format,
                            int width,
                            int height,
                            int padding,
                            Map<EncodeHintType, ?> hints) throws WriterException {

        QRCode code = getQrCode(contents, format, width, height, hints);
        return renderResult(code, width, height, padding);
    }

    /**
     * 有白边
     *
     * @param code
     * @param width
     * @param height
     * @return
     */
    private static BitMatrix renderResult(QRCode code, int width, int height, int padding) {
        ByteMatrix input = code.getMatrix();
        if (input == null) {
            throw new IllegalStateException();
        }
        int inputWidth = input.getWidth();
        int inputHeight = input.getHeight();
        // 这里qrWidth就是原始的二维码的宽度了，包含8单位宽度的白边
        int qrWidth = inputWidth + padding;
        int qrHeight = inputHeight + padding;
        // 依据用户的输入宽高，计算最后的输出宽高
        int outputWidth = Math.max(width, qrWidth);
        int outputHeight = Math.max(height, qrHeight);

        //计算缩放比例
        int multiple = Math.min(outputWidth / qrWidth, outputHeight / qrHeight);

        // 计算白边的宽度
        int leftPadding = (outputWidth - inputWidth * multiple) / 2;
        int topPadding = (outputHeight - inputHeight * multiple) / 2;

        BitMatrix output = new BitMatrix(outputWidth, outputHeight);

        int inputY = 0;
        // 嵌套循环，将ByteMatrix的内容计算padding后转换成BitMatrix
        for (int outputY = topPadding; inputY < inputHeight; inputY++, outputY += multiple) {
            int inputX = 0;
            for (int outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
                if (input.get(inputX, inputY) == 1) {
                    output.setRegion(outputX, outputY, multiple, multiple);
                }
            }
        }
        return output;
    }

//    /**
//     * 说好的无白边但是会出问题，内容全都堆在左上角
//     * @param code
//     * @param width
//     * @param height
//     * @return
//     */
//
//    private static BitMatrix renderResult(QRCode code, int width, int height) {
//        ByteMatrix input = code.getMatrix();
//        if (input == null) {
//            throw new IllegalStateException();
//        }
//        int inputWidth = input.getWidth();
//        int inputHeight = input.getHeight();
//        // 依据用户的输入宽高，计算最后的输出宽高
//        int outputWidth = Math.max(width, inputWidth);
//        int outputHeight = Math.max(height, inputHeight);
//
//        //计算缩放比例
//        int multiple = Math.min(outputWidth / inputWidth, outputHeight / inputHeight);
//
//        BitMatrix output = new BitMatrix(outputWidth, outputHeight);
//        int inputY = 0;
//        // 嵌套循环，将ByteMatrix的内容计算padding后转换成BitMatrix
//        for (int outputY = 0; inputY < inputHeight; inputY++, outputY += multiple) {
//            int inputX = 0;
//            for (int outputX = 0; inputX < inputWidth; inputX++, outputX += multiple) {
//                if (input.get(inputX, inputY) == 1) {
//                    output.setRegion(outputX, outputY, multiple, multiple);
//                }
//            }
//        }
//        return output;
//    }

    //    /**
//     * 有白边
//     *
//     * @param code
//     * @param width
//     * @param height
//     * @param quietZone
//     * @return
//     */
//    private static BitMatrix renderResult(QRCode code, int width, int height, int quietZone) {
//        ByteMatrix input = code.getMatrix();
//        if (input == null) {
//            throw new IllegalStateException();
//        }
//        int inputWidth = input.getWidth();
//        int inputHeight = input.getHeight();
//        int qrWidth = inputWidth + (quietZone * 2);
//        int qrHeight = inputHeight + (quietZone * 2);
//        int outputWidth = Math.max(width, qrWidth);
//        int outputHeight = Math.max(height, qrHeight);
//
//        int multiple = Math.min(outputWidth / qrWidth, outputHeight / qrHeight);
//        // Padding includes both the quiet zone and the extra white pixels to accommodate the requested
//        // dimensions. For example, if input is 25x25 the QR will be 33x33 including the quiet zone.
//        // If the requested size is 200x160, the multiple will be 4, for a QR of 132x132. These will
//        // handle all the padding from 100x100 (the actual QR) up to 200x160.
//        int leftPadding = (outputWidth - (inputWidth * multiple)) / 2;
//        int topPadding = (outputHeight - (inputHeight * multiple)) / 2;
//
//        BitMatrix output = new BitMatrix(outputWidth, outputHeight);
//
//        for (int inputY = 0, outputY = topPadding; inputY < inputHeight; inputY++, outputY += multiple) {
//            // Write the contents of this row of the barcode
//            for (int inputX = 0, outputX = leftPadding; inputX < inputWidth; inputX++, outputX += multiple) {
//                if (input.get(inputX, inputY) == 1) {
//                    output.setRegion(outputX, outputY, multiple, multiple);
//                }
//            }
//        }
//        return output;
//    }
    private QRCode getQrCode(String contents, BarcodeFormat format, int width, int height, Map<EncodeHintType, ?> hints) throws WriterException {
        if (contents.isEmpty()) {
            throw new IllegalArgumentException("Found empty contents");
        }

        if (format != BarcodeFormat.QR_CODE) {
            throw new IllegalArgumentException("Can only encode QR_CODE, but got " + format);
        }

        if (width < 0 || height < 0) {
            throw new IllegalArgumentException("Requested dimensions are too small: " + width + 'x' +
                    height);
        }

        ErrorCorrectionLevel errorCorrectionLevel = ErrorCorrectionLevel.L;
        if (hints != null) {
            if (hints.containsKey(EncodeHintType.ERROR_CORRECTION)) {
                errorCorrectionLevel = ErrorCorrectionLevel.valueOf(hints.get(EncodeHintType.ERROR_CORRECTION).toString());
            }
        }

        return Encoder.encode(contents, errorCorrectionLevel, hints);
    }

}