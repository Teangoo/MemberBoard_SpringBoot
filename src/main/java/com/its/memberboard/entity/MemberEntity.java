package com.its.memberboard.entity;

import com.its.memberboard.dto.MemberDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "member")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true , nullable = false)
    private String memberEmail;

    @Column(nullable = false)
    private String memberPassword;

    @Column(nullable = false)
    private String memberName;

    @Column(nullable = false)
    private String memberMobile;

    @Column
    private String memberProfileName;

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardEntity> boardEntityList = new ArrayList<>();

    @OneToMany(mappedBy = "memberEntity", cascade = CascadeType.PERSIST, orphanRemoval = false, fetch = FetchType.LAZY)
    private List<CommentEntity> commentEntityList = new ArrayList<>();

    public static MemberEntity toUpdateFileEntity(MemberDTO memberDTO, List<BoardEntity> boardRepositoryByMemberEntityId, List<CommentEntity> commentRepositoryByMemberEntityId) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberMobile(memberDTO.getMemberMobile());
        memberEntity.setMemberProfileName(memberDTO.getMemberProfileName());
        memberEntity.setBoardEntityList(boardRepositoryByMemberEntityId);
        memberEntity.setCommentEntityList(commentRepositoryByMemberEntityId);
        return memberEntity;
    }

    public static MemberEntity toUpdateNoFileEntity(MemberDTO memberDTO,MemberEntity memberEntity1, List<BoardEntity> boardRepositoryByMemberEntityId, List<CommentEntity> commentRepositoryByMemberEntityId) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setId(memberDTO.getId());
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberMobile(memberDTO.getMemberMobile());
        memberEntity.setMemberProfileName(memberEntity1.memberProfileName);
        memberEntity.setBoardEntityList(boardRepositoryByMemberEntityId);
        memberEntity.setCommentEntityList(commentRepositoryByMemberEntityId);
        return memberEntity;
    }

    @PreRemove
    private void preRemove() {
        boardEntityList.forEach(board -> board.setMemberEntity(null));
        commentEntityList.forEach(comment -> comment.setMemberEntity(null));
    }

    public static MemberEntity toSaveEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail());
        memberEntity.setMemberPassword(memberDTO.getMemberPassword());
        memberEntity.setMemberName(memberDTO.getMemberName());
        memberEntity.setMemberMobile(memberDTO.getMemberMobile());
        memberEntity.setMemberProfileName(memberDTO.getMemberProfileName());
        return memberEntity;
    }
}
