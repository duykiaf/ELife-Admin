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
import t3h.android.elifeadmin.databinding.ItemListLayoutBinding;
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
    private final ItemsListAdapter<Category> categoryAdapter = new ItemsListAdapter<>();
    private final ItemsListAdapter<Topic> topicAdapter = new ItemsListAdapter<>();
    private final ItemsListAdapter<Audio> audioAdapter = new ItemsListAdapter<>();

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
        fragmentListBinding.searchEdt.setText("");
        initSearchLayout();
        fragmentListBinding.goToTopImageView.setOnClickListener(v -> fragmentListBinding.listRcv.smoothScrollToPosition(0));
    }

    private void initItemsList() {
        switch (position) {
            case 0:
                CategoryViewModel categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
                categoryViewModel.getAllList().observe(requireActivity(), categoryAdapter::setData);
                fragmentListBinding.listRcv.setAdapter(categoryAdapter);
                categoryAdapter.bindAdapter(this::bindCategoryItemByStatus);
                categoryAdapter.setOnItemListClickListener((object ->
                        navigateToUpdateView(object, R.id.action_dashboardFragment_to_createNewCategoryFragment))
                );
                break;
            case 1:
                TopicViewModel topicViewModel = new ViewModelProvider(requireActivity()).get(TopicViewModel.class);
                topicViewModel.getAllList().observe(requireActivity(), topicAdapter::setData);
                fragmentListBinding.listRcv.setAdapter(topicAdapter);
                topicAdapter.bindAdapter(this::bindTopicItemByStatus);
                topicAdapter.setOnItemListClickListener((object ->
                        navigateToUpdateView(object, R.id.action_dashboardFragment_to_createNewTopicFragment)));
                break;
            case 2:
                AudioViewModel audioViewModel = new ViewModelProvider(requireActivity()).get(AudioViewModel.class);
                audioViewModel.getAllList().observe(requireActivity(), audioAdapter::setData);
                fragmentListBinding.listRcv.setAdapter(audioAdapter);
                audioAdapter.bindAdapter(this::bindAudioItemByStatus);
                audioAdapter.setOnItemListClickListener((object ->
                        navigateToUpdateView(object, R.id.action_dashboardFragment_to_createNewAudioFragment)));
                break;
        }
        fragmentListBinding.listRcv.setHasFixedSize(true);
        fragmentListBinding.listRcv.setLayoutManager(new LinearLayoutManager(requireActivity()));
    }

    private void initSearchLayout() {
        fragmentListBinding.searchImageView.setOnClickListener(v -> {
            if (fragmentListBinding.searchEdt.getVisibility() == View.VISIBLE) {
                fragmentListBinding.searchEdt.setVisibility(View.GONE);
                fragmentListBinding.searchImageView.setBackgroundResource(R.drawable.button_background);
                resetSearchLayout();
            } else {
                fragmentListBinding.searchEdt.setVisibility(View.VISIBLE);
                fragmentListBinding.searchEdt.requestFocus();
                fragmentListBinding.searchImageView.setBackgroundResource(R.drawable.pressed_button_background);
                switch (position) {
                    case 0:
                        loadCategoriesSearchList();
                        break;
                    case 1:
                        loadTopicsSearchList();
                        break;
                    case 2:
                        loadAudioSearchList();
                        break;
                }
            }
        });
    }

    private void loadCategoriesSearchList() {
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
                categoryAdapter.searchList(editable.toString());
            }
        });
    }

    private void loadTopicsSearchList() {
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
                topicAdapter.searchList(editable.toString());
            }
        });
    }

    private void loadAudioSearchList() {
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
                audioAdapter.searchList(editable.toString());
            }
        });
    }

    private void bindCategoryItemByStatus(Category model, ItemListLayoutBinding view) {
        resetViewColor(view);
        if (model.getStatus() == 0) {
            bindViewColor(view);
        }
        view.itemName.setText(model.getName());
    }

    private void bindTopicItemByStatus(Topic model, ItemListLayoutBinding view) {
        resetViewColor(view);
        if (model.getStatus() == 0) {
            bindViewColor(view);
        }
        view.itemName.setText(model.getName());
    }

    private void bindAudioItemByStatus(Audio model, ItemListLayoutBinding view) {
        resetViewColor(view);
        if (model.getStatus() == 0) {
            bindViewColor(view);
        }
        view.itemName.setText(model.getTitle());
    }

    private void resetViewColor(ItemListLayoutBinding view) {
        view.itemName.setTextColor(getResources().getColor(R.color.black));
        view.itemListLayout.setBackgroundResource(R.drawable.card_background);
    }

    private void bindViewColor(ItemListLayoutBinding view) {
        view.itemName.setTextColor(getResources().getColor(R.color.dangerColor));
        view.itemListLayout.setBackgroundResource(R.drawable.border_red_background);
    }

    private void resetSearchLayout() {
        fragmentListBinding.searchEdt.setText("");
        initItemsList();
    }

    private void navigateToUpdateView(Object object, int actionId) {
        bundle.putBoolean("isUpdate", true);
        if (object instanceof Category) {
            bundle.putSerializable("categoryInfo", (Category) object);
        } else if (object instanceof Topic) {
            bundle.putSerializable("topicInfo", (Topic) object);
        } else if (object instanceof Audio) {
            bundle.putSerializable("audioInfo", (Audio) object);
        }
        navController.navigate(actionId, bundle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentListBinding = null;
    }
}