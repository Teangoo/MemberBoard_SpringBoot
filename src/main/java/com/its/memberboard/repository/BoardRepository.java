package com.its.memberboard.repository;

import com.its.memberboard.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface BoardRepository extends JpaRepository<BoardEntity,Long> {
    @Modifying
    @Transactional
    @Query("update BoardEntity p set p.boardHits = p.boardHits + 1 where p.id =:id")
    void updateCount(Long id);

    Page<BoardEntity> findByBoardTitleContainingOrBoardWriterContaining(String q, String q1 , Pageable pageable);
    Page<BoardEntity> findByBoardTitleContaining(String q, Pageable pageable);
    Page<BoardEntity> findByBoardWriterContaining(String q, Pageable pageable);

}
