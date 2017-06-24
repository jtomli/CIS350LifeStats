package project.cis350.upenn.edu.project;

import java.util.ArrayList;

/**
 * Created by Colin on 2/23/17.
 *
 * This class is a convenient way to bundle information regarding a user.
 */

public class User {
    String id;
    String email;
    String name;
    ArrayList<String> reasons;
    String sentiment;


    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        reasons = new ArrayList<String>();
    }

    public String getID(){
        return this.id;
    }

    public void setID(String id){
        this.id = id;
    }

    public String getEmail(){
        return this.email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getName(){
        return this.name;
    }

    public void setName(String name) { this.name = name; }

    public void setReasons(ArrayList<String> r) {
        reasons = r;
    }

    public ArrayList<String> getReasons() {
        return reasons;
    }

    public void setSentiment(String s) {
        sentiment = s;
    }

    public String getSentiment() {
        return sentiment;
    }

}