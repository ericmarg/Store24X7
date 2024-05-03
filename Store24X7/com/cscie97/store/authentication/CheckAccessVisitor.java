package com.cscie97.store.authentication;

import java.util.Map;

/**
 * The CheckAccessVisitor traverses the object graph to check if the user associated with the given token has the
 * permission required to access the given resource. Returns right away if the permission is found.
 */
public class CheckAccessVisitor implements Visitor {

    private final AuthToken token;
    private final Permission permission;
    private final Resource resource;
    private boolean userHasAccess;

    public CheckAccessVisitor(AuthToken token, Permission p, Resource r) {
        this.token = token;
        this.permission = p;
        this.resource = r;
        userHasAccess = false;
    }

    /**
     * Create a new CheckAccessVisitor instance
     * @param token The authToken of the user whose access is being checked
     * @param p     The privilege the resource requires
     * @param r     The resource being accessed
     * @return      Visitor object instance
     */
    public static Visitor createVisitor(AuthToken token, Permission p, Resource r) {
        return new CheckAccessVisitor(token, p, r);
    }

    /**
     * Calls acceptVisitor on all the user's privileges
     * @param u The User being visited
     */
    @Override
    public void visitUser(User u) {
        for (Privilege p : u.getPrivileges()) {
            p.acceptVisitor(this);
            if (this.userHasAccess) break;
        }
    }

    /**
     * Calls acceptVisitor on the User associated with the authtoken
     * @param t The AuthToken being visited
     */
    @Override
    public void visitAuthToken(AuthToken t) {
        if (this.token == t) {
            User user = t.getUser();
            user.acceptVisitor(this);
        }
    }

    @Override
    public void visitResource(Resource r) {

    }

    @Override
    public void visitRole(Role r) {

    }

    @Override
    public void visitPermission(Permission p) {

    }

    /**
     * Visits all the AuthToken objects in the AuthenticationService's token map
     * @param authService   The authService being visited
     */
    @Override
    public void visitAuthService(AuthenticationService authService) {
        Map<String, AuthToken> tokens = authService.getTokenMap();
        for (Map.Entry<String, AuthToken> entry : tokens.entrySet()) {
            AuthToken t = entry.getValue();
            t.acceptVisitor(this);
            if (this.userHasAccess) break;
        }
    }

    /**
     * Checks if the privilege being visited is a ResourceRole. If it is, checks to see whether the permissions
     * associated with it and the resource tied to it match the ones the CheckAccessVisitor is looking for.
     * If both are a match, access is confirmed and the visitor returns.
     * @param privilege     The privilege being visited
     */
    @Override
    public void visitPrivilege(Privilege privilege) {
        if (privilege instanceof ResourceRole) {
            boolean resourcesMatch = false;
            boolean hasPermission = false;

            // Check that the resources is a match
            if (privilege.getResource() == this.resource) {
                resourcesMatch = true;
            }

            // Check that the necessary permission is present
            if (privilege.getChildren().contains(permission)) {
                hasPermission = true;
            }

            if (resourcesMatch && hasPermission) {
                userHasAccess = true;
                return;
            }
        }

        if (privilege.getChildren() != null) {
            // Traverse any child nodes in the structure
            for (Privilege p : privilege.getChildren()) {
                p.acceptVisitor(this);
            }
        }
    }

    /**
     * Flag to let the visitor stop traversing the object graph once access has been found
     * @return  True if it has been determined that the user does have access, else false
     */
    @Override
    public boolean getUserHasAccess() {
        return this.userHasAccess;
    }

}
