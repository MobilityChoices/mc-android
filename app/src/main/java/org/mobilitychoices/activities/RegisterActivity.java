package org.mobilitychoices.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import org.mobilitychoices.R;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");
        String password = bundle.getString("password");

        Button register = (Button) findViewById(R.id.registerBtn);
        register.setOnClickListener(view -> {
            Intent resultData = new Intent();
            resultData.putExtra("email", email);
            resultData.putExtra("password", password);

            setResult(Activity.RESULT_OK, resultData);
            finish();
        });
    }
}
