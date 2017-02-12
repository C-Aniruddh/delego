package clstr.delego.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aniruddhc on 12/2/17.
 */

public class Response {
    private List<Delegate> delegate = new ArrayList<Delegate>();

    public List<Delegate> getAndroid() {
        return delegate;
    }
}
