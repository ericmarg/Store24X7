package com.cscie97.store.authentication;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

/**
 * The User class represents users of a service such as customers in a store. Each User is created with an ID and a name,
 * and can later be supplied with authentication credentials, privileges, and an AuthToken. Passwords are stored as a
 * hash for security.
 */
public class User implements Visitable {
    public User(String id, String name) {
        this.id = id;
        this.name = name;
        this.priviliges = new ArrayList<>();
    }

    private final String id;

    private final String name;

    private String facePrint = null;

    private String voicePrint = null;

    private String passwordHash = null;

    private List<Privilege> priviliges;

    private AuthToken authToken;

    /**
     * Hashes the provided password String
     * @param password  The password to hash
     * @return          The hashed password String
     */
    public String hashPassword(String password) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

        // format the hash as a hex string and return it
        HexFormat format = HexFormat.of();
        return format.formatHex(hash);
    }

    /**
     * Adds the given Privilege to the user
     * @param p     Privilege to add
     */
    public void addPrivilige(Privilege p) {
        priviliges.add(p);
    }

    /**
     * Links an AuthToken to the user
     * @param authToken     an AuthToken
     */
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    /**
     * Gets the user's ID
     * @return  ID string
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the user's name
     * @return  Name string
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the user's privileges
     * @return  List<Privilege> of privileges that have been granted to the user
     */
    public List<Privilege> getPrivileges() {
        return priviliges;
    }

    /**
     * Gets a new AuthToken for the user
     * @return  A new AuthToken
     */
    public AuthToken getNewAuthToken() {
        AuthToken token = AuthToken.createAuthToken();
        setAuthToken(token);
        token.setUser(this);
        return token;
    }

    /**
     * Gets the user's AuthToekn
     * @return  AuthToken
     */
    public AuthToken getAuthToken() {
        return authToken;
    }

    /**
     * Checks the user's password against the (hashed) string provided and returns True if they match
     * @param credentials   Password String
     * @return              True if the hashes match otherwise false
     */
    public boolean checkPassword(String credentials) {
        return this.passwordHash != null && this.passwordHash.equals(hashPassword(credentials));
    }

    /**
     * Checks the user's voicePrint against the provided voice print
     * @param credentials   Voice print provided
     * @return              True if the voice prints match else false
     */
    public boolean checkVoicePrint(String credentials) {
        return this.voicePrint != null && this.voicePrint.equals(credentials);
    }

    /**
     * Checks the user's face print against the one provided
     * @param credentials   A face print
     * @return              True if the face prints match else false
     */
    public boolean checkFacePrint(String credentials) {
        return this.facePrint != null && this.facePrint.equals(credentials);
    }

    /**
     * Calls a Visitor to visit
     * @param v     The visiting visitor
     */
    @Override
    public void acceptVisitor(Visitor v) {
        v.visitUser(this);
    }

    /**
     * Sets the credentials provided
     * @param type  The type of credential
     * @param value The credential
     */
    public void setCredentials(CredentialType type, String value) {
        if (type == CredentialType.VOICE_PRINT) {
            this.voicePrint = value;
        } else if (type == CredentialType.FACE_PRINT) {
            this.facePrint = value;
        } else if (type == CredentialType.PASSWORD) {
            this.passwordHash = hashPassword(value);
        }
    }

    /**
     * Adds a ResourceRole to the user's list of privileges
     * @param rs    The ResourceRole to add
     */
    public void addResourceRole(ResourceRole rs) {
        this.priviliges.add(rs);
    }
}
