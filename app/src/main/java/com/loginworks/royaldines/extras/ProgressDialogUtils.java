package com.loginworks.royaldines.extras;


import android.app.Activity;
import android.app.ProgressDialog;

public class ProgressDialogUtils {
    private static ProgressDialog progressDialog;

    public static void startProgressBar(Activity activity, String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity.getBaseContext(), ProgressDialog.STYLE_SPINNER);
        }

        if (!progressDialog.isShowing() && !activity.isFinishing()) {
            progressDialog = ProgressDialog.show(activity, "Please wait...", msg, true, false);
        }
    }


    public static void stopProgress() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            progressDialog = null;
        }
    }

}
