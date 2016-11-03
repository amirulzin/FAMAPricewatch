package com.mynation.famapricewatch.view.recycler.common;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 *
 */

public class BindingHolder<VB extends ViewDataBinding> extends RecyclerView.ViewHolder {

    private final VB binding;

    public BindingHolder(VB viewDataBinding) {
        super(viewDataBinding.getRoot());
        this.binding = viewDataBinding;
    }

    public VB getBinding() {
        return binding;
    }
}
