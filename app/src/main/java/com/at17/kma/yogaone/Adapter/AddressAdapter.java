package com.at17.kma.yogaone.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.at17.kma.yogaone.MapActivity;
import com.at17.kma.yogaone.R;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    private List<String> addresses;
    private OnItemClickListener onItemClickListener;

    public AddressAdapter(List<String> addresses) {
        this.addresses = addresses;
    }

    public interface OnItemClickListener {
        void onItemClick(String address);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_address, parent, false);
        return new AddressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        final String address = addresses.get(position);
        holder.addressTextView.setText(address);
        Log.d("AddressAdapter", "Address: " + address);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("AddressAdapter", "Item Clicked: " + address);
                Toast.makeText(v.getContext(), "FFF", Toast.LENGTH_SHORT).show();
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(address);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public static class AddressViewHolder extends RecyclerView.ViewHolder {
        public TextView addressTextView;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            addressTextView = itemView.findViewById(R.id.addressTextView);
        }
    }
}
