package mysh3ll.loginapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import mysh3ll.loginapi.POJO.Order.Order;
import mysh3ll.loginapi.POJO.Order.UserOrderList;
import mysh3ll.loginapi.Retrofit.RetroClient;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserOrderActivity extends AppCompatActivity {

    private ListView listView;
    private View parentView;

    private ArrayList<Order> orderList;
    private MyOrderAdapter adapter;

    private String token;

    private static String ENDPOINT = "http://www.mysh3ll.fr/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);

        /**
         * Array List for Binding Data from JSON to this List
         */
        orderList = new ArrayList<>();

        parentView = findViewById(R.id.activity_user_order);

        /**
         * Getting List and Setting List Adapter
         */
        listView = (ListView) findViewById(R.id.listView_user_order);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(UserOrderActivity.this, orderList.get(position).getIdEvent().toString(), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(UserOrderActivity.this, UserOrderCodeUniqueActivity.class);
                intent.putExtra("idEvent", orderList.get(position).getIdEvent().toString());
                startActivity(intent);
            }
        });

        final ProgressDialog dialog;

        /**
         * Progress Dialog for User Interaction
         */
        dialog = new ProgressDialog(UserOrderActivity.this);
        dialog.setTitle(getString(R.string.string_profile_order_title));
        dialog.setMessage(getString(R.string.string_waiting_message));
        dialog.show();

        //-----------------Retrofit Get User Info---------------------------------------
        token = getToken();

        /**
         * Creating an object of our api interface
         */
        UserOrders clientUserOrder = RetroClient.getUserOrdersService(true, token);

//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//        // set your desired log level
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//
//        httpClient.addInterceptor(new Interceptor() {
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
//        httpClient.addInterceptor(logging);
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .client(httpClient.build())
//                .baseUrl(ENDPOINT)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        UserOrders clientUserOrder = retrofit.create(UserOrders.class);

        /**
         * Calling JSON
         */
        Call<UserOrderList> call = clientUserOrder.getOrders();

        call.enqueue(new Callback<UserOrderList>() {
            @Override
            public void onResponse(Call<UserOrderList> call, retrofit2.Response<UserOrderList> response) {
                //Dismiss Dialog
                dialog.dismiss();

                if (response.isSuccessful()) {
                    /**
                     * Got Successfully
                     */
                    orderList = (ArrayList<Order>) response.body().getOrders();

                    /**
                     * Binding that List to Adapter
                     */
                    adapter = new MyOrderAdapter(UserOrderActivity.this, orderList);
                    listView.setAdapter(adapter);
                } else {
                    Toast.makeText(UserOrderActivity.this, "Problème !", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserOrderList> call, Throwable t) {
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
