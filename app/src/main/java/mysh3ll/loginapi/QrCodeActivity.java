package mysh3ll.loginapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.IOException;

import mysh3ll.loginapi.Interface.ValidationTicket;
import mysh3ll.loginapi.POJO.Ticket;
import mysh3ll.loginapi.Retrofit.RetroClient;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.attr.data;

/**
 * Created by Michel on 03/02/2017.
 */

public class QrCodeActivity extends AppCompatActivity {

    //    private TextView token;
    private String token;
    private Button scan_btn;

    private ImageSwitcher validationTicket;

    //    private static String ENDPOINT = "http://172.16.200.108/TPResa_Symfony3-Git/web/app_dev.php/api/";
//    private static String ENDPOINT = "http://192.168.1.95/TPResa_Symfony3-Git/web/app_dev.php/api/";
    private static String ENDPOINT = "http://www.mysh3ll.fr/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

//        token = (TextView) findViewById(R.id.textToken);
//        token.setText(displayToken());
        validationTicket = (ImageSwitcher) findViewById(R.id.validationTicket);
        validationTicket.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return imageView;
            }
        });


        //-----------------QRCODE---------------------------------------

        scan_btn = (Button) findViewById(R.id.scan_btn);

        final Activity activity = this;

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scannez le QrCode");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.setOrientationLocked(true);
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Vous avez annulé le scan", Toast.LENGTH_LONG).show();
            } else {
//                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

                String codeUnique = result.getContents();

                //-----------------Retrofit validation ticket---------------------------------------
                token = getToken();

                /**
                 * Creating an object of our api interface
                 */
                ValidationTicket clientTicket = RetroClient.getValidationTicketService(true, token);

//                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        Request newRequest = chain.request().newBuilder()
//                                .addHeader("Authorization", "Bearer " + token)
//                                .build();
//
//                        return chain.proceed(newRequest);
//                    }
//                }).build();
//
//                Retrofit retrofit = new Retrofit.Builder()
//                        .client(client)
//                        .baseUrl(ENDPOINT)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build();
//
//                ValidationTicket clientTicket = retrofit.create(ValidationTicket.class);

                /**
                 * Calling JSON
                 */
                Call<Ticket> call = clientTicket.getTicket(codeUnique);

                call.enqueue(new Callback<Ticket>() {
                    @Override
                    public void onResponse(Call<Ticket> call, retrofit2.Response<Ticket> response) {
                        Ticket ticket = response.body();

                        if (response.isSuccessful()) {

                            if (ticket.getSuccess() == 1) {

                                validationTicket.setImageResource(R.drawable.success);
                                Toast.makeText(QrCodeActivity.this, ticket.getMessage(), Toast.LENGTH_LONG).show();

                            } else {

                                validationTicket.setImageResource(R.drawable.fail);
                                Toast.makeText(QrCodeActivity.this, ticket.getMessage(), Toast.LENGTH_LONG).show();

                            }

                        } else {
                            Intent intent = new Intent(QrCodeActivity.this, LoginActivity.class);
                            startActivity(intent);
                            Toast.makeText(QrCodeActivity.this, response.message(), Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<Ticket> call, Throwable t) {
                        Toast.makeText(QrCodeActivity.this, "Erreur !", Toast.LENGTH_LONG).show();
                    }
                });


            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    private String getToken() {

        SharedPreferences sharedPreferences = getSharedPreferences("userToken", Context.MODE_PRIVATE);

        String token = sharedPreferences.getString("token", null);

        return token;
    }
}
