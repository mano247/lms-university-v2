package com.lmsuniversity.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lmsuniversity.user.RegisteredUser;
import com.lmsuniversity.user.RegisteredUserRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  RegisteredUserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    RegisteredUser user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("No such email address: " + email));

    return UserDetailsImpl.build(user);
  }

}
