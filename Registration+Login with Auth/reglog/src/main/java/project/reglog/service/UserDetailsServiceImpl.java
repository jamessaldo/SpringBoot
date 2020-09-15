package project.reglog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import project.reglog.model.Users;
import project.reglog.repository.UserRepo;

import org.springframework.security.core.userdetails.*;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
	UserRepo userRepository;
	@Override
	public MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Users user = userRepository.findByUsername(username);
		
		if (user!=null) {
			return new MyUserDetails(user);
		} else {
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
}