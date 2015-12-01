package edu.self.ludus;

import java.util.ArrayList;

/**
 * Created by sbakshi on 11/22/2015.
 */
public class StudentList extends ArrayList<Students> {
    private static StudentList sInstance = null;

    private StudentList(){}

    public static StudentList getInstance(){
        if (sInstance == null){
            sInstance = new StudentList();
        }
        return sInstance;
    }
}
