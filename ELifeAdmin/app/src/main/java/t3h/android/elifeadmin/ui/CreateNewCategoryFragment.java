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
import android.widget.Toast;

import t3h.android.elifeadmin.R;
import t3h.android.elifeadmin.constant.AppConstant;
import t3h.android.elifeadmin.databinding.FragmentCreateNewCategoryBinding;

public class CreateNewCategoryFragment extends Fragment {
    private FragmentCreateNewCategoryBinding binding;
    private String[] items;
    private ArrayAdapter<String> adapterItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_create_new_category, container, false);

        View view = binding.getRoot();

        binding.appBarFragment.topAppBar.setTitle(AppConstant.CREATE_NEW_CATEGORY);
        binding.appBarFragment.topAppBar.setNavigationIcon(R.drawable.ic_back);

        items = getResources().getStringArray(R.array.status);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapterItems = new ArrayAdapter<>(requireActivity(), R.layout.item_dropdown_list, items);
        binding.autoCompleteTxt.setAdapter(adapterItems);
        binding.autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(requireActivity(), item, Toast.LENGTH_SHORT).show();
            }
        });
    }
}