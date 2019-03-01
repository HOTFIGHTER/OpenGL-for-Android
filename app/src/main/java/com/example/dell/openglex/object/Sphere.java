package com.example.dell.openglex.object;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;
//绘制球形图像
public class Sphere {
    float mAngleA,mAngleB;
    float mCos,mSin;
    float mR1,mR2;
    float mH1,mH2;
    float mStep = 10.0f;
    float[][] mVertex = new float[32][3];
    ByteBuffer vbb;
    FloatBuffer vBuf;

    public Sphere() {
        vbb = ByteBuffer.allocateDirect(mVertex.length * mVertex[0].length * 4);
        vbb.order(ByteOrder.nativeOrder());
        vBuf = vbb.asFloatBuffer();
    }

    public void draw(GL10 gl) {
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        for (mAngleA = -90.0f; mAngleA < 90.0f; mAngleA += mStep) {
            int n = 0;
            mR1 = (float) Math.cos(mAngleA * Math.PI / 180.0);
            mR2 = (float) Math.cos((mAngleA + mStep) * Math.PI / 180.0);
            mH1 = (float) Math.sin(mAngleA * Math.PI / 180.0);
            mH2 = (float) Math.sin((mAngleA + mStep) * Math.PI / 180.0);
            // 固定纬度, 360 度旋转遍历一条纬线
            for (mAngleB = 0.0f; mAngleB <= 360.0f; mAngleB += mStep) {
                mCos = (float) Math.cos(mAngleB * Math.PI / 180.0);
                mSin = -(float) Math.sin(mAngleB * Math.PI / 180.0);
                mVertex[n][0] = (mR2 * mCos);
                mVertex[n][1] = (mH2);
                mVertex[n][2] = (mR2 * mSin);
                mVertex[n + 1][0] = (mR1 * mCos);
                mVertex[n + 1][1] = (mH1);
                mVertex[n + 1][2] = (mR1 * mSin);
                vBuf.put(mVertex[n]);
                vBuf.put(mVertex[n + 1]);
                n += 2;
                Log.v("Yu","n:"+n);
                if (n > 31) {
                    vBuf.position(0);
                    gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vBuf);
                    gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, n);
                    n = 0;
                    mAngleB -= mStep;
                }
            }
            vBuf.position(0);
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vBuf);
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, n);
        }
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
