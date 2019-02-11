package com.example.dell.openglex.program;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.example.dell.openglex.utils.GLHelper;

public class ParticleProgram {
    protected static final String U_TIME = "u_Time";
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_DIRECTION_VECTOR = "a_DirectionVector";
    protected static final String A_PARTICLE_START_TIME = "a_ParticleStartTime";
    private int uMatrixLocation;
    private int uTimeLocation;
    private int aPositionLocation;
    private int aColorLocation;
    private int aDirectionVectorLocation;
    private int aParticleStartTimeLocation;
    // Shader着色器的代码
    private String mVertexShader;
    private String mFragmentShader;
    private int mProgram;

    public ParticleProgram(Context context, GLSurfaceView v){
        initShader(v);
    }

    public void initShader(GLSurfaceView v) {
//        mVertexShader = GLHelper.loadFromAssetsFile("vertex.sh",
//                v.getResources());
        mVertexShader="uniform mat4 u_Matrix;\n" +
                "uniform float u_Time;\n" +
                "attribute vec3 a_Position;\n" +
                "attribute vec3 a_Color;\n" +
                "attribute vec3 a_DirectionVector;\n" +
                "attribute float a_ParticleStartTime;\n" +
                "varying vec3 v_Color;\n" +
                "varying float v_ElapsedTime;\n" +
                "void main(){\n" +
                "v_Color=a_Color;" +
                "v_ElapsedTime=u_Time-a_ParticleStartTime;\n" +
                "vec3 currentPosition=a_Position+(a_DirectionVector*v_ElapsedTime);\n" +
                "gl_Position=u_Matrix*vec4(currentPosition,1.0);\n" +
                "gl_PointSize=10.0;\n" +
                "}\n";
//        mFragmentShader = GLHelper.loadFromAssetsFile("frag.sh",
//                v.getResources());
        mFragmentShader="precision mediump float;\n" +
                "varying vec3 v_Color;\n" +
                "varying float v_ElapsedTime;\n" +
                "void main(){\n" +
                "gl_FragColor=vec4(v_Color/v_ElapsedTime,1.0);\n" +
                "}\n";
        /*
         * 创建着色器程序, 传入顶点着色器脚本 和 片元着色器脚本 注意顺序不要错
         */
        mProgram = GLHelper.createProgram(mVertexShader, mFragmentShader);
        /*
         * 从着色程序中获取一致变量
         */
        uMatrixLocation = GLES20.glGetUniformLocation(mProgram, U_MATRIX);
        uTimeLocation = GLES20.glGetUniformLocation(mProgram, U_TIME);
        /*
         * 从着色程序中获取 属性变量 ,数据引用 其中的"aPosition"是顶点着色器中的顶点位置信息
         * 其中的"aColor"是顶点着色器的颜色信息等，返回一个ID，openGL程序就通过这个ID与GLSL
         * 进行数据交互
         */
        aPositionLocation = GLES20.glGetAttribLocation(mProgram, A_POSITION);
        aColorLocation = GLES20.glGetAttribLocation(mProgram, A_COLOR);
        aDirectionVectorLocation = GLES20.glGetAttribLocation(mProgram,
                A_DIRECTION_VECTOR);
        aParticleStartTimeLocation = GLES20.glGetAttribLocation(mProgram,
                A_PARTICLE_START_TIME);
    }

    //使用GLSL程序
    public void useProgram(){
        GLES20.glUseProgram(mProgram);
    }

    public void setUniforms(float[] matrix, float elapsedTime) {
        //将ViewPort矩阵变量传递给顶点Shader中的u_Matrix
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        //将GlobalTime传递给顶点Shader的u_Time
        GLES20.glUniform1f(uTimeLocation, elapsedTime);
    }

    //获取与GLSL交互的点的位置变量的ID
    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }
    //获取与GLSL交互的点的颜色变量的ID
    public int getColorAttributeLocation() {
        return aColorLocation;
    }
    //获取与GLSL交互的点的方向变量的ID
    public int getDirectionVectorAttributeLocation() {
        return aDirectionVectorLocation;
    }
    //获取与GLSL交互的点的时间变量的ID
    public int getParticleStartTimeAttributeLocation() {
        return aParticleStartTimeLocation;
    }
}
