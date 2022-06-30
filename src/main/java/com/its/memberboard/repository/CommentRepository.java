package com.its.memberboard.repository;

import com.its.memberboard.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {

   List<CommentEntity> findByBoardEntity_Id(Long id);
   List<CommentEntity> findByMemberEntityId(Long id);
}
