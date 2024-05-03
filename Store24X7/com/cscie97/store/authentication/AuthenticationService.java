package com.cscie97.store.authentication;

import com.cscie97.store.controller.StoreObserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The AuthenticationService is a Singleton class that acts as a facade and API for user authentication. Provides
 * login and logout functionality, authtoken creation and validation methods, and creation and designation of privileges
 * to users. It utilizes the Visitor Pattern to check access and display an inventory of all objects associated with it.
 * Privileges utilize the Composite Pattern to form their filesystem-like structure and parent-child relationships.
 */
public class AuthenticationService implements Visitable {
    private static AuthenticationService authenticationService = null;

    private final Map<String, User> userMap = new HashMap<>();

    private final Map<String, AuthToken> tokenMap = new HashMap<>();

    private final Map<String, Privilege> privilegeMap = new HashMap<>();

    private final Map<String, ResourceRole> resourceRoleMap = new HashMap<>();
    private final Map<String, Resource> resourceMap = new HashMap<>();

    /**
     * private constructor to avoid client applications using the constructor
     */
    private AuthenticationService() {
    }

    /**
     * getInstance() returns an instance of AuthenticationService
     * method adapted from wikipedia https://en.wikipedia.org/wiki/Singleton_pattern
     * @return  The single instance of the AuthenticationService class.
     */
    public static AuthenticationService getInstance() {
        if (authenticationService == null) {
            synchronized (StoreObserver.class) {
                if (authenticationService == null) {
                    authenticationService = new AuthenticationService();
                }
            }
        }
        return authenticationService;
    }

    /**
     * Creates a new Permission which may be applied to roles or to users.
     * @param id    Permission ID
     * @param name  Permission name
     * @param description   Permission description
     */
    public void createPermission(String id, String name, String description) {
        Permission permission = new Permission(id, name, description);
        Privilege priv = privilegeMap.putIfAbsent(id, permission);
        if (priv == null)
            System.out.println("--Added permission " + id + " " + name + " " + description);
        else
            System.out.println("--Permission already defined.");
    }

    /**
     * Creates a new Role, the composite of type Privilege. Applicable to Users and ResourceRoles.
     * @param id    Role ID
     * @param name  Role name
     * @param description   Role description
     */
    public void createRole(String id, String name, String description) {
        Role role = new Role(id, name, description);
        Privilege priv = privilegeMap.putIfAbsent(id, role);
        if (priv == null)
            System.out.println("--Added role " + id + " " + name + " " + description);
        else
            System.out.println("--Role already defined.");
    }

    /**
     * createUser creates a new User object
     * @param id    User ID
     * @param name  User's name
     */
    public void createUser(String id, String name) {
        User user = new User(id, name);
        user = userMap.putIfAbsent(id, user);
        if (user == null)
            System.out.println("--Added user " + id + " " + name);
        else
            System.out.println("--User already defined.");
    }

    /**
     * Create a new Resource object. Resources are abstract representations of any restricted resource that
     * requires authentication to access.
     * @param resourceID    Resource ID
     * @param description   Resource description
     */
    public void createResource(String resourceID, String description) {
        Resource resource = new Resource(resourceID, description);
        resource = resourceMap.putIfAbsent(resourceID, resource);
        if (resource == null)
            System.out.println("--Added resource " + resourceID);
        else
            System.out.println("--Resource already defined.");
    }

    /**
     * Creates a new ResourceRole which links a Role to a particular Resource.
     * @param name          ResourceRole name
     * @param roleId        ID of the role being coupled
     * @param resourceId    ID of the resource being coupled
     * @throws AuthenticationException
     */
    public void createResourceRole(String name, String roleId, String resourceId) throws AuthenticationException {
        Resource resource;
        Privilege role;
        resource = resourceMap.get(resourceId);
        role = privilegeMap.get(roleId);

        if (resource == null) {
            throw new AuthenticationException("No such resource");
        } else if (role == null) {
            throw new AuthenticationException("No such role");
        }

        String description = "Role " + roleId + " for resource " + resourceId;
        ResourceRole resourceRole = new ResourceRole(name, resourceId + roleId, description, resource);
        resourceRoleMap.put(name, resourceRole);

        // Copy role privileges over to the resourceRole
        for (Privilege p : role.getChildren()) {
            resourceRole.add(p);
        }
        System.out.println("--Created resource role " + name);
    }

    /**
     * Adds a Permission to a given role.
     * @param roleId    Role ID
     * @param permissionId  Permission ID
     */
    public void addRolePermission(String roleId, String permissionId) {
        Privilege role = privilegeMap.get(roleId);
        Privilege permission = privilegeMap.get(permissionId);

        if (role == null) {
            System.out.println("No such role: " + roleId);
        } else if (permission == null) {
            System.out.println("No such permission: " + permissionId);
        } else {
            role.add(permission);
            System.out.println("--Added permission " + permissionId + " to role " + roleId);
        }
    }

    /**
     * Adds a Permission to a given User
     * @param userId    User ID
     * @param permissionId  Permission ID
     */
    public void addUserPermission(String userId, String permissionId) {
        User user = userMap.get(userId);
        Privilege permission = privilegeMap.get(permissionId);
        if (user != null && permission != null) {
            user.addPrivilige(permission);
            System.out.println("Added role " + permissionId + " to user " + userId);
        }
    }

    /**
     * Adds a credential for a given user. Credentials can be any one of the CredentialType enum.
     * @param userId    The user's ID
     * @param type      The type of credential being added
     * @param value     The credential's value in String form.
     * @throws AuthenticationException
     */
    public void addUserCredential(String userId, CredentialType type, String value) throws AuthenticationException {
        User user = userMap.get(userId);
        if (user == null) {
            throw new AuthenticationException("Invalid user.");
        }
        user.setCredentials(type, value);
        System.out.println("--Set user credential " + value);
    }

    /**
     * Adds a Role to a given user
     * @param userId    User ID
     * @param roleId    Role ID
     */
    public void addUserRole(String userId, String roleId) {
        User user = userMap.get(userId);
        Privilege role = privilegeMap.get(roleId);
        if (user != null && role != null) {
            user.addPrivilige(role);
            System.out.println("--Added role " + roleId + " to user " + userId);
        }
    }

    /**
     * Adds a ResourceRole to a given User
     * @param userId    User ID
     * @param resourceRoleId    ResourceRole ID
     */
    public void addUserResourceRole(String userId, String resourceRoleId) {
        ResourceRole resourceRole = resourceRoleMap.get(resourceRoleId);
        User user = userMap.get(userId);
        if (resourceRole != null && user != null) {
            user.addResourceRole(resourceRole);
            System.out.println("--Set user " + userId + " resource role " + resourceRoleId);
        }
    }

    /**
     * Logs the user in when provided with a username and password (note, must be a password credential)
     * @param userId    The user's ID
     * @param password  The user's password
     * @return          A new auth token for the user
     */
    public AuthToken login(String userId, String password) {
        AuthToken token = null;
        User user = userMap.get(userId);
        if (user == null) return null;

        if (user.checkPassword(password)) {
            token = user.getNewAuthToken();
            tokenMap.put(token.getId(), token);
        }
        return token;
    }

    /**
     * Logs the user out. Supports any credential type of enum CredentialType
     * @param credential        The credential of the user trying to logout
     * @param credentialType    The type of credential being supplied
     */
    public void logout(String credential, CredentialType credentialType) {
        try {
            // Try to recognize the user and invalidate their auth token
            AuthToken token = authenticateUser(credential, credentialType);
            token.setState(false);
        } catch (AuthenticationException e) {
            // Failed to authenticate
            System.out.println(e.getMessage());
        }
    }

    /**
     * Checks that the user associated with the given auth token has the privileges to access the requested resource.
     * @param token         The requesting user's auth token
     * @param permission    The permission the resource requires
     * @param resource      The resource being accessed
     * @throws AccessDeniedException
     */
    public void checkAccess(String token, String permission, String resource) throws AccessDeniedException {
        AuthToken authToken = tokenMap.get(token);
        Permission p = (Permission) privilegeMap.get(permission);
        Resource r = resourceMap.get(resource);
        if (authToken == null || p == null || r == null)
            throw new AccessDeniedException("Access denied.");

        if (!authToken.isValid())
            throw new AccessDeniedException("Invalid token.");

        Visitor v = CheckAccessVisitor.createVisitor(authToken, p, r);
        this.acceptVisitor(v);

        if (!v.getUserHasAccess()) {
            throw new AccessDeniedException("Access denied.");
        }
    }

    /**
     * This overloaded checkAccess method is used for defining resources that are not tied to other resources and must
     * be bootstrapped by the system.
     * @param authToken     The auth token of the requester
     * @param permRequired  The permission required to access the resource
     * @throws AccessDeniedException
     */
    public void checkAccess(String authToken, String permRequired) throws AccessDeniedException {
        AuthToken token = tokenMap.get(authToken);

        if (token == null)
            throw new AccessDeniedException("Access denied.");
        if (!token.isValid())
            throw new AccessDeniedException("Invalid token.");

        User user = token.getUser();
        List<Privilege> privileges = user.getPrivileges();

        boolean hasRequiredPrivilege = false;
        for (Privilege p : privileges) {
            if (p.getId().equals(permRequired)) {
                hasRequiredPrivilege = true;
                break;
            }
        }

        if (!hasRequiredPrivilege) {
            throw new AccessDeniedException("Access denied.");
        }
    }

    /**
     * Displays details about all the objects in the AuthenticationService object graph using the Visitor Pattern
     */
    public void displayInventory() {
        Visitor v = TakeInventoryVisitor.createVisitor();
        this.acceptVisitor(v);
    }

    /**
     * Gets the map of all Users
     * @return  Map<String, User>
     */
    public Map<String, User> getUserMap() {
        return userMap;
    }

    /**
     * Gets the map of all Privileges
     * @return  Map<String, Privilege>
     */
    public Map<String, Privilege> getPrivilegeMap() {
        return privilegeMap;
    }

    /**
     * Gets the map of all Resources
     * @return  Map<String, Resource>
     */
    public Map<String, Resource> getResourceMap() {
        return resourceMap;
    }

    /**
     * Returns the map of all ResourceRoles
     * @return  Map<String, ResourceRole>
     */
    public Map<String, ResourceRole> getResourceRoleMap() {
        return resourceRoleMap;
    }

    /**
     * Returns the map of all AuthTokens
     * @return  Map<String, AuthToken>
     */
    public Map<String, AuthToken> getTokenMap() {
        return tokenMap;
    }

    /**
     * Attempts to match a user to the given credential. The credential must be unique, such as a biometric.
     * Generates a new token for the user if they do not already have a valid token.
     * @param credential        The credential provided
     * @param credentialType    The type of credential provided
     * @return                  Returns the AuthToken of the user if authentication was successful
     * @throws AuthenticationException
     */
    public AuthToken authenticateUser(String credential, CredentialType credentialType) throws AuthenticationException {
        AuthToken token = null;
        for (Map.Entry<String, User> entry : userMap.entrySet()) {
            User user = entry.getValue();
            if (credentialType == CredentialType.VOICE_PRINT && user.checkVoicePrint(credential)) {
                token = user.getAuthToken();
                if (token == null || !token.isValid()) token = user.getNewAuthToken();
                break;
            } else if (credentialType == CredentialType.FACE_PRINT && user.checkFacePrint(credential)) {
                token = user.getAuthToken();
                if (token == null || !token.isValid()) token = user.getNewAuthToken();
                break;
            } else if (credentialType == CredentialType.PASSWORD && user.checkPassword(credential)) {
                token = user.getAuthToken();
                if (token == null || !token.isValid()) token = user.getNewAuthToken();
                break;
            }
        }
        if (token == null)
            throw new AuthenticationException("Could not authenticate user.");

        tokenMap.putIfAbsent(token.getId(), token);
        return token;
    }

    /**
     * Visits the given Visitor
     * @param v     The visiting Visitor
     */
    @Override
    public void acceptVisitor(Visitor v) {
        v.visitAuthService(this);
    }

}
