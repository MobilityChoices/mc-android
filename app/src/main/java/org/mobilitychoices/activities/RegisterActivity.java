package org.mobilitychoices.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.mobilitychoices.R;
import org.mobilitychoices.entities.User;
import org.mobilitychoices.remote.RegisterTask;

public class RegisterActivity extends AppCompatActivity {

    private EditText email_EditText;
    private EditText password_EditText;
    private EditText password_confirm_EditText;
    private EditText username_EditText;
    private Button register_Btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViewElements();

        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");
        String password = bundle.getString("password");

        email_EditText.setText(email);
        email_EditText.setEnabled(false);
        password_EditText.setText(password);
        password_confirm_EditText.requestFocus();


        handleRegisterButton();
    }

    private void handleRegisterButton() {
        register_Btn.setOnClickListener(view -> {
            User user = new User();
            user.setEmail(email_EditText.getText().toString());

            //Validate password
            String pw1 = password_EditText.getText().toString();
            String pw2 = password_confirm_EditText.getText().toString();
            if (!pw1.equals(pw2)) {
                password_confirm_EditText.requestFocus();
                password_confirm_EditText.setError("Password does not match! Try again.");
                return;
            }
            user.setPasswort(pw1);

            String username = username_EditText.getText().toString();
            user.setUsername(username);

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("email", email_EditText.getText().toString());
                jsonObject.put("password", pw1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            new RegisterTask(success -> {
                Log.i(this.getLocalClassName(), String.valueOf(success));
                if (success) {
                    Intent resultData = new Intent();
                    resultData.putExtra("user", user);
                    setResult(Activity.RESULT_OK, resultData);
                    finish();
                } else {
                    //TODO set correct error
                }
            }).execute(jsonObject);
        });
    }

    private void initViewElements() {
        email_EditText = (EditText) findViewById(R.id.email_register_form);
        password_EditText = (EditText) findViewById(R.id.password_register_form);
        password_confirm_EditText = (EditText) findViewById(R.id.password_confirm_register_form);
        username_EditText = (EditText) findViewById(R.id.username_register_form);
        register_Btn = (Button) findViewById(R.id.registerBtn);
    }
}
