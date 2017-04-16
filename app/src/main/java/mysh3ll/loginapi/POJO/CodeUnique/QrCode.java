package mysh3ll.loginapi.POJO.CodeUnique;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QrCode {

    @SerializedName("codeUnique")
    @Expose
    private String codeUnique;
    @SerializedName("numPlace")
    @Expose
    private String numPlace;

    public String getCodeUnique() {
        return codeUnique;
    }

    public void setCodeUnique(String codeUnique) {
        this.codeUnique = codeUnique;
    }

    public String getNumPlace() {
        return numPlace;
    }

    public void setNumPlace(String numPlace) {
        this.numPlace = numPlace;
    }

}