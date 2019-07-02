package com.pontianak.kasirpakusu;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.NetworkResponse;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    Service_Connector service_connector;
    Helper helper;
    TextInputEditText edt_username,edt_password;
    Button bt_login;
    TextView txt_warning;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        service_connector=new Service_Connector();
        helper=new Helper(this);
        edt_username=findViewById(R.id.edt_username);
        edt_password=findViewById(R.id.edt_password);
        bt_login=findViewById(R.id.bt_login);
        txt_warning=findViewById(R.id.txt_warning);
        progressBar=findViewById(R.id.progressBar);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(edt_username.getText().toString(),edt_password.getText().toString());


            }
        });
        checklogin();
    }
    public void checklogin(){
        String session=helper.prefs.getString("Set-Cookie"," ");
        service_connector.sendgetrequestwithsession_v3(this, session, "checkloginkasir", new Service_Connector.VolleyResponseListener_v3() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {
                try{
                    JSONObject jsonObject=new JSONObject(response);
                    if(jsonObject.getString("login").equals("N")){
                        if(helper.prefs.getBoolean("remember_me",false)){
                            login(helper.prefs.getString("username","blah"),helper.prefs.getString("password","nanana"));

                        }
                        else{
                            helper.meditor.putBoolean("is_login",false).apply();

                        }
                    }
                    else if(jsonObject.getString("login").equals("1")){
                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }
                }
                catch (Throwable t){

                }

            }

            @Override
            public void onNoConnection(String message) {

            }

            @Override
            public void OnServerError(String message) {

            }

            @Override
            public void OnTimeOut() {

            }
        });

    }
    private void login(String usr,String psw){
        HashMap<String,String>param=new HashMap<>();
        param.put("username",usr);
        param.put("password",psw);
        txt_warning.setText(" ");
        progressBar.setVisibility(View.VISIBLE);

        service_connector.sendpostsessionrequest(this, "loginkasir", param, new Service_Connector.VolleyResponseCookieListener_v2() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponese(String response) {
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject datalogin = new JSONObject(response);
                    if(datalogin.getString("status").equals("1")){
                        helper.meditor.putBoolean("is_login",true).apply();


                            helper.meditor.putBoolean("remember_me",true).apply();
                            helper.meditor.putString("username",edt_username.getText().toString()).apply();
                            helper.meditor.putString("password",edt_password.getText().toString()).apply();

                        helper.meditor.putBoolean("is_login",true).apply();


                        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        LoginActivity.this.finish();

                    }
                    else{
                        txt_warning.setText(datalogin.getString("message"));
                    }



                } catch (Throwable t) {

                    Log.i("ezlogin", t.getMessage());
                }

            }

            @Override
            public void onCookie(NetworkResponse nr) {
                helper.meditor.putString("Set-Cookie", nr.headers.get("Set-Cookie"));
                helper.meditor.commit();

            }

            @Override
            public void onNoConnection(String message) {

            }

            @Override
            public void OnServerError(String message) {

            }

            @Override
            public void OnTimeOut() {

            }
        });
    }
}
