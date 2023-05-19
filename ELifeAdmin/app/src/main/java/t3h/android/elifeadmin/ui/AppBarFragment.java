package t3h.android.elifeadmin.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import t3h.android.elifeadmin.R;
import t3h.android.elifeadmin.databinding.FragmentAppBarBinding;

public class AppBarFragment extends Fragment {
    private FragmentAppBarBinding appBarBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        appBarBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_app_bar, container, false);
        // Inflate the layout for this fragment
        return appBarBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = requireArguments();
        appBarBinding.topAppBar.setTitle(args.getString("PAGE_TITLE", "No page found"));
        appBarBinding.topAppBar.setNavigationIcon(args.getInt("PAGE_ICON"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        appBarBinding = null;
    }
}