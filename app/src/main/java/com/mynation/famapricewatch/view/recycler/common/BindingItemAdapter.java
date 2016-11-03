package com.mynation.famapricewatch.view.recycler.common;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 *
 */

public abstract class BindingItemAdapter<T> extends RecyclerView.Adapter<BindingHolder<? extends ViewDataBinding>> {

    private final List<T> collection;

    public BindingItemAdapter(final List<T> collection) {
        this.collection = collection;
    }

    public List<T> getCollection() {
        return collection;
    }

    @Override
    public BindingHolder<? extends ViewDataBinding> onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return onCreateBindingHolder(LayoutInflater.from(parent.getContext()), parent, viewType);
    }

    public abstract BindingHolder<? extends ViewDataBinding> onCreateBindingHolder(LayoutInflater inflater, ViewGroup parent, final int viewType);

    @Override
    public void onBindViewHolder(final BindingHolder<? extends ViewDataBinding> holder, final int position) {
        onBind(holder, collection.get(position), position);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return collection.size();
    }

    public abstract void onBind(final BindingHolder<? extends ViewDataBinding> holder, final T item, final int position);

    public T getDataAt(final BindingHolder<? extends ViewDataBinding> holder) {
        return collection.get(holder.getAdapterPosition());
    }
}
