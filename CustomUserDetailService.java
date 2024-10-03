package Smartcontactmanager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import Smartcontactmanager.Entities.User;
import Smartcontactmanager.dao.UserRepository;

public class CustomUserDetailService implements UserDetailsService {
@Autowired
UserRepository userrepo; 
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userrepo.getUserByUserName(username);
		if(user==null) {
			throw new UsernameNotFoundException("user not found");}
			
		return new CustomeUser(user);
		
		
	}

}
 