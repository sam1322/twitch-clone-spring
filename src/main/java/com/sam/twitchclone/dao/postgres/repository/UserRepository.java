package com.sam.twitchclone.dao.postgres.repository;

import com.sam.twitchclone.dao.postgres.model.user.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByEmail(String email);
    @NonNull Optional<User> findById(@NonNull Integer id);
}
