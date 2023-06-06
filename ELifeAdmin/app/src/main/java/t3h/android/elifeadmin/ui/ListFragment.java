package t3h.android.elifeadmin.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import t3h.android.elifeadmin.R;
import t3h.android.elifeadmin.adapters.ItemsListAdapter;
import t3h.android.elifeadmin.databinding.FragmentListBinding;
import t3h.android.elifeadmin.models.Category;
import t3h.android.elifeadmin.models.Topic;
import t3h.android.elifeadmin.viewmodels.CategoryViewModel;
import t3h.android.elifeadmin.viewmodels.TopicViewModel;

public class ListFragment extends Fragment {
    private final Bundle bundle = new Bundle();
    private FragmentListBinding fragmentListBinding;
    private int position;
    private NavController navController;

    public ListFragment() {
    }

    public ListFragment(int position) {
        this.position = position;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentListBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_list, container, false);
        navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);
        return fragmentListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initItemsList();
    }

    @Override
    public void onResume() {
        super.onResume();
        // handle item click here
    }

    private void initItemsList() {
        switch (position) {
            case 0:
                ItemsListAdapter<Category> categoryAdapter = new ItemsListAdapter<>();
                CategoryViewModel categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
                categoryViewModel.getAllList().observe(requireActivity(), categoryAdapter::setData);
                categoryAdapter.bindAdapter((model, view) -> {
                    if (model.getStatus() == 0) {
                        view.itemName.setTextColor(getResources().getColor(R.color.dangerColor));
                        view.itemListLayout.setBackgroundResource(R.drawable.border_red_background);
                    }
                    view.itemName.setText(model.getName());
                });
                fragmentListBinding.listRcv.setAdapter(categoryAdapter);
                categoryAdapter.setOnItemListClickListener(object -> {
                    bundle.putBoolean("isUpdate", true);
                    bundle.putSerializable("categoryInfo", object);
                    navController.navigate(R.id.action_dashboardFragment_to_createNewCategoryFragment, bundle);
                });
                break;
            case 1:
                ItemsListAdapter<Topic> topicAdapter = new ItemsListAdapter<>();
                TopicViewModel topicViewModel = new ViewModelProvider(requireActivity()).get(TopicViewModel.class);
                topicViewModel.getAllList().observe(requireActivity(), topicAdapter::setData);
                fragmentListBinding.listRcv.setAdapter(topicAdapter);
                topicAdapter.bindAdapter((model, view) -> {
                    if (model.getStatus() == 0) {
                        view.itemName.setTextColor(getResources().getColor(R.color.dangerColor));
                        view.itemListLayout.setBackgroundResource(R.drawable.border_red_background);
                    }
                    view.itemName.setText(model.getName());
                });
                topicAdapter.setOnItemListClickListener(object -> {
                    bundle.putBoolean("isUpdate", true);
                    bundle.putSerializable("topicInfo", object);
                    navController.navigate(R.id.action_dashboardFragment_to_createNewTopicFragment, bundle);
                });
                break;
            case 2:
//                ItemsListAdapter<Audio> audioAdapter = new ItemsListAdapter<>();
//                fragmentListBinding.listRcv.setAdapter(audioAdapter);
                break;
        }
        fragmentListBinding.listRcv.setLayoutManager(new LinearLayoutManager(requireActivity()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentListBinding = null;
    }
}