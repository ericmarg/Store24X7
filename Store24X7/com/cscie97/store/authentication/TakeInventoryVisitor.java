package com.cscie97.store.authentication;

import java.util.Map;

/**
 * Prints details of all the users, tokens, privileges, and resources associated with the AuthenticationService
 * to stdout
 */
public class TakeInventoryVisitor implements Visitor {
    public TakeInventoryVisitor() {
    }

    /**
     * Displays details of the User object
     * @param u The User being visited
     */
    @Override
    public void visitUser(User u) {
        System.out.println("--Displaying User object details:");
            System.out.println("\tID: " + u.getId());
            System.out.println("\tName: " + u.getName());
            if (u.getPrivileges().size() > 0) {
                for (Privilege p : u.getPrivileges()) {
                    System.out.println("\tPrivilege: " + p.getName());
                }
            }
    }
    /**
     * Displays details of the AuthToken object
     * @param t The AuthToken being visited
     */
    @Override
    public void visitAuthToken(AuthToken t) {
        System.out.println("--Displaying AuthToken object details:");
        System.out.println("\tID: " + t.getId());
        System.out.println("\tName: " + t.getExpiration());
        System.out.println("\tDescription: " + t.getUser());
    }
    /**
     * Displays details of the Resource object
     * @param r The Resource being visited
     */
    @Override
    public void visitResource(Resource r) {
        System.out.println("--Displaying Resource object details:");
        System.out.println("\tID: " + r.getId());
        System.out.println("\tName: " + r.getDescription());
    }

    /**
     * Gets all the object maps from the AuthenticationService
     * @param authService the AuthenticationService being visited
     */
    @Override
    public void visitAuthService(AuthenticationService authService) {
        visitMap(authService.getTokenMap());
        visitMap(authService.getUserMap());
        visitMap(authService.getResourceMap());
        visitMap(authService.getPrivilegeMap());
        visitMap(authService.getResourceRoleMap());
    }

    /**
     * Helper method that calls acceptVisitor() on all the objects in the provided map
     * @param map   The Map being perused
     */
    private <T extends Visitable> void visitMap(Map<String, T> map) {
        for (T item : map.values()) {
            item.acceptVisitor(this);
        }
    }

    /**
     * Displays details of the Privilege object
     * @param privilege The Privilege being visited
     */
    @Override
    public void visitPrivilege(Privilege privilege) {
        System.out.println("--Displaying Privilege object details:");
        System.out.println("\tID: " + privilege.getId());
        System.out.println("\tName: " + privilege.getName());
        System.out.println("\tDescription: " + privilege.getDescription());
    }

    /**
     * Creates a new instance of the TakeInventoryVisitor and returns it
     * @return  A new TakeInventoryVisitor object
     */
    public static Visitor createVisitor() {
        return new TakeInventoryVisitor();
    }

    @Override
    public boolean getUserHasAccess() {
        return false;
    }

    @Override
    public void visitRole(Role r) {

    }

    @Override
    public void visitPermission(Permission p) {

    }
}
