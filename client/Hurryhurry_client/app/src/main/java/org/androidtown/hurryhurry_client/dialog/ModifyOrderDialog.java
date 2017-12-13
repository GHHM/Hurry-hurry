package org.androidtown.hurryhurry_client.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import org.androidtown.hurryhurry_client.R;
import org.androidtown.hurryhurry_client.utils.HttpPostSend;
import org.json.JSONObject;

import static android.R.transition.move;

/**
 * Created by HAMHAM on 2017-12-12.
 */

public class ModifyOrderDialog extends Dialog {
    static Dialog mProgressDialog;
    Context mContext;

    ImageButton ib_close;
    Button bt_request;
    RadioGroup rg_topping;
    public ModifyOrderDialog(Context context){
        super(context);
        mContext=context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_modify_order);
        initView();

        //닫기 버튼
        ib_close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //요청하기 버튼
        bt_request.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setRegDataParam();
            }
        });

    }
    void initView(){
        ib_close = (ImageButton) findViewById(R.id.ib_close);
        bt_request = (Button) findViewById(R.id.bt_request);
        rg_topping = (RadioGroup)findViewById(R.id.rg_topping);
    }

    private String setRegDataParam() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.accumulate("MEMBER_ID", "modify");
            jsonObject.accumulate("FOOD_NAME", "modified pizza");
            jsonObject.accumulate("RFID_ID", "1");
            jsonObject.accumulate("ARRIVAL_TIME", "20171212");
            jsonObject.accumulate("PROCESS_1", "complete");
            jsonObject.accumulate("PROCESS_2", "complete");
            jsonObject.accumulate("PROCESS_3", "not yet");
            //TODO
            // 토핑 추가시 요청사항 보내기
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

}
