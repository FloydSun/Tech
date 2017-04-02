package com.speed.template.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service(value="authenticationProvider")
public class AuthenticationProviderImpl extends AbstractUserDetailsAuthenticationProvider   {

	@Autowired
	UserDetailsService uds;
	
	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetail, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		System.out.println("additionalAuthenticationChecks");
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		System.out.println("retrieveUser");
		return (UserDetails) uds.loadUserByUsername(username);
	}



}
