package t3h.android.elifeadmin.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import t3h.android.elifeadmin.models.Audio;
import t3h.android.elifeadmin.models.Category;
import t3h.android.elifeadmin.models.Topic;
import t3h.android.elifeadmin.viewmodels.AudioViewModel;
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
        initSearchLayout();
        fragmentListBinding.goToTopImageView.setOnClickListener(v -> fragmentListBinding.listRcv.smoothScrollToPosition(0));
    }

    private void initItemsList() {
        switch (position) {
            case 0:
                ItemsListAdapter<Category> categoryAdapter = new ItemsListAdapter<>();
                CategoryViewModel categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
                categoryViewModel.getAllList().observe(requireActivity(), categories -> {
                    categoryAdapter.setData(categories);
                    fragmentListBinding.searchEdt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            categoryAdapter.cancelTimer();
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            categoryAdapter.searchList(editable.toString(), categories);
                        }
                    });
                });
                fragmentListBinding.listRcv.setAdapter(categoryAdapter);
                categoryAdapter.bindAdapter((model, view) -> {
                    if (model.getStatus() == 0) {
                        view.itemName.setTextColor(getResources().getColor(R.color.dangerColor));
                        view.itemListLayout.setBackgroundResource(R.drawable.border_red_background);
                    }
                    view.itemName.setText(model.getName());
                });
                categoryAdapter.setOnItemListClickListener(object -> {
                    bundle.putBoolean("isUpdate", true);
                    bundle.putSerializable("categoryInfo", object);
                    navController.navigate(R.id.action_dashboardFragment_to_createNewCategoryFragment, bundle);
                });
                break;
            case 1:
                ItemsListAdapter<Topic> topicAdapter = new ItemsListAdapter<>();
                TopicViewModel topicViewModel = new ViewModelProvider(requireActivity()).get(TopicViewModel.class);
                topicViewModel.getAllList().observe(requireActivity(), topics -> {
                    topicAdapter.setData(topics);
                    fragmentListBinding.searchEdt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            topicAdapter.cancelTimer();
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            topicAdapter.searchList(editable.toString(), topics);
                        }
                    });
                });
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
                ItemsListAdapter<Audio> audioAdapter = new ItemsListAdapter<>();
                AudioViewModel audioViewModel = new ViewModelProvider(requireActivity()).get(AudioViewModel.class);
                audioViewModel.getAllList().observe(requireActivity(), audios -> {
                    audioAdapter.setData(audios);
                    fragmentListBinding.searchEdt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            audioAdapter.cancelTimer();
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            audioAdapter.searchList(editable.toString(), audios);
                        }
                    });
                });
                fragmentListBinding.listRcv.setAdapter(audioAdapter);
                audioAdapter.bindAdapter((model, view) -> {
                    if (model.getStatus() == 0) {
                        view.itemName.setTextColor(getResources().getColor(R.color.dangerColor));
                        view.itemListLayout.setBackgroundResource(R.drawable.border_red_background);
                    }
                    view.itemName.setText(model.getTitle());
                });
                audioAdapter.setOnItemListClickListener(object -> {
                    bundle.putBoolean("isUpdate", true);
                    bundle.putSerializable("audioInfo", object);
                    navController.navigate(R.id.action_dashboardFragment_to_createNewAudioFragment, bundle);
                });
                break;
        }
        fragmentListBinding.listRcv.setLayoutManager(new LinearLayoutManager(requireActivity()));
    }

    private void initSearchLayout() {
        fragmentListBinding.searchImageView.setOnClickListener(v -> {
            if (fragmentListBinding.searchEdt.getVisibility() == View.VISIBLE) {
                fragmentListBinding.searchEdt.setVisibility(View.GONE);
                fragmentListBinding.searchImageView.setBackgroundResource(R.drawable.button_background);
            } else {
                fragmentListBinding.searchEdt.setVisibility(View.VISIBLE);
                fragmentListBinding.searchImageView.setBackgroundResource(R.drawable.pressed_button_background);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentListBinding = null;
    }
}