package br.inatel.thisismeapi.security;

import br.inatel.thisismeapi.entities.User;
import br.inatel.thisismeapi.repositories.UserRepository;
import br.inatel.thisismeapi.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        LOGGER.info("m=loadUserByUsername, username={}", username);

        UserUtils.verifyEmail(username);

        Optional<User> opUser = userRepository.findByEmail(username);

        if (opUser.isEmpty())
            throw new UsernameNotFoundException(username);

        return UserPrincipal.create(opUser.get());
    }
}
