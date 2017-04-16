package mysh3ll.loginapi.POJO.Order;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Order {

    @SerializedName("titre")
    @Expose
    private String titre;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("idEvent")
    @Expose
    private Integer idEvent;

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getIdEvent() {
        return idEvent;
    }

    public void setIdEvent(Integer idEvent) {
        this.idEvent = idEvent;
    }

}