package t3h.android.elifeadmin.helper;

import android.content.Context;
import android.content.SharedPreferences;

import t3h.android.elifeadmin.constant.AppConstant;
import t3h.android.elifeadmin.models.Token;

public class SharedPreferencesHelper {
    public static void saveToken(SharedPreferences sharedPref, Token token) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(AppConstant.ACCESS_TOKEN_KEY, token.getAccessToken());
        editor.putString(AppConstant.REFRESH_TOKEN_KEY, token.getRefreshToken());
        editor.apply();
    }

    public static String getAccessToken(Context context) {
        return context.getSharedPreferences("MainActivity", Context.MODE_PRIVATE)
                .getString(AppConstant.ACCESS_TOKEN_KEY, "");
    }

    public static String getRefreshToken(Context context) {
        return context.getSharedPreferences("MainActivity", Context.MODE_PRIVATE)
                .getString(AppConstant.REFRESH_TOKEN_KEY, "");
    }
}
