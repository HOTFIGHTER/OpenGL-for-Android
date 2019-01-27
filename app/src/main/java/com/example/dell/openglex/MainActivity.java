package com.example.dell.openglex;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dell.openglex.controller.LightGLRender;
import com.example.dell.openglex.controller.MatrixGLRender;
import com.example.dell.openglex.controller.ParticleGLRender;
import com.example.dell.openglex.controller.TextureGLRender;
import com.example.dell.openglex.controller.TriangleGLRender;
import com.example.dell.openglex.view.YuGLSurfaceView;

public class MainActivity extends AppCompatActivity {
    private YuGLSurfaceView mSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSurfaceView=new YuGLSurfaceView(this);
        //TextureGLRender render = new TextureGLRender(this);
        //TriangleGLRender render = new TriangleGLRender(this);
        //MatrixGLRender render=new MatrixGLRender(this);
        //LightGLRender render=new LightGLRender(this);
        ParticleGLRender render=new ParticleGLRender(this,mSurfaceView);
        mSurfaceView.setRenderer(render);
        mSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setContentView(mSurfaceView);
    }
}
