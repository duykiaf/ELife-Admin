package t3h.android.elifeadmin.helper;

import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class JWTHelper {
    public static String decoded(String refreshToken) {
        String password;
        String[] split = refreshToken.split("\\.");
        try {
            password = getPasswordFromJson(split[1]);
        } catch (UnsupportedEncodingException | JSONException e) {
            throw new RuntimeException(e);
        }
        Log.e("Password", password);
        return password;
    }

    private static String getPasswordFromJson(String strEncoded) throws UnsupportedEncodingException, JSONException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);
        JSONObject jsonObject = new JSONObject(decodedString);
        JSONObject dataObject = jsonObject.getJSONObject("data");
        return dataObject.getString("password");
    }
}
