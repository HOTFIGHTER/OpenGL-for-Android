package com.example.dell.openglex.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import com.example.dell.openglex.R;
import com.example.dell.openglex.object.Tunnel3D;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/*
 * 隧道效果渲染器
 * */
public class TunnelEffectRender implements GLSurfaceView.Renderer {
    private Context mContext;
    private Bitmap mBitmapTexture;
    private int[] mTexture = new int[1];
    private Tunnel3D mTunnel3D;
    private float centerX = 0;
    private float centerY = 0;

    public TunnelEffectRender(Context context) {
        mContext = context;
        mBitmapTexture = BitmapFactory.decodeResource(context.getResources(), R.drawable.battlebg);
        mTunnel3D = new Tunnel3D(10, 20);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //告诉系统要进行视图修正
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        //屏幕颜色清空
        gl.glClearColor(0, 0, 0, 1);
        //应用深度缓存
        gl.glEnable(GL10.GL_DEPTH_TEST);
        //初始化app
        initApp(gl);
        //设置灯光光效
        setUpLight(gl);

    }

    private void setUpLight(GL10 gl) {
        //开启光效
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);
        //环境光颜色
        FloatBuffer lightAmbient = FloatBuffer.wrap(new float[]{0.4f, 0.4f, 0.4f, 1});
        //设置环境光
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, lightAmbient);
        FloatBuffer lightDiffuse = FloatBuffer.wrap(new float[]{0.8f, 0.8f, 0.8f, 1});
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, lightDiffuse);
        FloatBuffer lightPosition = FloatBuffer.wrap(new float[]{10f, 10f, 10f});
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPosition);
    }

    private void initApp(GL10 gl) {
        //允许纹理贴图
        gl.glEnable(GL10.GL_TEXTURE_2D);
        //加载纹理贴图
        loadTexture(gl, mBitmapTexture);
    }

    //加载纹理贴图
    private void loadTexture(GL10 gl, Bitmap bmp) {
        ByteBuffer bb = ByteBuffer.allocateDirect(bmp.getHeight() * bmp.getWidth() * 4);
        bb.order(ByteOrder.nativeOrder());
        IntBuffer ib = bb.asIntBuffer();
        for (int y = 0; y < bmp.getHeight(); y++) {
            for (int x = 0; x < bmp.getWidth(); x++) {
                ib.put(bmp.getPixel(x, y));
            }
        }
        ib.position(0);
        bb.position(0);
        //创建纹理
        gl.glGenTextures(1, mTexture, 0);
        //绑定纹理
        gl.glBindTexture(GL10.GL_TEXTURE_2D, mTexture[0]);
        gl.glTexImage2D(GL10.GL_TEXTURE_2D, 0, GL10.GL_RGBA, bmp.getWidth(), bmp.getHeight(),
                0, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, bb);
        //设置纹理线性算法
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        float radio=width/height;
        gl.glViewport(0,0,width,height);
        //设置透视投影
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        //创建一个对称的透视投影矩阵
        GLU.gluPerspective(gl,45.0f,radio,1f,100f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT|GL10.GL_DEPTH_BUFFER_BIT);
        //设置模型观测试点矩阵
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        //重置矩阵
        gl.glLoadIdentity();
        //视点变换
        GLU.gluLookAt(gl, 0, 0, 1, centerX, centerY, 0, 0, 1, 0);
        //设置平滑的渲染模式
        gl.glShadeModel(GL10.GL_SMOOTH);
        //允许绘制顶点
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        mTunnel3D.render(gl,-0.6f);
        mTunnel3D.nextFrame();
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    }
}
