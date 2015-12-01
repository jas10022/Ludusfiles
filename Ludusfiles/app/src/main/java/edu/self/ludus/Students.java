package edu.self.ludus;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by sbakshi on 11/22/2015.
 */
public class Students implements Serializable{
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    private String mName;
    public ArrayList<String> sports;
    public ArrayList<String> skillLevel;
}
