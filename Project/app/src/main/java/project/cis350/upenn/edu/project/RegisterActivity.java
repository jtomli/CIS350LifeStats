package project.cis350.upenn.edu.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button backToMain = (Button)findViewById(R.id.backToMain);
        Button registerButton = (Button)findViewById(R.id.registerButton);
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    public void login(View v) {
        EditText emailInput = (EditText)findViewById(R.id.emailInput);
        EditText passwordInput = (EditText)findViewById(R.id.passwordInput);

        String username = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        DatabaseManager db = new DatabaseManager(this);
        User match = db.getUser(username);



        if (match != null) {
            if (match.getRawPassword().equals(password)) {
                Toast.makeText(this, "Logged in!", Toast.LENGTH_LONG).show();
                Intent i = new Intent(this, SetupActivityReasons.class);

                // send username to "reasons" DB
                i.putExtra("username", username);
                startActivity(i);
            } else {
                Toast.makeText(this, "Wrong password.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "This email is not registered.", Toast.LENGTH_LONG).show();
        }
    }
}