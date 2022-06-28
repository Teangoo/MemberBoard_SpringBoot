package com.its.memberboard.service;

import com.its.memberboard.dto.MemberDTO;
import com.its.memberboard.entity.MemberEntity;
import com.its.memberboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class MemberService {
    private final MemberRepository memberRepository;

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
        //toSaveEntity 메서드에 회원 엔티티를 같이 전달해야함.(로그인 이메일이 작성자와 동일하다는 전제조건)

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
}
