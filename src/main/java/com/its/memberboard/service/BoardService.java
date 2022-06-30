package com.its.memberboard.service;

import com.its.memberboard.dto.BoardDTO;
import com.its.memberboard.entity.BoardEntity;
import com.its.memberboard.entity.CommentEntity;
import com.its.memberboard.entity.MemberEntity;
import com.its.memberboard.repository.BoardRepository;
import com.its.memberboard.repository.CommentRepository;
import com.its.memberboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public Page<BoardDTO> findByAll(Pageable pageable) {
        Page<BoardEntity> boardEntityPage = boardRepository.findAll(pageable);
        Page<BoardDTO> boardDTOPage = boardEntityPage.map(
                board -> new BoardDTO(
                        board.getId(),
                        board.getBoardTitle(),
                        board.getBoardWriter(),
                        board.getBoardContents(),
                        board.getBoardHits(),
                        board.getCreatedTime(),
                        board.getBoardFileName()
                )
        );
        return boardDTOPage;

    }

    public Long save(BoardDTO boardDTO) throws IOException {
        MultipartFile boardFile = boardDTO.getBoardFile();
        String boardFileOriginalFilename = boardFile.getOriginalFilename();
        boardFileOriginalFilename = System.currentTimeMillis() + "_" + boardFileOriginalFilename;
        String savePath = "/Users/taeyeonlee/developer/spring_boot/board_img/" + boardFileOriginalFilename;
        if (!boardFile.isEmpty()) {
            boardFile.transferTo(new File(savePath));
        }
        boardDTO.setBoardFileName(boardFileOriginalFilename);
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(boardDTO.getBoardWriter());
        if (optionalMemberEntity.isPresent()) {
            MemberEntity memberEntity = optionalMemberEntity.get();
            BoardEntity boardEntity = BoardEntity.toEntity(boardDTO, memberEntity);
            Long saveId = boardRepository.save(boardEntity).getId();
            return saveId;
        } else {
            return null;
        }
    }

    @Transactional
    public BoardDTO findById(Long id) {
        boardRepository.updateCount(id);
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toDTO(boardEntity);
            return boardDTO;
        }
        return null;
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Long update(BoardDTO boardDTO) throws IOException {
        MultipartFile boardFile = boardDTO.getBoardFile();
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(boardDTO.getId());
        BoardEntity boardEntity1 = optionalBoardEntity.get();
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(boardDTO.getBoardWriter());
        MemberEntity memberEntity = optionalMemberEntity.get();
        List<CommentEntity> commentEntityList = commentRepository.findByBoardEntity_Id(boardDTO.getId());
        if (!boardFile.isEmpty()) {
            String boardFileOriginalFilename = boardFile.getOriginalFilename();
            boardFileOriginalFilename = System.currentTimeMillis() + "_" + boardFileOriginalFilename;
            String savePath = "/Users/taeyeonlee/developer/spring_boot/board_img/" + boardFileOriginalFilename;
            boardFile.transferTo(new File(savePath));
            boardDTO.setBoardFileName(boardFileOriginalFilename);
            BoardEntity boardEntity = BoardEntity.toUpdateFileEntity(boardDTO, boardEntity1,memberEntity,commentEntityList);
            Long saveId = boardRepository.save(boardEntity).getId();
            return saveId;
        } else {
            BoardEntity boardEntity = BoardEntity.toUpdateNoFileEntity(boardDTO, boardEntity1, memberEntity,commentEntityList);
            Long saveId = boardRepository.save(boardEntity).getId();
            return saveId;

        }
    }
    @Transactional
    public Page<BoardDTO> search(String q, String type,Pageable pageable) {
        if (type.equals("")) {
            var q1 = q;
            Page<BoardEntity> boardEntityList = boardRepository.findByBoardTitleContainingOrBoardWriterContaining(q,q1,pageable);
            Page<BoardDTO> boardDTOPage = boardEntityList.map(
                    board -> new BoardDTO(
                            board.getId(),
                            board.getBoardTitle(),
                            board.getBoardWriter(),
                            board.getBoardContents(),
                            board.getBoardHits(),
                            board.getCreatedTime(),
                            board.getBoardFileName()
                    )
            );
            return boardDTOPage;
        } else if (type.equals("boardTitle")) {
            Page<BoardEntity> boardEntityList = boardRepository.findByBoardTitleContaining(q,pageable);
            Page<BoardDTO> boardDTOPage = boardEntityList.map(
                    board -> new BoardDTO(
                            board.getId(),
                            board.getBoardTitle(),
                            board.getBoardWriter(),
                            board.getBoardContents(),
                            board.getBoardHits(),
                            board.getCreatedTime(),
                            board.getBoardFileName()
                    )
            );
            return boardDTOPage;
        } else if (type.equals("boardWriter")) {
            Page<BoardEntity> boardEntityList = boardRepository.findByBoardWriterContaining(q,pageable);
            Page<BoardDTO> boardDTOPage = boardEntityList.map(
                    board -> new BoardDTO(
                            board.getId(),
                            board.getBoardTitle(),
                            board.getBoardWriter(),
                            board.getBoardContents(),
                            board.getBoardHits(),
                            board.getCreatedTime(),
                            board.getBoardFileName()
                    )
            );
            return boardDTOPage;
        }
        return null;
    }

}
