package t3h.android.elifeadmin.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import t3h.android.elifeadmin.models.Account;
import t3h.android.elifeadmin.models.Token;

public interface TokenApi {
    @POST("/create_token")
    Call<Token> createToken(@Body Account account);

    @POST("/check_token")
    Call<Token> checkToken(@Body Token token);
}
