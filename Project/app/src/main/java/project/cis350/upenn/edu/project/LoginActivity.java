package project.cis350.upenn.edu.project;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button backToMain = (Button)findViewById(R.id.backToMain);
        Button loginButton = (Button)findViewById(R.id.loginButton);
        backToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    public void registerAccount(View v) {
        EditText emailInput = (EditText)findViewById(R.id.emailInput);
        EditText passwordInput = (EditText)findViewById(R.id.passwordInput);

        DatabaseManager db = new DatabaseManager(this);

        if (db.getUser(emailInput.getText().toString()) == null) {
            db.addUser(new User(emailInput.getText().toString(), passwordInput.getText().toString()));
            Toast.makeText(this, "Account created!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "This email is already registered!", Toast.LENGTH_LONG).show();
        }




        List<User> contacts = db.getAllUsers();

        for (User cn : contacts) {
            String log = "ID: " + cn.getID() + ", email: " + cn.getEmail() + ", password: " + cn.getRawPassword();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }

        /*
        MongoClientURI uri = new MongoClientURI("mongodb://kitchena:CIS350@ds131139.mlab.com:31139/cis350");
        MongoClient mongoClient = new MongoClient(uri);
        MongoDatabase db = mongoClient.getDatabase(uri.getDatabase());

        MongoCollection collection = db.getCollection("users");

        Document doc = new Document("email", emailInput.getText().toString())
                .append("password", passwordInput.getText().toString());

        collection.insertOne(doc);
        */
    }
}