package com.example.dell.openglex.controller;

import android.content.Context;
import android.opengl.GLSurfaceView;
import com.example.dell.openglex.utils.GLHelper;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/*
*  带颜色的基础图像三角形绘制
* */
public class TriangleGLRender implements GLSurfaceView.Renderer {
    //三角形坐标
    private float mVertices[] = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f,  0.5f, 0.0f
    };
    //颜色坐标
    private float[] mColors = new float[]{
            1, 1, 0, 1,
            0, 1, 0.5f, 1,
            1, 0, 1, 1
    };
    private Context mContext;
    private FloatBuffer mTriangleBuffer;
    private FloatBuffer mColorBuffer;

    public TriangleGLRender(Context context){
        mContext=context;
    }
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        gl10.glClearColor(0, 1, 1, 1);//设置背景色
        gl10.glShadeModel(GL10.GL_SMOOTH);
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl10.glEnableClientState(GL10.GL_COLOR_ARRAY);

        mTriangleBuffer=GLHelper.prepareBuffer(mVertices);
        mColorBuffer=GLHelper.prepareBuffer(mColors);
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
        //允许画点
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

        gl10.glDisableClientState(GL10.GL_COLOR_ARRAY);
        gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
