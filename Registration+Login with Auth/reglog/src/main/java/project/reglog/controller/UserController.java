package project.reglog.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import project.reglog.config.token.JwtTokenUtil;
import project.reglog.model.*;
import project.reglog.repository.*;
import project.reglog.service.*;

import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class UserController {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	UserRepo userRepository;
	@Autowired
	RoleRepo roleRepository;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	// authenticate username and password to login and getting a token
	public ResponseEntity<?> createAuthenticationToken(@Valid @RequestParam("username") String uname, @RequestParam("password") String pass) throws Exception {
		authenticate(uname, pass);
		final UserDetails userDetails = userDetailsService.loadUserByUsername(uname);
		final String token = jwtTokenUtil.generateToken(userDetails);
		String role = userDetails.getAuthorities().toString();
		Users user = userRepository.findByUsername(uname);
		Map<Object, Object> model = new HashMap<>();
			model.put("id", user.getId());
			model.put("username", uname);
			model.put("email", user.getEmail());
			model.put("token", token);
			model.put("role", role.substring(1, role.length()-1));
			model.put("status", 200);
		return ResponseEntity.ok(model);
	}
	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	// registration an user
	public ResponseEntity<?> registUser(@Valid @RequestParam("username") String uname, 
									@RequestParam("password") String pass,
									@RequestParam("email") String email, 
									@RequestParam("NIK") String NIK,
									@RequestParam("fullname") String fullname,
									@RequestParam("birth_date") @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate birth_date,
									@RequestParam("address") String address,
									@RequestParam("phone_number") String phone_number,
									@RequestParam("foto_diri") MultipartFile foto_diri) {
		Users user = userRepository.findByUsername(uname);
		Users mail = userRepository.findByEmail(email);
		if(user!=null){
			return new ResponseEntity<String>("Username has been used!",HttpStatus.BAD_REQUEST);
		}else if(mail!=null){
			return new ResponseEntity<String>("Email has been used!",HttpStatus.BAD_REQUEST);
		}
		LocalDateTime date = LocalDateTime.now();
		Roles role = roleRepository.getByName("USER");
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		if (email.matches(regex)){
			Users new_user = new Users(uname, email, passwordEncoder.encode(pass), address, date, role);
			userRepository.save(new_user);
			return new ResponseEntity<Users>(new_user,HttpStatus.CREATED);
		}else{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}