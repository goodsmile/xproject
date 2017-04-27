package com.certusnet.xproject.common.web.shiro.authz;

import java.util.Set;

import org.apache.shiro.authz.SimpleAuthorizationInfo;

public class CustomAuthorizationInfo<T> extends SimpleAuthorizationInfo {

	private static final long serialVersionUID = 1L;

	private Set<T> resources;

	public Set<T> getResources() {
		return resources;
	}

	public void setResources(Set<T> resources) {
		this.resources = resources;
	}
	
}
