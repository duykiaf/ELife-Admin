package t3h.android.elifeadmin.helper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import t3h.android.elifeadmin.models.Account;
import t3h.android.elifeadmin.viewmodels.TokenViewModel;

public class ReAuthHelper {

    public static void reAuth(String email, String password, SharedPreferences sharedPref, Activity activity) {
        FirebaseAuthHelper.signIn(email, password, task -> {
            if (task.isSuccessful()) {
                TokenViewModel tokenViewModel = new ViewModelProvider((AppCompatActivity) activity).get(TokenViewModel.class);
                tokenViewModel.createToken(new Account(email, password)).observe((AppCompatActivity) activity, token -> {
                    if (!token.getAccessToken().trim().isEmpty()) {
                        SharedPreferencesHelper.saveToken(sharedPref, token);
                    }
                    Log.e("re-auth", token.getMessage());
                });
            } else {
                Log.e("re-auth", "re-auth failed");
            }
        });
    }
}
