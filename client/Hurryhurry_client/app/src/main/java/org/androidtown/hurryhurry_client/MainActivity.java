package org.androidtown.hurryhurry_client;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import org.androidtown.hurryhurry_client.order_service.fragment.OrderFragment;
import org.androidtown.hurryhurry_client.order_service.fragment.RealTimeInfoFragment;

import static java.security.AccessController.getContext;
import static org.androidtown.hurryhurry_client.R.id.bt_orderingMenu;

public class MainActivity extends AppCompatActivity {

    public static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mContext = this.getApplicationContext();
        Button bt_orderingMenu = (Button) findViewById(R.id.bt_orderingMenu);
        Button bt_currentOrder = (Button) findViewById(R.id.bt_currentOrder);

        bt_orderingMenu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                OrderFragment orderFragment = new OrderFragment();
                ft.replace(R.id.ll_content, orderFragment).commit();
            }
        });

        bt_currentOrder.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                RealTimeInfoFragment realTimeInfoFragment = new RealTimeInfoFragment();
                ft.replace(R.id.ll_content, realTimeInfoFragment).commit();
            }
        });


       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
