package t3h.android.elifeadmin.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import t3h.android.elifeadmin.R;
import t3h.android.elifeadmin.constant.AppConstant;
import t3h.android.elifeadmin.databinding.FragmentCreateNewTopicBinding;

public class CreateNewTopicFragment extends Fragment {
    private FragmentCreateNewTopicBinding binding;
    private String[] status;
    private String[] categories = {"Category 1", "Category 2", "Category 3", "Category 4"};
    private ArrayAdapter<String> statusAdapterItems, categoryAdapterItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_create_new_topic, container, false);

        View view = binding.getRoot();

        binding.appBarFragment.topAppBar.setTitle(AppConstant.CREATE_NEW_TOPIC);
        binding.appBarFragment.topAppBar.setNavigationIcon(R.drawable.ic_back);

        status = getResources().getStringArray(R.array.status);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categoryAdapterItems = new ArrayAdapter<>(requireActivity(), R.layout.item_dropdown_list, categories);
        binding.categoryCompleteTxt.setAdapter(categoryAdapterItems);
        binding.categoryCompleteTxt.setOnItemClickListener((adapterView, view1, i, l) -> {
            String getCategory = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(requireActivity(), getCategory, Toast.LENGTH_SHORT).show();
        });

        statusAdapterItems = new ArrayAdapter<>(requireActivity(), R.layout.item_dropdown_list, status);
        binding.statusCompleteTxt.setAdapter(statusAdapterItems);
        binding.statusCompleteTxt.setOnItemClickListener((adapterView, view12, i, l) -> {
            String getStatus = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(requireActivity(), getStatus, Toast.LENGTH_SHORT).show();
        });
    }
}