package com.example.test_ttokshow;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.test_ttokshow.Recy.ItemData;
import com.example.test_ttokshow.Scanner.ScannerCaptureActivity;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScannerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(ScannerCaptureActivity.class);
        integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("onActivityResult", "onActivityResult: .");

        Intent scan = getIntent();

        if (resultCode == Activity.RESULT_OK) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            String re = scanResult.getContents();
            Log.d("onActivityResult", "onActivityResult: ." + re);
            Toast.makeText(this, re, Toast.LENGTH_LONG).show();
            Intent intent_nxt;
            if(re.length()==13) {
                Client.send = re;
                Client.cam = true;
                MainActivity.client = true;
                intent_nxt = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent_nxt);
            }
            else {
                Client.modelNum = re;
                HomeActivity.infoMessage = "똑쇼디바이스 연결\nID : TS"+re;
                HomeActivity.info.setText(HomeActivity.infoMessage);
                Client.cam = false;
                Client.needMatch = true;
                Thread CThread = new Thread() {
                    public void run() {
                        Client.main();
                    }
                };
                CThread.start();
//                while (Client.needMatch)
//                    continue;
                //intent_nxt= new Intent(getApplicationContext(),HomeActivity.class);
            }
            //startActivity(intent_nxt);
            finish();
        }

    }
}