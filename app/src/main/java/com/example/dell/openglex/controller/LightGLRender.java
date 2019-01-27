package com.example.dell.openglex.controller;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.example.dell.openglex.utils.GLHelper;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/*
*  光照效果
* */
public class LightGLRender implements GLSurfaceView.Renderer {
    //三角形坐标
    private float mVertices[] = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f, 0.5f, 0.0f
    };
    float[] ambient = {0.1f, 0.1f, 0.1f, 1.0f,};
    float[] diffuse = {0.9f, 0.9f, 0.9f, 1.0f,};
    float[] specular = {0.0f, 0.0f, 0.0f, 1.0f,};
    float[] lightPosition = {0.5f, 0.5f, 0.5f, 0.0f,};
    private Context mContext;
    private FloatBuffer mTriangleBuffer;

    public LightGLRender(Context context){
        mContext=context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        gl10.glClearColor(0, 0, 0, 1);//设置背景色
        gl10.glShadeModel(GL10.GL_SMOOTH);
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        //启用光照功能
        gl10.glEnable(GL10.GL_LIGHTING);
        //开启0号灯
        gl10.glEnable(GL10.GL_LIGHT0);
        //打开光照
        openLight(gl10);
        mTriangleBuffer=GLHelper.prepareBuffer(mVertices);
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
        //设置顶点位置数据
        gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, mTriangleBuffer);
        //根据顶点数据绘制平面图形
        gl10.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
        gl10.glFinish();
        gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }

    public void openLight(GL10 gl) {
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, GLHelper.prepareBuffer(ambient));
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, GLHelper.prepareBuffer(diffuse));
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, GLHelper.prepareBuffer(specular));
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, GLHelper.prepareBuffer(lightPosition));
    }
}
