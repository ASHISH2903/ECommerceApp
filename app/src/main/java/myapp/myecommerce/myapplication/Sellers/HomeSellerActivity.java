package myapp.myecommerce.myapplication.Sellers;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import myapp.myecommerce.myapplication.Buyers.MainActivity;
import myapp.myecommerce.myapplication.Model.Products;
import myapp.myecommerce.myapplication.R;

import myapp.myecommerce.myapplication.ViewHolder.itemViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeSellerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private DatabaseReference unVerifiedProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_seller);
        BottomNavigationView navView = findViewById(R.id.nav_view_seller);
        unVerifiedProducts = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = findViewById(R.id.seller_home_recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.navigation_home:
                        /*Intent intent_home = new Intent(HomeSellerActivity.this, SellerProductCategoryActivity.class);
                        startActivity(intent_home);*/
                        finish();
                        return true;
                    case R.id.navigation_add:
                        Intent intent_add = new Intent(HomeSellerActivity.this, SellerProductCategoryActivity.class);
                        startActivity(intent_add);

                        return true;
                    case R.id.navigation_logout:
                        final FirebaseAuth mAuth;
                        mAuth = FirebaseAuth.getInstance();
                        mAuth.signOut();
                        Intent intent_logout = new Intent(HomeSellerActivity.this, MainActivity.class);
                        intent_logout.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent_logout);
                        finish();
                }
                return false;
            }
        });
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
       /* AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_add, R.id.navigation_logout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_seller);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);*/

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options = new  FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(unVerifiedProducts.orderByChild("sid").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()), Products.class)
                .build();

        FirebaseRecyclerAdapter<Products, itemViewHolder> adapter
                = new FirebaseRecyclerAdapter<Products, itemViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull itemViewHolder holder, int position, @NonNull final Products model) {
                holder.txtproductName.setText(model.getPname());
                holder.txtproductDescription.setText(model.getDescription());
                holder.txtProductStatus.setText("Status : " + model.getProductstate());
                holder.txtproductPrice.setText("Price = ₹" + model.getPrice());
                Picasso.get().load(model.getImage()).into(holder.imageView);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String productId = model.getPid();

                        CharSequence options[] = new CharSequence[]{
                                "Yes",
                                "No"
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeSellerActivity.this);
                        builder.setTitle("Do you want to Delete this Product? Are you sure?");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                if(pos == 0)
                                {
                                    deleteProduct(productId);
                                }
                                if(pos == 1)
                                {

                                }
                            }
                        });
                        builder.show();
                    }
                });
            }

            @NonNull
            @Override
            public itemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_item_view, parent, false);
                itemViewHolder holder = new itemViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void deleteProduct(String productId) {
        unVerifiedProducts.child(productId)
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(HomeSellerActivity.this, "Product Deleted Successfully...", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
