package com.bank.debt;

import org.springframework.security.core.GrantedAuthority;

public class GrantedAuthorityImpl implements GrantedAuthority {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String authority;
	
	public GrantedAuthorityImpl(String authority){
		this.authority = authority;
	}
	
	
	@Override
	public String getAuthority() {
		return authority;
	}

}
