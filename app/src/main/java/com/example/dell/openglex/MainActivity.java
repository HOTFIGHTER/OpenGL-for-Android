package com.example.dell.openglex;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.openglex.controller.CarouselGLRender;
import com.example.dell.openglex.controller.LightGLRender;
import com.example.dell.openglex.controller.MatrixGLRender;
import com.example.dell.openglex.controller.ParticleGLRender;
import com.example.dell.openglex.controller.ShapeGLRender;
import com.example.dell.openglex.controller.TextGLRender;
import com.example.dell.openglex.controller.TextureGLRender;
import com.example.dell.openglex.controller.TriangleGLRender;
import com.example.dell.openglex.controller.TunnelEffectRender;
import com.example.dell.openglex.view.YuGLSurfaceView;

public class MainActivity extends AppCompatActivity {
    private YuGLSurfaceView mSurfaceView;
    private TextView mTvActivityMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSurfaceView=new YuGLSurfaceView(this);
        //TextureGLRender render = new TextureGLRender(this);
        //TriangleGLRender render = new TriangleGLRender(this);
        //MatrixGLRender render=new MatrixGLRender(this);
        //LightGLRender render=new LightGLRender(this);
        //ParticleGLRender render=new ParticleGLRender(this,mSurfaceView);
        //TunnelEffectRender render=new TunnelEffectRender(this);
        //TextGLRender render=new TextGLRender(this);
//        setContentView(R.layout.activity_main);
//        mTvActivityMain=findViewById(R.id.tv_activity);
//        mTvActivityMain.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(MainActivity.this,R.string.click_info,Toast.LENGTH_SHORT).show();
//            }
//        });
        //CarouselGLRender render=new CarouselGLRender(this);
        ShapeGLRender render=new ShapeGLRender(this);
        mSurfaceView.setRenderer(render);
//        ViewGroup.LayoutParams params= new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 500);
//        addContentView(mSurfaceView,params);
        //mSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        setContentView(mSurfaceView);
    }
}
