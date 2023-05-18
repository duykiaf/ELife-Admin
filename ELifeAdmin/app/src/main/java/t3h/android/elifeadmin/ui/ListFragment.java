package t3h.android.elifeadmin.ui;

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

import t3h.android.elifeadmin.R;
import t3h.android.elifeadmin.databinding.FragmentListBinding;

public class ListFragment extends Fragment {
    private FragmentListBinding fragmentListBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentListBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);

        // Inflate the layout for this fragment
        return fragmentListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentListBinding = null;
    }
}