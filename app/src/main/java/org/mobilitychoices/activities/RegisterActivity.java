package org.mobilitychoices.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.mobilitychoices.R;
import org.mobilitychoices.remote.RegisterTask;
import org.mobilitychoices.remote.ResponseError;

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
        email_EditText.requestFocus();

        handleRegisterButton();
    }

    private void handleRegisterButton() {
        register_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO validate email
                String email = email_EditText.getText().toString();

                //Validate password
                String pw1 = password_EditText.getText().toString();
                String pw2 = password_confirm_EditText.getText().toString();
                if (!pw1.equals(pw2)) {
                    password_confirm_EditText.requestFocus();
                    password_confirm_EditText.setError("Password does not match! Try again.");
                    return;
                }

                String username = username_EditText.getText().toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", email_EditText.getText().toString());
                    jsonObject.put("password", pw1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new RegisterTask(response -> {
                    Log.i(RegisterActivity.class.getName(), String.valueOf(response.getCode()));

                    if (response.getCode() == 201) {
                        Intent resultData = new Intent();
                        resultData.putExtra("email", email);
                        setResult(Activity.RESULT_OK, resultData);
                        finish();
                    } else {
                        if(response.getCode() == 400){
                            ResponseError error = response.getError();
                            //TODO handle error
                        }else if(response.getCode() == 500){
                            //Server Error
                        }
                    }
                }).execute(jsonObject);
            }
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
