package t3h.android.elifeadmin.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import t3h.android.elifeadmin.R;
import t3h.android.elifeadmin.adapters.DashboardAdapter;
import t3h.android.elifeadmin.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {
    private static String[] tabLayoutNames = {"Categories", "Topics", "Audios"};
    private DashboardAdapter dashboardAdapter;
    private FragmentDashboardBinding dashboardBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dashboardBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);

        View view = dashboardBinding.getRoot();

        dashboardBinding.appBarFragment.topAppBar.setTitle("Dashboard");
        dashboardBinding.appBarFragment.topAppBar.setNavigationIcon(R.drawable.ic_dashboard);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dashboardAdapter = new DashboardAdapter(this);
        dashboardBinding.pager.setAdapter(dashboardAdapter);
        TabLayout tabLayout = dashboardBinding.tabLayout;
        new TabLayoutMediator(tabLayout, dashboardBinding.pager,
                (tab, position) -> tab.setText(tabLayoutNames[position])
        ).attach();
    }
}