package myapp.myecommerce.myapplication.Sellers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import myapp.myecommerce.myapplication.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SellerLoginActivity extends AppCompatActivity {

    private Button RegisterSellerBtn;
    private EditText sellerEmail, sellerPassword;
    private ProgressDialog loadingBar;
    private FirebaseAuth mAuthentication;
    private TextView forgotPwd;

    private DatabaseReference sellerRef;

    private String recoverPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_login);
        loadingBar = new ProgressDialog(this);
        mAuthentication = FirebaseAuth.getInstance();
        sellerRef = FirebaseDatabase.getInstance().getReference().child("Sellers");

        RegisterSellerBtn = (Button) findViewById(R.id.seller_login_btn);
        sellerEmail = (EditText) findViewById(R.id.seller_login_email);
        sellerPassword = (EditText) findViewById(R.id.seller_login_password);
        forgotPwd = (TextView) findViewById(R.id.seller_forgot_pwd);

        RegisterSellerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginSeller();
            }
        });

        forgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SellerLoginActivity.this);
                builder.setTitle("Enter Email\n");
                final EditText res_Email = new EditText(SellerLoginActivity.this);
                res_Email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                //res_Email.setTransformationMethod(PasswordTransformationMethod.getInstance());
                res_Email.setHint("Enter Email id here...");
                //final String email = res_Email.getText().toString();
                builder.setView(res_Email);
                builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!res_Email.getText().toString().equals("")) {
                            mAuthentication.sendPasswordResetEmail(res_Email.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SellerLoginActivity.this, "Check email to reset your password!", Toast.LENGTH_SHORT).show();
                                                /*Intent intent = new Intent(SellerLoginActivity.this, SellerLoginActivity.class);
                                                startActivity(intent);*/
                                            } else {
                                                Toast.makeText(SellerLoginActivity.this, "Fail to send reset password email!", Toast.LENGTH_LONG).show();
                                                /*Intent intent = new Intent(SellerLoginActivity.this, SellerLoginActivity.class);
                                                startActivity(intent);*/
                                            }
                                        }
                                    });
                        }
                        else
                        {
                            Toast.makeText(SellerLoginActivity.this, "Email not matched", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });




    }

    private void LoginSeller() {
        final String Email = sellerEmail.getText().toString();
        String Password = sellerPassword.getText().toString();

        if(!Email.equals("") && !Password.equals(""))
        {
            loadingBar.setTitle("Seller Login");
            loadingBar.setMessage("Please wait, while we checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            mAuthentication.signInWithEmailAndPassword(Email,Password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                loadingBar.dismiss();
                                Toast.makeText(SellerLoginActivity.this, "Login Successfully...", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SellerLoginActivity.this, HomeSellerActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                            else
                            {
                                loadingBar.dismiss();
                                Toast.makeText(SellerLoginActivity.this, "Email id or Password does not match with the database.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            Toast.makeText(this, "Please Complete the Login Form.", Toast.LENGTH_SHORT).show();
        }
    }
}
