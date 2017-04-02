package com.bank.debt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service(value="userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		
		List<GrantedAuthority> gas = new ArrayList<GrantedAuthority>();
		gas.add(new GrantedAuthorityImpl("ROLE_USER"));
		gas.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
		gas.add(new GrantedAuthorityImpl("ROLE_SERVICE"));
		
		return new User("test", "test", gas);
	}

}
