package com.example.dell.openglex.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;

public class YuGLSurfaceView extends GLSurfaceView {
    public YuGLSurfaceView(Context context) {
        super(context);
    }

    public YuGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        super.surfaceChanged(holder, format, w, h);
    }
}
