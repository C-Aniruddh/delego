package clstr.delego.models;

/**
 * Created by aniruddhc on 12/2/17.
 */

public class Notification {
    private String name;
    private String content;
    private String numid;

    public String getTitle() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getID() {
        return numid;
    }
}
