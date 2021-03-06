package com.example.dell.openglex.controller;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import com.example.dell.openglex.utils.GLHelper;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/*
*  基础图形的矩阵变换，视角变化
* */
public class MatrixGLRender implements GLSurfaceView.Renderer{
    //三角形坐标
    private float mVertices[] = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f,  0.5f, 0.0f
    };

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private Context mContext;
    private FloatBuffer mTriangleBuffer;

    public MatrixGLRender(Context context){
        mContext=context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        gl10.glClearColor(0, 1, 1, 1);//设置背景色
        gl10.glShadeModel(GL10.GL_SMOOTH);
        gl10.glMatrixMode(GL10.GL_PROJECTION);
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);

        mTriangleBuffer=GLHelper.prepareBuffer(mVertices);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        gl10.glViewport(0, 0, width, height);
        //设置宽高比
        float ratio =(float) width / height;
        // 此投影矩阵在onDrawFrame()中将应用到对象的坐标
        //Matrix.frustumM(mProjectionMatrix,0,-ratio, ratio,-1,1,3,7);
        Matrix.perspectiveM(mProjectionMatrix, 0, 45, (float)width / (float)height, 1, 10);
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, 2.8f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        Matrix.rotateM(mViewMatrix,0, 60, 0f, 0f, 0.0f);
        // 计算投影和视图变换
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        //矩阵单位化
        gl10.glLoadIdentity();
        //允许画点
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        //设置顶点位置数据
        gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, mTriangleBuffer);
        //旋转基本图形
        gl10.glRotatef(85f,1,0,0);
        //根据顶点数据绘制平面图形
        gl10.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);

        gl10.glFinish();
        gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
