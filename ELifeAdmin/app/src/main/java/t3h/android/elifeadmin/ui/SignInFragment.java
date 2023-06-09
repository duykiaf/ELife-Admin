package t3h.android.elifeadmin.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseUser;

import t3h.android.elifeadmin.R;
import t3h.android.elifeadmin.constant.AppConstant;
import t3h.android.elifeadmin.databinding.FragmentSignInBinding;
import t3h.android.elifeadmin.helper.FirebaseAuthHelper;
import t3h.android.elifeadmin.helper.SharedPreferencesHelper;
import t3h.android.elifeadmin.listener.OnBackPressedListener;
import t3h.android.elifeadmin.models.Account;
import t3h.android.elifeadmin.viewmodels.TokenViewModel;

public class SignInFragment extends Fragment implements OnBackPressedListener {
    private FragmentSignInBinding binding;
    private TokenViewModel tokenViewModel;
    private SharedPreferences sharedPref;
    private NavController navController;
    private boolean backPressedOnce = false;
    private Toast toast;
    private FirebaseUser firebaseUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        firebaseUser = FirebaseAuthHelper.getCurrentUser();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);
        if (firebaseUser != null) {
            navController.navigate(R.id.dashboardFragment);
            onDestroy();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (firebaseUser == null) {
            binding.forgotPwdTxt.setOnClickListener(v -> navController.navigate(R.id.action_signInFragment_to_forgotPasswordFragment));
            binding.signInBtn.setOnClickListener(v -> signInBtnClickListener());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    private void signInBtnClickListener() {
        String email = binding.emailEdt.getText().toString().trim();
        String password = binding.passwordEdt.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireActivity(), AppConstant.EMPTY_ERROR, Toast.LENGTH_LONG).show();
        } else {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            FirebaseAuthHelper.signIn(email, password, task -> {
                binding.progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
                    tokenViewModel = new ViewModelProvider(requireActivity()).get(TokenViewModel.class);
                    tokenViewModel.createToken(new Account(email, password)).observe(requireActivity(), token -> {
                        if (!token.getAccessToken().trim().isEmpty()) {
                            SharedPreferencesHelper.saveToken(sharedPref, token);
                        }
                        Log.d("message", token.getMessage());
                    });
                    navController.navigate(R.id.dashboardFragment);
                } else {
                    Toast.makeText(requireActivity(), AppConstant.SIGN_FAILED, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    @Override
    public void onFragmentBackPressed() {
        if (backPressedOnce) {
            requireActivity().finishAffinity();
            toast.cancel();
        } else {
            toast = Toast.makeText(requireActivity(), AppConstant.PRESS_AGAIN, Toast.LENGTH_SHORT);
            toast.show();
            backPressedOnce = true;
            new Handler().postDelayed(() -> backPressedOnce = false, 2000); // Reset the flag after a delay
        }
    }
}