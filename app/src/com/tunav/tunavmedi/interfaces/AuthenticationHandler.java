package com.tunav.tunavmedi.interfaces;

public interface AuthenticationHandler {

    /*
     * Check if the credential given by the user are correct (true) or not
     * (false).
     * 
     * @param ID User identifier.
     * 
     * @param md5hash md5 cryptographic hash of the base64 password encoding.
     * 
     * @return whether the suppled id and password hash are correct or not.
     */
    boolean authenticate(String ID, byte[] md5hash);

    /*
     * @return the authentication token generated by the server after a
     * successful authentication.
     */
    void setAppAuthToken(byte[] authToken);

    void revokeToken(byte[] token);

    byte[] requestToken();
}
