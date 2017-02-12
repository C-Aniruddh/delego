package clstr.delego.models;

/**
 * Created by aniruddhc on 12/2/17.
 */

public class Delegate {
    private String name;
    private String type;
    private String user_image;
    private String user_id;

    public String getName() {
        return name;
    }

    public String getImage(){
        return user_image;
    }
    public String getType() {
        return type;
    }
    public String getID(){
        return user_id;
    }
}
