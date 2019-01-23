package com.example.dell.openglex.controller;

import android.opengl.GLSurfaceView;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class YuGLRender implements GLSurfaceView.Renderer {
    private FloatBuffer mTriangleBuffer;
    private FloatBuffer mColorBuffer;

    //三角形坐标
    private float vertices[] = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f,  0.5f, 0.0f
    };
    private float[] mColor = new float[]{
            1, 1, 0, 1,
            0, 1, 0.5f, 1,
            1, 0, 1, 1
    };

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        gl10.glClearColor(0, 1, 1, 1);//设置背景色
        gl10.glShadeModel(GL10.GL_SMOOTH);
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl10.glEnableClientState(GL10.GL_COLOR_ARRAY);

        //先初始化buffer，数组的长度*4，因为一个float占4个字节
        ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * 4);
        //以本机字节顺序来修改此缓冲区的字节顺序
        bb.order(ByteOrder.nativeOrder());
        mTriangleBuffer = bb.asFloatBuffer();
        //将给定float[]数据从当前位置开始，依次写入此缓冲区
        mTriangleBuffer.put(vertices);
        //设置此缓冲区的位置。如果标记已定义并且大于新的位置，则要丢弃该标记。
        mTriangleBuffer.position(0);

        //颜色相关
        ByteBuffer bb2 = ByteBuffer.allocateDirect(mColor.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        mColorBuffer = bb2.asFloatBuffer();
        mColorBuffer.put(mColor);
        mColorBuffer.position(0);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        gl10.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        //矩阵单位化
        gl10.glLoadIdentity();
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        //启用顶点颜色数组
        gl10.glEnableClientState(GL10.GL_COLOR_ARRAY);
        //绝对值必须都小于1
        gl10.glTranslatef(-0.5f, 0.5f, -0f);
        //设置顶点位置数据
        gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, mTriangleBuffer);
        //设置顶点颜色数据
        gl10.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);
        //根据顶点数据绘制平面图形
        gl10.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
        gl10.glFinish();


    }
}
