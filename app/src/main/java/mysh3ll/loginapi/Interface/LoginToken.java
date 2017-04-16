package mysh3ll.loginapi.Interface;

import mysh3ll.loginapi.POJO.Login;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Michel on 03/02/2017.
 */

public interface LoginToken {

    @FormUrlEncoded
    @POST("login")
    Call<Login> getUserToken(@Field("username") String username, @Field("password") String password);
}
