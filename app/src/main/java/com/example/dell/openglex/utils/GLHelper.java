package com.example.dell.openglex.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Vector;

import javax.microedition.khronos.opengles.GL10;

public class GLHelper {
    //坐标分配缓存对象
    public static FloatBuffer prepareBuffer(float[] vertices) {
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

    //要打印的字  字体大小  字的信息   最大宽度
    public static Bitmap getImage(String str, int fontsize, Paint paint,
                                  int maxWidth) {

        String[] text = StringFormat(str, maxWidth, fontsize);
        int[] count = getLinesMaxLength(text);
        Bitmap bitmap = Bitmap.createBitmap(count[0] * (fontsize / 2)
                        + count[1] * fontsize +5, (text.length) * fontsize,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        paint.setTextSize(fontsize);
        for (int i = 0; i < text.length; i++) {
            canvas.drawText(text[i], 0, (i+1) * fontsize -3, paint);
        }
        return bitmap;
    }

    //对String 进行分段
    public static String[] StringFormat(String text, int maxWidth, int fontSize) {
        String[] result = null;
        Vector<String> tempR = new Vector<String>();
        int lines = 0;
        int len = text.length();
        int index0 = 0;
        int index1 = 0;
        boolean wrap;
        while (true) {
            int widthes = 0;
            wrap = false;
            for (index0 = index1; index1 < len; index1++) {
                if (text.charAt(index1) == '\n') {
                    index1++;
                    wrap = true;
                    break;
                }
                widthes = fontSize + widthes;
                if (widthes > maxWidth) {
                    break;
                }
            }
            lines++;
            if (wrap) {
                tempR.addElement(text.substring(index0, index1 - 1));
            } else {
                tempR.addElement(text.substring(index0, index1));
            }
            if (index1 >= len) {
                break;
            }
        }
        result = new String[lines];
        tempR.copyInto(result);
        return result;
    }

    /**
     * 返回字数最多的那个行中中英文的数量
     *
     * @param lines
     * @return int[0] 英文的数量 int[1] 中文的数量
     */
    public static int[] getLinesMaxLength(String[] lines) {
        int max = 0, index = 0;
        for (int i = 0; i < lines.length; i++) {
            if (max < lines[i].getBytes().length) {
                max = lines[i].getBytes().length;
                index = i;
            }
        }
        int[] count = new int[2];
        for (int i = 0; i < lines[index].length(); i++) {
            if (lines[index].charAt(i) > 255) {
                count[1]++;
            } else {
                count[0]++;
            }
        }
        return count;
    }

    //加载图片
    public static Bitmap loadBitmap(Context context, String path) {
        InputStream in = null;
        try {
            //todo IO读取过程中加入encode和decode的过程
            in = context.getAssets().open(path);  //数据流加载图片
            return BitmapFactory.decodeStream(in);
        } catch (final IOException e) {
            return null;
        } finally {
            if (in != null) {
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
            bitmap = loadBitmap(context, path);
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

     /*
     通过调用loadShader方法，分别加载顶点着色器与片元着色器的源代码进GPU，并分别进行编译
     然后创建一个着色器程序，分别将相应的顶点与片元着色器添加其中
     ，最后将两个着色器链接为一个整体着色器程序
     */

    public static int createProgram(String vertexSource, String fragmentSource) {
        //加载顶点着色器
        int vextexShder = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
        if (vextexShder == 0) {
            return 0;
        }
        //加载片元着色器
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
        if (fragmentShader == 0) {
            return 0;
        }
        int program = GLES20.glCreateProgram();
        if (program != 0) {
            GLES20.glAttachShader(program, vextexShder);
            GLES20.glAttachShader(program, fragmentShader);
            GLES20.glLinkProgram(program);//链接程序
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE) {
                GLES20.glDeleteProgram(program);
                program = 0;
            }
        }
        return program;
    }

    public static int loadShader(int shaderType, String shaderSource) {
        int shader = GLES30.glCreateShader(shaderType); //创建shader并记录它的id
        if (shader != 0) {
            GLES30.glShaderSource(shader, shaderSource);//加载着色器代码
            GLES30.glCompileShader(shader);//编译
            int[] compiled = new int[1];
            //获取shader编译情况
            GLES30.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) { //如果编译失败
                GLES30.glDeleteShader(shader);
                shader = 0;
            }
        }
        return shader;
    }

    /**
     * 从assets中加载着色脚本
     * <p>
     * ① 打开assets目录中的文件输入流
     * ② 创建带缓冲区的输出流
     * ③ 逐个字节读取文件数据, 放入缓冲区
     * ④ 将缓冲区中的数据转为字符串
     *
     * @param fileName  assets目录中的着色脚本文件名
     * @param resources 应用的资源
     * @return
     */
    public static String loadFromAssetsFile(String fileName, Resources resources) {
        String result = null;
        try {
            //1. 打开assets目录中读取文件的输入流, 相当于创建了一个文件的字节输入流
            InputStream is = resources.getAssets().open(fileName);
            int ch = 0;
            //2. 创建一个带缓冲区的输出流, 每次读取一个字节, 注意这里字节读取用的是int类型
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //3. 逐个字节读取数据, 并将读取的数据放入缓冲器中
            while ((ch = is.read()) != -1) {
                baos.write(ch);
            }
            //4. 将缓冲区中的数据转为字节数组, 并将字节数组转换为字符串
            byte[] buffer = baos.toByteArray();
            baos.close();
            is.close();
            result = new String(buffer, "UTF-8");
            result = result.replaceAll("\\r\\n", "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
