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
        String result = context.getSharedPreferences("MainActivity", Context.MODE_PRIVATE)
                .getString(AppConstant.ACCESS_TOKEN_KEY, "");
        return result;
    }
}
