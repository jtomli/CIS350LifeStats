package com.example.jamietomlinson.iteration2;

import static android.R.attr.id;
import static android.R.attr.name;

/**
 * Created by Colin on 2/23/17.
 */

public class User {
    int _id;
    String _email;
    String _rawPassword;

    public User(int id, String email, String rawPassword) {
        this._id = id;
        this._email = email;
        this._rawPassword = rawPassword;
    }

    public User(String email, String rawPassword) {
        this._email = email;
        this._rawPassword = rawPassword;
    }

    public String getEmail(){
        return this._email;
    }

    public void setEmail(String email){
        this._email = email;
    }

    public String getRawPassword(){
        return this._rawPassword;
    }

    public void setRawPassword(String password){
        this._rawPassword = password;
    }

    public int getID(){
        return this._id;
    }
}
