package com.sam.twitchclone.dao.postgres.repository;

import com.sam.twitchclone.dao.postgres.model.Video;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VideoRepository extends JpaRepository<Video, UUID> {

    List<Video> findVideoByUserId(UUID UserId, Sort sort);
    List<Video> findVideoByTitle(String title, Sort sort);

    @Query("SELECT v FROM Video v WHERE LOWER(v.title) LIKE CONCAT('%', LOWER(:term), '%') ORDER BY v.createdAt DESC")
    List<Video> findVideoByTitleContaining(@Param("term") String term);

    @Query("SELECT v FROM Video v WHERE LOWER(v.title) LIKE CONCAT('%', LOWER(:term), '%') OR LOWER(v.title) LIKE CONCAT('%', REPLACE(LOWER(:term), ' ', '%'), '%') ORDER BY v.createdAt DESC")
    List<Video> findVideoByTitleContainingAnyWord(@Param("term") String term);

    List<Video> findByTitleContainingIgnoreCase(String term);
}

