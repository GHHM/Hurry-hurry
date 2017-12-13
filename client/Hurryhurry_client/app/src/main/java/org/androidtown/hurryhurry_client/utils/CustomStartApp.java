package org.androidtown.hurryhurry_client.utils;

/**
 * Created by WonHada.com on 2016-04-20.
 */

import android.app.Application;
import com.tsengvn.typekit.Typekit;


public class CustomStartApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "BMJUA_ttf.ttf"))
                .addBold(Typekit.createFromAsset(this, "BMJUA_ttf.ttf"))
                .addCustom1(Typekit.createFromAsset(this, "BMJUA_ttf.ttf"));// "fonts/폰트.ttf"
    }
}
