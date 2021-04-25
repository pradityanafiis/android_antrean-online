package com.praditya.antreanonline.view.ui.queue;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textview.MaterialTextView;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.Merchant;
import com.praditya.antreanonline.model.Queue;
import com.praditya.antreanonline.model.QueueStatus;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.view.adapter.MerchantAdapter;
import com.praditya.antreanonline.view.adapter.QueueAdapter;
import com.praditya.antreanonline.view.dialog.QueueBottomSheet;
import com.praditya.antreanonline.viewmodel.MerchantViewModel;
import com.praditya.antreanonline.viewmodel.QueueViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActiveQueueFragment extends Fragment implements QueueAdapter.OnClickCallback, QueueBottomSheet.OnClickCallback {

    private final String TAG = getTag();
    private QueueViewModel queueViewModel;
    private QueueAdapter queueAdapter;
    @BindView(R.id.rv_active_queue)
    RecyclerView recyclerView;
    @BindView(R.id.loading)
    ProgressBar loading;
    @BindView(R.id.cv_empty_message)
    MaterialCardView cvEmptyMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_active_queue, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getQueue();
    }

    @Override
    public void showQueue(Queue queue) {
        QueueBottomSheet queueBottomSheet = new QueueBottomSheet(queue, this.getContext());
        queueBottomSheet.show(getChildFragmentManager(), "Queue Bottom Sheet");
        queueBottomSheet.setOnClickCallback(this::cancelQueue);
        queueViewModel.setQueue(queue);
    }

    @Override
    public void cancelQueue(Queue queue) {
        queueViewModel.setQueueStatus(QueueStatus.CANCELED);
    }

    private void initView() {
        queueAdapter = new QueueAdapter(this.getContext());
        queueAdapter.setOnClickCallback(this::showQueue);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(queueAdapter);

        queueViewModel = new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication()).create(QueueViewModel.class);
        queueViewModel.updateStatus().observe(getViewLifecycleOwner(), new Observer<Resource<Queue>>() {
            @Override
            public void onChanged(Resource<Queue> queueResource) {
                switch (queueResource.getStatus()) {
                    case SUCCESS:
                        getQueue();
                        showMessage(queueResource.getMessage());
                        break;
                    case EMPTY:
                    case ERROR:
                        showMessage(queueResource.getMessage());
                        break;
                }
            }
        });
        getQueue();
    }

    private void getQueue() {
        showRecyclerView(false);
        showEmptyMessage(false);
        showLoading(true);
        queueViewModel.findActiveByUser().observe(getViewLifecycleOwner(), new Observer<Resource<ArrayList<Queue>>>() {
            @Override
            public void onChanged(Resource<ArrayList<Queue>> arrayListResource) {
                switch (arrayListResource.getStatus()) {
                    case SUCCESS:
                        queueAdapter.setQueues(arrayListResource.getData());
                        showRecyclerView(true);
                        showEmptyMessage(false);
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
            loading.setVisibility(View.VISIBLE);
        else
            loading.setVisibility(View.GONE);
    }

    private void showMessage(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}