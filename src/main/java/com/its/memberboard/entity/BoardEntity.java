package com.its.memberboard.entity;

import com.its.memberboard.dto.BoardDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "board")
public class BoardEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Column(nullable = false)
    private String boardTitle;

    @Column(nullable = false)
    private String boardWriter;

    @Column(nullable = false)
    private String boardContents;

    @Column
    @ColumnDefault("0")
    private int boardHits;

    @Column
    private String boardFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CommentEntity> commentEntityList = new ArrayList<>();

    @PreRemove
    private void preRemove() {
        commentEntityList.forEach(comment -> comment.setMemberEntity(null));
    }

    public static BoardEntity toEntity(BoardDTO boardDTO , MemberEntity memberEntity) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardWriter(memberEntity.getMemberEmail());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardFileName(boardDTO.getBoardFileName());
        boardEntity.setMemberEntity(memberEntity);
        return boardEntity;
    }

    public static BoardEntity toUpdateFileEntity(BoardDTO boardDTO, BoardEntity boardEntity1, MemberEntity memberEntity,List<CommentEntity> commentEntityList) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(boardDTO.getId());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardWriter(boardEntity1.getBoardWriter());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardFileName(boardDTO.getBoardFileName());
        boardEntity.setBoardHits(boardEntity1.getBoardHits());
        boardEntity.setMemberEntity(memberEntity);
        boardEntity.setCommentEntityList(commentEntityList);
        return boardEntity;
    }

    public static BoardEntity toUpdateNoFileEntity(BoardDTO boardDTO, BoardEntity boardEntity1, MemberEntity memberEntity, List<CommentEntity> commentEntityList) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(boardDTO.getId());
        boardEntity.setBoardWriter(boardEntity1.getBoardWriter());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardFileName(boardEntity1.getBoardFileName());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardWriter(boardEntity1.getBoardWriter());
        boardEntity.setBoardHits(boardEntity1.getBoardHits());
        boardEntity.setMemberEntity(memberEntity);
        boardEntity.setCommentEntityList(commentEntityList);
        return boardEntity;
    }
}
