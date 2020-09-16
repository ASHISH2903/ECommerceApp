package myapp.myecommerce.myapplication.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import myapp.myecommerce.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SellerRegistrationActivity extends AppCompatActivity {

    private Button sellerLoginBegin;
    private EditText sellerName, sellerPhone, sellerEmail, sellerPassword, sellerAddress;
    private Button sellerRegistrationBtn;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuthentication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_registration);

        loadingBar = new ProgressDialog(this);

        mAuthentication = FirebaseAuth.getInstance();

        sellerLoginBegin = (Button) findViewById(R.id.seller_already_have_account_btn);
        sellerName = (EditText) findViewById(R.id.seller_name);
        sellerPhone = (EditText) findViewById(R.id.seller_phone);
        sellerEmail = (EditText) findViewById(R.id.seller_email);
        sellerPassword = (EditText) findViewById(R.id.seller_password);
        sellerAddress = (EditText) findViewById(R.id.seller_address);
        sellerRegistrationBtn = (Button) findViewById(R.id.seller_register_btn);

        sellerLoginBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                startActivity(intent);
            }
        });

        sellerRegistrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterSeller();
            }
        });
    }

    private void RegisterSeller() {
        final String Name = sellerName.getText().toString();
        final String Phone = sellerPhone.getText().toString();
        final String Email = sellerEmail.getText().toString();
        String Password = sellerPassword.getText().toString();
        final String Address = sellerAddress.getText().toString();

        if(!Name.equals("") && !Phone.equals("") && !Email.equals("") && !Password.equals("") && !Address.equals(""))
        {
            loadingBar.setTitle("Creating Seller Account");
            loadingBar.setMessage("Please wait, while we checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            final DatabaseReference sellerRef;
            sellerRef = FirebaseDatabase.getInstance().getReference();
            mAuthentication.createUserWithEmailAndPassword(Email,Password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                String sid = mAuthentication.getCurrentUser().getUid();

                                HashMap<String,Object> SellerMap = new HashMap<>();
                                SellerMap.put("sid",sid);
                                SellerMap.put("name",Name);
                                SellerMap.put("phone",Phone);
                                SellerMap.put("email",Email);
                                SellerMap.put("address",Address);

                                sellerRef.child("Sellers").child(sid).updateChildren(SellerMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    loadingBar.dismiss();
                                                    Toast.makeText(SellerRegistrationActivity.this, "Registration done Successfully...", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(SellerRegistrationActivity.this, SellerLoginActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }

                                            }
                                        });
                            }
                            else
                            {
                                loadingBar.dismiss();
                                Toast.makeText(SellerRegistrationActivity.this, "This email id is already exist...", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "Please Complete the Registration Form.", Toast.LENGTH_SHORT).show();
        }
    }
}
