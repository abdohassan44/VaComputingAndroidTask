package com.example.vacomputingtask;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

public class EquationAdapter extends ListAdapter<Equation, EquationViewHolder> {

    public EquationAdapter(@NonNull DiffUtil.ItemCallback<Equation> diffCallback) {
        super(diffCallback);

    }

    @Override
    public EquationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return EquationViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(EquationViewHolder holder, int position) {
        Equation current = getItem(position);
        holder.bind(current);
    }

    static class WordDiff extends DiffUtil.ItemCallback<Equation> {

        @Override
        public boolean areItemsTheSame(@NonNull Equation oldItem, @NonNull Equation newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Equation oldItem, @NonNull Equation newItem) {
            return oldItem.getId() == newItem.getId();
        }
    }
}

