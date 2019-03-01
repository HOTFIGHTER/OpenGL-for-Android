package com.example.dell.openglex.controller;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.example.dell.openglex.utils.GLHelper;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/*
 * 坐标手绘轮播图的渲染引擎
 * */
public class CarouselGLRender implements GLSurfaceView.Renderer {
    private Context mContext;
    private int mTextureId1, mTextureId2;
    private FloatBuffer mTexture1, mTexture2;
    private FloatBuffer mVertices1, mVertices2;
    //图片1 vertex的高度
    private float mPos1ReducedHeight = -1f;
    private float mPos1TextureHeight = 0f;
    //标识哪个图在上面
    private boolean mWhichPos = true;
    //标记是否需要轮播
    private boolean mIsNeedCarousel = true;
    //两个图像的显示区域
    private float[] sPos1 = {
            -1.0f, 1.0f,    //左上角
            1.0f, 1.0f,     //右上角
            -1.0f, -1f,   //左下角
            1.0f, -1f    //右下角
    };
    private float[] sPos2 = {
            -1.0f, -1f,    //左上角
            1.0f, -1f,     //右上角
            -1.0f, -1f,   //左下角
            1.0f, -1f    //右下角
    };
    //纹理的裁剪区，初始裁剪高度大小为1
    private float[] sTexPos1 = {
            0, 0f,  //裁剪原点
            1f, 0f,
            0f, 1f,
            1f, 1f,//裁剪宽最大值，高最大值
    };
    //纹理的裁剪区，初始裁剪高度大小为0
    private float[] sTexPos2 = {
            0, 0f,  //裁剪原点
            1f, 0f,
            0f, 0f,
            1f, 0f,
    };

    public CarouselGLRender(Context context) {
        mContext = context;
        initReducedHeight();
        //初始化buffer
        ByteBuffer textureBuffer1 = ByteBuffer.allocateDirect(4 * 2 * 4);
        textureBuffer1.order(ByteOrder.nativeOrder());
        mTexture1 = textureBuffer1.asFloatBuffer();
        mTexture1.put(sTexPos1);
        mTexture1.position(0);
        ByteBuffer textureBuffer2 = ByteBuffer.allocateDirect(4 * 2 * 4);
        textureBuffer2.order(ByteOrder.nativeOrder());
        mTexture2 = textureBuffer2.asFloatBuffer();
        mTexture2.put(sTexPos2);
        mTexture2.position(0);
        ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(4 * 2 * 4);
        byteBuffer1.order(ByteOrder.nativeOrder());
        mVertices1 = byteBuffer1.asFloatBuffer();
        mVertices1.put(sPos1);
        mVertices1.position(0);
        ByteBuffer byteBuffer2 = ByteBuffer.allocateDirect(4 * 2 * 4);
        byteBuffer2.order(ByteOrder.nativeOrder());
        mVertices2 = byteBuffer2.asFloatBuffer();
        mVertices2.put(sPos2);
        mVertices2.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glEnable(GL10.GL_DEPTH_TEST); // 启用深度缓存
        gl.glClearColor(0f, 0f, 0f, 0f);// 设置深度缓存值
        //加载纹理
        mTextureId1 = GLHelper.loadTexture(gl, mContext, "banner1.jpeg");
        mTextureId2 = GLHelper.loadTexture(gl, mContext, "banner2.jpg");
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //设置适口大小
        GLES20.glViewport(0, 0, width, height);
    }

    //初始化坐标
    private void initReducedHeight() {
        mPos1ReducedHeight = -1;
        mPos1TextureHeight = 0;
        //初始化顶点坐标和纹理坐标
        sPos1[1] = 1;
        sPos1[3] = 1;
        sPos1[5] = -1;
        sPos1[7] = -1;
        sPos2[1] = -1;
        sPos2[3] = -1;
        sPos2[5] = -1;
        sPos2[7] = -1;

        sTexPos1[1] = 0;
        sTexPos1[3] = 0;
        sTexPos1[5] = 1;
        sTexPos1[7] = 1;
        sTexPos2[5] = 0;
        sTexPos2[7] = 0;
        sTexPos2[5] = 0;
        sTexPos2[7] = 0;
        //用于标记图片
        mWhichPos = !mWhichPos;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if (mIsNeedCarousel) {
            mPos1ReducedHeight += 0.01f;
            mPos1TextureHeight += 0.005f;//因为有个两倍关系在里边，坐标系统的变化量是纹理的两倍
            if (mPos1ReducedHeight >= 1 || mPos1TextureHeight >= 1) {
                //重新初始化
                initReducedHeight();
                //如果只需要一次轮播，设置mIsNeedCarousel为false
                //mIsNeedCarousel=false;
            }
            //设置坐标
            sPos1[5] = mPos1ReducedHeight;
            sPos1[7] = mPos1ReducedHeight;
            sPos2[1] = mPos1ReducedHeight;
            sPos2[3] = mPos1ReducedHeight;
            //设置纹理位置
            sTexPos1[1] = mPos1TextureHeight;
            sTexPos1[3] = mPos1TextureHeight;
            sTexPos2[5] = mPos1TextureHeight;
            sTexPos2[7] = mPos1TextureHeight;
        }
        //将数组信息置于纹理和数组中
        mTexture1.clear();
        mTexture1.put(sTexPos1);
        mTexture1.position(0);
        mTexture2.clear();
        mTexture2.put(sTexPos2);
        mTexture2.position(0);
        mVertices1.clear();
        mVertices1.put(sPos1);
        mVertices1.position(0);
        mVertices2.clear();
        mVertices2.put(sPos2);
        mVertices2.position(0);

        // 清除屏幕和深度缓存
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glEnable(GL10.GL_TEXTURE_2D);
        //允许画点
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        //允许使用纹理数组
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        //判断轮播图是哪个？
        if (mWhichPos) {
            gl.glVertexPointer(2, GL10.GL_FLOAT, 0, mVertices1);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId1);
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexture1);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

            gl.glVertexPointer(2, GL10.GL_FLOAT, 0, mVertices2);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId2);
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexture2);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        } else {
            gl.glVertexPointer(2, GL10.GL_FLOAT, 0, mVertices1);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId2);
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexture1);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

            gl.glVertexPointer(2, GL10.GL_FLOAT, 0, mVertices2);
            gl.glBindTexture(GL10.GL_TEXTURE_2D, mTextureId1);
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, mTexture2);
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        }
        //不允许画点
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        //不允许使用纹理数组
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisable(GL10.GL_TEXTURE_2D);
    }
}
