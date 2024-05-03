package com.cscie97.store.authentication;

import java.util.List;

/**
 * The abstract composite class that Roles and Permissions both inherit from.
 */
public abstract class Privilege implements Visitable {

    private final String id;

    private final String name;

    private final String description;

    public Privilege(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public void add(Privilege p) {

    }

    public void remove(Privilege p) {

    }

    public Privilege getChild(int child) {
        return null;
    }

    /**
     * Gets the ID of the privilege
     * @return  ID string
     */

    public String getId() {
        return id;
    }

    /**
     * Gets the name of the privilege
     * @return  Name string
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the description of the privilege
     * @return  Description string
     */
    public String getDescription() {
        return description;
    }

    public Resource getResource() {
        return null;
    }

    public List<Privilege> getChildren() {
        return null;
    }

    @Override
    public void acceptVisitor(Visitor v) {
        v.visitPrivilege(this);
    }
}
