package com.example.dell.openglex.controller;

import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import com.example.dell.openglex.object.ParticleShooter;
import com.example.dell.openglex.object.ParticleSystem;
import com.example.dell.openglex.object.Point3;
import com.example.dell.openglex.object.Vector3;
import com.example.dell.openglex.program.ParticleProgram;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class ParticleGLRender implements GLSurfaceView.Renderer {
    private GLSurfaceView mGLView;
    private Context mContext;
    //视图矩阵
    private final float[] mProjectionMatrix=new float[16];
    private final float[] mViewMatrix=new float[16];
    private final float[] mViewProjectionMatrix=new float[16];
    private ParticleProgram mParticleProgram;
    private ParticleSystem mParticleSystem;
    private long mGlobalStartTime;
    private ParticleShooter mRedParticleShooter;
    private ParticleShooter mGreenParticleShooter;
    private ParticleShooter mBlueParticleShooter;

    public ParticleGLRender(Context context,GLSurfaceView glView){
        this.mContext=context;
        this.mGLView=glView;
    }
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        //设置屏幕背景色
        GLES20.glClearColor(0.0f,0.0f,1.0f,1.0f);
        mParticleProgram=new ParticleProgram(mContext,mGLView);
        //获取粒子系统的实例，初始化粒子系统包含10000个粒子
        mParticleSystem=new ParticleSystem(10000);
        //获取系统时间
        mGlobalStartTime=System.nanoTime();
        Vector3 particleDirection=new Vector3(0f,0.5f,0f);
        //新建粒子发射器（红色粒子），获取实例
        mRedParticleShooter=new ParticleShooter(
                new Point3(-1f,0f,0f),
                particleDirection,
                Color.rgb(255,50,5));
        //新建粒子发射器（绿色粒子），获取实例
        mGreenParticleShooter=new ParticleShooter(
                new Point3(0f,0f,0f),
                particleDirection,
                Color.rgb(25,255,25));
        //新建粒子发射器（蓝色粒子），获取实例
        mBlueParticleShooter=new ParticleShooter(
                new Point3(1f,0f,0f),
                particleDirection,
                Color.rgb(5,50,255));
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        //设置当前的视点适应新的尺寸
        GLES20.glViewport(0,0,width,height);
        Matrix.perspectiveM(mProjectionMatrix, 0, 45, (float)width/(float)height,
                1f, 10f);
        Matrix.setIdentityM(mViewMatrix,0);
        Matrix.translateM(mViewMatrix,0,0f,-1.5f,-5f);
        //设置最终的视点
        Matrix.multiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        //清除位缓存
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        float currentTime=(System.nanoTime()-mGlobalStartTime)/1000000000f;
        mRedParticleShooter.addParticles(mParticleSystem,currentTime,5);
        mGreenParticleShooter.addParticles(mParticleSystem,currentTime,5);
        mBlueParticleShooter.addParticles(mParticleSystem,currentTime,5);
        //使用程序
        mParticleProgram.useProgram();
        //设置参数
        mParticleProgram.setUniforms(mViewProjectionMatrix,currentTime);
        /**必须在USE progrem之后，才能进行数据绑定，即操作glVertexAttribPointer之类的函数
         * bindData()将所有的数据:粒子顶点数据、颜色数据、方向数据、时间捆绑在一个VBO中，然后
         * 通过这个VBO绘制的，
         **/
        mParticleSystem.bindData(mParticleProgram);
        //绘制粒子
        mParticleSystem.draw(mParticleProgram);
    }
}
