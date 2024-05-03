package com.cscie97.store.authentication;

import java.security.SecureRandom;
import java.time.LocalDateTime;

/**
 * AuthTokens are created and tied to Users during the login and authentication process. Tokens provide users the ability
 * to request access to restricted resources, though the request may be denied. Tokens are invalidated after a period of
 * three hours or when a user logs out, whichever is sooner.
 */
public class AuthToken implements Visitable {

    private final String id;

    private final LocalDateTime expiration;

    private boolean state;

    private User user;

    public AuthToken(String id, LocalDateTime expiration, boolean state) {
        this.id = id;
        this.expiration = expiration;
        this.state = state;
    }

    /** Creates a new AuthToken object
     * The token is given a random ID 20 bytes long and the expiration time is set 3 hours from creation time.
     * @return  The new AuthToken instance
     */
    public static AuthToken createAuthToken() {
        // Generate an ID (credit: https://stackoverflow.com/questions/13992972/how-to-create-a-authentication-token-using-java)
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        String newId = bytes.toString();

        // Set an expiration time
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime exp = now.plusHours(3);

        return new AuthToken(newId, exp, true);
    }

    /**
     * Checks whether the token is valid or not.
     * @return  True if valid false if invalid
     */
    public boolean isValid() {
        if (LocalDateTime.now().isAfter(this.expiration)) {
            this.setState(false);
        }
        return state;
    }

    /**
     * Gets the User object associated with the token
     * @return  a User object
     */
    public User getUser() {
        return user;
    }

    /**
     * Links the token to the given User
     * @param user  User being linked
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns the ID of the token
     * @return  ID string
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the expiration date and time of the token
     * @return  LocalDateTime expiration time
     */
    public LocalDateTime getExpiration() {
        return expiration;
    }

    /**
     * Sets the state flag of the token
     * @param state Boolean indicating whether the token is valid
     */
    public void setState(boolean state) {
        this.state = state;
    }

    /**
     * Accepts a Visitor
     * @param v The calling Visitor object
     */
    @Override
    public void acceptVisitor(Visitor v) {
        v.visitAuthToken(this);
    }
}
