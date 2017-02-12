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
import java.util.Map;

/**
 * @author ZM.Wang
 */
public class QrcodeUtils {
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    private static final int FRAME_WIDTH = 2;
    private static QRCodeWriter qrCodeWriter = new QRCodeWriter();
    private static final Color color = new Color(255, 255, 255);

    /**
     * 生成简单二维码
     *
     * @param content 内容
     * @param width   宽度
     * @param padding 边框
     * @return
     * @throws WriterException
     */
    public static BufferedImage createSimpleQrcode(String content, int width, int padding, Color color) throws WriterException {
        if (padding * 2 > width - 50) {
            throw new RuntimeException("边框过大");
        }
        BitMatrix bitMatrix = getBitMatrix(content, width);
        bitMatrix = deleteWhite(bitMatrix);
        BufferedImage masterMap = toBufferedImage(bitMatrix, color == null ? QrcodeUtils.color : color);
        return qrcodePaddingHandle(width, padding, masterMap, color == null ? QrcodeUtils.color : color);
    }

    public static BufferedImage getComplexQrcode(String content, int width, BufferedImage srcImage, int padding) throws WriterException, IOException {
        if (padding * 2 > width - 50) {
            throw new RuntimeException("边框过大");
        }
        int iconWidth = width / 4;
        int iconHeight = width / 4;
        int iconHalfWidth = iconWidth / 2;

        //logo图
        BufferedImage scaleImage = imageZoom(srcImage, iconWidth, iconHeight, color);
        //得到一个二维数组
        int[][] srcPixels = new int[iconHeight][iconWidth];
        //存储每个像素点的颜色
        for (int i = 0; i < scaleImage.getWidth(); i++) {
            for (int j = 0; j < scaleImage.getHeight(); j++) {
                srcPixels[i][j] = scaleImage.getRGB(i, j);//图片的像素点
            }
        }

        // 生成二维码
        BitMatrix matrix = getBitMatrix(content, width);
        matrix = deleteWhite(matrix);
        int qrcodeWidth = matrix.getWidth();
        int qrcodeHeight = matrix.getHeight();
        // 二维矩阵转为一维像素数组
        int halfW = matrix.getWidth() / 2;
        int halfH = matrix.getHeight() / 2;
        int[] pixels = new int[qrcodeWidth * qrcodeHeight];

        for (int y = 0; y < matrix.getHeight(); y++) {
            for (int x = 0; x < matrix.getWidth(); x++) {
                // 左上角颜色,根据自己需要调整颜色范围和颜色
//                if (x > 0 && x < 170 && y > 0 && y < 170) {
//                    Color color = new Color(231, 144, 56);
//                    int colorInt = color.getRGB();
//                    pixels[y * qrcodeWidth + x] = matrix.get(x, y) ? colorInt
//                            : 16777215;
//                }
//                // 读取图片
//                else
                if (x > halfW - iconHalfWidth && x < halfW + iconHalfWidth && y > halfH - iconHalfWidth
                        && y < halfH + iconHalfWidth) {
                    pixels[y * qrcodeWidth + x] = srcPixels[x - halfW + iconHalfWidth][y - halfH + iconHalfWidth];
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

                    pixels[y * qrcodeWidth + x] = Color.white.getRGB();
                } else {
                    // 二维码颜色（RGB）
                    int num1 = (int) (50 - (50.0 - 13.0) / matrix.getHeight() * (y + 1));
                    int num2 = (int) (100 - (165.0 - 72.0) / matrix.getHeight() * (y + 1));
                    int num3 = (int) (150 - (162.0 - 107.0) / matrix.getHeight() * (y + 1));
                    Color color = new Color(num1, num2, num3);
                    int colorInt = color.getRGB();
                    // 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；
                    //最后那个是透明色
                    pixels[y * qrcodeWidth + x] = matrix.get(x, y) ? colorInt : 16777215;
                }
            }
        }

        BufferedImage image = new BufferedImage(qrcodeWidth, qrcodeHeight, BufferedImage.TYPE_INT_ARGB);
        image.getRaster().setDataElements(0, 0, qrcodeWidth, qrcodeHeight, pixels);
        return qrcodePaddingHandle(width, padding, image, color);
    }

    /**
     * 图像变流
     *
     * @param image
     * @return
     * @throws IOException
     */
    public static ByteArrayOutputStream imageToStream(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        return outputStream;
    }

    /**
     * 删除zxing创建的二维矩阵边框
     *
     * @param matrix
     * @return
     */
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

    /**
     * 根据内容创建一个二维矩阵
     *
     * @param content 内容
     * @param width   宽度
     * @return
     * @throws WriterException
     */
    private static BitMatrix getBitMatrix(String content, int width) throws WriterException {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        return qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, width, hints);
    }

    /**
     * 图片二维码的边框处理
     *
     * @param width     目标宽度
     * @param padding   边框
     * @param masterMap 原图
     * @return
     */
    private static BufferedImage qrcodePaddingHandle(int width, int padding, BufferedImage masterMap, Color color) {
        if (padding == 0) {
            return imageZoom(masterMap, width, width, color);
        } else if (padding > 0) {
            //二维码目标宽度
            int targetWidth = width - (padding * 2);
            BufferedImage qrcode = imageZoom(masterMap, targetWidth, targetWidth, color);
            return drawing(width, width, qrcode, padding, padding, color);
        } else {
            throw new RuntimeException("padding必须为自然数");
        }
    }

    /**
     * 图片缩放
     *
     * @param masterMap 原图
     * @param width     目标宽度
     * @param height    目标高度
     * @return
     */
    private static BufferedImage imageZoom(BufferedImage masterMap, int width, int height, Color color) {
        double ratio; // 缩放比例
        // 计算比例
        if (masterMap.getHeight() > masterMap.getWidth()) {
            ratio = (new Integer(height)).doubleValue() / masterMap.getHeight();
        } else {
            ratio = (new Integer(width)).doubleValue() / masterMap.getWidth();
        }
        AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
        //把图缩小
        Image destImage = op.filter(masterMap, null);
        //真正图，把缩小的图画到这个里面
//        if (width == destImage.getWidth(null)) {
        return drawing(width, height, destImage, 0, (height - destImage.getHeight(null)) / 2, color);
//        }
//        return drawing(width, height, destImage, (width - destImage.getWidth(null)) / 2, 0);
    }

    /**
     * 把图片画入另一个图里
     *
     * @param width     外图宽度
     * @param height    外图高度
     * @param destImage 画入的图
     * @param x         画入图在外图位置x坐标
     * @param y         y坐标
     * @return
     */
    private static BufferedImage drawing(int width, int height, Image destImage, int x, int y, Color color) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphic = image.createGraphics();
        graphic.setColor(color);
        graphic.fillRect(0, 0, width, height);
        graphic.drawImage(destImage, x, y, destImage.getWidth(null), destImage.getHeight(null), color, null);
        graphic.dispose();
        return image;
    }

    /**
     * 二维矩阵变图片
     *
     * @param matrix 矩阵
     * @return
     */
    private static BufferedImage toBufferedImage(BitMatrix matrix, Color color) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : color.getRGB());
            }
        }
        return image;
    }

    public static void main(String[] args) throws IOException, WriterException {
        // 依次为内容(不支持中文),宽,长,logo图标路径,储存路径
        File imageFile = new File("D:\\logo.jpg");
        //传进来的图
        BufferedImage srcImage = ImageIO.read(imageFile);
        ImageIO.write(getComplexQrcode("http://www.baidu.com/http://www.baidu.com/http://www.baidu.com/http://www.baidu.com/", 500, srcImage, 0), "png", new File("D:\\201301.png"));

        BufferedImage image = QrcodeUtils.createSimpleQrcode("http://www.baidu.com/http://www.baidu.com/http://www.baidu.com/http://www.baidu.com/", 500, 0, new Color(255, 255, 255, 0));
        try (FileOutputStream outputStream = new FileOutputStream("d:/zi.png")) {
            try (ByteArrayOutputStream file = QrcodeUtils.imageToStream(image)) {
                outputStream.write(file.toByteArray());
            }
        }
    }

}
