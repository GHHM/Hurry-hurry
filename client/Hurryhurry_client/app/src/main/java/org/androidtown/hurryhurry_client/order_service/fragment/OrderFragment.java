package org.androidtown.hurryhurry_client.order_service.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import org.androidtown.hurryhurry_client.MainActivity;
import org.androidtown.hurryhurry_client.R;
import org.androidtown.hurryhurry_client.utils.HttpPostSend;
import org.androidtown.hurryhurry_client.utils.JSONUtil;
import org.androidtown.hurryhurry_client.utils.Util;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by HAMHAM on 2017-11-14.
 * 음식을 주문할 수 있는 페이지
 */

public class OrderFragment extends Fragment {

    protected Activity mActivity;
    protected static Dialog mProgressDialog;
    Context mContext;

    private EditText ed_orderFoodName;
    private Button bt_ordering;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_order, container, false);
        mActivity = this.getActivity();
        mContext = rootView.getContext();
        mProgressDialog = Util.showProgressDialog(mContext);
        initView(rootView);

        bt_ordering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new JSONTask().execute("http://192.168.1.46:3000/post");
                new JSONTask().execute();
            }
        });

        return rootView;
    }

    private void initView(View rootView) {
        ed_orderFoodName = (EditText) rootView.findViewById(R.id.ed_orderFoodName);
        bt_ordering = (Button) rootView.findViewById(R.id.bt_ordering);
    }



    //화면 초기화
    private void initScreen() {
        ed_orderFoodName.setText("");
    }

    //토스트
    protected void showToast(int msg) {
        Toast.makeText(mActivity, mActivity.getString(msg), Toast.LENGTH_LONG).show();
    }

    protected void showToast(String msg) {
        Toast.makeText(mActivity, msg, Toast.LENGTH_LONG).show();
    }

    //JSON 오브젝트를 string으로 바꿔준다.
    private String setRegDataParam() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("MEMBER_ID", "androidTest");
            jsonObject.accumulate("FOOD_NAME", "pizza");
            jsonObject.accumulate("RFID_ID", "1");
            jsonObject.accumulate("ARRIVAL_TIME", "20171212");
            jsonObject.accumulate("PROCESS_1", "complete");
            jsonObject.accumulate("PROCESS_2", "not yet");
            jsonObject.accumulate("PROCESS_3", "not yet");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    //JSON을 이용한 HTTP 통신
    public class JSONTask extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String result;
            result = HttpPostSend.exeRegOrder(setRegDataParam());
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgressDialog.dismiss();
        }
    }

  /*  public class JSONTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                //JSONObject를 만들고 key value 형식으로 값을 저장해준다.

                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("user_id", "androidTest");
                jsonObject.accumulate("name", "yun");
                HttpURLConnection con = null;
                BufferedReader reader = null;

                try {
                    //URL url = new URL("http://192.168.25.16:3000/users");
                    URL url = new URL(urls[0]);
                    //연결을 함
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("POST");//POST방식으로 보냄
                    con.setRequestProperty("Cache-Control", "no-cache");//캐시 설정
                    con.setRequestProperty("Content-Type", "application/json");//application JSON 형식으로 전송
                    con.setRequestProperty("Accept", "text/html");//서버에 response 데이터를 html로 받음
                    con.setDoOutput(true);//Outstream으로 post 데이터를 넘겨주겠다는 의미
                    con.setDoInput(true);//Inputstream으로 서버로부터 응답을 받겠다는 의
                    con.connect();
                    //서버로 보내기위해서 스트림 만듬
                    OutputStream outStream = con.getOutputStream();
                    //버퍼를 생성하고 넣음

                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                    writer.write(jsonObject.toString());
                    writer.flush();
                    writer.close();//버퍼를 받아줌
                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();
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
                    if (con != null) {
                        con.disconnect();
                    }
                    try {
                        if (reader != null) {
                            reader.close();//버퍼를 닫아줌
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }*/
}
