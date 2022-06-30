package com.its.memberboard.service;

import com.its.memberboard.dto.CommentDTO;
import com.its.memberboard.entity.BoardEntity;
import com.its.memberboard.entity.CommentEntity;
import com.its.memberboard.entity.MemberEntity;
import com.its.memberboard.repository.BoardRepository;
import com.its.memberboard.repository.CommentRepository;
import com.its.memberboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    public void save(CommentDTO commentDTO) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(commentDTO.getCommentWriter());
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());
        if (optionalMemberEntity.isPresent()) {
            if (optionalBoardEntity.isPresent()){
                MemberEntity memberEntity = optionalMemberEntity.get();
                BoardEntity boardEntity = optionalBoardEntity.get();
                CommentEntity commentEntity = CommentEntity.toEntity(commentDTO,memberEntity,boardEntity);
                commentRepository.save(commentEntity);
            }

        }
    }

    public List<CommentDTO> findById(Long id) {
        List<CommentEntity> commentEntityList = commentRepository.findByBoardEntity_Id(id);
        if(commentEntityList != null || !commentEntityList.isEmpty()){
            List<CommentDTO> commentDTOList = new ArrayList<>();
            for(CommentEntity comment:commentEntityList){
                commentDTOList.add(CommentDTO.toDTO(comment));
            }
            return commentDTOList;
        }else{
            return null;
        }
    }

    public void delete(Long id) {
        commentRepository.deleteById(id);
    }
}
