package com.its.memberboard.controller;

import com.its.memberboard.dto.MemberDTO;
import com.its.memberboard.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/save-form")
    public String saveForm(){
        return "/memberPages/save";
    }
    @PostMapping("/duplicate-check")
    public @ResponseBody String duplicateCheck(@RequestParam("memberEmail") String memberEmail){
        String emailCheck = memberService.findByEmail(memberEmail);
        return emailCheck;
    }
    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO) throws IOException {
        Long saveId = memberService.save(memberDTO);
        if(saveId>0){
            return "/memberPages/login";
        }else{
            return "/save-fail";
        }
    }
    @GetMapping("/login-form")
    public String loginForm(){
        return "/memberPages/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session, Model model){
        MemberDTO loginMember = memberService.login(memberDTO);
        if(loginMember != null){
            session.setAttribute("loginId",loginMember.getId());
            session.setAttribute("loginEmail",loginMember.getMemberEmail());
            return "redirect:/board/list";
        }else{
            model.addAttribute("loginFail","아이디 또는 비밀번호가 틀립니다.");
            return "/memberPages/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/delete")
    public ResponseEntity delete(@RequestParam("id") Long id) {
        memberService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/mypage")
    public String mypage(Model model , HttpSession session){
        MemberDTO memberDTO = memberService.findById((Long) session.getAttribute("loginId"));
        model.addAttribute("member",memberDTO);
        return "/memberPages/mypage";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute MemberDTO memberDTO) throws IOException {
        memberService.update(memberDTO);
        return "redirect:/member/mypage";
    }
}
