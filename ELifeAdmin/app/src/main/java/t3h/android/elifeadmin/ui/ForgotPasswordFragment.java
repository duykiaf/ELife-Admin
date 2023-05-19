package t3h.android.elifeadmin.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import t3h.android.elifeadmin.R;
import t3h.android.elifeadmin.databinding.FragmentForgotPasswordBinding;

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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}