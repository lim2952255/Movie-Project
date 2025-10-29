package com.limhs.movie_project.config.springSecurity;

import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User savedUser = userRepository.findByUserIdForRead(username).orElse(null);

        if(savedUser == null){
            throw new UsernameNotFoundException(username + " not found"); //사용자를 찾을 수 없는 경우 예외 던지기
        }

        return new CustomUserDetails(savedUser);
    }
}
