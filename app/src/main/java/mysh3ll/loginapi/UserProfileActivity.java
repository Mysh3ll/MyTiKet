package mysh3ll.loginapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

import mysh3ll.loginapi.Interface.User.UserInfo;
import mysh3ll.loginapi.Interface.User.UserInfoUpdate;
import mysh3ll.loginapi.POJO.User.UserProfile;
import mysh3ll.loginapi.POJO.User.UserProfileUpdate;
import mysh3ll.loginapi.Retrofit.RetroClient;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserProfileActivity extends AppCompatActivity {

    private EditText userName, userMail, userPassword;
    private TextView titleUserName, titleUserMail, titleUserPassword;
    private Button btnUserProfile;

    private String token;

    private static String ENDPOINT = "http://www.mysh3ll.fr/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userName = (EditText) findViewById(R.id.user_name);
        userMail = (EditText) findViewById(R.id.user_mail);
        userPassword = (EditText) findViewById(R.id.user_password);

        titleUserName = (TextView) findViewById(R.id.textViewUsername);
        titleUserMail = (TextView) findViewById(R.id.textViewMail);
        titleUserPassword = (TextView) findViewById(R.id.textViewPassword);

        btnUserProfile = (Button) findViewById(R.id.btn_user_profile);

        /**
         * Progress Dialog for User Interaction
         */
        final ProgressDialog dialog;
        dialog = new ProgressDialog(UserProfileActivity.this);
        dialog.setTitle(getString(R.string.string_profile_title));
        dialog.setMessage(getString(R.string.string_waiting_message));
        dialog.show();

        //-----------------Retrofit Get User Info---------------------------------------
        token = getToken();
        /**
         * Creating an object of our api interface
         */
        final UserInfo clientUser = RetroClient.getUserInfoService(true, token);
//
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
//
//        final UserInfo clientUser = retrofit.create(UserInfo.class);

        /**
         * Calling JSON
         */
        Call<UserProfile> call = clientUser.getUser();

        call.enqueue(new Callback<UserProfile>() {
            @Override
            public void onResponse(Call<UserProfile> call, retrofit2.Response<UserProfile> response) {
                UserProfile user = response.body();

                if (response.isSuccessful()) {

                    if (user.getSuccess() == 1) {

                        userName.setText(user.getUsername());
                        userMail.setText(user.getMail());

                        //Dismiss Dialog
                        dialog.dismiss();

                        //----------------------Update User Info------------------------------------
                        btnUserProfile.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                boolean error = false;

                                if (TextUtils.isEmpty(getUsername())) {
                                    error = true;
                                    Toast.makeText(UserProfileActivity.this, "Veuillez renter un pseudo.", Toast.LENGTH_LONG).show();
                                }

                                if (TextUtils.isEmpty(getMail())) {
                                    error = true;
                                    Toast.makeText(UserProfileActivity.this, "Veuillez renter un e-mail.", Toast.LENGTH_LONG).show();
                                }

                                if (TextUtils.isEmpty(getPassword())) {
                                    error = true;
                                    Toast.makeText(UserProfileActivity.this, "Veuillez renter votre mot de passe actuel.", Toast.LENGTH_LONG).show();
                                }

                                if (!error) {
                                    /**
                                     * Progress Dialog for User Interaction
                                     */
                                    final ProgressDialog dialog;
                                    dialog = new ProgressDialog(UserProfileActivity.this);
                                    dialog.setTitle(getString(R.string.string_profile_edit_title));
                                    dialog.setMessage(getString(R.string.string_waiting_message));
                                    dialog.show();

                                    /**
                                     * Creating an object of our api interface
                                     */
                                    UserInfoUpdate clientUserUpdate = RetroClient.getUserInfoUpdateService(true, token);

//                                    UserInfoUpdate clientUserUpdate = retrofit.create(UserInfoUpdate.class);

                                    /**
                                     * Calling JSON
                                     */
                                    Call<UserProfileUpdate> call = clientUserUpdate.updateUserInfo(getUsername(), getPassword(), getMail());

                                    call.enqueue(new Callback<UserProfileUpdate>() {
                                        @Override
                                        public void onResponse(Call<UserProfileUpdate> call, retrofit2.Response<UserProfileUpdate> response) {
                                            //Dismiss Dialog
                                            dialog.dismiss();

                                            UserProfileUpdate userUpdate = response.body();

                                            if (response.isSuccessful()) {

                                                if (userUpdate.getSuccess() == 1) {

                                                    String token = userUpdate.getToken();
                                                    saveToken(token);

                                                    Toast.makeText(UserProfileActivity.this, userUpdate.getMessage(), Toast.LENGTH_LONG).show();
                                                    finish();

                                                } else {
                                                    Toast.makeText(UserProfileActivity.this, userUpdate.getMessage(), Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<UserProfileUpdate> call, Throwable t) {
                                            //Dismiss Dialog
                                            dialog.dismiss();
                                            Toast.makeText(UserProfileActivity.this, "Erreur !", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }

                            }
                        });
                    } else {

//                        validationTicket.setImageResource(R.drawable.fail);
                        Toast.makeText(UserProfileActivity.this, "Erreur !", Toast.LENGTH_LONG).show();

                    }

                } else {
                    Intent intent = new Intent(UserProfileActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(UserProfileActivity.this, response.message(), Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<UserProfile> call, Throwable t) {
                Toast.makeText(UserProfileActivity.this, "Erreur !", Toast.LENGTH_LONG).show();
            }
        });

    }

    private String getPassword() {
        return userPassword.getText().toString().trim();
    }

    private String getUsername() {
        return userName.getText().toString().trim();
    }

    private String getMail() {
        return userMail.getText().toString().trim();
    }

    private String getToken() {

        SharedPreferences sharedPreferences = getSharedPreferences("userToken", Context.MODE_PRIVATE);

        String token = sharedPreferences.getString("token", null);

        return token;
    }

    private void saveToken(String token) {

        SharedPreferences sharedPreferences = getSharedPreferences("userToken", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.apply();
    }
}
