package t3h.android.elifeadmin.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import t3h.android.elifeadmin.models.Account;
import t3h.android.elifeadmin.models.Token;
import t3h.android.elifeadmin.repositories.TokenRepository;

public class TokenViewModel extends ViewModel {
    private final TokenRepository tokenRepo;

    public TokenViewModel() {
        tokenRepo = new TokenRepository();
    }

    public LiveData<Token> createToken(Account account) {
        return tokenRepo.createToken(account);
    }

    public LiveData<Token> refreshToken(Token token) {
        return tokenRepo.refreshToken(token);
    }
}
