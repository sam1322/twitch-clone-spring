package com.sam.twitchclone.dao.postgres.repository;

import com.sam.twitchclone.dao.postgres.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    //    @NonNull Optional<User> findById(@NonNull Integer id);
    Optional<User> findById(UUID uuid);

    Optional<User> findByFullNameIgnoreCase(String fullName);

}
