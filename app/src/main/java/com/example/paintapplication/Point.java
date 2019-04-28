package com.example.paintapplication;

import android.graphics.Path;
import android.util.Log;

import java.util.ArrayList;

public class Point {
    Path path;
    int paintAlpha;
    int color;
    float brushSize;
    ArrayList<FloatPoint> eachPoints;

    public Point(Path path, int paintAlpha, int color, float brushSize){
        this.path = path;
        this.paintAlpha = paintAlpha;
        this.color = color;
        this.brushSize = brushSize;
        this.eachPoints = null;
    }

    // 이건 불러오기 기능때 쓰일 생성자
    public Point(int paintAlpha, int color, float brushSize){
        Log.d("jinjin","constructor checkkk");
        this.path = null;
        this.paintAlpha = paintAlpha;
        this.color = color;
        this.brushSize = brushSize;
        this.eachPoints = null;

    }

    public void setEachPoints(ArrayList<FloatPoint> points){
        //this.eachPoints = points;
        eachPoints = new ArrayList<>();
        for (int i = 0; i < points.size();i++){
            eachPoints.add(points.get(i));
        }
        for (FloatPoint e : eachPoints){  // 한 패스당 모든 거쳐간 좌표 표시하기
            Log.d("jinjin", e.x + ", " + e.y);
        }
        Log.d("jinjin",eachPoints.size()+"개");
    }

    public void setPath(Path path){
        this.path = path;
    }

    @Override
    public String toString(){
        String returnValue = this.color + "$" + this.paintAlpha + "$" + this.brushSize + "$";
        for (FloatPoint each : this.eachPoints){
            returnValue += each.getX() + "," + each.getY() + "^";
        }
        returnValue += "$";
        Log.d("jinjin",returnValue);
        return returnValue;
    }


}
