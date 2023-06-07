package t3h.android.elifeadmin.ui;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import t3h.android.elifeadmin.R;
import t3h.android.elifeadmin.databinding.FragmentChangePasswordBinding;
import t3h.android.elifeadmin.helper.FirebaseAuthHelper;

public class ChangePasswordFragment extends Fragment {
    private FragmentChangePasswordBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_change_password, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.backTxt.setOnClickListener(v -> {
            requireActivity().onBackPressed();
        });

        binding.saveBtn.setOnClickListener(v -> {
            String oldPwd = binding.oldPasswordEdt.getText().toString();
            String newPwd = binding.newPasswordEdt.getText().toString();
            String confirmNewPwd = binding.confirmNewPasswordEdt.getText().toString();
            if (oldPwd.isEmpty() || newPwd.isEmpty() || confirmNewPwd.isEmpty()) {
                Toast.makeText(requireActivity(), "Please fill out the form!", Toast.LENGTH_LONG).show();
            } else {
                if (!confirmNewPwd.equals(newPwd)) {
                    Toast.makeText(requireActivity(), "Confirm password does not match new password!", Toast.LENGTH_LONG).show();
                } else {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    // set progress bar color
                    binding.progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
                    FirebaseAuthHelper.changePassword(oldPwd, newPwd, task -> {
                        binding.progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            Toast.makeText(requireActivity(), "Change password successfully! You can sign in again.",
                                    Toast.LENGTH_LONG).show();
                            NavController navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);
                            FirebaseAuthHelper.signOut();
                            navController.navigate(R.id.signInFragment);
                        } else {
                            Toast.makeText(requireActivity(), "Change password failed. Please try again!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}