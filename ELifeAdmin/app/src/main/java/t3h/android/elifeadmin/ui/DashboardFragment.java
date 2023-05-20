package t3h.android.elifeadmin.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import t3h.android.elifeadmin.R;
import t3h.android.elifeadmin.adapters.DashboardAdapter;
import t3h.android.elifeadmin.constant.AppConstant;
import t3h.android.elifeadmin.databinding.FragmentDashboardBinding;
import t3h.android.elifeadmin.helper.FirebaseAuthHelper;

public class DashboardFragment extends Fragment {
    private String[] tabLayoutNames;
    private DashboardAdapter dashboardAdapter;
    private FragmentDashboardBinding dashboardBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dashboardBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);

        View view = dashboardBinding.getRoot();

        dashboardBinding.appBarFragment.topAppBar.setTitle(AppConstant.DASHBOARD);
        dashboardBinding.appBarFragment.topAppBar.setNavigationIcon(R.drawable.ic_dashboard);

        tabLayoutNames = getResources().getStringArray(R.array.tabLayoutNames);

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

        dashboardBinding.pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        dashboardBinding.addNewImageView.setContentDescription(AppConstant.NEW_CATEGORY);
                        dashboardBinding.addNewTxtView.setText(AppConstant.NEW_CATEGORY);
                        dashboardBinding.searchImageView.setContentDescription(AppConstant.SEARCH_CATEGORY);
                        dashboardBinding.searchEdt.setHint(AppConstant.SEARCH_CATEGORY);
                        break;
                    case 1:
                        dashboardBinding.addNewImageView.setContentDescription(AppConstant.NEW_TOPIC);
                        dashboardBinding.addNewTxtView.setText(AppConstant.NEW_TOPIC);
                        dashboardBinding.searchImageView.setContentDescription(AppConstant.SEARCH_TOPIC);
                        dashboardBinding.searchEdt.setHint(AppConstant.SEARCH_TOPIC);
                        break;
                    case 2:
                        dashboardBinding.addNewImageView.setContentDescription(AppConstant.NEW_AUDIO);
                        dashboardBinding.addNewTxtView.setText(AppConstant.NEW_AUDIO);
                        dashboardBinding.searchImageView.setContentDescription(AppConstant.SEARCH_AUDIO);
                        dashboardBinding.searchEdt.setHint(AppConstant.SEARCH_AUDIO);
                        break;
                }
            }
        });

        NavController navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);
        dashboardBinding.addNewImageView.setOnClickListener(v -> {
            String getContentDescription = dashboardBinding.addNewImageView.getContentDescription().toString();
            switch (getContentDescription) {
                case AppConstant.NEW_CATEGORY:
                    navController.navigate(R.id.action_dashboardFragment_to_createNewCategoryFragment);
                    break;
                case AppConstant.NEW_TOPIC:
                    navController.navigate(R.id.action_dashboardFragment_to_createNewTopicFragment);
                    break;
                case AppConstant.NEW_AUDIO:
                    navController.navigate(R.id.action_dashboardFragment_to_createNewAudioFragment);
                    break;
            }
        });

        dashboardBinding.searchImageView.setOnClickListener(v -> {
            if (dashboardBinding.searchEdt.getVisibility() == View.VISIBLE) {
                dashboardBinding.searchEdt.setVisibility(View.GONE);
            } else {
                dashboardBinding.searchEdt.setVisibility(View.VISIBLE);
            }
        });

        if (!dashboardBinding.appBarFragment.topAppBar.getTitle().equals(AppConstant.DASHBOARD)) {
            dashboardBinding.appBarFragment.topAppBar.setNavigationOnClickListener(v -> {
                requireActivity().onBackPressed();
            });
        }

        dashboardBinding.appBarFragment.topAppBar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.changePasswordItem:
                    navController.navigate(R.id.action_dashboardFragment_to_changePasswordFragment);
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
        dashboardBinding = null;
    }
}