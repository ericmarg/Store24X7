package com.cscie97.store.authentication;

/**
 * Interface to provide Visitor Pattern functionality
 * Visits any objects that call it
 */
public interface Visitor {
    public abstract void visitUser(User u);

    public abstract void visitAuthToken(AuthToken t);

    public abstract void visitResource(Resource r);

    public abstract void visitRole(Role r);

    public abstract void visitPermission(Permission p);

    public abstract void visitAuthService(AuthenticationService authService);

    public abstract void visitPrivilege(Privilege privilege);

    public abstract boolean getUserHasAccess();

    public static Visitor createVisitor() {
        return null;
    }
}
