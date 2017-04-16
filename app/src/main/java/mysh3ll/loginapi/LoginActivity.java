package mysh3ll.loginapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.util.List;

import mysh3ll.loginapi.Interface.LoginToken;
import mysh3ll.loginapi.POJO.Login;
import mysh3ll.loginapi.Retrofit.RetroClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Michel on 31/01/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText editPseudo, editPassword;
    private Button btnLogin;
    private int REQUEST_CODE = 1;

    //    private static String ENDPOINT = "http://172.16.200.108/TPResa_Symfony3-Git/web/app_dev.php/api/";
//    private static String ENDPOINT = "http://192.168.1.95/TPResa_Symfony3-Git/web/app_dev.php/api/";
//    private static String ENDPOINT = "http://www.mysh3ll.fr/api/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editPassword = (EditText) findViewById(R.id.edit_password);
        editPseudo = (EditText) findViewById(R.id.edit_pseudo);
        btnLogin = (Button) findViewById(R.id.btn_login);

        /**
         * Creating an object of our api interface
         */
        final LoginToken client = RetroClient.getLoginTokenService(false, null);


//        Retrofit.Builder builder = new Retrofit.Builder()
//                .baseUrl(ENDPOINT)
//                .addConverterFactory(GsonConverterFactory.create());
//
//        Retrofit retrofit = builder.build();

//        final LoginToken client = retrofit.create(LoginToken.class);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean error = false;

                if (TextUtils.isEmpty(getPseudo())) {
                    error = true;
                    Toast.makeText(LoginActivity.this, "Veuillez renter un pseudo.", Toast.LENGTH_LONG).show();
                }

                if (TextUtils.isEmpty(getPassword())) {
                    error = true;
                    Toast.makeText(LoginActivity.this, "Veuillez renter le mot de passe.", Toast.LENGTH_LONG).show();
                }

                if (!error) {

                    /**
                     * Progress Dialog for User Interaction
                     */
                    final ProgressDialog dialog;
                    dialog = new ProgressDialog(LoginActivity.this);
                    dialog.setTitle(getString(R.string.string_authentification_title));
                    dialog.setMessage(getString(R.string.string_waiting_message));
                    dialog.show();

                    /**
                     * Calling JSON
                     */
                    Call<Login> call = client.getUserToken(getPseudo(), getPassword());

                    call.enqueue(new Callback<Login>() {
                        @Override
                        public void onResponse(Call<Login> call, Response<Login> response) {
                            //Dismiss Dialog
                            dialog.dismiss();

                            Login login = response.body();

                            Toast.makeText(LoginActivity.this, login.getMessage(), Toast.LENGTH_SHORT).show();

                            if (login.getSuccess() == 1) {

                                String token = login.getToken();
                                List<String> role = login.getRole();

                                saveToken(token);

                                if (role.get(0).equals("ROLE_GUICHER")) {
                                    Intent intent = new Intent(LoginActivity.this, QrCodeActivity.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                    startActivity(intent);
                                    startActivityForResult(intent, REQUEST_CODE);
                                }

                            }

                        }

                        @Override
                        public void onFailure(Call<Login> call, Throwable t) {
                            //Dismiss Dialog
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Erreur !", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data) {
        if (requestcode == REQUEST_CODE){
            if (resultcode == RESULT_OK) {
                editPseudo.setText("");
                editPassword.setText("");
                editPseudo.requestFocus();
            }
            if (resultcode == RESULT_CANCELED) {
                editPseudo.setText("");
                editPassword.setText("");
                editPseudo.requestFocus();
            }
        }
    }

    private String getPassword() {
        return editPassword.getText().toString().trim();
    }

    private String getPseudo() {
        return editPseudo.getText().toString().trim();
    }

    private void saveToken(String token) {

        SharedPreferences sharedPreferences = getSharedPreferences("userToken", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
//        editor.putString("role", role);
        editor.apply();
    }
}
