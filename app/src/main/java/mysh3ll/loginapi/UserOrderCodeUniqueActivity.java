package mysh3ll.loginapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import mysh3ll.loginapi.Interface.Order.UserOrders;
import mysh3ll.loginapi.POJO.CodeUnique.QrCode;
import mysh3ll.loginapi.POJO.CodeUnique.UserCodeUniqueList;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserOrderCodeUniqueActivity extends AppCompatActivity {

    private ListView listView;
    private View parentView;

    private ArrayList<QrCode> codeUniqueList;
    private MyCodeUniqueAdapter adapter;

    private String token;
    private String  idEvent;

    private static String ENDPOINT = "http://www.mysh3ll.fr/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order_code_unique);

        /**
         * Array List for Binding Data from JSON to this List
         */
        codeUniqueList = new ArrayList<>();

        parentView = findViewById(R.id.activity_user_order_code_unique);

        /**
         * Getting List and Setting List Adapter
         */
        listView = (ListView) findViewById(R.id.listView_user_order_code_unique);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(UserOrderCodeUniqueActivity.this, codeUniqueList.get(position).getCodeUnique(), Toast.LENGTH_LONG).show();
//
//            }
//        });


        /**
         * Progress Dialog for User Interaction
         */
        final ProgressDialog dialog;
        dialog = new ProgressDialog(UserOrderCodeUniqueActivity.this);
        dialog.setTitle(getString(R.string.string_profile_ticket_title));
        dialog.setMessage(getString(R.string.string_waiting_message));
        dialog.show();

        //-----------------Retrofit Get User Info---------------------------------------
        token = getToken();
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + token)
                        .build();

                return chain.proceed(newRequest);
            }
        }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserOrders clientUserOrder = retrofit.create(UserOrders.class);

        idEvent = getIntent().getStringExtra("idEvent");

        Call<UserCodeUniqueList> call = clientUserOrder.getCodeUnique(idEvent);

        call.enqueue(new Callback<UserCodeUniqueList>() {
            @Override
            public void onResponse(Call<UserCodeUniqueList> call, retrofit2.Response<UserCodeUniqueList> response) {
                //Dismiss Dialog
                dialog.dismiss();

                if (response.isSuccessful()) {
                    /**
                     * Got Successfully
                     */
                    codeUniqueList = (ArrayList<QrCode>) response.body().getQrCode();

                    /**
                     * Binding that List to Adapter
                     */
                    adapter = new MyCodeUniqueAdapter(UserOrderCodeUniqueActivity.this, codeUniqueList);
                    listView.setAdapter(adapter);

                } else {
                    Toast.makeText(UserOrderCodeUniqueActivity.this, "Problème !", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<UserCodeUniqueList> call, Throwable t) {
                //Dismiss Dialog
                dialog.dismiss();
            }
        });
    }

    private String getToken() {

        SharedPreferences sharedPreferences = getSharedPreferences("userToken", Context.MODE_PRIVATE);

        String token = sharedPreferences.getString("token", null);

        return token;
    }
}
