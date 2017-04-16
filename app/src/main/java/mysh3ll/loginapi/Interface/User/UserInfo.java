package mysh3ll.loginapi.Interface.User;

import mysh3ll.loginapi.POJO.User.UserProfile;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Michel on 07/02/2017.
 */

public interface UserInfo {

    @GET("user")
    Call<UserProfile> getUser();
}
