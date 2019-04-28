package com.example.paintapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.constraint.solver.widgets.Rectangle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.jar.Attributes;

public class SingleTouchView extends View {
    ArrayList<Point> pathList = new ArrayList<>();
    private ArrayList<FloatPoint> inputEachPoints = new ArrayList<>();
    private Paint paint = new Paint();
    private Path path;
    int paintAlpha = 255;
    int color = Color.BLACK;
    int eraserColor = Color.WHITE;
    int eraserAlpha = 100;
    float brushSize = 10f;
    boolean isEraserMode = false;

    float eraserX = 0;
    float eraserY = 0;
    int eraserSize = 50;

    public SingleTouchView(Context context, AttributeSet attrs){
        super(context, attrs);

        paint.setAntiAlias(true);   // 선분을 매끄럽게 그리기 위하여 앤티 에일리어싱을 설정한다.
        paint.setStrokeWidth(brushSize);  // 선분의 두께를 10으로 한다.
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    // 현재까지의 경로를 모두 그린다.
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        synchronized (pathList) {
            for (Point point : pathList) {
                //if (point.eachPoints!=null){
                //Log.d("jinjin", ""+point.eachPoints.size());}
                paint.setColor(point.color);
                paint.setAlpha(point.paintAlpha);
                paint.setStrokeWidth(point.brushSize);
                canvas.drawPath(point.path, paint);
            }
        }
        if (isEraserMode){
            canvas.drawCircle(eraserX,eraserY,eraserSize,paint);
        }
        //changeColor(color);
        //setPaintAlpha(paintAlpha);
    }

    @Override
    // 터치된 위치를 얻는다.
    public boolean onTouchEvent(MotionEvent event){
        float eventX = event.getX();
        float eventY = event.getY();


        synchronized (pathList) {
            if (!isEraserMode) {
                inputEachPoints.add(new FloatPoint(eventX, eventY));
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        path = new Path();
                        path.moveTo(eventX, eventY);
                        pathList.add(new Point(path, paintAlpha, color, brushSize));

                        return true;
                    case MotionEvent.ACTION_MOVE:
                        path.lineTo(eventX, eventY);
                        break;
                    case MotionEvent.ACTION_UP:
                        pathList.get(pathList.size() - 1).setEachPoints(inputEachPoints);
                        inputEachPoints.clear(); // 한 패스 끝내면 eachpoints 비우기
                        Log.d("jinjin", "action_up executed!");
                        break;
                    default:
                        return false;
                }
            } else {   // eraser Mode일 경우
                //inputEachPoints.add(new FloatPoint(eventX,eventY));
                eraserX = eventX;
                eraserY = eventY;
                //RectF rect = new RectF();
                RectF myRect = new RectF();
                //myRect.setBounds(eraserX,eraserY,eraserSize,eraserSize);
                myRect.left = eraserX;
                myRect.right = eraserX + eraserSize;
                myRect.top = eraserY;
                myRect.bottom = eraserY + eraserSize;
                //Log.d("jinjin","debuggginggggggg...1   " + pathList.size());
                int max_1 = pathList.size();
                for (int i = 0; i < max_1; i++) {
//                    Log.d("jinjin","debuggginggggggg...4    " + pathList.get(i).eachPoints.size() + "\n"+ pathList.get(i).toString());
                    int max_2 = pathList.get(i).eachPoints.size();
                    Point tmpPoint = pathList.get(i);
                    for (int j = 0; j < max_2; j++) {
                        //Log.d("jinjin","debuggginggggggg...5    " + pathList.get(i).eachPoints.size() + "\n"+ pathList.get(i).toString());

                        //Rectangle pathRect = new Rectangle();
                        RectF pathRect = new RectF();
                        //pathRect.setBounds((int)(pathList.get(i).eachPoints.get(j).getX()), (int)(pathList.get(i).eachPoints.get(j).getY()), 10, 10);
                        pathRect.left = (int) (tmpPoint.eachPoints.get(j).getX());
                        pathRect.right = (int) (tmpPoint.eachPoints.get(j).getX()) + 1;
                        pathRect.top = (int) (tmpPoint.eachPoints.get(j).getY());
                        pathRect.bottom = (int) (tmpPoint.eachPoints.get(j).getY()) + 1;
                        //Log.d("jinjin", "debuggginggggggg...2 " + "j = " + j + " " + RectF.intersects(myRect, pathRect) + " cxheck");
                        //if (myRect.intersects(pathRect)) {
                        if (RectF.intersects(myRect, pathRect)) {
                            //Log.d("jinjin", "debuggginggggggg...3");
                            pathList.remove(tmpPoint);
                            max_1--;
                        }
                    }
                }
            }
        }
        invalidate();
        return true;
    }

    public void changeColor(int color){
        isEraserMode = false;
        this.color = color;
        paint.setColor(color);
    }

    public int getPaintAlpha(){
        isEraserMode = false;
        return Math.round((float)paintAlpha/255*100);
    }

    public void setPaintAlpha(int newAlpha){
        isEraserMode = false;
        paintAlpha = Math.round((float)newAlpha/100*255);
        //drawPaint.setColor(paintColo)
        paint.setAlpha(paintAlpha);
    }

    public float getBrushSize(){
        isEraserMode = false;
        return brushSize;
    }

    public void setBrushSize(float newSize){
        isEraserMode = false;
        brushSize = newSize;
        paint.setStrokeWidth(brushSize);
    }

    public void erasePaths(int color){
        isEraserMode = true;
        /*eraserColor = color;
        changeColor(eraserColor);
        setPaintAlpha(eraserAlpha);*/
    }

    public void makePathList(ArrayList<Point> pathList){
        Log.d("jinjin","check function callllllllllllllllllll");
        this.pathList.clear();

        for (int i = 0; i < pathList.size();i++){
            this.pathList.add(pathList.get(i));
        }
        invalidate();
    }
/*
    public void changeBgColorSideEffect(int oldColor, int newColor){
        for (int i = 0; i<pathList.size();i++){
            if (pathList.get(i).color == oldColor){
                pathList.get(i).color = newColor;
            }
            invalidate();
        }
    }*/

    public void removePaths(){
        this.pathList.clear();
        invalidate();
    }
}

