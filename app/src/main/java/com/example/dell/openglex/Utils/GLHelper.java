package com.example.dell.openglex.Utils;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

public class GLHelper {
    //坐标分配缓存对象
    public static FloatBuffer prepareBuffer(float[] vertices){
        //先初始化buffer，数组的长度*4，因为一个float占4个字节
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        //以本机字节顺序来修改此缓冲区的字节顺序
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer buffer = bb.asFloatBuffer();
        //将给定float[]数据从当前位置开始，依次写入此缓冲区
        buffer.put(vertices);
        //设置此缓冲区的位置。如果标记已定义并且大于新的位置，则要丢弃该标记。
        buffer.position(0);
        return buffer;
    }

    //加载图片
    private static Bitmap loadBitmap(Context context,String path) {
        InputStream in = null;
        try {
            //todo IO读取过程中加入encode和decode的过程
            in = context.getAssets().open(path);  //数据流加载图片
            return BitmapFactory.decodeStream(in);
        } catch (final IOException e) {
            return null;
        } finally {
            if(in != null) {
                try {
                    in.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //加载贴图
    public static int loadTexture(GL10 gl, Context context, String path) {
        Bitmap bitmap = null;
        int[] textures = new int[1];
        try {
            // 加载位图
            bitmap = loadBitmap(context,path);
            // 指定生成N个纹理（第一个参数指定生成1个纹理），
            // textures数组将负责存储所有纹理的代号。
            gl.glGenTextures(1, textures, 0);
            // 通知OpenGL将texture纹理绑定到GL10.GL_TEXTURE_2D目标中
            gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
            // 设置纹理被缩小（距离视点很远时被缩小）时候的滤波方式
            gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                    GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
            // 设置纹理被放大（距离视点很近时被方法）时候的滤波方式
            gl.glTexParameterf(GL10.GL_TEXTURE_2D,
                    GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
            // 设置在横向、纵向上都是平铺纹理
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
                    GL10.GL_REPEAT);
            gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
                    GL10.GL_REPEAT);
            // 加载位图生成纹理
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        } finally {
            // 生成纹理之后，回收位图
            if (bitmap != null)
                bitmap.recycle();
            return textures[0];
        }
    }
}
