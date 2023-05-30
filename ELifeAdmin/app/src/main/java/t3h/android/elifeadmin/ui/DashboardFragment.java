package t3h.android.elifeadmin.ui;

import android.os.Bundle;
import android.os.Handler;
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
import t3h.android.elifeadmin.listener.OnBackPressedListener;

public class DashboardFragment extends Fragment implements OnBackPressedListener {
    private String[] tabLayoutNames;
    private DashboardAdapter dashboardAdapter;
    private FragmentDashboardBinding dashboardBinding;
    private boolean backPressedOnce = false;
    private Toast toast;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dashboardBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);
        return dashboardBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTopAppBarUI();
        initPager();
    }

    @Override
    public void onResume() {
        super.onResume();
        onMenuItemClick();
        onBackPressed();
        initSearchLayout();
        onNavigateCreateFragment();
    }

    private void initTopAppBarUI() {
        dashboardBinding.appBarFragment.topAppBar.setTitle(AppConstant.DASHBOARD);
        dashboardBinding.appBarFragment.topAppBar.setNavigationIcon(R.drawable.ic_dashboard);
    }

    private void onMenuItemClick() {
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

    private void onBackPressed() {
        if (!dashboardBinding.appBarFragment.topAppBar.getTitle().equals(AppConstant.DASHBOARD)) {
            dashboardBinding.appBarFragment.topAppBar.setNavigationOnClickListener(v -> {
                requireActivity().onBackPressed();
            });
        }
    }

    private void initPager() {
        dashboardAdapter = new DashboardAdapter(this);
        dashboardBinding.pager.setAdapter(dashboardAdapter);
        tabLayoutNames = getResources().getStringArray(R.array.tabLayoutNames);
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
                        initUIByPager(AppConstant.NEW_CATEGORY, AppConstant.SEARCH_CATEGORY);
                        break;
                    case 1:
                        initUIByPager(AppConstant.NEW_TOPIC, AppConstant.SEARCH_TOPIC);
                        break;
                    case 2:
                        initUIByPager(AppConstant.NEW_AUDIO, AppConstant.SEARCH_AUDIO);
                        break;
                }
            }
        });
    }

    private void initSearchLayout() {
        dashboardBinding.searchImageView.setOnClickListener(v -> {
            if (dashboardBinding.searchEdt.getVisibility() == View.VISIBLE) {
                dashboardBinding.searchEdt.setVisibility(View.GONE);
            } else {
                dashboardBinding.searchEdt.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initUIByPager(String addNewTxt, String searchTxt) {
        dashboardBinding.addNewImageView.setContentDescription(addNewTxt);
        dashboardBinding.addNewTxtView.setText(addNewTxt);
        dashboardBinding.searchImageView.setContentDescription(searchTxt);
        dashboardBinding.searchEdt.setHint(searchTxt);
    }

    private void onNavigateCreateFragment() {
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dashboardBinding = null;
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