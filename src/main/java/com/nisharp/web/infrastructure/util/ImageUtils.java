package com.nisharp.web.infrastructure.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author ZM.Wang
 */
public class ImageUtils {

    public static ByteArrayOutputStream overlapImg(String displayText,int displayTextLeftOffset, BufferedImage topImg, int topImgWidth, BufferedImage bottomImg, int topOffset) throws IOException {
        //得到画笔对象
        Graphics g = bottomImg.getGraphics();

        int bottomImgWidth = bottomImg.getWidth();

        int topImgLeftOffset = (bottomImgWidth - topImgWidth) / 2;

        //将小图片绘到大图片上。
        //中间数字表示你的小图片在大图片上的位置。
        g.drawImage(topImg, topImgLeftOffset, topOffset, null);

        //设置颜色。
        g.setColor(Color.BLACK);

        //最后一个参数用来设置字体的大小
        Font f = new Font("微软雅黑", Font.PLAIN, 25);
        Color mycolor = Color.black;//new Color(0, 0, 255);
        g.setColor(mycolor);
        g.setFont(f);

        int textTopOffset = topOffset + topImgWidth + 10;

        //10,20 表示这段文字在图片上的位置(x,y) .第一个是你设置的内容。
        g.drawString(displayText, displayTextLeftOffset, textTopOffset);
        g.dispose();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bottomImg, "jpg", os);
        return os;
    }
}
