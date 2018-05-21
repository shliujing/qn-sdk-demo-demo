package com.device.utils;

import org.opencv.core.*;
import org.opencv.imgcodecs.*;
import org.opencv.imgproc.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;


@Component
public class ImageUtils {
    static
    {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        ImageUtils.compareImage("C:/device-check/main/20180429190437.jpeg", "C:/device-check/main/20180425121420.jpeg", 104, 221, 137, 25, 22, 2);
}

    @Value("${win.gallery.dir}")
    private  String winGallery;

    @Value("${linux.gallery.dir}")
    private  String linuxGallery;

    private  Path path = null;

    /**
     * 功能描述：base64字符串转换成图片
     *
     * @since 2016/5/24
     */
    public boolean generateImage(String imgStr, String filePath, String fileName) {
        try {
            if (imgStr == null) {
                return false;
            }
            BASE64Decoder decoder = new BASE64Decoder();
            //Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            //如果目录不存在，则创建
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            //生成图片
            OutputStream out = new FileOutputStream(filePath + fileName);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
    适用于传输文件流
     */
    public Boolean store(MultipartFile file, String fileName) {
        Boolean flag = false;
        if (file.isEmpty()) {
            throw new RuntimeException("Fail to store empty file");
        }

        try {
            path = Paths.get(getGalleryPath(), fileName);
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }

            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
            flag =  false;
        }

        return flag;
    }

    public  String getGalleryPath() {
        String osname = System.getProperty("os.name");
        String galleryPath = null;
        if (osname.startsWith("Windows")) {
            // 在 Windows 操作系统上
            galleryPath = winGallery;
        } else if (osname.startsWith("Linux")) {
            // 在 Linux 操作系统上
            galleryPath = linuxGallery;
        }
        return galleryPath;
    }
    //去噪点函数  add by hf 20180423  建立模板时再使用
    public  void cutImage( String pathj,float Vaulej, String filename )
    {
        Mat frame = Imgcodecs.imread(pathj,0);
        Imgproc.blur(frame, frame, new Size(3,3));
        Imgproc.threshold(frame, frame, (int) (Vaulej) , 255, Imgproc.THRESH_BINARY);
        Imgcodecs.imwrite(filename, frame);
        frame.release();

    }
    //对比函数 20180423 hf

    public static double compareImage( String pathj,String pathg,float Vaulej,int x,int y,int width,int height,int type)   //对比
   {
       System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        double m;
        File filej=new File(pathj);
         // pathj= filej.getName();
       String patha=pathj;
       patha="C:/device-check/main/20180425121419.jpeg";
       File fileg=new File(pathg);
        //   pathg= fileg.getName();
       pathg="C:/device-check/main/20180425121420.jpeg";
        Mat pic1 = Imgcodecs.imread("C:/device-check/main/20180425121419.jpeg",0);     //模板图片路径
        Mat pic2 = Imgcodecs.imread("C:/device-check/main/20180425121420.jpeg",0);     //实时截图路径
// 这个类没加载到Imgcodecs
        Rect r1 = new Rect();
        r1.x = x;
        r1.y = y;
        r1.width = width;
        r1.height = height;
        pic1 = pic1.submat(r1);
        pic2 = pic2.submat(r1);

        Imgproc.blur(pic1, pic1, new Size(3,3));
        Imgproc.blur(pic2, pic2, new Size(3,3));
        Imgproc.threshold(pic1, pic1, (int) (Vaulej ) , 255, Imgproc.THRESH_BINARY);
        Imgproc.threshold(pic2, pic2, (int) (Vaulej ) , 255, Imgproc.THRESH_BINARY);

       if (type == 2)
       {
           Core.bitwise_not(pic1,pic1);
           Core.bitwise_not(pic2,pic2);
       }


        int minx1,maxx1,miny1,maxy1;
        minx1 = pic1.width();    //最左点
        maxx1 = 0;               //最右点
        miny1 = pic1.height();   //最上行
        maxy1 = 0;               //最下行

        for(int i=0;i< pic1.rows();i++)
        {
            for(int j=0;j< pic1.cols();j++)
            {
                if (pic1.get(i, j)[0] > 0 )
                {
                    if(j < minx1) minx1 = j;
                    if(j > maxx1) maxx1 = j;
                    if(i < miny1) miny1 = i;
                    if(i > maxy1) maxy1 = i;
                }
            }
        }

        if (maxx1 == 0)
        {
            m = 1.0;
            System.out.println(m);
            return m ;
        }

        Rect r11 = new Rect();
        r11.x = minx1;
        r11.y = miny1;
        r11.width  = maxx1 - minx1;
        r11.height = maxy1 - miny1;

        Mat dst1 = pic1.submat(r11);
        Imgproc.resize(dst1, dst1, new Size(50,50));

        int minx2,maxx2,miny2,maxy2;
        minx2 = pic2.width();    //最左点
        maxx2 = 0;               //最右点
        miny2 = pic2.height();   //最上行
        maxy2 = 0;               //最下行

        for(int i=0;i< pic2.rows();i++)
        {
            for(int j=0;j< pic2.cols();j++)
            {
                if (pic2.get(i, j)[0] > 0 )
                {
                    if(j < minx2) minx2 = j;
                    if(j > maxx2) maxx2 = j;
                    if(i < miny2) miny2 = i;
                    if(i > maxy2) maxy2 = i;
                }
            }
        }

        Rect r22 = new Rect();
        r22.x = minx2;
        r22.y = miny2;
        r22.width  = maxx2 - minx2;
        r22.height = maxy2 - miny2;

        if (maxx2 == 0)
        {
            m = 1.0;
            System.out.println(m);
            return m ;
        }
        Mat dst2 = pic2.submat(r22);
        Imgproc.resize(dst2, dst2, new Size(50,50));

        List<Mat>Matdst2 = new ArrayList<Mat>();
        Matdst2.add(dst2);
        Matdst2.add(dst2);

        Core.hconcat(Matdst2,dst2);
        Core.vconcat(Matdst2,dst2);

        Mat result = new Mat();
        int result_cols =  dst2.cols() - dst1.cols() + 1;
        int result_rows =  dst2.rows() - dst1.rows() + 1;
        result.create( result_rows, result_cols, CvType.CV_32FC1 );
        Imgproc.matchTemplate(dst2, dst1, result, Imgproc.TM_SQDIFF);
        Core.normalize( result, result, 0.0, 1.0, Core.NORM_MINMAX);

        //MinMaxLocResult mmr = Core.minMaxLoc(result);
        m = result.get(0, 0)[0];
        return m ;
    }

}