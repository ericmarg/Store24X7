package com.cscie97.store.authentication;

/**
 * The Permission represents the leaf node in the Privilege tree structure. Permissions can be applied to Users or to
 * Roles.
 */
public class Permission extends Privilege {

	public Permission(String id, String name, String description) {
		super(id, name, description);
	}

}
