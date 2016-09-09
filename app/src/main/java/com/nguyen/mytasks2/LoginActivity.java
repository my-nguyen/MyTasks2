package com.nguyen.mytasks2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by My on 9/7/2016.
 */
public class LoginActivity extends AppCompatActivity {
   TextView info;
   LoginButton loginButton;
   CallbackManager callbackManager;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      FacebookSdk.sdkInitialize(getApplicationContext());
      callbackManager = CallbackManager.Factory.create();

      setContentView(R.layout.login_activity);
      info = (TextView)findViewById(R.id.info);
      loginButton = (LoginButton)findViewById(R.id.login_button);
      loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
         @Override
         public void onSuccess(LoginResult loginResult) {
            /*
            info.setText("User ID: " + loginResult.getAccessToken().getUserId() +
                  "\n" + "Auth Token: " + loginResult.getAccessToken().getToken());
            String message = "User " + loginResult.getAccessToken().getUserId() + " logged in successfully";
            */
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
         }

         @Override
         public void onCancel() {
            info.setText("Login attempt canceled.");
         }

         @Override
         public void onError(FacebookException error) {
            info.setText("Login attempt failed.");
         }
      });
   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      callbackManager.onActivityResult(requestCode, resultCode, data);
   }
}
