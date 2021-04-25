package com.praditya.antreanonline.view.ui.merchant.manage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.Queue;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.view.adapter.ManageQueueAdapter;
import com.praditya.antreanonline.viewmodel.QueueViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ManageWaitingQueueFragment extends Fragment implements ManageQueueAdapter.OnClickCallback {

    private final String TAG = getClass().getSimpleName();
    private QueueViewModel queueViewModel;
    private ManageQueueAdapter adapter;
    @BindView(R.id.rv_waiting_queue)
    RecyclerView recyclerView;
    @BindView(R.id.cv_empty_message)
    MaterialCardView cvEmptyMessage;
    @BindView(R.id.loading)
    ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manage_waiting_queue, container, false);
        ButterKnife.bind(this, view);
        showRecyclerView(false);
        showEmptyMessage(false);
        showLoading(true);
        initRecyclerView();
        initViewModel();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showRecyclerView(false);
        showEmptyMessage(false);
        showLoading(true);
        initRecyclerView();
        initViewModel();
    }

    private void initRecyclerView() {
        adapter = new ManageQueueAdapter(this.getContext());
        adapter.setOnClickCallback(this::showQueue);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void initViewModel() {
        queueViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(QueueViewModel.class);
        queueViewModel.findWaitingByMerchant().observe(getViewLifecycleOwner(), new Observer<Resource<ArrayList<Queue>>>() {
            @Override
            public void onChanged(Resource<ArrayList<Queue>> arrayListResource) {
                switch (arrayListResource.getStatus()) {
                    case SUCCESS:
                        adapter.setQueues(arrayListResource.getData());
                        showEmptyMessage(false);
                        showRecyclerView(true);
                        break;
                    case EMPTY:
                        showRecyclerView(false);
                        showEmptyMessage(true);
                        break;
                    case ERROR:
                        showRecyclerView(false);
                        showEmptyMessage(false);
                        showMessage(arrayListResource.getMessage());
                        break;
                }
                showLoading(false);
            }
        });
    }

    private void showRecyclerView(boolean state) {
        if (state)
            recyclerView.setVisibility(View.VISIBLE);
        else
            recyclerView.setVisibility(View.GONE);
    }

    private void showEmptyMessage(boolean state) {
        if (state)
            cvEmptyMessage.setVisibility(View.VISIBLE);
        else
            cvEmptyMessage.setVisibility(View.GONE);
    }

    private void showLoading(boolean state) {
        if (state)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }

    private void showMessage(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showQueue(Queue queue) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("queue", queue);
        Intent intent = new Intent(getActivity(), ShowQueueActivity.class);
        intent.putExtra("queue", queue);
        startActivity(intent);
    }
}