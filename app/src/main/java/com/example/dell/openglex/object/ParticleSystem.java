package com.example.dell.openglex.object;
import android.graphics.Color;
import android.opengl.GLES20;
import android.util.Log;

import com.example.dell.openglex.program.ParticleProgram;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

//粒子系统模型
public class ParticleSystem {
    private static final int BYTES_PER_FLOAT = 4;
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int VECTOR_COMPONENT_COUNT = 3;
    private static final int PARTICLE_START_TIME_COMPONENT_COUNT = 1;
    private static final int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT
            + COLOR_COMPONENT_COUNT + VECTOR_COMPONENT_COUNT
            + PARTICLE_START_TIME_COMPONENT_COUNT;
    private static final int STRIDE = TOTAL_COMPONENT_COUNT * BYTES_PER_FLOAT;

    private FloatBuffer mVertexArrayBuffer;
    private float[] mParticles;
    private final int maxParticleCount;
    private int mNextParticle = 0;
    private int mCurrentParticleCount;
    //使用vbo
    private int[] mVboID = new int[1];
    private int dataOffset = 0;

    public ParticleSystem(int maxParticleCount) {
        mParticles = new float[maxParticleCount * TOTAL_COMPONENT_COUNT];
        this.maxParticleCount = maxParticleCount;
        /*包装particles成为FloatBuffer*/
        // 初始化ByteBuffer，长度为arr.length * 4,因为float占4个字节
        ByteBuffer qbb = ByteBuffer.allocateDirect(maxParticleCount * 4 *TOTAL_COMPONENT_COUNT);
        // 数组排列用nativeOrder
        qbb.order(ByteOrder.nativeOrder());
        mVertexArrayBuffer = qbb.asFloatBuffer();
    }

    public void addParticle(Point3 position, int color, Vector3 direction, float particleStartTime) {
        int particleOffset = mNextParticle * TOTAL_COMPONENT_COUNT;
        int currentOffset = particleOffset;
        mNextParticle++;
        if (mCurrentParticleCount < maxParticleCount) {
            mCurrentParticleCount++;
        }
        if (mNextParticle == maxParticleCount) {
            mNextParticle = 0;
        }
        mParticles[currentOffset++] = position.mPx;
        mParticles[currentOffset++] = position.mPy;
        mParticles[currentOffset++] = position.mPz;
        mParticles[currentOffset++] = Color.red(color) / 255f;
        mParticles[currentOffset++] = Color.green(color) / 255f;
        mParticles[currentOffset++] = Color.blue(color) / 255f;
        mParticles[currentOffset++] = direction.mVx;
        mParticles[currentOffset++] = direction.mVy;
        mParticles[currentOffset++] = direction.mVz;
        mParticles[currentOffset++] = particleStartTime;
        //更新本地缓存位置
        mVertexArrayBuffer.position(particleOffset);
        mVertexArrayBuffer.put(mParticles, particleOffset, TOTAL_COMPONENT_COUNT);
        mVertexArrayBuffer.position(0);
    }

    public void bindData(ParticleProgram particleProgram) {
        //int dataOffset = 0;
        // 将particles包含的package数组包装成FloatBuffer
//        vertexArrayBuffer = floatBufferUtil(particles);
//        vertexArrayBuffer.put(particles);
//        vertexArrayBuffer.position(0);
        // 创建VBO
        GLES20.glGenBuffers(1, mVboID, 0);
        // 绑定VBO
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, mVboID[0]);
        // 绑定数据,最后一个参数要改为GL_DYNAMIC_DRAW，表示数据是在变化的
        Log.d("Yu", "vertexArrayBuffer.capacity ( "+mVertexArrayBuffer.capacity()+")");
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
                mVertexArrayBuffer.capacity() * BYTES_PER_FLOAT,
                mVertexArrayBuffer, GLES20.GL_DYNAMIC_DRAW);
            //启用顶点位置数据的 属性数组
            Log.v("Yu", "PositionAttributeLocation:" + particleProgram.getPositionAttributeLocation());
            GLES20.glEnableVertexAttribArray(particleProgram
                    .getPositionAttributeLocation());
            GLES20.glVertexAttribPointer(particleProgram.getPositionAttributeLocation(),
                    POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE,
                    dataOffset);
            dataOffset += POSITION_COMPONENT_COUNT * BYTES_PER_FLOAT;
            GLES20.glEnableVertexAttribArray(particleProgram
                    .getColorAttributeLocation());
            GLES20.glVertexAttribPointer(
                    particleProgram.getColorAttributeLocation(),
                    COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE,
                    dataOffset);
            dataOffset += COLOR_COMPONENT_COUNT * BYTES_PER_FLOAT;
            GLES20.glEnableVertexAttribArray(particleProgram
                    .getDirectionVectorAttributeLocation());
            GLES20.glVertexAttribPointer(
                    particleProgram.getDirectionVectorAttributeLocation(),
                    VECTOR_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE,
                    dataOffset);
            dataOffset += VECTOR_COMPONENT_COUNT * BYTES_PER_FLOAT;
            GLES20.glEnableVertexAttribArray(particleProgram
                    .getParticleStartTimeAttributeLocation());
            GLES20.glVertexAttribPointer(
                    particleProgram.getParticleStartTimeAttributeLocation(),
                    PARTICLE_START_TIME_COMPONENT_COUNT, GLES20.GL_FLOAT, false,
                    STRIDE, dataOffset);
        // 这里很重要，处理完后，需要解除数据绑定
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    public void draw(ParticleProgram particleProgram) {
        //开始绘制这些粒子（点）
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, mCurrentParticleCount);
        /**
         * 这里非常非常的重要，不加上这句话，会照成内存泄露，程序运行几秒就蹦了
         * 猜测可能是GLES20.glBufferData()函数里有申请内存的语句，开始以为解除
         * 绑定就可以了即（GLES20.glBindBuffer）,但是发现结果还是一样，后来上
         * 官网（http://www.khronos.org/opengles/sdk/docs/man/）查看发现如下：
         * "glBufferData creates a new data store for the buffer object currently bound to target"
         * 应该要使用GLES20.glDeleteBuffers才能释放内存
         */
        GLES20.glDeleteBuffers(1, mVboID, 0);
        GLES20.glDisableVertexAttribArray(particleProgram
                .getPositionAttributeLocation());
        GLES20.glDisableVertexAttribArray(particleProgram
                .getColorAttributeLocation());
        GLES20.glDisableVertexAttribArray(particleProgram
                .getDirectionVectorAttributeLocation());
        GLES20.glDisableVertexAttribArray(particleProgram
                .getParticleStartTimeAttributeLocation());
    }
}
