package org.mobilitychoices.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.mobilitychoices.R;
import org.mobilitychoices.remote.RegisterTask;
import org.mobilitychoices.remote.Response;
import org.mobilitychoices.remote.ResponseError;

public class RegisterActivity extends AppCompatActivity {

    private EditText email_EditText;
    private EditText password_EditText;
    private EditText password_confirm_EditText;
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
                String email = email_EditText.getText().toString();

                //Validate password
                String pw1 = password_EditText.getText().toString();
                String pw2 = password_confirm_EditText.getText().toString();
                if (!pw1.equals(pw2)) {
                    password_confirm_EditText.requestFocus();
                    password_confirm_EditText.setError("Password does not match! Try again.");
                    return;
                }

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", email_EditText.getText().toString());
                    jsonObject.put("password", pw1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new RegisterTask(new RegisterTask.IRegisterCallback() {
                    @Override
                    public void done(Response<Object> response) {
                        if (response != null) {
                            Log.i(RegisterActivity.class.getName(), String.valueOf(response.getCode()));

                            switch (response.getCode()){
                                case 201:
                                    Intent resultData = new Intent();
                                    resultData.putExtra("email", email);
                                    setResult(Activity.RESULT_OK, resultData);
                                    finish();
                                    break;
                                case 400:
                                    ResponseError error = response.getError();
                                    if (error.getTarget().equals("password")) {
                                        password_confirm_EditText.setText("");
                                        password_EditText.setText("");
                                        password_EditText.setError("Invalid Password! Min. length: 3");
                                        password_EditText.requestFocus();
                                    } else if (error.getTarget().equals("email")) {
                                        email_EditText.setError("Email invalid or already in use!");
                                        email_EditText.requestFocus();
                                    }
                                    break;
                                default:
                                    Toast.makeText(RegisterActivity.this.getApplicationContext(), String.valueOf(getString(R.string.internalServerError)), Toast.LENGTH_LONG).show();
                                    break;
                            }
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
//        EditText username_EditText = (EditText) findViewById(R.id.username_register_form);
        register_Btn = (Button) findViewById(R.id.registerBtn);
    }
}
