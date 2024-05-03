package com.cscie97.store.authentication;

public class ResourceRole extends Role {
	private Resource resource;

	public ResourceRole(String id, String name, String description, Resource resource) {
		super(id, name, description);
		this.resource = resource;
	}

	@Override
	public Resource getResource() {
		return resource;
	}
}
