package com.sam.twitchclone.dao.postgres.repository;

import com.sam.twitchclone.dao.postgres.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("""
        Select t from Token  t inner join User u
        on t.user.id = u.id
        where t.user.id = :userId and t.loggedOut = false
        order by t.createdTime desc
""")
    List<Token> findAllValidTokenByUser(UUID userId);
//    List<Token> findAllValidTokenByUser(Integer userId);

    Optional<Token> findByToken(String token);
}
