package com.sam.twitchclone.dao.postgres.repository;

import com.sam.twitchclone.dao.postgres.model.Stream;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StreamRepository extends JpaRepository<Stream, UUID> {

//    Optional<List<Stream>> findByUserIdAndStatus(UUID userId, StreamStatus status);

    List<Stream> findByUserIdAndLiveOrExpired(UUID userId, Boolean live, Boolean expired, Sort sort);
    List<Stream> findByUserIdAndLiveOrUserIdAndExpired(UUID userId, Boolean live,UUID userId1 , Boolean expired, Sort sort);

    Optional<Stream> findFirstByUserIdAndLiveOrderByCreatedAtDesc(UUID userId, Boolean live);

    Optional<Stream> findFirstByUserIdOrderByCreatedAtDesc(UUID userId);

    Optional<Stream> findByIdAndExpiredIsFalse(UUID userId);

}

