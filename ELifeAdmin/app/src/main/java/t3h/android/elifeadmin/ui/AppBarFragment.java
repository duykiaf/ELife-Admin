package t3h.android.elifeadmin.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import t3h.android.elifeadmin.R;
import t3h.android.elifeadmin.databinding.FragmentAppBarBinding;
import t3h.android.elifeadmin.helper.FirebaseAuthHelper;

public class AppBarFragment extends Fragment {
    private FragmentAppBarBinding appBarBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        appBarBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_app_bar, container, false);
        return appBarBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        onMenuItemClick();
    }

    private void onMenuItemClick() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);
        appBarBinding.topAppBar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.changePasswordItem:
                    navController.navigate(R.id.changePasswordFragment);
                    return true;
                case R.id.logoutItem:
                    FirebaseAuthHelper.signOut();
                    navController.navigate(R.id.signInFragment);
                    return true;
            }
            return false;
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        appBarBinding = null;
    }
}