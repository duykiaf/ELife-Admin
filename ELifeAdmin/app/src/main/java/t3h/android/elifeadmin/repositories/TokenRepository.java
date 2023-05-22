package t3h.android.elifeadmin.repositories;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import t3h.android.elifeadmin.api.ApiProvider;
import t3h.android.elifeadmin.api.TokenApi;
import t3h.android.elifeadmin.models.Account;
import t3h.android.elifeadmin.models.Token;

public class TokenRepository {
    public Token createToken(Account account) {
        Token token = new Token();

        TokenApi tokenApi = ApiProvider.getTokenApi();
        tokenApi.createToken(account).enqueue(new Callback<Token>() {
            @Override
            public void onResponse(Call<Token> call, Response<Token> response) {
                if (response.code() == 200) {
                    if (response.body() != null) {
                        token.setAccessToken(response.body().getAccessToken());
                        token.setRefreshToken(response.body().getRefreshToken());
                    }
                }
            }

            @Override
            public void onFailure(Call<Token> call, Throwable t) {
                Log.e("ERROR", "Error when generate token");
            }
        });

        return token;
    }
}
