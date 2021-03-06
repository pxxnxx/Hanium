package com.example.test_ttokshow;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.test_ttokshow.Recy.Adapter;
import com.example.test_ttokshow.Recy.ItemData;
import com.example.test_ttokshow.Recy.RecyclerDeco;
import com.example.test_ttokshow.Recy.ViewType;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hedgehog.ratingbar.RatingBar;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class MainActivity extends Activity {
    private ImageButton open_bu;
    private Dialog dialog;
    private ImageView iv_image;
    private ImageView pro_image;
    private ItemData item;
    private ImageButton tts;
    private Adapter adapter;
    public ArrayList<ItemData> list_s;
    public ArrayList<ItemData> list;
    public ArrayList<ItemData> list_d;
    public String[] output = new String[0];
    public static Boolean client = false;
    private TextToSpeech TTS;
    private String TTSText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        list_s = new ArrayList<>();
        list = new ArrayList<>();
        list_d = new ArrayList<>();
        staticItem myApp = (staticItem)getApplicationContext();

        Thread CThread = new Thread() {
            public void run() {
                Client.main();
                String[] out = Client.getOutput();
                output = out;
                for (int i=0; i < (output.length / 5)-1; i++) {
                    item = new ItemData(output[5*i + 7], output[5*i + 8], output[5*i + 4], output[5*i + 5], output[5*i + 6]);
                    if (i < 10) list_s.add(item);
                    list.add(item);
                }
                if(output.length!=0)myApp.setState(output[2],output[1],output[0],output.length/5 - 1);
            client = false;
        }
    };
        if (client) {
            CThread.start();
            try {
                CThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace(); }
        }

        else {

            output = Client.getOutput();
            for (int i=0; i < (output.length / 5)-1; i++) {
                item = new ItemData(output[5*i + 7], output[5*i + 8], output[5*i + 4], output[5*i + 5], output[5*i + 6]);
                if (i < 10) list_s.add(item);
                list.add(item);
            }
            if(output.length!=0)myApp.setState(output[2],output[1],output[0],output.length/5 - 1);
        }

        if(output.length<=5){
            /**Error Dialog*/
            dialog = new Dialog(MainActivity.this);       // Dialog ?????????
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // ????????? ??????
            dialog.setContentView(R.layout.error_popup); //xml ??????
            showDialog();
        }

        /**TTS*/
        TTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result= TTS.setLanguage(Locale.KOREAN);
                    if(result ==TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(MainActivity.this,"???????????? ?????? ??????",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        if(myApp.isTts()){
            if(myApp.isTts()){
                Log.d("TTS", "onCreate:"+myApp.isTts());
//            TTS.setPitch((float) 0.1);      // ??????
//            TTS.setSpeechRate((float) 1.0); // ????????????
                TTSText = myApp.getProName() + "??? ?????????????????????." + "  ???????????? " + myApp.getCnt() + "???" +
                        " ??? ??????" + myApp.getAvg() + "???  ?????? ??? ?????? ?????? ??????" + myApp.getWord();
                TTS.speak(TTSText, TextToSpeech.QUEUE_FLUSH,null,null);
            }
        }

        /**Button*/
        BtnOnClickListener onClickListener = new BtnOnClickListener();
        //inflation layout
        open_bu = findViewById(R.id.open);
        open_bu.setOnClickListener(onClickListener);

        //all review
        Button all_review = findViewById(R.id.all_review);
        all_review.setOnClickListener(onClickListener);

        //scanner btn
        ImageButton camera = findViewById(R.id.cameraBtn);
        camera.setOnClickListener(onClickListener);

        //home btn
        ImageButton home = findViewById(R.id.home_btn);
        home.setOnClickListener(onClickListener);

        //tts
        tts= findViewById(R.id.ttsBtn);
        tts.setOnClickListener(onClickListener);

        /**Text View*/
        TextView product_name = findViewById(R.id.name);
        product_name.setText(myApp.getProName());
        TextView grade_float = findViewById(R.id.gradef);
        grade_float.setText(myApp.getAvg()+"/5");
        TextView person_many = findViewById(R.id.cnt_per);
        person_many.setText(Integer.toString(myApp.getCnt())+"???");

        /**Recycler view*/
        RecyclerView recyclerView = findViewById(R.id.recyclerView_s);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        RecyclerDeco decoration_Width = new RecyclerDeco(26,26,0,0);
        recyclerView.addItemDecoration(decoration_Width);

        adapter = new Adapter(ViewType.small,list_s);
        adapter.setOnItemClickListener((v, position) -> {
            // ??????????????? ??????????????? ??????????????? ??????
            ItemData item = adapter.getItemPos(position);
            System.out.println("click "+item);
            Intent intent_Z=  new Intent(getApplicationContext(), Zoom_Review.class);
            intent_Z.putExtra("Item", item);
            startActivity(intent_Z);
        });
        recyclerView.setAdapter(adapter);

        /**custom star*/
        RatingBar mRatingBar =findViewById(R.id.ratingBar);
        mRatingBar.setStarCount(5);
//        System.out.println("rating     "+myApp.starRating());
        mRatingBar.setStar(myApp.starRating());

        /**Text*/
        product_name = findViewById(R.id.name);
        product_name.setSingleLine(true);    // ????????? ????????????
        product_name.setEllipsize(TextUtils.TruncateAt.MARQUEE); // ????????? ?????????
        product_name.setSelected(true);      // ????????????

        /**image*/
        iv_image = findViewById(R.id.keywordbox);
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://ttks-161718.appspot.com/");
        StorageReference storageRef = storage.getReference();
        if(output.length>0) {
            storageRef.child(output[3]+".jpg").getDownloadUrl().addOnSuccessListener(uri -> Glide.with(getApplicationContext())
                    .load(uri)
                    .into(iv_image)).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Toast.makeText(getApplicationContext(), "??????", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    class BtnOnClickListener implements Button.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.all_review:
                    Intent intent = new Intent(getApplicationContext(), Total_Review.class);
                    intent.putExtra("Item", list);
                    startActivity(intent);
                    break;
                case R.id.open:
                    LinearLayout inflatedLayout = findViewById(R.id.inflatedlayout);
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    if (!open_bu.isSelected()) {
                        inflater.inflate(R.layout.inflated_layout, inflatedLayout);
                        open_bu.setSelected(true);
                        pro_image= findViewById(R.id.productImggggg);
                        FirebaseStorage storage2 = FirebaseStorage.getInstance("gs://ttks-161718.appspot.com/");
                        StorageReference storageRef = storage2.getReference();
                        storageRef.child("pro_img/"+output[3]+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(getApplicationContext())
                                        .load(uri)
                                        .into(pro_image);
                            }
                        }).addOnFailureListener(e -> Toast.makeText(getApplicationContext(), "??????",Toast.LENGTH_SHORT).show());

                    } else {
                        inflatedLayout.removeAllViews();
                        open_bu.setSelected(false);
                    }
                    break;
                case R.id.cameraBtn:
                    Intent scan = new Intent(getApplicationContext(), ScannerActivity.class);
                    startActivity(scan);
                    break;
                case R.id.ttsBtn:
                    TTS.speak(TTSText, TextToSpeech.QUEUE_FLUSH,null,null);
                    break;
                case R.id.home_btn:
                    Intent intent_home = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent_home);
                    break;
            }
        }

    }
    public void showDialog(){
        dialog.show();
        //round ????????????
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // ??????????????? ??? ??????????????? ????????????
        dialog.setCanceledOnTouchOutside(false);
        // ??????????????? ???????????? ?????? ??????
        dialog.setCancelable(false);
        Button goBackBtn = dialog.findViewById(R.id.goBack);
        goBackBtn.setOnClickListener(view -> {
            try {
                Intent intent =new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
            }
            catch (Exception e){
                e.printStackTrace();
            }
            dialog.dismiss(); // ??????????????? ??????
        });
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