package com.its.memberboard.service;

import com.its.memberboard.dto.MemberDTO;
import com.its.memberboard.entity.BoardEntity;
import com.its.memberboard.entity.CommentEntity;
import com.its.memberboard.entity.MemberEntity;
import com.its.memberboard.repository.BoardRepository;
import com.its.memberboard.repository.CommentRepository;
import com.its.memberboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class MemberService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public String findByEmail(String memberEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(memberEmail);
        if (optionalMemberEntity.isPresent()) {
            return "no";
        } else {
            return "ok";
        }
    }

    public Long save(MemberDTO memberDTO) throws IOException {
        MultipartFile memberProfile = memberDTO.getMemberProfile();
        String memberProfileOriginalFilename = memberProfile.getOriginalFilename();
        memberProfileOriginalFilename = System.currentTimeMillis() + "_" + memberProfileOriginalFilename;
        String savePath = "/Users/taeyeonlee/developer/spring_boot/member_img/" + memberProfileOriginalFilename;
        if (!memberProfile.isEmpty()) {
            memberProfile.transferTo(new File(savePath));
        }
        memberDTO.setMemberProfileName(memberProfileOriginalFilename);

        MemberEntity memberEntity = MemberEntity.toSaveEntity(memberDTO);
        Long saveId = memberRepository.save(memberEntity).getId();
        return saveId;

    }

    public MemberDTO login(MemberDTO memberDTO) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());
        if(optionalMemberEntity.isPresent()){
            MemberEntity loginEntity = optionalMemberEntity.get();
            if(loginEntity.getMemberPassword().equals(memberDTO.getMemberPassword())){
                MemberDTO loginDTO = MemberDTO.toDTO(loginEntity);
                return loginDTO;
            }else{
                return null;
            }
        }else{
            return null;
        }
    }
    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for(MemberEntity memberEntity:memberEntityList){
            memberDTOList.add(MemberDTO.toDTO(memberEntity));
        }
        return memberDTOList;
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

    public MemberDTO findById(Long loginId) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(loginId);
        if(optionalMemberEntity.isPresent()){
            MemberEntity memberEntity = optionalMemberEntity.get();
            MemberDTO memberDTO = MemberDTO.toDTO(memberEntity);
            return memberDTO;
        }else{
            return null;
        }
    }

    public void update(MemberDTO memberDTO) throws IOException {
        MultipartFile memberProfile = memberDTO.getMemberProfile();
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(memberDTO.getId());
        MemberEntity memberEntity1 = optionalMemberEntity.get();
        List<BoardEntity> boardRepositoryByMemberEntityId = boardRepository.findByMemberEntityId(memberDTO.getId());
        List<CommentEntity> commentRepositoryByMemberEntityId = commentRepository.findByMemberEntityId(memberDTO.getId());
        if (!memberProfile.isEmpty()) {
            String memberProfileOriginalFilename = memberProfile.getOriginalFilename();
            memberProfileOriginalFilename = System.currentTimeMillis() + "_" + memberProfileOriginalFilename;
            String savePath = "/Users/taeyeonlee/developer/spring_boot/member_img/" + memberProfileOriginalFilename;
            memberProfile.transferTo(new File(savePath));
            memberDTO.setMemberProfileName(memberProfileOriginalFilename);
            MemberEntity memberEntity = MemberEntity.toUpdateFileEntity(memberDTO,boardRepositoryByMemberEntityId,commentRepositoryByMemberEntityId);
            Long saveId = memberRepository.save(memberEntity).getId();
        }else {
            MemberEntity memberEntity = MemberEntity.toUpdateNoFileEntity(memberDTO,memberEntity1,boardRepositoryByMemberEntityId,commentRepositoryByMemberEntityId);
            Long saveId = memberRepository.save(memberEntity).getId();
        }
    }
}
