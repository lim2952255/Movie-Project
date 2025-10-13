package com.limhs.movie_project.service.user;

import com.limhs.movie_project.domain.LoginDTO;
import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.exception.DuplicatedUserId;
import com.limhs.movie_project.exception.LoginFailException;
import com.limhs.movie_project.repository.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${tmdb.image.base.url}")
    private String tmdbImageBaseUrl;

    public User save(User user) throws DuplicatedUserId {
        String password = user.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);

        Optional<User> findUser = userRepository.findByUserId(user.getUserId());

        if(findUser.isPresent()){
            throw new DuplicatedUserId("사용자 id가 중복됩니다. 다른 id로 시도해주세요");
        }

        userRepository.save(user);
        return user;
    }

    public User login(LoginDTO loginDTO) throws LoginFailException {
        Optional<User> findUser = userRepository.findByUserId(loginDTO.getUserId());

        if(findUser.isEmpty()){
            throw new LoginFailException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        boolean matches = passwordEncoder.matches(loginDTO.getPassword(), findUser.get().getPassword());

        if(!matches){
            throw new LoginFailException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return findUser.get();
    }

    public User getUser(HttpSession session){
        Object findUser = session.getAttribute("user");
        if(findUser != null){
            User getUser = (User) findUser;
            User user = userRepository.findByUserId(getUser.getUserId()).get();
            return user;
        }
        throw new RuntimeException();
    }
}
