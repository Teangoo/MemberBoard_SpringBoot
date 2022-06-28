package com.its.memberboard.service;

import com.its.memberboard.dto.BoardDTO;
import com.its.memberboard.entity.BoardEntity;
import com.its.memberboard.entity.MemberEntity;
import com.its.memberboard.repository.BoardRepository;
import com.its.memberboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;


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
        if(!boardFile.isEmpty()){
            boardFile.transferTo(new File(savePath));
        }
        boardDTO.setBoardFileName(boardFileOriginalFilename);
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(boardDTO.getBoardWriter());
        if(optionalMemberEntity.isPresent()){
            MemberEntity memberEntity = optionalMemberEntity.get();
            BoardEntity boardEntity = BoardEntity.toEntity(boardDTO, memberEntity);
            Long saveId = boardRepository.save(boardEntity).getId();
            return saveId;
        }else{
            return null;
        }
    }
}
