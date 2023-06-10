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
                categoryViewModel.getAllList().observe(requireActivity(), categoryAdapter::setData);
                fragmentListBinding.listRcv.setAdapter(categoryAdapter);
                categoryAdapter.bindAdapter(this::bindCategoryItemByStatus);
                categoryAdapter.setOnItemListClickListener((object ->
                        navigateToUpdateView(object, R.id.action_dashboardFragment_to_createNewCategoryFragment))
                );
                break;
            case 1:
                ItemsListAdapter<Topic> topicAdapter = new ItemsListAdapter<>();
                TopicViewModel topicViewModel = new ViewModelProvider(requireActivity()).get(TopicViewModel.class);
                topicViewModel.getAllList().observe(requireActivity(), topicAdapter::setData);
                fragmentListBinding.listRcv.setAdapter(topicAdapter);
                topicAdapter.bindAdapter(this::bindTopicItemByStatus);
                topicAdapter.setOnItemListClickListener((object ->
                        navigateToUpdateView(object, R.id.action_dashboardFragment_to_createNewTopicFragment)));
                break;
            case 2:
                ItemsListAdapter<Audio> audioAdapter = new ItemsListAdapter<>();
                AudioViewModel audioViewModel = new ViewModelProvider(requireActivity()).get(AudioViewModel.class);
                audioViewModel.getAllList().observe(requireActivity(), audioAdapter::setData);
                fragmentListBinding.listRcv.setAdapter(audioAdapter);
                audioAdapter.bindAdapter(this::bindAudioItemByStatus);
                audioAdapter.setOnItemListClickListener((object ->
                        navigateToUpdateView(object, R.id.action_dashboardFragment_to_createNewAudioFragment)));
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

                switch (position) {
                    case 0:
                        CategoryViewModel categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
                        ItemsListAdapter<Category> searchListAdapter = new ItemsListAdapter<>();
                        fragmentListBinding.searchEdt.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                searchListAdapter.cancelTimer();
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                categoryViewModel.getAllList().observe(requireActivity(), categories -> {
                                    searchListAdapter.setData(categories);
                                    searchListAdapter.searchList(editable.toString(), categories);
                                });
                                fragmentListBinding.listRcv.setAdapter(searchListAdapter);
                                searchListAdapter.bindAdapter((model, view) -> bindCategoryItemByStatus(model, view));
                                searchListAdapter.setOnItemListClickListener(object ->
                                        navigateToUpdateView(object, R.id.action_dashboardFragment_to_createNewCategoryFragment)
                                );
                            }
                        });
                        break;
                    case 1:
                        TopicViewModel topicViewModel = new ViewModelProvider(requireActivity()).get(TopicViewModel.class);
                        ItemsListAdapter<Topic> searchTopicsListAdapter = new ItemsListAdapter<>();
                        fragmentListBinding.searchEdt.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                searchTopicsListAdapter.cancelTimer();
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                topicViewModel.getAllList().observe(requireActivity(), topics -> {
                                    searchTopicsListAdapter.setData(topics);
                                    searchTopicsListAdapter.searchList(editable.toString(), topics);
                                });
                                fragmentListBinding.listRcv.setAdapter(searchTopicsListAdapter);
                                searchTopicsListAdapter.bindAdapter((model, view) -> bindTopicItemByStatus(model, view));
                                searchTopicsListAdapter.setOnItemListClickListener(object ->
                                        navigateToUpdateView(object, R.id.action_dashboardFragment_to_createNewTopicFragment)
                                );
                            }
                        });
                        break;
                    case 2:
                        AudioViewModel audioViewModel = new ViewModelProvider(requireActivity()).get(AudioViewModel.class);
                        ItemsListAdapter<Audio> searchAudiosListAdapter = new ItemsListAdapter<>();
                        fragmentListBinding.searchEdt.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                                searchAudiosListAdapter.cancelTimer();
                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                audioViewModel.getAllList().observe(requireActivity(), audios -> {
                                    searchAudiosListAdapter.setData(audios);
                                    searchAudiosListAdapter.searchList(editable.toString(), audios);
                                });
                                fragmentListBinding.listRcv.setAdapter(searchAudiosListAdapter);
                                searchAudiosListAdapter.bindAdapter((model, view) -> bindAudioItemByStatus(model, view));
                                searchAudiosListAdapter.setOnItemListClickListener(object ->
                                        navigateToUpdateView(object, R.id.action_dashboardFragment_to_createNewAudioFragment)
                                );
                            }
                        });
                        break;
                }
            }
        });
    }

    private void bindCategoryItemByStatus(Category model, ItemListLayoutBinding view) {
        if (model.getStatus() == 0) {
            bindViewColor(view);
        }
        view.itemName.setText(model.getName());
    }

    private void bindTopicItemByStatus(Topic model, ItemListLayoutBinding view) {
        if (model.getStatus() == 0) {
            bindViewColor(view);
        }
        view.itemName.setText(model.getName());
    }

    private void bindAudioItemByStatus(Audio model, ItemListLayoutBinding view) {
        if (model.getStatus() == 0) {
            bindViewColor(view);
        }
        view.itemName.setText(model.getTitle());
    }

    private void bindViewColor(ItemListLayoutBinding view) {
        view.itemName.setTextColor(getResources().getColor(R.color.dangerColor));
        view.itemListLayout.setBackgroundResource(R.drawable.border_red_background);
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