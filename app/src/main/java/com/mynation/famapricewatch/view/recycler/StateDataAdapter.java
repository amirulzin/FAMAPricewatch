package com.mynation.famapricewatch.view.recycler;

import android.databinding.ViewDataBinding;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.mynation.famapricewatch.data.Commodity;
import com.mynation.famapricewatch.databinding.ItemCommodityBinding;
import com.mynation.famapricewatch.presenter.CommodityDataPresenter;
import com.mynation.famapricewatch.presenter.CommodityUIPresenter;
import com.mynation.famapricewatch.view.recycler.common.BindingHolder;
import com.mynation.famapricewatch.view.recycler.common.BindingItemAdapter;

import java.util.List;

/**
 *
 */
public final class StateDataAdapter extends BindingItemAdapter<Commodity> implements CommodityDataPresenter.Listener {
    private final CommodityUIPresenter uiPresenter;
    private final CommodityDataPresenter dataPresenter;
    private boolean init = true;

    public StateDataAdapter(final List<Commodity> collection, final CommodityUIPresenter uiPresenter, CommodityDataPresenter dataPresenter) {
        super(collection);
        this.uiPresenter = uiPresenter;
        this.dataPresenter = dataPresenter;
    }

    @Override
    public BindingHolder<? extends ViewDataBinding> onCreateBindingHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new BindingHolder<>(ItemCommodityBinding.inflate(inflater, parent, false));
    }

    @Override
    public void onBind(BindingHolder<? extends ViewDataBinding> holder, Commodity item, int position) {
        ViewDataBinding binding = holder.getBinding();
        if (binding instanceof ItemCommodityBinding) {
            final ItemCommodityBinding b = (ItemCommodityBinding) binding;
            uiPresenter.bind(item, b.commodityName, b.commodityUnit, b.commodityGrade, b.commodityLow, b.commodityAverage, b.commodityHigh);
        }
    }

    @Override
    public void onDataUpdated() {
        if (init) {
            init = false;
            String stateId = uiPresenter.getInitialStateId(dataPresenter.getStateIds());
            setState(stateId);
        } else {
            notifyDataSetChanged();
        }
    }

    @Override
    public void onDataError() {

    }

    public void setState(String stateId) {
        getCollection().clear();
        getCollection().addAll(dataPresenter.getStateData(stateId).getCommodities());
        notifyDataSetChanged();
    }
}
