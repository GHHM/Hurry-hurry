package org.androidtown.hurryhurry_client.order_service.fragment;

import android.app.Dialog;
import android.app.Fragment;
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

import org.androidtown.hurryhurry_client.R;
import org.androidtown.hurryhurry_client.dialog.ModifyOrderDialog;
import org.androidtown.hurryhurry_client.utils.HttpPostSend;
import org.androidtown.hurryhurry_client.utils.Util;
import org.json.JSONObject;

import static org.androidtown.hurryhurry_client.MainActivity.mContext;

/**
 * Created by HAMHAM on 2017-11-14.
 * 현재 주문의 진행 상황을 보여주는 페이지
 */

public class RealTimeInfoFragment extends Fragment{

    protected static Dialog mProgressDialog;
    Context mContext;
    Button bt_modify_p1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_real_time_info, container, false);
        mContext = rootView.getContext();
        mProgressDialog = Util.showProgressDialog(mContext);
        initView(rootView);

        //변경하기 버튼1
        bt_modify_p1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ModifyOrderDialog dialog = new ModifyOrderDialog(mContext);
                dialog.setCancelable(false);
                dialog.show();
            }
        });

        return rootView;
    }

    private void initView(View rootView) {
        bt_modify_p1 = (Button) rootView.findViewById(R.id.bt_modify_p1);
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
    public class JSONTask extends AsyncTask<String, String, String> {

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
}
