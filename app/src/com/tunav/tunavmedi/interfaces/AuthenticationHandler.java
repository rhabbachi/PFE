package com.tunav.tunavmedi.interfaces;

public interface AuthenticationHandler {
    
    //Return true if the user is giving the correct credential
    // ID is a String as the user identification
    // password is a md5 hash of the base64 encoding of the password provided by the user.
    boolean authenticate(String ID, byte[] md5hash);
}
