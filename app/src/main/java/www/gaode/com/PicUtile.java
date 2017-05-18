package www.gaode.com;

/**
 * Created by Administrator on 2017/5/18 0018.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PicUtile {
    //压缩图片类
    public static void createCompressBitmap(String fileSource, File toFile) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高
        Bitmap bitmap = BitmapFactory.decodeFile(fileSource, options); // 此时返回bm为空
        options.inJustDecodeBounds = false;
        // 计算缩放比
        int be = (int) (options.outWidth / (float) 480);
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;

        bitmap = BitmapFactory.decodeFile(fileSource, options);
        if (bitmap == null) {
            return;
        }
        // 解决翻转
        Matrix matrix = new Matrix();
        matrix.postRotate(90f); /* 翻转 */
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        compressBmpToFile(bitmap, toFile);
    }

    /**
     * 压缩到100k以下
     *
     * @param bmp
     * @param file
     */
    public static void compressBmpToFile(Bitmap bmp, File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 100;// 100表示不压缩
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        //修改压缩的尺寸大小
        while (baos.toByteArray().length / 1024 > 200) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
