package com.example.dell.openglex.controller;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/*
* 含文本的渲染器
* */
public class TextGLRender implements GLSurfaceView.Renderer{
    private Context mContext;
    private float rot=0f;
    private int mWidth,mHeight;
    private FloatBuffer vertices_buffer;
    private float[] vertices={
            0f,-0.525731f,0.850651f,
            0.850651f,0f,0.525731f,
            0.850651f,0f,-0.525731f,
            -0.850651f,0f,-0.525731f,
            -0.850651f,0f,0.525731f,
            -0.525731f,0.850651f,0f,
            0.525731f,0.850651f,0f,
            0.525731f,-0.850651f,0f,
            -0.525731f,-0.850651f,0f,
            0f,-0.525731f,-0.850651f,
            0f,0.525731f,-0.850651f,
            0f,0.525731f,0.850651f,
    };
    private FloatBuffer color_buffer;
    private float[] color={
            1,0,0,1,
            1,0.5f,0,1,
            1,1,0,1,
            0.5f,1,0,1,
            0,1,0,1,
            0,1,0.5f,1,
            0,1,1,1,
            0,0.5f,1,1,
            0,0,1,1,
            0.5f,0,1,1,
            1,0,1,1,
            1,0,0.5f,1,
    };
    private ByteBuffer icosahedranFaces = ByteBuffer.wrap(new byte[]{
            1, 2, 6,
            1, 7, 2,
            3, 4, 5,
            4, 3, 8,
            6, 5, 11,
            5, 8, 10,
            9, 10, 2,
            10, 9, 3,
            7, 8, 9,
            8, 7, 0,
            11, 0, 1,
            0, 11, 4,
            6, 2, 10,
            1, 6, 11,
            3, 5, 10,
            5, 4, 11,
            2, 7, 9,
            7, 1, 0,
            3, 9, 8,
            4, 8, 0,
    });

    public TextGLRender(Context context){
        mContext=context;
        vertices_buffer=ByteBuffer.allocateDirect(vertices.length * (Float.SIZE >> 3)).
                order(ByteOrder.nativeOrder()).asFloatBuffer();
        color_buffer=ByteBuffer.allocateDirect(color.length * (Float.SIZE >> 4)).
                order(ByteOrder.nativeOrder()).asFloatBuffer();

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //告诉系统要进行视图修正,表示颜色和纹理坐标插补的质量
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        //屏幕颜色清空
        gl.glClearColor(0, 0, 0, 1);
        //应用深度缓存,当颜色深度一致时，后者绘制的图像不会在前者之上绘制
        gl.glEnable(GL10.GL_DEPTH_TEST);
        initText();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
          mWidth=width;
          mHeight=height;
          float radio=(float)width/height;
          gl.glViewport(0,0,width,height);
          gl.glMatrixMode(GL10.GL_PROJECTION);
          gl.glLoadIdentity();
          //通过头上矩阵设置视口大小
          gl.glFrustumf(-radio,radio,-1,1,1,1000);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //清除颜色和深度缓存
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT|GL10.GL_DEPTH_BUFFER_BIT);
        //设置模型观测试点矩阵
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl,0,0,3,0,0,0,0,1,0);
        draw3D(gl);
        drawText(gl);
    }

    private void initText() {

    }

    private void draw3D(GL10 gl){
        gl.glFrontFace(GL10.GL_CCW);
        //平移操作
        gl.glTranslatef(0,-1,-3);
        //旋转操作
        gl.glRotatef(rot,1,1,1);
        //缩放操作
        gl.glScalef(3,3,3);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glVertexPointer(3,GL10.GL_FLOAT,0,vertices_buffer);
        gl.glColorPointer(4,GL10.GL_FLOAT,0,color_buffer);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glDrawElements(GL10.GL_TRIANGLES,60,GL10.GL_UNSIGNED_BYTE,icosahedranFaces);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        rot+=0.5f;
    }

    private void drawText(GL10 gl) {

    }
}
