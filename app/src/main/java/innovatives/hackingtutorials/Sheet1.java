
package innovatives.hackingtutorials;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sheet1 {

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Link")
    @Expose
    private String link;


    @SerializedName("Link")
    @Expose
    private int pos;

    public Sheet1(String title, String link, int pos) {
        this.title = title;
        this.link = link;
        this.pos = pos;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
