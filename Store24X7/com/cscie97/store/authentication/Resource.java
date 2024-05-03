package com.cscie97.store.authentication;

public class Resource implements Visitable {

	private final String id;

	private final String description;

	public Resource(String id, String description) {
		this.id = id;
		this.description = description;
	}


	@Override
	public void acceptVisitor(Visitor v) {
		v.visitResource(this);
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}
}
