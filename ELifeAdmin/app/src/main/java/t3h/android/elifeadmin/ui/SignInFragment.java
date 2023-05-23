package t3h.android.elifeadmin.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
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

import t3h.android.elifeadmin.R;
import t3h.android.elifeadmin.databinding.FragmentSignInBinding;
import t3h.android.elifeadmin.helper.FirebaseAuthHelper;
import t3h.android.elifeadmin.helper.SharedPreferencesHelper;
import t3h.android.elifeadmin.models.Account;
import t3h.android.elifeadmin.viewmodels.TokenViewModel;

public class SignInFragment extends Fragment {
    private FragmentSignInBinding binding;
    private TokenViewModel tokenViewModel;
    private SharedPreferences sharedPref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);
        binding.forgotPwdTxt.setOnClickListener(v -> navController.navigate(R.id.action_signInFragment_to_forgotPasswordFragment));

        binding.signInBtn.setOnClickListener(v -> {
            String email = binding.emailEdt.getText().toString().trim();
            String password = binding.passwordEdt.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireActivity(), "Please fill out the form!", Toast.LENGTH_LONG).show();
            } else {
                binding.progressBar.setVisibility(View.VISIBLE);
                // set progress bar color
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
                        Toast.makeText(requireActivity(), "Sign in failed. Please try again!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}