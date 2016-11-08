package com.mynation.famapricewatch.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mynation.famapricewatch.R;
import com.mynation.famapricewatch.data.Commodity;
import com.mynation.famapricewatch.data.parser.FAMAEndpoint;
import com.mynation.famapricewatch.databinding.ActivityHomeBinding;
import com.mynation.famapricewatch.presenter.CommodityDataPresenter;
import com.mynation.famapricewatch.presenter.CommodityUIPresenter;
import com.mynation.famapricewatch.view.recycler.StateDataAdapter;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements CommodityDataPresenter.Listener, SwipeRefreshLayout.OnRefreshListener {

    private CommodityUIPresenter uiPresenter;
    private CommodityDataPresenter dataPresenter;

    private ActivityHomeBinding homeBinding;
    private StateDataAdapter recyclerAdapter;
    private ArrayAdapter<String> spinnerAdapter;
    private RefreshPresenter refreshPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        dataPresenter = new CommodityDataPresenter(getApplicationContext());
        uiPresenter = new CommodityUIPresenter();

        //SwipeRefresh
        SwipeRefreshLayout swipeRefreshLayout = homeBinding.swipeRefreshContainer;
        swipeRefreshLayout.setOnRefreshListener(this);
        refreshPresenter = new RefreshPresenter(homeBinding.swipeRefreshContainer);
        dataPresenter.addListener(refreshPresenter);

        //Recycler
        RecyclerView recyclerView = homeBinding.mainItemsRV;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerAdapter = new StateDataAdapter(new ArrayList<Commodity>(), uiPresenter, dataPresenter);
        recyclerView.setAdapter(recyclerAdapter);
        dataPresenter.addListener(recyclerAdapter);

        //defaults
        dataPresenter.addListener(this);
        requestData(dataPresenter);

        spinnerAdapter = new ArrayAdapter<>(HomeActivity.this, R.layout.spinner_header, android.R.id.text1, dataPresenter.getStateIds());

        Spinner spinner = homeBinding.topSpinner;
        spinner.setAdapter(spinnerAdapter);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (recyclerAdapter != null) {
                    recyclerAdapter.setState(dataPresenter.getStateIds().get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void requestData(CommodityDataPresenter dataPresenter) {
        dataPresenter.requestData(FAMAEndpoint.PriceKey.RETAIL);
    }

    @Override
    public void onDataPreload() {

    }

    @Override
    public void onDataUpdated() {
        spinnerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDataError() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        dataPresenter.addListener(this); //valid as listeners backed by HashSet
    }

    @Override
    protected void onPause() {
        dataPresenter.removeListener(this);
        super.onPause();
    }

    @Override
    public void onRefresh() {
        requestData(dataPresenter);
    }

    private static final class RefreshPresenter implements CommodityDataPresenter.Listener {
        private final SwipeRefreshLayout swipeRefreshLayout;

        public <V extends SwipeRefreshLayout> RefreshPresenter(V swipeRefreshLayout) {
            this.swipeRefreshLayout = swipeRefreshLayout;
        }

        @Override
        public void onDataPreload() {
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        public void onDataUpdated() {
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onDataError() {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

}
