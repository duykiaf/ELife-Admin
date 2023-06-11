package t3h.android.elifeadmin.helper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.content.AsyncTaskLoader;

import t3h.android.elifeadmin.models.Account;
import t3h.android.elifeadmin.viewmodels.TokenViewModel;

public class ReAuthHelper {
    public void reAuth(Activity activity, String email, String password,
                       SharedPreferences sharedPref, OnFinishListener onFinishListener) {
        new ReAuth(activity, email, password, sharedPref, onFinishListener).forceLoad();
    }

    public interface OnFinishListener {
        void onFinish(boolean isSuccess);
    }

    private class ReAuth extends AsyncTaskLoader<Boolean> {
        private final Activity activity;
        private final String email;
        private final String password;
        private final SharedPreferences sharedPref;
        private final OnFinishListener onFinishListener;

        public ReAuth(@NonNull Activity activity, String email, String password,
                      SharedPreferences sharedPref, OnFinishListener onFinishListener) {
            super(activity);
            this.activity = activity;
            this.email = email;
            this.password = password;
            this.sharedPref = sharedPref;
            this.onFinishListener = onFinishListener;
        }

        @Nullable
        @Override
        public Boolean loadInBackground() {
            FirebaseAuthHelper.signIn(email, password, task -> {
                if (task.isSuccessful()) {
                    TokenViewModel tokenViewModel = new ViewModelProvider((AppCompatActivity) activity).get(TokenViewModel.class);
                    tokenViewModel.createToken(new Account(email, password)).observe((AppCompatActivity) activity, token -> {
                        if (!token.getAccessToken().trim().isEmpty()) {
                            SharedPreferencesHelper.saveToken(sharedPref, token);
                        }
                        Log.e("re-auth", token.getMessage());
                    });
                    deliverResult(true);
                } else {
                    Log.e("re-auth", "re-auth failed");
                    deliverResult(false);
                }
            });
            return null;
        }

        @Override
        public void deliverResult(@Nullable Boolean data) {
            super.deliverResult(data);
            if (onFinishListener != null) {
                onFinishListener.onFinish(Boolean.TRUE.equals(data));
            }
        }
    }
}
