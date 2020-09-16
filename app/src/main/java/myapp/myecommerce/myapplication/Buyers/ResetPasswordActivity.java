package myapp.myecommerce.myapplication.Buyers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import myapp.myecommerce.myapplication.Prevalent.Prevalent;
import myapp.myecommerce.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class ResetPasswordActivity extends AppCompatActivity {

    private String check = "";
    private TextView pageTitle, titleQuestion;
    private EditText phoneNumber, Question1, Question2;
    private Button verifyButn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        check = getIntent().getStringExtra("check");
        pageTitle = (TextView) findViewById(R.id.page_title);
        titleQuestion = (TextView) findViewById(R.id.title_questions);
        phoneNumber = (EditText) findViewById(R.id.find_phone_number);
        Question1 = (EditText) findViewById(R.id.question_1);
        Question2 = (EditText) findViewById(R.id.question_2);
        verifyButn = (Button) findViewById(R.id.verify_answer_btn);
    }

    @Override
    protected void onStart() {
        super.onStart();

        phoneNumber.setVisibility(View.GONE);
        if(check.equals("settings"))
        {
            pageTitle.setText("Set Security Questions");
            titleQuestion.setText("Answer set Answers for the Following Security Questions?");
            verifyButn.setText("Set");

            DisplayPreviousAns();
            verifyButn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SetAnswers();
                }
            });
        }
        else if(check.equals("login"))
        {
            phoneNumber.setVisibility(View.VISIBLE);
            verifyButn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    verifyUser();
                }
            });
        }
    }



    private void SetAnswers()
    {
        String ans1 = Question1.getText().toString().toLowerCase();
        String ans2 = Question2.getText().toString().toLowerCase();

        if(Question1.equals("") && Question2.equals(""))
        {
            Toast.makeText(ResetPasswordActivity.this, "Please Answer Both the Questions", Toast.LENGTH_SHORT).show();
        }
        else
        {
            DatabaseReference ref = FirebaseDatabase
                    .getInstance()
                    .getReference().child("Users")
                    .child(Prevalent.currentOnlineUser.getPhone());

            HashMap<String,Object> userdataMap = new HashMap<>();
            userdataMap.put("answer1",ans1);
            userdataMap.put("answer2",ans2);

            ref.child("Security Questions").updateChildren(userdataMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ResetPasswordActivity.this, "You have answer the security Questions Successfully...", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ResetPasswordActivity.this, SettingsActivity.class);
                                startActivity(intent);
                            }
                        }
                    });

        }
    }

    private void DisplayPreviousAns()
    {
        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference().child("Users")
                .child(Prevalent.currentOnlineUser.getPhone());

        ref.child("Security Questions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    String ans_1 = dataSnapshot.child("answer1").getValue().toString();
                    String ans_2 = dataSnapshot.child("answer2").getValue().toString();

                    Question1.setText(ans_1);
                    Question2.setText(ans_2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void verifyUser() {

        final String phone = phoneNumber.getText().toString();
        final String answer1 = Question1.getText().toString().toLowerCase();
        final String answer2 = Question2.getText().toString().toLowerCase();

        if(!phone.equals("") && !answer1.equals("") && !answer2.equals(""))
        {
            final DatabaseReference ref = FirebaseDatabase
                    .getInstance()
                    .getReference().child("Users")
                    .child(phone);

            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        String existPhone = dataSnapshot.child("phone").getValue().toString();
                        String Verifyans1 = dataSnapshot.child("Security Questions").child("answer1").getValue().toString();
                        String Verifyans2 = dataSnapshot.child("Security Questions").child("answer2").getValue().toString();
                        if(!Verifyans1.equals(answer1))
                        {
                            Toast.makeText(ResetPasswordActivity.this, "Answer 1 is incorrect...", Toast.LENGTH_SHORT).show();
                        }
                        else if(!Verifyans2.equals(answer2))
                        {
                            Toast.makeText(ResetPasswordActivity.this, "Answer 2 is incorrect...", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                            builder.setTitle("Enter New Password\n");
                            final EditText newPassword = new EditText(ResetPasswordActivity.this);
                            newPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            newPassword.setHint("Enter New Password Here");
                            builder.setView(newPassword);
                            builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!newPassword.getText().toString().equals(""))
                                    {
                                        ref.child("password").setValue(newPassword.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            Toast.makeText(ResetPasswordActivity.this, "Password Change Successfully...", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    }
                                                });
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
                        /*if(dataSnapshot.hasChild("Security Questions"))
                        {

                        }
                        else
                        {
                            Toast.makeText(ResetPasswordActivity.this, "You have not set the Security Questions...", Toast.LENGTH_SHORT).show();
                        }*/
                    }
                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this, "This Phone number does not exist...", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else {
            Toast.makeText(this, "Please complete the form...", Toast.LENGTH_SHORT).show();
        }
    }
}
