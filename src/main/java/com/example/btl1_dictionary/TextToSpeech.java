package com.example.btl1_dictionary;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.sound.sampled.*;

public class TextToSpeech {
    public static void TextTSP(int choice) {
        try {
            //Nếu choice  = 2 là nói tiếng việt, còn lại nói tiếng anh;
            Scanner scan = new Scanner(System.in);
            String apiRe = scan.nextLine();
            String tmp = apiRe.replace(" ", "%20");
            System.out.println(tmp);
            // Xác định URL
            String apiUrl = "https://api.voicerss.org/?key=331802f6088c4348b53f5cb3f553e3f3&hl=en-us&v=Odai&src=";
            if(choice == 2) {
                System.out.println("Da vao choice = 2");
                apiUrl = "https://api.voicerss.org/?key=331802f6088c4348b53f5cb3f553e3f3&hl=vi-vn&v=Chi&src=";
            }
            apiUrl += tmp;
            // Tạo URL object
            URI uri = new URI(apiUrl);
            URL url = uri.toURL();
            // Tạo kết nối HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // Đọc dữ liệu từ API
            InputStream inputStream = connection.getInputStream();
            byte[] data = inputStream.readAllBytes();

            // Tạo một ByteArrayInputStream từ dữ liệu âm thanh
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);

            // Mở audio line để phát âm thanh
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(byteArrayInputStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // Chơi âm thanh
            clip.start();

            // Đợi cho âm thanh chơi xong
            Thread.sleep(clip.getMicrosecondLength() / 1000);

            // Đóng tài nguyên
            clip.close();
            audioInputStream.close();
            byteArrayInputStream.close();
            inputStream.close();
            connection.disconnect();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    }
