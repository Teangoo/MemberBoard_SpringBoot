package com.its.memberboard.entity;

import com.its.memberboard.dto.BoardDTO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Entity
@Getter
@Setter
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

    public static BoardEntity toEntity(BoardDTO boardDTO , MemberEntity memberEntity) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardWriter(memberEntity.getMemberEmail());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
//        boardEntity.setBoardHits(0);
        boardEntity.setBoardFileName(boardDTO.getBoardFileName());
        boardEntity.setMemberEntity(memberEntity);
        return boardEntity;
    }
}
