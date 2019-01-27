package com.example.dell.openglex.object;
//粒子发射器
public class ParticleShooter {
    private Point3 mPosition;
    private Vector3 mDirection;
    private int mColor;

    /**
     * 创建粒子流
     * @1：位置桌标
     * @2：粒子流的放向量坐标
     * @3：颜色
     **/
    public ParticleShooter(Point3 position,Vector3 direction,int color){
        this.mPosition=position;
        this.mDirection=direction;
        this.mColor=color;
    }

    /*
    * 加载粒子到粒子系统中
    * */
    public void addParticles(ParticleSystem particleSystem,float currentTime,int count){
        for(int i=0;i<count;i++){
            particleSystem.addParticle(mPosition,mColor,mDirection,currentTime);
        }
    }
}
