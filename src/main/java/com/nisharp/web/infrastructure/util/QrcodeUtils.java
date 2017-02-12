package com.nisharp.web.infrastructure.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.nisharp.web.infrastructure.qrcodw.QRCodeWriter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author ZM.Wang
 */
public class QrcodeUtils {
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int FRAME_WIDTH = 2;
    private static QRCodeWriter qrCodeWriter = new QRCodeWriter();


    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    public static BufferedImage createQrcode(String content, int width) throws WriterException {
        BitMatrix bitMatrix = getBitMatrix(content, width);
        bitMatrix = deleteWhite(bitMatrix);
        return toBufferedImage(bitMatrix);
    }


    public static BufferedImage createQrcode(String content, int width, int padding) throws WriterException {
        BitMatrix bitMatrix = getBitMatrix(content, width);
        bitMatrix = deleteWhite(bitMatrix);
        BufferedImage masterMap = toBufferedImage(bitMatrix);
        if (padding == 0) {
            return imageZoom(masterMap, width, width);
        } else if (padding > 0) {
            int masterMapWidth = masterMap.getWidth();
            int masterMapHeight = masterMap.getHeight();
            int targetWidth = width - (padding * 2);
            BufferedImage qrcode = imageZoom(masterMap, targetWidth, targetWidth);
        } else {
            throw new RuntimeException("padding必须为自然数");
        }
        return masterMap;
    }

    private static BitMatrix getBitMatrix(String content, int width) throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        return qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, width, hints);
    }


    public static ByteArrayOutputStream imageToStream(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        return outputStream;
    }

    private static BufferedImage imageZoom(BufferedImage masterMap, int width, int height) {
        Image destImage = zoom(masterMap, width, height);
        //真正图，把缩小的图画到这个里面
        if (width == destImage.getWidth(null)) {
            return drawing(width, height, destImage, 0, (height - destImage.getHeight(null)) / 2);
        }
        return drawing(width, height, destImage, (width - destImage.getWidth(null)) / 2, 0);
    }

    private static Image zoom(BufferedImage masterMap, int width, int height) {
        double ratio; // 缩放比例
        // 计算比例
        if (masterMap.getHeight() > masterMap.getWidth()) {
            ratio = (new Integer(height)).doubleValue() / masterMap.getHeight();
        } else {
            ratio = (new Integer(width)).doubleValue() / masterMap.getWidth();
        }
        AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
        //把图缩小
        return op.filter(masterMap, null);
    }

    private static BufferedImage drawing(int width, int height, Image destImage, int x, int y) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphic = image.createGraphics();
        graphic.setColor(Color.white);
        graphic.fillRect(0, 0, width, height);
        graphic.drawImage(destImage, x, y, destImage.getWidth(null), destImage.getHeight(null), Color.white, null);
        graphic.dispose();
        return image;
    }

    private static BufferedImage genBarcode(String content, int width, int height, String srcImagePath) throws WriterException, IOException {
        int iconWidth = width / 4;
        int iconHeight = width / 4;
        int iconHalfWidth = iconWidth / 2;
        // 读取源图像
        File imageFile = new File(srcImagePath);
        //传进来的图
        BufferedImage srcImage = ImageIO.read(imageFile);

        BufferedImage scaleImage = imageZoom(srcImage, iconWidth, iconHeight);

        //得到一个二维数组
        int[][] srcPixels = new int[iconHeight][iconWidth];

        //存储每个像素点的颜色
        for (int i = 0; i < scaleImage.getWidth(); i++) {
            for (int j = 0; j < scaleImage.getHeight(); j++) {
                srcPixels[i][j] = scaleImage.getRGB(i, j);//图片的像素点
            }
        }
        //编码
        Hashtable<EncodeHintType, Object> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 生成二维码
        BitMatrix matrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, 0, hints);

        // 二维矩阵转为一维像素数组
        int halfW = matrix.getWidth() / 2;
        int halfH = matrix.getHeight() / 2;
        int[] pixels = new int[width * height];

        for (int y = 0; y < matrix.getHeight(); y++) {
            for (int x = 0; x < matrix.getWidth(); x++) {
                // 左上角颜色,根据自己需要调整颜色范围和颜色
//                if (x > 0 && x < 170 && y > 0 && y < 170) {
//                    Color color = new Color(231, 144, 56);
//                    int colorInt = color.getRGB();
//                    pixels[y * width + x] = matrix.get(x, y) ? colorInt
//                            : 16777215;
//                }
//                // 读取图片
//                else
                if (x > halfW - iconHalfWidth && x < halfW + iconHalfWidth && y > halfH - iconHalfWidth
                        && y < halfH + iconHalfWidth) {
                    pixels[y * width + x] = srcPixels[x - halfW + iconHalfWidth][y - halfH + iconHalfWidth];
                }

                // 在图片四周形成边框
                else if ((x > halfW - iconHalfWidth - FRAME_WIDTH
                        && x < halfW - iconHalfWidth + FRAME_WIDTH
                        && y > halfH - iconHalfWidth - FRAME_WIDTH && y < halfH
                        + iconHalfWidth + FRAME_WIDTH)
                        || (x > halfW + iconHalfWidth - FRAME_WIDTH
                        && x < halfW + iconHalfWidth + FRAME_WIDTH
                        && y > halfH - iconHalfWidth - FRAME_WIDTH && y < halfH
                        + iconHalfWidth + FRAME_WIDTH)
                        || (x > halfW - iconHalfWidth - FRAME_WIDTH
                        && x < halfW + iconHalfWidth + FRAME_WIDTH
                        && y > halfH - iconHalfWidth - FRAME_WIDTH && y < halfH
                        - iconHalfWidth + FRAME_WIDTH)
                        || (x > halfW - iconHalfWidth - FRAME_WIDTH
                        && x < halfW + iconHalfWidth + FRAME_WIDTH
                        && y > halfH + iconHalfWidth - FRAME_WIDTH && y < halfH
                        + iconHalfWidth + FRAME_WIDTH)) {

                    pixels[y * width + x] = Color.white.getRGB();
                } else {
                    // 二维码颜色（RGB）
                    int num1 = (int) (50 - (50.0 - 13.0) / matrix.getHeight() * (y + 1));
                    int num2 = (int) (165 - (165.0 - 72.0) / matrix.getHeight() * (y + 1));
                    int num3 = (int) (162 - (162.0 - 107.0) / matrix.getHeight() * (y + 1));
                    Color color = new Color(num1, num2, num3);
                    int colorInt = color.getRGB();
                    // 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；
                    //最后那个是透明色
                    pixels[y * width + x] = matrix.get(x, y) ? colorInt : 16777215;
                }
            }
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        image.getRaster().setDataElements(0, 0, width, height, pixels);
        return image;
    }


    public static void encode(String content, int width, int height, String srcImagePath, String destImagePath) {
        try {
            //生成图片文件
            ImageIO.write(genBarcode(content, width, height, srcImagePath), "png", new File(destImagePath));
        } catch (IOException | WriterException e) {
            e.printStackTrace();
        }
    }

    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] - 1;
        int resHeight = rec[3] - 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }
        return resMatrix;
    }

    public static void main(String[] args) throws IOException, WriterException {
        // 依次为内容(不支持中文),宽,长,logo图标路径,储存路径
        QrcodeUtils.encode("http://www.baidu.com/http://www.baidu.com/http://www.baidu.com/http://www.baidu.com/", 700, 700, "D:\\logo.jpg",
                "D:\\201301.png");

        BufferedImage image = QrcodeUtils.createQrcode("http://www.baidu.com/http://www.baidu.com/http://www.baidu.com/http://www.baidu.com/", 1000, 1);
        try (FileOutputStream outputStream = new FileOutputStream("d:/zi.png")) {
            try (ByteArrayOutputStream file = QrcodeUtils.imageToStream(image)) {
                outputStream.write(file.toByteArray());
            }
        }
    }

}
