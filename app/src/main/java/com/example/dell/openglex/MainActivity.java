package com.example.dell.openglex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.dell.openglex.controller.YuGLRender;
import com.example.dell.openglex.view.YuGLSurfaceView;

public class MainActivity extends AppCompatActivity {
    private YuGLSurfaceView mSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSurfaceView=new YuGLSurfaceView(this);
        YuGLRender render = new YuGLRender();
        mSurfaceView.setRenderer(render);
        setContentView(mSurfaceView);
    }
}
