package mysh3ll.loginapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import mysh3ll.loginapi.Interface.User.UserInfoUpdate;
import mysh3ll.loginapi.POJO.User.UserProfilePassword;
import mysh3ll.loginapi.Retrofit.RetroClient;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserPasswordActivity extends AppCompatActivity {

    private EditText userPassword, userNewPassword, userConfirmNewPassword;
    private TextView titleUserPassword, titleUserNewPassword, titleUserConfirmNewPassword;
    private Button btnUserPassword;

    private String token;

    private static String ENDPOINT = "http://www.mysh3ll.fr/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_password);

        userPassword = (EditText) findViewById(R.id.user_password);
        userNewPassword = (EditText) findViewById(R.id.user_new_password);
        userConfirmNewPassword = (EditText) findViewById(R.id.user_confirm_new_password);

        titleUserPassword = (TextView) findViewById(R.id.textViewPassword);
        titleUserNewPassword = (TextView) findViewById(R.id.textViewNewPassword);
        titleUserConfirmNewPassword = (TextView) findViewById(R.id.textViewConfirmNewPassword);

        btnUserPassword = (Button) findViewById(R.id.btn_user_password);

        //-----------------Retrofit---------------------------------------
        token = getToken();
//        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Chain chain) throws IOException {
//                Request newRequest = chain.request().newBuilder()
//                        .addHeader("Authorization", "Bearer " + token)
//                        .build();
//
//                return chain.proceed(newRequest);
//            }
//        }).build();
//
//        final Retrofit retrofit = new Retrofit.Builder()
//                .client(client)
//                .baseUrl(ENDPOINT)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();

        btnUserPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error = false;

                if (TextUtils.isEmpty(getPassword())) {
                    error = true;
                    Toast.makeText(UserPasswordActivity.this, "Veuillez renter votre mot de passe actuel.", Toast.LENGTH_LONG).show();
                }

                if (TextUtils.isEmpty(getNewPassword())) {
                    error = true;
                    Toast.makeText(UserPasswordActivity.this, "Veuillez renter votre nouveau mot de passe.", Toast.LENGTH_LONG).show();
                }

                if (TextUtils.isEmpty(getConfirmNewPassword())) {
                    error = true;
                    Toast.makeText(UserPasswordActivity.this, "Veuillez confirmer votre nouveau mot de passe.", Toast.LENGTH_LONG).show();
                }

                if (!TextUtils.equals(getNewPassword(), getConfirmNewPassword())) {
                    error = true;
                    Toast.makeText(UserPasswordActivity.this, "Vous n'avez pas saisi le même mot de passe.", Toast.LENGTH_LONG).show();
                }

                if (!error) {
                    /**
                     * Progress Dialog for User Interaction
                     */
                    final ProgressDialog dialog;
                    dialog = new ProgressDialog(UserPasswordActivity.this);
                    dialog.setTitle(getString(R.string.string_profile_password_title));
                    dialog.setMessage(getString(R.string.string_waiting_message));
                    dialog.show();

                    /**
                     * Creating an object of our api interface
                     */
                    UserInfoUpdate clientUserPassword = RetroClient.getUserInfoUpdateService(true, token);

//                    UserInfoUpdate clientUserPassword = retrofit.create(UserInfoUpdate.class);
                    /**
                     * Calling JSON
                     */
                    Call<UserProfilePassword> call = clientUserPassword.updateUserPassword(getPassword(), getNewPassword());

                    call.enqueue(new Callback<UserProfilePassword>() {
                        @Override
                        public void onResponse(Call<UserProfilePassword> call, retrofit2.Response<UserProfilePassword> response) {
                            //Dismiss Dialog
                            dialog.dismiss();

                            UserProfilePassword userPassword = response.body();

                            if (response.isSuccessful()) {

                                if (userPassword.getSuccess() == 1) {

                                    Toast.makeText(UserPasswordActivity.this, userPassword.getMessage(), Toast.LENGTH_LONG).show();
                                    finish();

                                } else {
                                    Toast.makeText(UserPasswordActivity.this, userPassword.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<UserProfilePassword> call, Throwable t) {
                            //Dismiss Dialog
                            dialog.dismiss();
                            Toast.makeText(UserPasswordActivity.this, "Erreur !", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });


    }

    private String getPassword() {
        return userPassword.getText().toString().trim();
    }

    private String getNewPassword() {
        return userNewPassword.getText().toString().trim();
    }

    private String getConfirmNewPassword() {
        return userConfirmNewPassword.getText().toString().trim();
    }

    private String getToken() {

        SharedPreferences sharedPreferences = getSharedPreferences("userToken", Context.MODE_PRIVATE);

        String token = sharedPreferences.getString("token", null);

        return token;
    }
}
