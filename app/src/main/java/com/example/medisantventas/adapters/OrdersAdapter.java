package com.example.medisantventas.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medisantventas.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.NumberFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private JSONArray orders;
    private Context mContext;

    public OrdersAdapter(JSONArray orders, Context context) {
        this.orders = orders;
        this.mContext = context;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderDate, orderTotal, orderStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderDate = itemView.findViewById(R.id.tv_order_date);
            orderTotal = itemView.findViewById(R.id.tv_order_total);
            orderStatus = itemView.findViewById(R.id.tv_order_status);
        }
    }

    @NonNull
    @Override
    public OrdersAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.OrderViewHolder holder, int position) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

                holder.orderTotal.setText(NumberFormat.getCurrencyInstance(Locale.US).format(this.orders.getJSONObject(position).getDouble("total")));
                switch (this.orders.getJSONObject(position).getInt("status")) {
                    case 1:
                        OffsetDateTime created = OffsetDateTime.ofInstant(Instant.parse(this.orders.getJSONObject(position).getString("created_at")), ZoneId.systemDefault());
                        holder.orderDate.setText(DateTimeFormatter.ofPattern("dd MMMM yyyy").format(created));
                        holder.orderStatus.setTextColor(Color.parseColor("#0398fc"));
                        holder.orderStatus.setText("Recibido");
                        break;
                    case 2:
                        OffsetDateTime updated = OffsetDateTime.ofInstant(Instant.parse(this.orders.getJSONObject(position).getString("updated_at")), ZoneId.systemDefault());
                        holder.orderDate.setText("Salida: "+DateTimeFormatter.ofPattern("hh:mm:ssa").format(updated));
                        holder.orderStatus.setTextColor(Color.parseColor("#fcd703"));
                        holder.orderStatus.setText("En ruta");
                        break;
                    case 3:
                        OffsetDateTime delivered = OffsetDateTime.ofInstant(Instant.parse(this.orders.getJSONObject(position).getString("delivered")), ZoneId.systemDefault());
                        OffsetDateTime createdDate = OffsetDateTime.ofInstant(Instant.parse(this.orders.getJSONObject(position).getString("updated_at")), ZoneId.systemDefault());
                        holder.orderDate.setText(DateTimeFormatter.ofPattern("dd MMMM yyyy").format(createdDate));
                        holder.orderStatus.setTextColor(Color.parseColor("#FF018786"));
                        holder.orderStatus.setText(DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm:ssa").format(delivered));
                        break;
                    default:
                        holder.orderStatus.setTextColor(Color.parseColor("#d40222"));
                        holder.orderStatus.setText("Cancelado");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return orders.length();
    }


}
