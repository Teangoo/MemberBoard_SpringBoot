package com.its.memberboard.entity;

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

}
