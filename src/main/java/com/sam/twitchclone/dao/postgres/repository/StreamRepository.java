package com.sam.twitchclone.dao.postgres.repository;

import com.sam.twitchclone.constant.enums.StreamStatus;
import com.sam.twitchclone.dao.postgres.model.Stream;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StreamRepository extends JpaRepository<Stream, UUID> {

    Optional<List<Stream>> findByUserIdAndStatus(UUID userId, StreamStatus status);

}
