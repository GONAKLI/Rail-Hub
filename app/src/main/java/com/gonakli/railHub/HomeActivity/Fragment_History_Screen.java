package com.gonakli.railHub.HomeActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.gonakli.railHub.ADAPTERS.User_History_ListView_Adapter;
import com.gonakli.railHub.DB_WORK.User_Routes_History_DB_Helper;
import com.gonakli.railHub.R;
import com.gonakli.railHub.Structure_Class.User_History_Structure;

import java.util.ArrayList;

public class Fragment_History_Screen extends Fragment {
    ListView historyListView;
    LinearLayout emptyStateContainer;
    View history;
    ArrayList<User_History_Structure> arrHistoryData;
    User_History_ListView_Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        history = inflater.inflate(R.layout.history_activity_fragment, container, false);
        requireActivity().setTitle("Search History");
        find_all_id();
        list_view_setUp();
        return history;
    }

    private void list_view_setUp() {
        new Thread(() -> {
            arrHistoryData = fetchHistory();
           if(isAdded() && getActivity() != null) {
               requireActivity().runOnUiThread(() -> {
                   if (arrHistoryData.isEmpty()) {
                       emptyStateContainer.setVisibility(View.VISIBLE);
                   } else {
                       emptyStateContainer.setVisibility(View.GONE);
                       adapter = new User_History_ListView_Adapter(requireContext(), arrHistoryData);

                       historyListView.setAdapter(adapter); // ✅ अब main thread पर

                   }
               });
           }

        }).start();

    }

    private void find_all_id() {
        historyListView = history.findViewById(R.id.historyListView);
        emptyStateContainer = history.findViewById(R.id.emptyStateContainer);
    }

    public void refreshData() {
        arrHistoryData.clear();
        adapter.notifyDataSetChanged();
        list_view_setUp();

    }

    private ArrayList<User_History_Structure> fetchHistory() {
        User_Routes_History_DB_Helper helper = new User_Routes_History_DB_Helper(requireContext());
        return helper.getHistory();
    }

}
