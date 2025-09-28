package com.limhs.movie_project.service;

import com.limhs.movie_project.domain.User;
import com.limhs.movie_project.exception.DuplicatedUserId;
import com.limhs.movie_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public User save(User user) throws DuplicatedUserId {
        String password = user.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);

        Optional<User> findUser = repository.findByUserId(user.getUserId());

        if(findUser.isPresent()){
            throw new DuplicatedUserId("사용자 id가 중복됩니다. 다른 id로 시도해주세요");
        }
        repository.save(user);
        return user;
    }
}
