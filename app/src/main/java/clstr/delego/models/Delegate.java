package clstr.delego.models;

/**
 * Created by aniruddhc on 12/2/17.
 */

public class Delegate {
    private String name;
    private String committee;
    private String country;
    private String numid;
    private String identifier;

    public String getName() {
        return name;
    }

    public String getImage(){
        return country;
    }
    public String getType() {
        return committee;
    }
    public String getID(){
        return numid;
    }
    public String getIdentifier(){
        return identifier;
    }
}
