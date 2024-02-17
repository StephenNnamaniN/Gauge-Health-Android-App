package uk.ac.tees.a0278818.gaugehealth.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import uk.ac.tees.a0278818.gaugehealth.R;
import uk.ac.tees.a0278818.gaugehealth.conditionsModels.Item;

public class ItemsRecyclerViewAdapter extends RecyclerView.Adapter<ItemsRecyclerViewAdapter.ViewHolder> {
    private final List<Item> itemList;
    private final OnItemClickListener itemClickListener;

    public ItemsRecyclerViewAdapter(List<Item> itemList, OnItemClickListener itemClickListener) {
        this.itemList = itemList;
        this.itemClickListener = itemClickListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.conditions_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.conditionsName.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView conditionsName;
        OnItemClickListener onItemClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            conditionsName = itemView.findViewById(R.id.conditions_header);
            this.onItemClickListener = itemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Item currItem = itemList.get(getAdapterPosition());
            onItemClickListener.onItemClicked(currItem);
        }
    }
}
