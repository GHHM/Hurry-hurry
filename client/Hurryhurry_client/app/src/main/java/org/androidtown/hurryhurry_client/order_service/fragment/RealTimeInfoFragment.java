package org.androidtown.hurryhurry_client.order_service.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.androidtown.hurryhurry_client.R;
import org.androidtown.hurryhurry_client.utils.Util;

import static org.androidtown.hurryhurry_client.MainActivity.mContext;

/**
 * Created by HAMHAM on 2017-11-14.
 * 현재 주문의 진행 상황을 보여주는 페이지
 */

public class RealTimeInfoFragment extends Fragment{

    protected static Dialog mProgressDialog;
    Context mContext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_real_time_info, container, false);
        mContext = rootView.getContext();
        mProgressDialog = Util.showProgressDialog(mContext);
        initView(rootView);

        return rootView;
    }

    private void initView(View rootView) {

    }
}
