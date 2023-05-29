package t3h.android.elifeadmin.ui;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import t3h.android.elifeadmin.R;
import t3h.android.elifeadmin.constant.AppConstant;
import t3h.android.elifeadmin.databinding.FragmentCreateNewCategoryBinding;
import t3h.android.elifeadmin.helper.FirebaseAuthHelper;
import t3h.android.elifeadmin.models.Category;
import t3h.android.elifeadmin.repositories.CategoryRepository;
import t3h.android.elifeadmin.viewmodels.CategoryViewModel;

public class CreateNewCategoryFragment extends Fragment {
    private FragmentCreateNewCategoryBinding binding;
    private String[] status;
    private ArrayAdapter<String> adapterItems;
    private CategoryViewModel categoryViewModel;
    private String getCategoryName = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_create_new_category, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTopAppBarUI();
        initStatusDropdownUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        onMenuItemClick();
        onBackPressed();

        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        binding.setCategoryViewModel(categoryViewModel);
        binding.nameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                InputFilter[] filters = new InputFilter[1];
                filters[0] = new InputFilter.LengthFilter(AppConstant.INPUT_MAX_LENGTH);
                binding.nameEdt.setFilters(filters);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                categoryViewModel.setCategoryName(charSequence.toString());
                if (charSequence.toString().length() == 20) {
                    binding.nameEdt.setError(AppConstant.LIMIT_ERROR);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                getCategoryName = categoryViewModel.getCategoryName().getValue().trim();
            }
        });

        binding.saveBtn.setOnClickListener(v -> onSaveBtnClick());
    }

    private void initTopAppBarUI() {
        binding.appBarFragment.topAppBar.setTitle(AppConstant.CREATE_NEW_CATEGORY);
        binding.appBarFragment.topAppBar.setNavigationIcon(R.drawable.ic_back);
    }

    private void onMenuItemClick() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);
        binding.appBarFragment.topAppBar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.changePasswordItem:
                    navController.navigate(R.id.action_createNewCategoryFragment_to_changePasswordFragment);
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
        if (!binding.appBarFragment.topAppBar.getTitle().equals(AppConstant.DASHBOARD)) {
            binding.appBarFragment.topAppBar.setNavigationOnClickListener(v -> {
                requireActivity().onBackPressed();
            });
        }
    }

    private void initStatusDropdownUI() {
        status = getResources().getStringArray(R.array.status);
        adapterItems = new ArrayAdapter<>(requireActivity(), R.layout.item_dropdown_list, status);
        binding.autoCompleteTxt.setAdapter(adapterItems);
        // get selected item
//        binding.autoCompleteTxt.setOnItemClickListener((adapterView, view1, i, l) -> {
//            String item = adapterView.getItemAtPosition(i).toString();
//            binding.autoCompleteTxt.setText(item);
//        });
    }

    private void onSaveBtnClick() {
        if (getCategoryName.isEmpty()) {
            binding.nameEdt.setError(AppConstant.MUST_NOT_BE_EMPTY);
            binding.nameEdt.requestFocus();
        } else {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);

            CategoryRepository categoryRepo = new CategoryRepository(requireActivity().getApplication());
            categoryRepo.getCategoryByName(getCategoryName, categoryList -> {
                binding.progressBar.setVisibility(View.GONE);
                if (categoryList != null) {
                    binding.nameEdt.setError(AppConstant.IS_EXISTS_MESSAGE);
                    binding.nameEdt.requestFocus();
                } else {
                    Category category = new Category(getCategoryName, 1);
                    categoryViewModel.createCategory(category).observe(requireActivity(), result -> {
                        if (result != null) {
                            binding.nameEdt.setText("");
                            Toast.makeText(requireActivity(), AppConstant.CREATE_SUCCESSFULLY, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(requireActivity(), AppConstant.CREATE_FAILED, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}