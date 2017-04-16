package mysh3ll.loginapi.POJO.CodeUnique;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserCodeUniqueList {

    @SerializedName("qrCode")
    @Expose
    private List<QrCode> qrCode = null;

    public List<QrCode> getQrCode() {
        return qrCode;
    }

    public void setQrCode(List<QrCode> qrCode) {
        this.qrCode = qrCode;
    }

}