package t3h.android.elifeadmin.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import t3h.android.elifeadmin.api.ApiProvider;
import t3h.android.elifeadmin.api.TokenApi;
import t3h.android.elifeadmin.models.Account;
import t3h.android.elifeadmin.models.Token;

public class TokenRepository {
    private final TokenApi tokenApi;
    public TokenRepository(){
        tokenApi = ApiProvider.getTokenApi();
    }

    public LiveData<Token> createToken(Account account) {
        MutableLiveData<Token> token = new MutableLiveData<>();

        tokenApi.createToken(account).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                token.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.e("CREATE TOKEN FAILED", t.getMessage());
            }
        });

        return token;
    }

    public LiveData<Token> checkToken(Token token) {
        MutableLiveData<Token> liveData = new MutableLiveData<>();
        tokenApi.checkToken(token).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                liveData.setValue(response.body());
                Log.d("CHECK TOKEN", String.valueOf(response.code()));
                Log.d("RESPONSE BODY", String.valueOf(response.body()));
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.e("CHECK TOKEN FAILED", t.getMessage());
            }
        });
        return liveData;
    }
}
