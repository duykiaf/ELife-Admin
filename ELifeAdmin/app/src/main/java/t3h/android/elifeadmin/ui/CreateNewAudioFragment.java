package t3h.android.elifeadmin.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import t3h.android.elifeadmin.R;
import t3h.android.elifeadmin.constant.AppConstant;
import t3h.android.elifeadmin.databinding.FragmentCreateNewAudioBinding;
import t3h.android.elifeadmin.helper.DropdownListHelper;
import t3h.android.elifeadmin.helper.FirebaseAuthHelper;
import t3h.android.elifeadmin.helper.SharedPreferencesHelper;
import t3h.android.elifeadmin.models.Audio;
import t3h.android.elifeadmin.models.DropdownItem;
import t3h.android.elifeadmin.models.Token;
import t3h.android.elifeadmin.models.Topic;
import t3h.android.elifeadmin.viewmodels.AudioViewModel;
import t3h.android.elifeadmin.viewmodels.TokenViewModel;
import t3h.android.elifeadmin.viewmodels.TopicViewModel;

public class CreateNewAudioFragment extends Fragment {
    private FragmentCreateNewAudioBinding binding;
    private AudioViewModel audioViewModel;
    private Audio inputData, alreadyAvailableAudio;
    private String getAudioTitle = "";
    private String getAudioLyrics = "";
    private Spannable spannableStr;
    private int getSelectionStart, getSelectionEnd;
    private Uri selectedUri;
    private final StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_create_new_audio, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        inputData = new Audio();
        initAudioViewModel();
        binding.underlineBtn.setPaintFlags(binding.underlineBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if (requireArguments().getBoolean("isUpdate")) {
            alreadyAvailableAudio = (Audio) requireArguments().get("audioInfo");
            initUpdateUI();
        } else {
            initTopAppBarUI(false);
            audioViewModel.setAudioTitle("");
            audioViewModel.setAudioLyrics("");
        }
    }

    private void initUpdateUI() {
        initTopAppBarUI(true);
        audioViewModel.setAudioTitle(alreadyAvailableAudio.getTitle());
        binding.fileNamePreview.setText(alreadyAvailableAudio.getFileName());
        audioViewModel.setAudioLyrics(alreadyAvailableAudio.getLyrics());
        initStatusDropdownUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        onBackPressed();
        onMenuItemClick();
        onTitleEdtTextChangedListener();
        binding.uploadAudioBtn.setOnClickListener(v -> onUploadAudioClickListener());
        lyricsEdtTextChangedListener();
        lyricsEdtOnTouchListener();
        binding.boldBtn.setOnClickListener(v -> boldBtn());
        binding.italicBtn.setOnClickListener(v -> italicBtn());
        binding.underlineBtn.setOnClickListener(v -> underlineBtn());
        binding.noFormatBtn.setOnClickListener(v -> noFormatBtn());
        initTopicsDropdownUI();
        binding.saveBtn.setOnClickListener(v -> onSaveBtnClick());
    }

    private void initTopAppBarUI(Boolean isUpdate) {
        if (isUpdate) {
            binding.appBarFragment.topAppBar.setTitle(AppConstant.UPDATE_AUDIO);
        } else {
            binding.appBarFragment.topAppBar.setTitle(AppConstant.CREATE_NEW_AUDIO);
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
                    navController.navigate(R.id.action_createNewAudioFragment_to_changePasswordFragment);
                    return true;
                case R.id.logoutItem:
                    FirebaseAuthHelper.signOut();
                    navController.navigate(R.id.signInFragment);
                    return true;
            }
            return false;
        });
    }

    private void initAudioViewModel() {
        audioViewModel = new ViewModelProvider(requireActivity()).get(AudioViewModel.class);
        binding.setAudioViewModel(audioViewModel);
    }

    private void onTitleEdtTextChangedListener() {
        binding.titleEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                InputFilter[] filters = new InputFilter[1];
                filters[0] = new InputFilter.LengthFilter(AppConstant.INPUT_MAX_LENGTH);
                binding.titleEdt.setFilters(filters);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                audioViewModel.setAudioTitle(charSequence.toString());
                if (charSequence.toString().length() == 50) {
                    binding.titleEdt.setError(AppConstant.LIMIT_ERROR);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                getAudioTitle = audioViewModel.getAudioTitle().getValue().trim();
                inputData.setTitle(getAudioTitle);
            }
        });
    }

    private void onUploadAudioClickListener() {
        if (ContextCompat.checkSelfPermission(requireActivity().getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9999);
        } else {
            selectAudioFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppConstant.REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectAudioFile();
            } else {
                Toast.makeText(requireActivity(), "Please enable file permissions to upload audio!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void selectAudioFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, AppConstant.REQUEST_CODE_SELECT_AUDIO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstant.REQUEST_CODE_SELECT_AUDIO && data != null) {
            selectedUri = data.getData();
            // get audio name
            Cursor returnCursor = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                returnCursor = requireActivity().getContentResolver().query(selectedUri, null, null, null);
            }
            if (returnCursor != null) {
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                String fileName = returnCursor.getString(nameIndex);
                binding.fileNamePreview.setText(fileName);
                inputData.setFileName(fileName);
            } else {
                Toast.makeText(requireActivity(), "Select audio file successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void lyricsEdtOnTouchListener() {
        binding.lyricsEdt.setOnTouchListener((view, motionEvent) -> {
            if (view.getId() == R.id.lyricsEdt) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                if ((motionEvent.getAction() & MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                    return true;
                }
            }
            return false;
        });
    }

    private void boldBtn() {
        spannableStr = new SpannableStringBuilder(binding.lyricsEdt.getText());
        getSelectionStart = binding.lyricsEdt.getSelectionStart();
        getSelectionEnd = binding.lyricsEdt.getSelectionEnd();
        if (getSelectionStart < getSelectionEnd) {
            spannableStr.setSpan(new StyleSpan(Typeface.BOLD), getSelectionStart, getSelectionEnd, 0);
        } else {
            spannableStr.setSpan(new StyleSpan(Typeface.BOLD), getSelectionEnd, getSelectionStart, 0);
        }
        binding.lyricsEdt.setText(spannableStr);
    }

    private void italicBtn() {
        spannableStr = new SpannableStringBuilder(binding.lyricsEdt.getText());
        getSelectionStart = binding.lyricsEdt.getSelectionStart();
        getSelectionEnd = binding.lyricsEdt.getSelectionEnd();
        if (getSelectionStart < getSelectionEnd) {
            spannableStr.setSpan(new StyleSpan(Typeface.ITALIC), getSelectionStart, getSelectionEnd, 0);
        } else {
            spannableStr.setSpan(new StyleSpan(Typeface.ITALIC), getSelectionEnd, getSelectionStart, 0);
        }
        binding.lyricsEdt.setText(spannableStr);
    }

    private void underlineBtn() {
        spannableStr = new SpannableStringBuilder(binding.lyricsEdt.getText());
        getSelectionStart = binding.lyricsEdt.getSelectionStart();
        getSelectionEnd = binding.lyricsEdt.getSelectionEnd();
        if (getSelectionStart < getSelectionEnd) {
            spannableStr.setSpan(new UnderlineSpan(), getSelectionStart, getSelectionEnd, 0);
        } else {
            spannableStr.setSpan(new UnderlineSpan(), getSelectionEnd, getSelectionStart, 0);
        }
        binding.lyricsEdt.setText(spannableStr);
    }

    private void noFormatBtn() {
        binding.lyricsEdt.setText(getAudioLyrics);
    }

    private void lyricsEdtTextChangedListener() {
        binding.lyricsEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                audioViewModel.setAudioLyrics(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                getAudioLyrics = audioViewModel.getAudioLyrics().getValue().trim();
                inputData.setLyrics(getAudioLyrics);
            }
        });
    }

    private void initTopicsDropdownUI() {
        TopicViewModel topicViewModel = new ViewModelProvider(requireActivity()).get(TopicViewModel.class);
        topicViewModel.getActiveTopicsList().observe(requireActivity(), topics -> {
            if (topics != null) {
                List<DropdownItem> activeTopicsList = new ArrayList<>();
                for (Topic topic : topics) {
                    activeTopicsList.add(new DropdownItem(topic.getId(), topic.getName()));
                }
                ArrayAdapter<DropdownItem> adapter = new ArrayAdapter<>(requireActivity(),
                        android.R.layout.simple_spinner_item, activeTopicsList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.topicsSpinner.setAdapter(adapter);

                if (alreadyAvailableAudio != null) {
                    DropdownItem selectedItem = null;
                    for (DropdownItem item : activeTopicsList) {
                        if (item.getHiddenValue() == alreadyAvailableAudio.getTopicId()) {
                            selectedItem = item;
                            break;
                        }
                    }
                    if (selectedItem != null) {
                        binding.topicsSpinner.setSelection(activeTopicsList.indexOf(selectedItem));
                    }
                }
            } else {
                Toast.makeText(requireActivity(), "Topic not found. Please add topic first!", Toast.LENGTH_LONG).show();
            }
        });

        binding.topicsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inputData.setTopicId(((DropdownItem) adapterView.getSelectedItem()).getHiddenValue());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void initStatusDropdownUI() {
        binding.selectStatusLabel.setVisibility(View.VISIBLE);
        binding.statusSpinner.setVisibility(View.VISIBLE);
        ArrayAdapter<DropdownItem> adapter = new ArrayAdapter<>(requireActivity(),
                android.R.layout.simple_spinner_item, DropdownListHelper.statusDropdown());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.statusSpinner.setAdapter(adapter);
        binding.statusSpinner.setSelection(alreadyAvailableAudio.getStatus());
        binding.statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                inputData.setStatus(((DropdownItem) adapterView.getSelectedItem()).getHiddenValue());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void onSaveBtnClick() {
        if (getAudioTitle.isEmpty()) {
            binding.titleEdt.setError(AppConstant.MUST_NOT_BE_EMPTY);
            binding.titleEdt.requestFocus();
        } else if (binding.fileNamePreview.getText().toString().isEmpty()) {
            binding.fileNamePreview.setError(AppConstant.NO_FILE);
        } else if (getAudioLyrics.isEmpty()) {
            binding.lyricsEdt.setError(AppConstant.MUST_NOT_BE_EMPTY);
            binding.lyricsEdt.requestFocus();
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
                    storeAudio();
                } else {
                    storeAudio();
                }
            } else {
                Toast.makeText(requireActivity(), AppConstant.SIGN_IN_AGAIN, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeAudio() {
        if (alreadyAvailableAudio != null) {
            if (!alreadyAvailableAudio.getTitle().equals(getAudioTitle)) {
                checkAudioTitle(true);
            } else {
                checkSelectedUri();
            }
        } else {
            checkAudioTitle(false);
        }
    }

    private void checkAudioTitle(Boolean isUpdate) {
        audioViewModel.getAudioByTitle(getAudioTitle, resultList -> {
            if (resultList != null && resultList.size() > 0) {
                binding.titleEdt.setError("This title already exists");
                binding.titleEdt.requestFocus();
            } else {
                if (isUpdate) {
                    checkSelectedUri();
                } else {
                    uploadToFirebase(selectedUri, false);
                }
            }
        });
    }

    // use when update
    private void checkSelectedUri() {
        if (selectedUri == null) { // do not change audio file
            inputData.setAudioFile(alreadyAvailableAudio.getAudioFile());
            inputData.setFileName(alreadyAvailableAudio.getFileName());
            updateAudio();
        } else { // select another audio file
            uploadToFirebase(selectedUri, true);
        }
    }

    private void uploadToFirebase(Uri uri, Boolean isUpdate) {
        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(taskSnapshot ->
                fileRef.getDownloadUrl().addOnSuccessListener(result -> {
                    inputData.setAudioFile(result.toString());
                    if (isUpdate) {
                        updateAudio();
                    } else {
                        createAudio();
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

    private void createAudio() {
        inputData.setStatus(1);
        audioViewModel.createAudio(inputData).observe(requireActivity(), audio -> {
            if (audio != null) {
                binding.titleEdt.setText("");
                binding.lyricsEdt.setText("");
                binding.fileNamePreview.setText("");
                Toast.makeText(requireActivity(), AppConstant.CREATE_SUCCESSFULLY, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(requireActivity(), AppConstant.CREATE_FAILED, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateAudio() {
        inputData.setId(alreadyAvailableAudio.getId());
        audioViewModel.updateAudio(inputData).observe(requireActivity(), audio -> {
            if (audio != null) {
                Toast.makeText(requireActivity(), AppConstant.UPDATE_SUCCESSFULLY, Toast.LENGTH_LONG).show();
                requireActivity().onBackPressed();
            } else {
                Toast.makeText(requireActivity(), AppConstant.UPDATE_FAILED, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}