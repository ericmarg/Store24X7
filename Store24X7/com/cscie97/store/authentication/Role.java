package com.cscie97.store.authentication;

import java.util.ArrayList;
import java.util.List;

/**
 * The Role is the composite member of the Privilege structure, acting as a parent or child node in the tree.
 * Provides a way to group Permissions and apply them to a User as a composite.
 */
public class Role extends Privilege {
	private List<Privilege> children;

	public Role(String id, String name, String description) {
		super(id, name, description);
		this.children = new ArrayList<>();
	}

	/**
	 * Adds the given Privilege to the list of the role's children
	 * @param p		The child Privilege
	 */
    @Override
	public void add(Privilege p) {
		this.children.add(p);
	}

	/**
	 * Removes the Privilege from the role's children
	 * @param p		The child Privilege
	 */
	@Override
	public void remove(Privilege p) {
		this.children.remove(p);
	}

	/**
	 * Gets the list of the Role's children
	 * @return	List<Privilege> of child nodes
	 */
	@Override
	public List<Privilege> getChildren() {
		return children;
	}
}
