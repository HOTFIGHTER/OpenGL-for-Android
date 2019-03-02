package com.example.dell.openglex.object;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.opengles.GL10;

public class Circle {
    private FloatBuffer vertexData;
    // 定义圆心坐标
    private float x;
    private float y;
    // 半径
    private float r;
    // 三角形分割的数量
    private int count = 40;
    private int nodeCount;
    // 每个顶点包含的数据个数 （ x 和 y ）
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int BYTES_PER_FLOAT = 4;

    public Circle() {
        x = 0f;
        y = 0f;
        r = 0.6f;
        initVertexData();
    }

    private void initVertexData() {
        // 顶点的个数，我们分割count个三角形，有count+1个点，再加上圆心共有count+2个点
        nodeCount = count + 2;
        float circleCoords[] = new float[nodeCount * POSITION_COMPONENT_COUNT];
        int offset = 0;
        circleCoords[offset++] = x;// 中心点
        circleCoords[offset++] = y;
        circleCoords[offset++]=0;
        for (int i = 0; i < count+1; i++) {
            float angleInRadians = ((float) i / (float) count)
                    * ((float) Math.PI * 2f);
            circleCoords[offset++] = x + r * (float)Math.sin(angleInRadians);
            circleCoords[offset++] = y + r * (float)Math.cos(angleInRadians);
            circleCoords[offset++]=0;
        }
        // 为存放形状的坐标，初始化顶点字节缓冲
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (坐标数 * 4)float占四字节
                circleCoords.length * BYTES_PER_FLOAT);
        // 设用设备的本点字节序
        bb.order(ByteOrder.nativeOrder());
        // 从ByteBuffer创建一个浮点缓冲
        vertexData = bb.asFloatBuffer();
        // 把坐标们加入FloatBuffer中
        vertexData.put(circleCoords);
        // 设置buffer，从第一个坐标开始读
        vertexData.position(0);
    }

    public void draw(GL10 gl){
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexData);
        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, nodeCount);//扇形绘制圆
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
