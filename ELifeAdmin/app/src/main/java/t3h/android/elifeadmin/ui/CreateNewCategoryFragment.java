package t3h.android.elifeadmin.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import t3h.android.elifeadmin.helper.DropdownListHelper;
import t3h.android.elifeadmin.helper.FirebaseAuthHelper;
import t3h.android.elifeadmin.helper.SharedPreferencesHelper;
import t3h.android.elifeadmin.models.Category;
import t3h.android.elifeadmin.models.DropdownItem;
import t3h.android.elifeadmin.models.Token;
import t3h.android.elifeadmin.repositories.CategoryRepository;
import t3h.android.elifeadmin.viewmodels.CategoryViewModel;
import t3h.android.elifeadmin.viewmodels.TokenViewModel;

public class CreateNewCategoryFragment extends Fragment {
    private FragmentCreateNewCategoryBinding binding;
    private CategoryViewModel categoryViewModel;
    private String getCategoryName = "";
    private Category alreadyAvailableCategory;
    private Category inputData;

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
        inputData = new Category();
        initCategoryViewModel();
        if (requireArguments().getBoolean("isUpdate")) {
            alreadyAvailableCategory = (Category) requireArguments().get("categoryInfo");
            initUpdateUI();
        } else {
            categoryViewModel.setCategoryName("");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onMenuItemClick();
        onBackPressed();
        onEdtTextChangedListener();
        binding.saveBtn.setOnClickListener(v -> onSaveBtnClick());
    }

    private void initCategoryViewModel() {
        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        binding.setCategoryViewModel(categoryViewModel);
    }

    private void initUpdateUI() {
        binding.appBarFragment.topAppBar.setTitle(AppConstant.UPDATE_CATEGORY);
        categoryViewModel.setCategoryName(alreadyAvailableCategory.getName());
        initStatusDropdownUI();
    }

    private void onEdtTextChangedListener() {
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
        binding.spinner.setVisibility(View.VISIBLE);
        ArrayAdapter<DropdownItem> adapter = new ArrayAdapter<>(requireActivity(),
                android.R.layout.simple_spinner_item, DropdownListHelper.statusDropdown());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setSelection(alreadyAvailableCategory.getStatus());
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                DropdownItem item = (DropdownItem) adapterView.getSelectedItem();
                inputData.setStatus(item.getHiddenValue());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void onSaveBtnClick() {
        if (getCategoryName.isEmpty()) {
            errorMess(true);
        } else {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            checkToken();
        }
    }

    private void checkToken() {
        TokenViewModel tokenViewModel = new ViewModelProvider(requireActivity()).get(TokenViewModel.class);
        Token token = new Token(SharedPreferencesHelper.getAccessToken(requireContext()),
                SharedPreferencesHelper.getRefreshToken(requireContext()));
        tokenViewModel.checkToken(token).observe(requireActivity(), result -> {
            binding.progressBar.setVisibility(View.GONE);
            if (result != null) {
                if (AppConstant.REFRESH_TOKEN_SUCCESSFULLY.equals(result.getMessage())) {
                    // save access and refresh token
                    SharedPreferences sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE);
                    SharedPreferencesHelper.saveToken(sharedPref, result);
                    storeCategory();
                } else {
                    storeCategory();
                }
            } else {
                Toast.makeText(requireActivity(), AppConstant.SIGN_IN_AGAIN, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeCategory() {
        // create or update
        if (alreadyAvailableCategory != null) {
            if (!alreadyAvailableCategory.getName().equals(getCategoryName)) {
                checkValidate(true);
            } else {
                updateCategory();
            }
        } else {
            checkValidate(false);
        }
    }

    private void checkValidate(Boolean isUpdate) {
        CategoryRepository categoryRepo = new CategoryRepository(requireActivity().getApplication());
        categoryRepo.getCategoryByName(getCategoryName, categoryList -> {
            binding.progressBar.setVisibility(View.GONE);
            if (categoryList != null) {
                errorMess(false);
            } else {
                if (isUpdate) {
                    updateCategory();
                } else {
                    createCategory();
                }
            }
        });
    }

    private void errorMess(boolean isEmptyError) {
        if (isEmptyError) {
            binding.nameEdt.setError(AppConstant.MUST_NOT_BE_EMPTY);
        } else {
            binding.nameEdt.setError(AppConstant.IS_EXISTS_MESSAGE);
        }
        binding.nameEdt.requestFocus();
    }

    private void createCategory() {
        inputData.setName(getCategoryName);
        inputData.setStatus(1);
        categoryViewModel.createCategory(inputData).observe(requireActivity(), result -> {
            if (result != null) {
                binding.nameEdt.setText("");
                Toast.makeText(requireActivity(), AppConstant.CREATE_SUCCESSFULLY, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(requireActivity(), AppConstant.CREATE_FAILED, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateCategory() {
        inputData.setName(getCategoryName);
        inputData.setId(alreadyAvailableCategory.getId());
        categoryViewModel.updateCategory(inputData).observe(requireActivity(), result -> {
            if (result != null) {
                Toast.makeText(requireActivity(), AppConstant.UPDATE_SUCCESSFULLY, Toast.LENGTH_SHORT).show();
                requireActivity().onBackPressed();
            } else {
                Toast.makeText(requireActivity(), AppConstant.UPDATE_FAILED, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

}