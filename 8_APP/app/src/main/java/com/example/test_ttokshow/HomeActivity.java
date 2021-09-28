package com.example.test_ttokshow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class HomeActivity extends AppCompatActivity {
    private ImageButton deviceBtn;
    private ImageButton ttsBtn;
    private TextView info;
    private ImageView icon;
    private TextToSpeech TTS;
    staticItem myApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        myApp= (staticItem) getApplicationContext();

        /**TextView*/
        info=findViewById(R.id.comment);
        info.setText("똑쇼 디바이스가\n연결되지 않았습니다.");

        /**ImageView*/
        icon=findViewById(R.id.comIcon);
        icon.setImageResource(R.drawable.warning);

        /**Button*/
        BtnOnClickListener onClickListener = new BtnOnClickListener();
        //deviceConDiscon
        deviceBtn = findViewById(R.id.deviceConDiscon);
        deviceBtn.setOnClickListener(onClickListener);
        //searchProduct
        ImageButton searchP = findViewById(R.id.searchProduct);
        searchP.setOnClickListener(onClickListener);
        //lastestReview
        ImageButton lastestRev=findViewById(R.id.lastestReview);
        lastestRev.setOnClickListener(onClickListener);
        //ttsOnOff
        ttsBtn=findViewById(R.id.ttsOnOff);
        ttsBtn.setOnClickListener(onClickListener);

        /**TTS TEST*/
        TTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result= TTS.setLanguage(Locale.KOREAN);
                    if(result ==TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(HomeActivity.this,"지원하지 않는 언어",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        if(myApp.isTts()){
            Log.d("TTS", "onCreate:"+myApp.isTts());
//            TTS.setPitch((float) 0.1);      // 음량
//            TTS.setSpeechRate((float) 1.0); // 재생속도
            TTS.speak("welcome", TextToSpeech.QUEUE_FLUSH,null,null);
        }

    }
    class BtnOnClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.deviceConDiscon:
                    if(!deviceBtn.isSelected()){
                        deviceBtn.setSelected(true);
//                        Intent intent = new Intent(getApplicationContext(),ScannerActivity.class);
//                        startActivity(intent);
                        String con_info="똑쇼디바이스 연결\nID : ";
                        con_info+="000000";
                        info.setText(con_info);
                        icon.setImageResource(R.drawable.device);
                    }
                    else{
                        deviceBtn.setSelected(false);
                        //TODO 연결해제
                        info.setText("똑쇼 디바이스가\n연결되지 않았습니다.");
                        icon.setImageResource(R.drawable.warning);
                    }
                    break;
                case R.id.searchProduct:
                    //Intent intent = new Intent(getApplicationContext(),ScannerActivity.class);
                    Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.lastestReview:
                    finish();
                    break;
                case R.id.ttsOnOff:
//                    if(!ttsBtn.isSelected()){
//                        myApp.setTts(true);
//                        ttsBtn.setSelected(true);
//                    }
//                    else{
//                        myApp.setTts(false);
//                        ttsBtn.setSelected(false);
//                    }
                    TTS.speak("welcome", TextToSpeech.QUEUE_FLUSH,null,null);

                    break;
            }
        }
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // hideNavigationBar();
            showSystemUI();
        }
    }
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}