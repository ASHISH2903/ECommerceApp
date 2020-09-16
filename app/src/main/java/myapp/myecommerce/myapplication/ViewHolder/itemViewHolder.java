package myapp.myecommerce.myapplication.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import myapp.myecommerce.myapplication.Interface.ItemClickListner;
import myapp.myecommerce.myapplication.R;


public class itemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtproductName, txtproductDescription, txtproductPrice, txtProductStatus;
    public ImageView imageView;
    public ItemClickListner listner;

    public itemViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.product_seller_image);
        txtproductName = (TextView) itemView.findViewById(R.id.product_seller_name);
        txtproductDescription = (TextView) itemView.findViewById(R.id.product_seller_description);
        txtproductPrice = (TextView) itemView.findViewById(R.id.product_seller_price);
        txtProductStatus = (TextView) itemView.findViewById(R.id.product_seller_status);
    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }
    @Override
    public void onClick(View view) {
        listner.onClick(view, getAdapterPosition(), false);
    }
}

