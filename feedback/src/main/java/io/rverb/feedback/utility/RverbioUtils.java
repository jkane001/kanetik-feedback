package io.rverb.feedback.utility;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import static io.rverb.feedback.utility.AppUtils.getPackageName;

public class RverbioUtils {
    private static final String RVERBIO_PREFS = "rverbio";
    private static final String SUPPORT_ID_KEY = "support_id";

    private static String _newSupportId;

    public static boolean initializeSupportId(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(RVERBIO_PREFS, Context.MODE_PRIVATE);
        String supportId = prefs.getString(SUPPORT_ID_KEY, "");

        if (isNullOrWhiteSpace(supportId)) {
            _newSupportId = UUID.randomUUID().toString();
            prefs.edit().putString(SUPPORT_ID_KEY, _newSupportId).apply();

            return true;
        }

        return false;
    }

    public static boolean isNullOrWhiteSpace(String string) {
        if (string == null) {
            return true;
        }

        int stringLength = string.length();
        if (stringLength == 0) {
            return true;
        }

        for (int i = 0; i < stringLength; i++) {
            if (!Character.isWhitespace(string.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static String getSupportId(Context context) {
        final SharedPreferences prefs = context.getSharedPreferences(RVERBIO_PREFS, Context.MODE_PRIVATE);
        String supportId = prefs.getString(SUPPORT_ID_KEY, "");

        if (isNullOrWhiteSpace(supportId)) {
            throw new IllegalStateException("You must call Rverbio#initialize before accessing the SupportId",
                    new Throwable("Rverbio instance not initialized"));
        }

        return supportId;
    }

    public static String getNetworkType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork.getType() == ConnectivityManager.TYPE_WIFI ? "WiFi" : "Not WiFi";
    }

    public static File createScreenshotFile(@NonNull Activity activity) {
        try {
            File imageFile = File.createTempFile("rv_screenshot", ".png", activity.getCacheDir());

            View v1 = activity.getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);

            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            FileOutputStream fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 75, fout);

            fout.flush();
            fout.close();

            return imageFile;
        } catch (IOException e) {
            LogUtils.w(e.getMessage(), e);
        }

        return null;
    }

    public static String getApiKey(Context context) {
        ApplicationInfo ai = null;

        try {
            ai = context.getPackageManager().getApplicationInfo(getPackageName(context), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Bundle bundle = ai.metaData;
        return bundle.getString("io.rverb.apiKey");
    }
}