package com.limhs.movie_project.repository.user;

import com.limhs.movie_project.domain.user.User;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @QueryHints({
            @QueryHint(name = "org.hibernate.readOnly",value = "true"),
            @QueryHint(name = "org.hibernate.cacheable", value = "true")})
    @Query("select u from User u where u.userId = :userId")
    Optional<User> findByUserIdForRead(@Param("userId")String userId);

    @Query("select u from User u where u.userId = :userId")
    Optional<User> findByUserIdForUpdate(String userId);
}
