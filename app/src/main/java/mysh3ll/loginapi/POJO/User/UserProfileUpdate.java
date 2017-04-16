package mysh3ll.loginapi.POJO.User;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfileUpdate {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("success")
    @Expose
    private Integer success;
    @SerializedName("token")
    @Expose
    private String token;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }
}
