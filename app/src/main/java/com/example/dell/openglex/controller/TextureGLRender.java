package com.example.dell.openglex.controller;

import android.content.Context;
import android.opengl.GLSurfaceView;

import com.example.dell.openglex.utils.GLHelper;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/*
*  带贴图的基础图形的绘制
* */
public class TextureGLRender implements GLSurfaceView.Renderer {
    //三角形坐标
    private float mVertices[] = {
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.0f,  0.5f, 0.0f
    };

    //纹理贴图的坐标
    private float[] mCoords={
            0.0f,0.0f,
            0.0f,1.0f,
            0.5f,0.0f,
            1.0f,1.0f,
    };

    private Context mContext;
    private FloatBuffer mTriangleBuffer;
    private FloatBuffer mTextureBuffer;//顶点纹理数据缓冲
    private int mTextureId;

    public TextureGLRender(Context context){
        mContext=context;
    }
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        gl10.glClearColor(0, 1, 1, 1);//设置背景色
        gl10.glShadeModel(GL10.GL_SMOOTH);
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        //纹理贴图情况下去掉颜色着色器功能，不然会崩溃，使用的话必须赋值，绘制顺序 1.图形--> 2.纹理 --> 3.颜色
        //gl10.glEnableClientState(GL10.GL_COLOR_ARRAY);

        mTriangleBuffer=GLHelper.prepareBuffer(mVertices);
        mTextureBuffer=GLHelper.prepareBuffer(mCoords);
        //加载纹理
        mTextureId=GLHelper.loadTexture(gl10,mContext,"036b.png");
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
//        //绝对值必须都小于1
        gl10.glTranslatef(-0.5f, 0.5f, -0f);
        //设置顶点位置数据
        gl10.glVertexPointer(3, GL10.GL_FLOAT, 0, mTriangleBuffer);
        //开启纹理
        gl10.glEnable(GL10.GL_TEXTURE_2D);
        //允许使用纹理数组
        gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl10.glTexCoordPointer(2,                 //每个顶点两个纹理坐标数据 S、T
                GL10.GL_FLOAT,        //数据类型
                0,                 //连续纹理坐标数据之间的间隔
                mTextureBuffer    //纹理坐标数据
        );
        gl10.glBindTexture(GL10.GL_TEXTURE_2D,mTextureId);

        //根据顶点数据绘制平面图形
        gl10.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
        //gl10.glFinish();

        //gl10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl10.glDisable(GL10.GL_TEXTURE_2D);
    }
}
