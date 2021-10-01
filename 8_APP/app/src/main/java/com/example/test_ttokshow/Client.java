package com.example.test_ttokshow;

import android.app.Application;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class Client extends AppCompatActivity {
        private static String[] output;
        static String send = "5012501081100";
        static String token = "#duVCxvvKRHetPGmc-qO-ti:APA91bGX4POqrviJtSyK8yrtsKeam7tMWvsbd8f_sQjXIHxJYTD8xwF8jjnRDeXRvJoICVO6w72TVW2ZZDJ-rYsyLvu-agWx3kPddsJ-8ND0LArg16h7QvOf9s83ur_oqiCsvX2SlCbx";
        static String modelNum = "00000001";
        static Boolean cam = false;
        static Boolean needMatch = true;
        public static void main(String... args) {
            try (Socket client = new Socket()) {
                InetSocketAddress ipep = new InetSocketAddress("18.216.76.16", 9999);
                //3.144.33.17
                //18.216.76.16
                client.connect(ipep);
                try (OutputStream sender = client.getOutputStream(); InputStream receiver = client.getInputStream()) {

                    for (int i = 0; i < 1; i++) {
                        if (needMatch){
                            String mat = modelNum+token;
                            byte[] data = mat.getBytes();
                            ByteBuffer b = ByteBuffer.allocate(4);
                            b.order(ByteOrder.LITTLE_ENDIAN);
                            b.putInt(data.length);
                            sender.write(b.array(),0,4);
                            sender.write(data);
                            needMatch = false;
                        }
                        if (cam) { //!needMatch 오류 제어
                            byte[] data = send.getBytes();
                            ByteBuffer b = ByteBuffer.allocate(4);
                            b.order(ByteOrder.LITTLE_ENDIAN);
                            b.putInt(data.length);
                            sender.write(b.array(), 0, 4);
                            sender.write(data);
                            cam = false;
                        }

                        byte[] data = new byte[4];
                        //sender.write(data,0,4);
                        receiver.read(data, 0, 4);
                        ByteBuffer bu = ByteBuffer.wrap(data);
                        bu.order(ByteOrder.LITTLE_ENDIAN);

                        int length = bu.getInt();
                        data = new byte[length];
                        receiver.read(data, 0, length);

                        String msg = new String(data, "UTF-8");
                        output = msg.split("#");
                        System.out.println("ABC" + output.length);
                        System.out.println(output[0]);
                        System.out.println(output[1]);
                        System.out.println(output[2]);
                        System.out.println(output[3]);
                        System.out.println(output[4]);

                    }

                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        static String[] getOutput(){
            return output;
        }

    }

