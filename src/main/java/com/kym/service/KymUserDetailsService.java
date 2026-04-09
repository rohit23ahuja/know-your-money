package com.kym.service;

import com.kym.entity.KymUser;
import com.kym.repository.KymUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class KymUserDetailsService implements UserDetailsService {

    private final KymUserRepository kymUserRepository;

    public KymUserDetailsService(KymUserRepository kymUserRepository) {
        this.kymUserRepository = kymUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        KymUser kymUser = kymUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found." + username));
        return User.builder().username(kymUser.getUsername()).password(kymUser.getPassword()).roles(kymUser.getRole().name()).build();
    }
}
