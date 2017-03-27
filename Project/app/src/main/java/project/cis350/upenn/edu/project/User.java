package project.cis350.upenn.edu.project;
<<<<<<< HEAD
=======

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
>>>>>>> master

import static android.R.attr.id;
import static android.R.attr.name;
import static project.cis350.upenn.edu.project.R.string.r;

/**
 * Created by Colin on 2/23/17.
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

    /*
    public User(Parcel in) {
        String[] d = new String[3];
        in.readStringArray(d);

        this.id = d[0];
        this.email = d[1];
        this.name = d[2];
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.id, this.email, this.name});
    }

    @Override
    public int describeContents() {
        return 0;
    }
    */

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
    /*
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };
    */

}
