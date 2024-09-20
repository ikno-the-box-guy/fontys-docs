package me.ikno.userapi.repositories;

import me.ikno.userapi.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Integer>{
    @Query(value = "SELECT `user_id`, `display_name`, `email`, `password`, `root_directory_id` FROM user u WHERE u.email = ?1", nativeQuery = true)
    Optional<UserModel> findByEmail(String email);
}
