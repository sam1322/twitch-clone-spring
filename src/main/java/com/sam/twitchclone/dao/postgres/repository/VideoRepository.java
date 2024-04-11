package com.sam.twitchclone.dao.postgres.repository;

import com.sam.twitchclone.dao.postgres.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {


}

