package mysh3ll.loginapi.Retrofit;

import java.io.IOException;

import mysh3ll.loginapi.Interface.LoginToken;
import mysh3ll.loginapi.Interface.Order.UserOrders;
import mysh3ll.loginapi.Interface.User.UserInfo;
import mysh3ll.loginapi.Interface.User.UserInfoUpdate;
import mysh3ll.loginapi.Interface.ValidationTicket;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Michel on 14/02/2017.
 */

public class RetroClient {

    /********
     * URLS
     *******/
    private static final String ROOT_URL = "http://www.mysh3ll.fr/api/";

    /**
     * Get Retrofit Instance
     * @param auth
     */
    private static Retrofit getRetrofitInstance(boolean auth, final String token) {
        if(auth == false) {

            return new Retrofit.Builder()
                    .baseUrl(ROOT_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        } else {

            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + token)
                            .build();

                    return chain.proceed(newRequest);
                }
            }).build();

            return new Retrofit.Builder()
                    .client(client)
                    .baseUrl(ROOT_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
    }

    /**
     * Get LoginToken Service
     *
     * @return LoginToken Service
     * @param auth
     */
    public static LoginToken getLoginTokenService(boolean auth, String token) {
        return getRetrofitInstance(auth, token).create(LoginToken.class);
    }

    /**
     * Get ValidationTicket Service
     *
     * @return ValidationTicket Service
     */
    public static ValidationTicket getValidationTicketService(boolean auth, String token) {
        return getRetrofitInstance(auth, token).create(ValidationTicket.class);
    }

    /**
     * Get UserInfo Service
     *
     * @return UserInfo Service
     */
    public static UserInfo getUserInfoService(boolean auth, String token) {
        return getRetrofitInstance(auth, token).create(UserInfo.class);
    }

    /**
     * Get UserInfoUpdate Service
     *
     * @return UserInfoUpdate Service
     */
    public static UserInfoUpdate getUserInfoUpdateService(boolean auth, String token) {
        return getRetrofitInstance(auth, token).create(UserInfoUpdate.class);
    }

    /**
     * Get UserOrders Service
     *
     * @return UserOrders Service
     */
    public static UserOrders getUserOrdersService(boolean auth, String token) {
        return getRetrofitInstance(auth, token).create(UserOrders.class);
    }

}
