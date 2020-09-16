package myapp.myecommerce.myapplication.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import myapp.myecommerce.myapplication.R;
import myapp.myecommerce.myapplication.Sellers.SellerProductCategoryActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class AdminMaintainProductsActivity extends AppCompatActivity {

    private ImageView maintainImageView;
    private Button applyChangesBtn, deleteProductBtn;
    private EditText maintainProductName, maintainProductPrice, maintainProductDescription;
    private String productId = "";
    private DatabaseReference productsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maintain_products);

        productId = getIntent().getStringExtra("pid");
        productsRef = FirebaseDatabase.getInstance().getReference().child("Products").child(productId);

        maintainImageView = (ImageView) findViewById(R.id.product_image_maintain);
        applyChangesBtn = (Button) findViewById(R.id.apply_changes_btn);
        deleteProductBtn = (Button) findViewById(R.id.delete_product_btn);
        maintainProductName = (EditText) findViewById(R.id.product_name_maintain);
        maintainProductPrice = (EditText) findViewById(R.id.product_price_maintain);
        maintainProductDescription = (EditText) findViewById(R.id.product_description_maintain);

        displaySpecificProductInfo();

        applyChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyChanges();
            }
        });

        deleteProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteThisProduct();
            }
        });
    }

    private void deleteThisProduct() {
        productsRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(AdminMaintainProductsActivity.this, SellerProductCategoryActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(AdminMaintainProductsActivity.this, "The Product is Deleted Successfully...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void applyChanges() {
        String pNameMaintain = maintainProductName.getText().toString();
        String pPriceMaintain = maintainProductPrice.getText().toString();
        String pDescriptionMaintain = maintainProductDescription.getText().toString();

        if(pNameMaintain.equals(""))
        {
            Toast.makeText(this, "Please write product name.", Toast.LENGTH_SHORT).show();
        }
        else if(pPriceMaintain.equals(""))
        {
            Toast.makeText(this, "Please write product price.", Toast.LENGTH_SHORT).show();
        }
        else if(pDescriptionMaintain.equals(""))
        {
            Toast.makeText(this, "Please write product description.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            HashMap<String ,Object> productMap = new HashMap<>();
            productMap.put("pid", productId);
            productMap.put("description", pDescriptionMaintain);
            productMap.put("price", pPriceMaintain);
            productMap.put("pname", pNameMaintain);

            productsRef.updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(AdminMaintainProductsActivity.this, "Changes Applied Successfully..", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AdminMaintainProductsActivity.this, SellerProductCategoryActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }

    }

    private void displaySpecificProductInfo() {
        productsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String pName = dataSnapshot.child("pname").getValue().toString();
                    String pPrice = dataSnapshot.child("price").getValue().toString();
                    String pDescription = dataSnapshot.child("description").getValue().toString();
                    String pImage =  dataSnapshot.child("image").getValue().toString();

                    maintainProductName.setText(pName);
                    maintainProductPrice.setText(pPrice);
                    maintainProductDescription.setText(pDescription);

                    Picasso.get().load(pImage).into(maintainImageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
