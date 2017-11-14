package org.androidtown.hurryhurry_client.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import org.androidtown.hurryhurry_client.R;

/**
 * Created by HAMHAM on 2017-11-14.
 */

public class OrderConfirmDialog extends Dialog{

    Context mContext;

    public OrderConfirmDialog(Context context){
        super(context);
        mContext=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_order_confirm);
    }
}
