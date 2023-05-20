package t3h.android.elifeadmin.ui;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import t3h.android.elifeadmin.R;
import t3h.android.elifeadmin.databinding.FragmentForgotPasswordBinding;
import t3h.android.elifeadmin.helper.FirebaseAuthHelper;

public class ForgotPasswordFragment extends Fragment {
    private FragmentForgotPasswordBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_forgot_password, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.backTxt.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        binding.resetPwdBtn.setOnClickListener(v -> {
            String email = binding.emailEdt.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(requireActivity(), "Email must not empty!", Toast.LENGTH_LONG).show();
            } else {
                binding.progressBar.setVisibility(View.VISIBLE);
                // set progress bar color
                binding.progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                FirebaseAuthHelper.forgotPassword(email, task -> {
                    binding.progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        Toast.makeText(requireActivity(), "Check your email, please!", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(requireActivity(), "Reset password failed. Please try again!", Toast.LENGTH_LONG).show();
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