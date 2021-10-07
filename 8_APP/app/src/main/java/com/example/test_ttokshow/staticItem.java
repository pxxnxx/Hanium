package com.example.test_ttokshow;

import android.app.Application;

public class staticItem extends Application {
    private String avg;
    private String proName;
    private String word;
    private int cnt;
    private boolean tts=true;

    @Override
    public void onCreate() {
        //전역 변수 초기화
        avg = "0.0";
        proName=" ";
        cnt=0;
        word = " ";
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }
    public boolean isTts() {
        return tts;
    }
    public void setTts(boolean tts) {
        this.tts = tts;
    }
    public void setState(String avg, String proName, String word, int cnt){
        this.avg=avg;
        this.proName=proName;
        this.cnt=cnt;
        this.word=word;
    }
    public String getProName(){
        return proName;
    }

    public String getAvg() {
        return avg;
    }

    public String getWord() {return word; }

    public int getCnt() {
        return cnt*8;
    }

    public float starRating(){
        float fAvg= Float.parseFloat(avg);
        int d= (int)fAvg;
        float f = fAvg-d;
        float half= (float)0.5;
        if(f>=0.75) return (float)d+1;
        else if(f<=0.25)return (float)d;
        else return (float)d+half;
    }

}
