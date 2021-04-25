package com.praditya.antreanonline.view.ui.merchant.manage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;
import com.praditya.antreanonline.R;
import com.praditya.antreanonline.model.Queue;
import com.praditya.antreanonline.model.Resource;
import com.praditya.antreanonline.viewmodel.QueueViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageQueueActivity extends AppCompatActivity {

    private QueueViewModel queueViewModel;
    @BindView(R.id.app_bar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @OnClick(R.id.fab_scan_qrcode)
    void openScanner() {
        startActivity(new Intent(ManageQueueActivity.this, QRCodeReaderActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_queue);
        ButterKnife.bind(this);
        ManageQueuePagerAdapter pagerAdapter = new ManageQueuePagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        queueViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(QueueViewModel.class);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setBadge();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBadge();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setBadge() {
        queueViewModel.countWaitingByMerchant().observe(ManageQueueActivity.this, new Observer<Resource<Integer>>() {
            @Override
            public void onChanged(Resource<Integer> integerResource) {
                switch (integerResource.getStatus()) {
                    case SUCCESS:
                        tabLayout.getTabAt(0).getOrCreateBadge().setNumber(integerResource.getData());
                        break;
                    case EMPTY:
                        tabLayout.getTabAt(0).removeBadge();
                        break;
                }
            }
        });
    }
}