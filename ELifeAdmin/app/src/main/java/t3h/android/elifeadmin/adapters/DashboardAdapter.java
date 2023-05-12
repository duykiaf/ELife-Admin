package t3h.android.elifeadmin.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

import t3h.android.elifeadmin.ui.ListFragment;

public class DashboardAdapter extends FragmentStateAdapter {
    public DashboardAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Return a NEW fragment instance in createFragment(int)
        Fragment fragment = new ListFragment();

        List<String> btnTxtList = new ArrayList<>();
        btnTxtList.add("New category");
        btnTxtList.add("New topic");
        btnTxtList.add("New audio");

        Bundle args = new Bundle();
        args.putString(ListFragment.ARG_OBJECT, btnTxtList.get(position));

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
