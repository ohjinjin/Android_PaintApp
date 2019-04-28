package com.example.paintapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import org.w3c.dom.Text;
import yuku.ambilwarna.AmbilWarnaDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1001;

    SingleTouchView singleTouchView;
    TextView mEdit;
    int DefaultColor ;
    String FILENAME = "test";
    String path = "/sdcard/TEST/";  // 파일 경로

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);  // 권한 확인

        if (permissionCheck!= PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this,"권한 승인이 필요합니다",Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this,"저장기능 사용을 위해 내부저장소 쓰기 권한이 필요합니다.",Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                Toast.makeText(this,"저장기능 사용을 위해 내부저장소 쓰기 권한이 필요합니다.",Toast.LENGTH_LONG).show();

            }
        }

        singleTouchView = (SingleTouchView)findViewById(R.id.singletouchview);
        //DefaultColor = ContextCompat.getColor(MainActivity.this, R.color.colorPrimary);
        DefaultColor = singleTouchView.color;
        //forTest
        //mEdit = (TextView)findViewById(R.id.textView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this,"승인이 허가되어 있습니다.",Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this,"아직 승인받지 않았습니다.",Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }


    public void createNewFile(View v){
        // 새로운 파일을 쓴다는것을 알려야하남...?
        // 기존에 저장되어있는애를 불러다가 뉴파일했을때 문제가 생기니까 새파일
        singleTouchView.removePaths();  // 모두지우기
    }

    public void getOldFile() {
        // 파일탐색기 나오도록
        Log.d("jinjin", "whts wrong with it");
        try {
            String fileExtenstion = ".txt";

            FileInputStream fis = new FileInputStream(path + FILENAME + fileExtenstion);
            Log.d("jinjin", "whts wrong with it1");
            byte[] data = new byte[fis.available()];

            //Log.d("jinjin", data.length + " " + data.toString());

            //while(fis.read(data) != -1) {Log.d("jinjin","whts wrong with it2");;}
            fis.read(data);

            fis.close();
            String s = new String(data);

            ArrayList<Point> pathList = new ArrayList<>();
            Log.d("jinjin", data.length + " " + s);

            String[] Points = s.split("&"); // path개수만큼이 요소개수
            String[] p = new String[10];
            Log.d("jinjin","length : " + Points[0]);
            /*for (String eachStr : Points) {
                p = eachStr.split("$"); // 4개 고정
                Log.d("jinjin","chekdjksjlfkdsjk");
                pathList.add(new Point(Integer.parseInt(p[1]), Integer.parseInt(p[0]), Float.parseFloat(p[2])));
                Log.d("jinjin","chekddddddddddddddddddd");
            }*/
            for (int i = 0; i< Points.length;i++){
                p = Points[i].split("\\$"); // 4개 고정
                //Log.d("jinjin","cccccc"+p[1] + "  "+p[0] + " " + p[2]);
                pathList.add(new Point(Integer.parseInt(p[1]), Integer.parseInt(p[0]), Float.parseFloat(p[2])));
                //Log.d("jinjin","chekddddddddddddddddddd");
            }
            Log.d("jinjin","whts wrong with it122222");
            String[][] finalData = new String[Points.length][4];
            for (int i = 0; i < finalData.length; i++) {
                for (int j = 0; j < 4; j++) {
                    finalData[i][j] = p[j + 4 * i];
                    Log.d("jinjin","chekddddddddddddddddddd");
                }

            }
            Log.d("jinjin","whts wrong with it2222222222");
            ArrayList<String> strArr = new ArrayList<>();
            ArrayList<FloatPoint> eachPoints = new ArrayList<>();

            for (int i = 0; i < finalData.length; i++) {
                strArr = (ArrayList<String>) Arrays.asList(finalData[i][3].split("^"));
                for (int j = 0; j < strArr.size(); j++) {
                    String[] tmpStr = strArr.get(j).split(",");
                    eachPoints.add(new FloatPoint(Float.parseFloat(tmpStr[0]), Float.parseFloat(tmpStr[1])));
                }
                pathList.get(i).setEachPoints(eachPoints);
            }
            Log.d("jinjin","whts wrong with it333333333333");
            singleTouchView.makePathList(pathList);


            //Log.d("jinjin",s);
            //mEdit.setText(s);
            //Log.d("jinjin",s);
        } catch (FileNotFoundException e) {
            mEdit.setText("File Not Found");
            //Log.d("jinjin","check");
        } catch (Exception e) {
            //Log.d("jinjin","cccccccc");}
        }
    }


    public boolean saveFile(){
        // 파일탐색기 나오도록
        // bitmap으로
//비트맵에 컴포넌트뷰 저장
        final Bitmap buffer= Bitmap.createBitmap(singleTouchView.getWidth(),singleTouchView.getHeight(),Bitmap.Config.ARGB_8888);
        final Canvas cv=new Canvas(buffer);
        singleTouchView.draw(cv);

        String fileExtenstion = ".jpeg";
        Log.d("jinjin",FILENAME+fileExtenstion);

        File file1 = new File(path,FILENAME+fileExtenstion);

        try{
            OutputStream fos = new FileOutputStream(file1);  // 파일 생성
            buffer.compress(Bitmap.CompressFormat.JPEG,100,fos); // jpeg 파일로 비트맵 저장
            fos.close();    // 파일 닫기

            Log.d("jinjin","succedddddss");

        }
        catch (IOException e){
            Log.d("jinjin","errorrrrr");

            e.printStackTrace();
        }
        finally {
            buffer.recycle();

            // txt로

            //String FILENAME = "test";
            fileExtenstion = ".txt";
            Log.d("jinjin",FILENAME+fileExtenstion);

            File file2 = new File(path+FILENAME+fileExtenstion);

            String contents = "";
            try{
                //FileOutputStream fos = openFileOutput(FILENAME+fileExtenstion, Context.MODE_PRIVATE);
                FileOutputStream fos = new FileOutputStream(file2);  // 파일 생성
                for (Point each : singleTouchView.pathList) {
                    Log.d("jinjin", each.toString());
                    contents += each.toString()+"&";
                }
                fos.write(contents.getBytes());   // 파일에 내용 저장
                fos.close();    // 파일 닫기
                Log.d("jinjin","success");
                return true;
            }
            catch (IOException e){
                Log.d("jinjin","errorrrrr");

                e.printStackTrace();
                return false;
            }

        }
        }




    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // 메뉴 리소스를 팽창한다. 액션바에 항목들을 추가한다.
        getMenuInflater().inflate(R.menu.action_bar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // 액션바의 항목들이 클릭되면 이벤트를 처리한다.
        switch (item.getItemId()){
            case R.id.action_refresh:
                Toast.makeText(this, "refresh", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_save:
                makeSaveDialog();   // get FileName
                if (saveFile()){
                    Toast.makeText(this, "The file was successfully saved.",Toast.LENGTH_SHORT).show();
                };
                return true;
            case R.id.action_read:
                getOldFile();
                Toast.makeText(this,"read",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // 붓의 색상 고르기 함수-https://www.android-examples.com/android-create-color-picker-dialog-example-tutorial-using-library/
    public void pickerClick(View v){
        DefaultColor = singleTouchView.color;   // 현재 전경색으로 default 설정
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(MainActivity.this, DefaultColor, false, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color) {
                DefaultColor = color;
                singleTouchView.changeColor(color);
            }
            @Override
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
                Toast.makeText(MainActivity.this, "붓의 색 고르기를 취소하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        ambilWarnaDialog.show();
    }

    // 지우개 클릭시 - 추후 수정? 다시 검토해보기
    public void eraserClick(View v){
        //singleTouchView.changeColor(constraintLayout.getBackgroundColor());
        //Drawable background = constraintLayout.getBackground();
        singleTouchView.erasePaths(((ColorDrawable)singleTouchView.getBackground()).getColor());
    }

    // 배경색의 색상 고르기 함수-https://www.android-examples.com/android-create-color-picker-dialog-example-tutorial-using-library/
    public void fillBackgroundClick(View v){
        DefaultColor = ((ColorDrawable)singleTouchView.getBackground()).getColor(); // 현재 배경색으로 default 설정
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(MainActivity.this, DefaultColor, false, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog ambilWarnaDialog, int color) {
                DefaultColor = color;
                //singleTouchView.changeBgColorSideEffect(DefaultColor,color);
                singleTouchView.setBackgroundColor(color);

            }
            @Override
            public void onCancel(AmbilWarnaDialog ambilWarnaDialog) {
                Toast.makeText(MainActivity.this, "배경색 바꾸기를 취소하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        ambilWarnaDialog.show();
    }

    public void changeToolClick(View v){
        if (v.getId()==R.id.brush){
            final Dialog seekDialog = new Dialog(this);
            //seekDialog.setTitle("Choose Tool");
            seekDialog.setContentView(R.layout.change_tool);

            final TextView opcText = (TextView)seekDialog.findViewById(R.id.opacity_txt);
            final SeekBar opcBar = (SeekBar)seekDialog.findViewById(R.id.opacity_seek);
            final TextView sizeText = (TextView)seekDialog.findViewById(R.id.size_txt);
            final SeekBar sizeBar = (SeekBar)seekDialog.findViewById(R.id.size_seek);

            opcBar.setMax(100);
            sizeBar.setMax(100);

            Button changeTool_Ok = (Button)seekDialog.findViewById(R.id.option_ok);
            Button changeTool_Cancel = (Button)seekDialog.findViewById(R.id.option_cancel);

            int currentAlphaLevel = singleTouchView.getPaintAlpha();
            int currentSize = (int)(singleTouchView.getBrushSize());
            opcText.setText("Brush Opacity : " + currentAlphaLevel + "%");
            opcBar.setProgress(currentAlphaLevel);
            sizeText.setText("Brush Size : " + currentSize + "");
            sizeBar.setProgress(currentSize);

            opcBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    opcText.setText("Brush Opacity : " + Integer.toString(progress)+"%");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            sizeBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    sizeText.setText("Brush Size : " + Integer.toString(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });

            changeTool_Ok.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    singleTouchView.setPaintAlpha(opcBar.getProgress());
                    singleTouchView.setBrushSize((float)(sizeBar.getProgress()));
                    seekDialog.dismiss();
                }
            });
            changeTool_Cancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    seekDialog.dismiss();
                    Toast.makeText(MainActivity.this, "툴 설정 변경을 취소하였습니다.", Toast.LENGTH_SHORT).show();
                }
            });

            seekDialog.show();
        }
    }

    public void makeSaveDialog(){
        final Dialog saveDialog = new Dialog(this);
        //seekDialog.setTitle("Choose Tool");
        saveDialog.setContentView(R.layout.save_activity);

        //final TextView fileName = (TextView)saveDialog.findViewById(R.id.filename);
        final EditText fileName = (EditText) saveDialog.findViewById(R.id.filename);


        Button saveDialog_Ok = (Button)saveDialog.findViewById(R.id.option_ok);
        Button saveDialog_Cancel = (Button)saveDialog.findViewById(R.id.option_cancel);

        int currentAlphaLevel = singleTouchView.getPaintAlpha();
        int currentSize = (int)(singleTouchView.getBrushSize());

        saveDialog_Ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FILENAME = fileName.getText().toString();
                saveDialog.dismiss();
            }
        });
        saveDialog_Cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDialog.dismiss();
                Toast.makeText(MainActivity.this, "파일 저장을 취소하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        saveDialog.show();

    }
}
