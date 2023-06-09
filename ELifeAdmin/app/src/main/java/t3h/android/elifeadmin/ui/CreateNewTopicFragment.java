package t3h.android.elifeadmin.ui;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import t3h.android.elifeadmin.R;
import t3h.android.elifeadmin.constant.AppConstant;
import t3h.android.elifeadmin.databinding.FragmentCreateNewTopicBinding;
import t3h.android.elifeadmin.helper.DropdownListHelper;
import t3h.android.elifeadmin.helper.FirebaseAuthHelper;
import t3h.android.elifeadmin.helper.SharedPreferencesHelper;
import t3h.android.elifeadmin.models.Category;
import t3h.android.elifeadmin.models.DropdownItem;
import t3h.android.elifeadmin.models.Token;
import t3h.android.elifeadmin.models.Topic;
import t3h.android.elifeadmin.repositories.TopicRepository;
import t3h.android.elifeadmin.viewmodels.CategoryViewModel;
import t3h.android.elifeadmin.viewmodels.TokenViewModel;
import t3h.android.elifeadmin.viewmodels.TopicViewModel;

public class CreateNewTopicFragment extends Fragment {
    private FragmentCreateNewTopicBinding binding;
    private TopicViewModel topicViewModel;
    private Topic inputData, alreadyAvailableTopic;
    private String getTopicName = "";
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    private Uri selectedImageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_create_new_topic, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputData = new Topic();
        initTopicViewModel();
        if (requireArguments().getBoolean("isUpdate")) {
            alreadyAvailableTopic = (Topic) requireArguments().get("topicInfo");
            initUpdateUI();
        } else {
            initTopAppBarUI(false);
            topicViewModel.setTopicName("");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onBackPressed();
        onMenuItemClick();
        onEdtTextChangedListener();
        binding.addImage.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(requireActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9999);
            } else {
                selectImage();
            }
        });
        initCategoriesDropdown();
        binding.saveBtn.setOnClickListener(v -> onSaveBtnClick());
    }

    private void initTopicViewModel() {
        topicViewModel = new ViewModelProvider(requireActivity()).get(TopicViewModel.class);
        binding.setTopicViewModel(topicViewModel);
    }

    private void initTopAppBarUI(Boolean isUpdate) {
        if (isUpdate) {
            binding.appBarFragment.topAppBar.setTitle(AppConstant.UPDATE_TOPIC);
        } else {
            binding.appBarFragment.topAppBar.setTitle(AppConstant.CREATE_NEW_TOPIC);
        }
        binding.appBarFragment.topAppBar.setNavigationIcon(R.drawable.ic_back);
    }

    private void onBackPressed() {
        if (!binding.appBarFragment.topAppBar.getTitle().equals(AppConstant.DASHBOARD)) {
            binding.appBarFragment.topAppBar.setNavigationOnClickListener(v -> {
                requireActivity().onBackPressed();
            });
        }
    }

    private void onMenuItemClick() {
        NavController navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment);
        binding.appBarFragment.topAppBar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.changePasswordItem:
                    navController.navigate(R.id.action_createNewTopicFragment_to_changePasswordFragment);
                    return true;
                case R.id.logoutItem:
                    FirebaseAuthHelper.signOut();
                    navController.navigate(R.id.signInFragment);
                    return true;
            }
            return false;
        });
    }

    private void initUpdateUI() {
        initTopAppBarUI(true);
        topicViewModel.setTopicName(alreadyAvailableTopic.getName());
        Glide.with(requireActivity())
                .load(alreadyAvailableTopic.getImage())
                .centerCrop()
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_broken_image)
                .into(binding.imagePreview);
        initStatusDropdown();
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
                topicViewModel.setTopicName(charSequence.toString());
                if (charSequence.toString().length() == 50) {
                    binding.nameEdt.setError(AppConstant.LIMIT_ERROR);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                getTopicName = topicViewModel.getTopicName().getValue().trim();
                inputData.setName(getTopicName);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConstant.REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(requireActivity(), "Please enable file permissions to use this feature!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, AppConstant.REQUEST_CODE_SELECT_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstant.REQUEST_CODE_SELECT_IMAGE && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                Glide.with(requireActivity())
                        .load(selectedImageUri)
                        .centerCrop()
                        .placeholder(R.drawable.ic_image)
                        .error(R.drawable.ic_broken_image)
                        .into(binding.imagePreview);
            }
        }
    }

    private void initStatusDropdown() {
        binding.selectStatusLabel.setVisibility(View.VISIBLE);
        binding.statusSpinner.setVisibility(View.VISIBLE);
        ArrayAdapter<DropdownItem> adapter = new ArrayAdapter<>(requireActivity(),
                android.R.layout.simple_spinner_item, DropdownListHelper.statusDropdown());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.statusSpinner.setAdapter(adapter);
        binding.statusSpinner.setSelection(alreadyAvailableTopic.getStatus());
        binding.statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void initCategoriesDropdown() {
        CategoryViewModel categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        categoryViewModel.getActiveCategories().observe(requireActivity(), categories -> {
            if (categories != null) {
                List<DropdownItem> activeCategories = new ArrayList<>();
                for (Category category : categories) {
                    activeCategories.add(new DropdownItem(category.getId(), category.getName()));
                }
                ArrayAdapter<DropdownItem> activeCategoriesAdapter = new ArrayAdapter<>(requireActivity(),
                        android.R.layout.simple_spinner_item, activeCategories);
                activeCategoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.categoriesSpinner.setAdapter(activeCategoriesAdapter);

                if (alreadyAvailableTopic != null) {
                    DropdownItem selectedItem = null;
                    for (DropdownItem item : activeCategories) {
                        if (item.getHiddenValue() == (alreadyAvailableTopic.getCategoryId())) {
                            selectedItem = item;
                            break;
                        }
                    }
                    if (selectedItem != null) {
                        int position = activeCategories.indexOf(selectedItem);
                        binding.categoriesSpinner.setSelection(position);
                    }
                }
            } else {
                Toast.makeText(requireActivity(), "Category not found. Please add category first!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                DropdownItem item = (DropdownItem) adapterView.getSelectedItem();
                inputData.setCategoryId(item.getHiddenValue());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void onSaveBtnClick() {
        if (getTopicName.isEmpty()) {
            errorMess(true);
        } else {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            checkToken();
        }
    }

    private void errorMess(boolean isEmptyError) {
        if (isEmptyError) {
            binding.nameEdt.setError(AppConstant.MUST_NOT_BE_EMPTY);
        } else {
            binding.nameEdt.setError(AppConstant.IS_EXISTS_MESSAGE);
        }
        binding.nameEdt.requestFocus();
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
                    storeTopic();
                } else {
                    storeTopic();
                }
            } else {
                Toast.makeText(requireActivity(), AppConstant.SIGN_IN_AGAIN, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeTopic() {
        if (alreadyAvailableTopic != null) { // update when topic name changed
            if (!alreadyAvailableTopic.getName().equals(getTopicName)) {
                checkValidate(true);
            } else { // update when topic name not change
                checkSelectedImageUri(true);
            }
        } else { // create
            checkValidate(false);
        }
    }

    private void checkValidate(Boolean isUpdate) {
        TopicRepository topicRepo = new TopicRepository(requireActivity().getApplication());
        topicRepo.getTopicByName(getTopicName, resultList -> {
            if (resultList.size() > 0) {
                errorMess(false);
            } else {
                checkSelectedImageUri(isUpdate);
            }
        });
    }

    private void checkSelectedImageUri(Boolean isUpdate) {
        if (selectedImageUri != null) {
            if (isUpdate) {
                uploadToFirebase(selectedImageUri, true);
            } else {
                uploadToFirebase(selectedImageUri, false);
            }
        } else {
            if (isUpdate) {
                inputData.setImage(alreadyAvailableTopic.getImage());
                updateTopic();
            } else {
                inputData.setImage(AppConstant.IMAGE_DEFAULT);
                createTopic();
            }
        }
    }

    private void uploadToFirebase(Uri uri, Boolean isUpdate) {
        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(taskSnapshot ->
                fileRef.getDownloadUrl().addOnSuccessListener(result -> {
                    inputData.setImage(result.toString());
                    if (isUpdate) {
                        updateTopic();
                    } else {
                        createTopic();
                    }
                }).addOnFailureListener(e ->
                        Toast.makeText(requireActivity(), AppConstant.UPLOAD_FILE_FAILED, Toast.LENGTH_SHORT).show()
                ));
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = requireActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void createTopic() {
        inputData.setStatus(1);
        topicViewModel.createTopic(inputData).observe(requireActivity(), topic -> {
            if (topic != null) {
                binding.nameEdt.setText("");
                binding.imagePreview.setImageResource(R.drawable.ic_image);
                Toast.makeText(requireActivity(), AppConstant.CREATE_SUCCESSFULLY, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(requireActivity(), AppConstant.CREATE_FAILED, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateTopic() {
        inputData.setId(alreadyAvailableTopic.getId());
        topicViewModel.updateTopic(inputData).observe(requireActivity(), topic -> {
            if (topic != null) {
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