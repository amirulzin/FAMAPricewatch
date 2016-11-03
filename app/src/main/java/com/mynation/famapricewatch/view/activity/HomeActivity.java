package com.mynation.famapricewatch.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class HomeActivity extends AppCompatActivity implements CommodityDataPresenter.Listener {

    private CommodityUIPresenter uiPresenter;
    private CommodityDataPresenter dataPresenter;

    private ActivityHomeBinding homeBinding;
    private StateDataAdapter recyclerAdapter;
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        dataPresenter = new CommodityDataPresenter(getApplicationContext());
        uiPresenter = new CommodityUIPresenter();

        //Recycler
        RecyclerView recyclerView = homeBinding.mainItemsRV;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerAdapter = new StateDataAdapter(new ArrayList<Commodity>(), uiPresenter, dataPresenter);
        recyclerView.setAdapter(recyclerAdapter);

        //defaults
        dataPresenter.requestData(FAMAEndpoint.PriceKey.RETAIL);
        dataPresenter.addListener(this);

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

        //spinnerAdapter.notifyDataSetChanged();
        Log.e(getClass().getSimpleName(), "Spinner count " + spinnerAdapter.getCount());
    }

    @Override
    public void onDataUpdated() {
        Handler handler = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Spinner
//                spinnerAdapter = new ArrayAdapter<>(HomeActivity.this, R.layout.spinner_header, R.id.spinnerTextHeader, dataPresenter.getStateIds());
//                homeBinding.topSpinner.setAdapter(spinnerAdapter);
                spinnerAdapter.notifyDataSetChanged();
            }
        });
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

}
