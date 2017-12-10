package org.androidtown.hurryhurry_client.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import org.androidtown.hurryhurry_client.MainActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;


public class HttpPostSend {

    public static final String API_SERVER_URL = "http://192.168.1.46:3000/post";    //test server url
    //public static final String API_SERVER_URL = "http://www.icthvn.or.kr:1111/app/";

    private static String executeClient(String urlString, String postParams) {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
            connection.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
            connection.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            OutputStream outStream = connection.getOutputStream();
            //버퍼를 생성하고 넣음

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
            writer.write(postParams);
            writer.flush();
            writer.close();//버퍼를 받아줌
            //서버로 부터 데이터를 받음
            InputStream stream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(stream));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();//서버로 부터 받은 값을 리턴해줌 아마 OK!!가 들어올것임
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();//버퍼를 닫아줌
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
     return null;
}

    //주문 정보 조회하기
    public static String exeGetOrderInfo(String params) {
        return executeClient(API_SERVER_URL, params);
    }

    //주문 등록하기
    public static String exeRegOrder(String params) {
        //URL 뒤에 정확한 URL이....
        return executeClient(API_SERVER_URL, params);
    }

    //주문 수정하기
    public static String exeModOrder(String params) {
        //URL 뒤에 정확한 URL이....
        return executeClient(API_SERVER_URL, params);
    }


}