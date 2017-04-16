package mysh3ll.loginapi.POJO.User;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProfile {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("mail")
    @Expose
    private String mail;
    @SerializedName("success")
    @Expose
    private Integer success;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

}
