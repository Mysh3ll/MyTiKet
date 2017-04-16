package mysh3ll.loginapi.Interface.User;

import mysh3ll.loginapi.POJO.User.UserProfileUpdate;
import mysh3ll.loginapi.POJO.User.UserProfilePassword;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Michel on 07/02/2017.
 */

public interface UserInfoUpdate {

    @FormUrlEncoded
    @POST("user/profile")
    Call<UserProfileUpdate> updateUserInfo(@Field("username") String username, @Field("password") String password, @Field("mail") String mail);

    @FormUrlEncoded
    @POST("user/password")
    Call<UserProfilePassword> updateUserPassword(@Field("password") String password, @Field("new_password") String new_password);
}
