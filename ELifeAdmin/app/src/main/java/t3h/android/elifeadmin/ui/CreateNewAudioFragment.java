package t3h.android.elifeadmin.ui;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import t3h.android.elifeadmin.R;
import t3h.android.elifeadmin.adapters.ColorsAdapter;
import t3h.android.elifeadmin.constant.AppConstant;
import t3h.android.elifeadmin.databinding.FragmentCreateNewAudioBinding;

public class CreateNewAudioFragment extends Fragment {
    private FragmentCreateNewAudioBinding binding;
    private String[] topics = {"Topic 1", "Topic 2", "Topic 3", "Topic 4"};
    private ArrayAdapter<String> statusAdapterItems, topicAdapterItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_create_new_audio, container, false);

        View view = binding.getRoot();

        binding.appBarFragment.topAppBar.setTitle(AppConstant.CREATE_NEW_AUDIO);
        binding.appBarFragment.topAppBar.setNavigationIcon(R.drawable.ic_back);

        int[] colors = getResources().getIntArray(R.array.colorsStyle);
        ColorsAdapter colorsAdapter = new ColorsAdapter(colors);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false);
        binding.colorsRcv.setLayoutManager(layoutManager);
        binding.colorsRcv.setAdapter(colorsAdapter);

        binding.underlineBtn.setPaintFlags(binding.underlineBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.lyricsEdt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (view.getId() == R.id.lyricsEdt) {
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        topicAdapterItems = new ArrayAdapter<>(requireActivity(), R.layout.item_dropdown_list, topics);
        binding.topicCompleteTxt.setAdapter(topicAdapterItems);
        binding.topicCompleteTxt.setOnItemClickListener((adapterView, view1, i, l) -> {
            String getTopic = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(requireActivity(), getTopic, Toast.LENGTH_SHORT).show();
        });

        String[] status = getResources().getStringArray(R.array.status);
        statusAdapterItems = new ArrayAdapter<>(requireActivity(), R.layout.item_dropdown_list, status);
        binding.statusCompleteTxt.setAdapter(statusAdapterItems);
        binding.statusCompleteTxt.setOnItemClickListener((adapterView, view12, i, l) -> {
            String getStatus = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(requireActivity(), getStatus, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}