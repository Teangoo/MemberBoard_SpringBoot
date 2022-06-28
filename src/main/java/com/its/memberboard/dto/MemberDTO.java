package com.its.memberboard.dto;

import com.its.memberboard.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private Long id;
    private String memberEmail;
    private String memberPassword;
    private String memberName;
    private String memberMobile;
    private MultipartFile memberProfile;
    private String memberProfileName;

    public static MemberDTO toDTO(MemberEntity loginEntity) {
    MemberDTO memberDTO = new MemberDTO();
    memberDTO.setId(loginEntity.getId());
    memberDTO.setMemberEmail(loginEntity.getMemberEmail());
    memberDTO.setMemberPassword(loginEntity.getMemberPassword());
    memberDTO.setMemberName(loginEntity.getMemberName());
    memberDTO.setMemberMobile(loginEntity.getMemberMobile());
    memberDTO.setMemberProfileName(loginEntity.getMemberProfileName());
    return memberDTO;
    }
}
