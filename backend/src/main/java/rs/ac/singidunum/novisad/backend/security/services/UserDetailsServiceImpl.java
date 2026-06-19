package rs.ac.singidunum.novisad.backend.security.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.singidunum.novisad.backend.model.user.RegisteredUser;
import rs.ac.singidunum.novisad.backend.repository.RegistrovaniKorisnikRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  RegistrovaniKorisnikRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    RegisteredUser user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UsernameNotFoundException("Ne postojeca mejl address: " + email));

    return UserDetailsImpl.build(user);
  }

}
