package com.limhs.movie_project.service.user;

import com.limhs.movie_project.domain.LoginDTO;
import com.limhs.movie_project.domain.user.User;
import com.limhs.movie_project.exception.DuplicatedUserId;
import com.limhs.movie_project.exception.LoginFailException;
import com.limhs.movie_project.repository.user.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User save(User user) throws DuplicatedUserId {
        String password = user.getPassword();
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);

        Optional<User> findUser = userRepository.findByUserIdForRead(user.getUserId());

        if(findUser.isPresent()){
            throw new DuplicatedUserId("사용자 id가 중복됩니다. 다른 id로 시도해주세요");
        }

        userRepository.save(user);
        return user;
    }

    @Transactional(readOnly = true)
    public User login(LoginDTO loginDTO) throws LoginFailException {
        User user = userRepository.findByUserIdForRead(loginDTO.getUserId()).orElse(null);

        if(user == null){
            throw new LoginFailException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        boolean matches = passwordEncoder.matches(loginDTO.getPassword(), user.getPassword());

        if(!matches){
            throw new LoginFailException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    @Transactional(readOnly = true)
    public User getUserForRead(HttpSession session){
        Object findUser = session.getAttribute("user");
        if(findUser != null){
            User getUser = (User) findUser;
            User user = userRepository.findByUserIdForRead(getUser.getUserId()).get();
            return user;
        }
        throw new RuntimeException();
    }

    @Transactional
    public User getUserForUpdate(HttpSession session){
        Object findUser = session.getAttribute("user");
        if(findUser != null){
            User getUser = (User) findUser;
            User user = userRepository.findByUserIdForUpdate(getUser.getUserId()).get();
            return user;
        }
        throw new RuntimeException();
    }
}
