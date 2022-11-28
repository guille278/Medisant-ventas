package com.example.medisantventas.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medisantventas.R;
import com.example.medisantventas.config.Config;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.NumberFormat;
import java.util.Locale;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    private JSONArray products;
    private Context mContext;

    public OrderDetailAdapter(JSONArray products, Context mContext) {
        this.products = products;
        this.mContext = mContext;
    }

    public class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productShortDesc, productPrice, productQuantity;

        public OrderDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.iv_product_image);
            productName = itemView.findViewById(R.id.tv_product_name);
            productShortDesc = itemView.findViewById(R.id.tv_product_short_description);
            productPrice = itemView.findViewById(R.id.tv_product_price);
            productQuantity = itemView.findViewById(R.id.tv_product_quantity);
        }
    }

    @NonNull
    @Override
    public OrderDetailAdapter.OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_card, parent, false);
        return new OrderDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        try {
            Picasso.get().load(Config.URL + "storage/" + products.getJSONObject(position).getString("image")).into(holder.productImage);
            holder.productName.setText(products.getJSONObject(position).getString("name"));
            holder.productShortDesc.setText(products.getJSONObject(position).getString("short_description"));
            holder.productPrice.setText(NumberFormat.getCurrencyInstance(Locale.US).format(products.getJSONObject(position).getJSONObject("pivot").getDouble("subtotal")));
            holder.productQuantity.setText("X" + products.getJSONObject(position).getJSONObject("pivot").getString("quantity"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return products.length();
    }


}
