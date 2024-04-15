package com.blog.blogger.controller;

import com.blog.blogger.entity.Role;
import com.blog.blogger.entity.User;
import com.blog.blogger.payload.JWTAuthResponse;
import com.blog.blogger.payload.LoginDto;
import com.blog.blogger.payload.SignUpDto;
import com.blog.blogger.repository.UserRepository;
import com.blog.blogger.repository.roleRepository;
import com.blog.blogger.security.JwtTokenProvider;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.remote.JMXAuthenticator;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private roleRepository roleRepository;

    @Autowired
    private JwtTokenProvider tokenProvider;


    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){
         UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),loginDto.getPassword());


        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);


        //get token from tokenProvider
        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponse(token));



    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){

        if(userRepository.existsByEmail(signUpDto.getEmail())){
            return new ResponseEntity<>("Email id exists :"+signUpDto.getEmail(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(userRepository.existsByUsername(signUpDto.getUsername())){
            return new ResponseEntity<>("Username exists :"+signUpDto.getUsername(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        User user = new User();
        user.setName(signUpDto.getName());
        user.setEmail(signUpDto.getEmail());
        user.setUsername(signUpDto.getUsername());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        Role roles = roleRepository.findByName("ROLE_ADMIN").get();
        user.setRole(Collections.singleton(roles));  //used to convert the roles to set.

        User savedUser = userRepository.save(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }
}
