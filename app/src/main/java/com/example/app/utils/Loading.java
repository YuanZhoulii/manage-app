package com.example.app.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

public class Loading {
    private static boolean progressShow;
    private static ProgressDialog pd;

    public static void show(Context context, String msg) {
        pd = new ProgressDialog(context);
        Log.e("", "show: FFFF" );
        pd.setMessage(msg);
        pd.show();
    }

    public static void dismiss() {
        if (pd != null) {
            pd.dismiss();
        }
    }
}
