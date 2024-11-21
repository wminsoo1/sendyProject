package com.example.controller.member;

import static org.springframework.http.HttpStatus.*;

import com.example.controller.aop.ValidateUser;
import com.example.global.Roles;
import com.example.global.jwt.JwtToken;
import com.example.member.model.dto.request.SignUpRequest;
import com.example.member.model.dto.request.SignInRequest;
import com.example.member.model.dto.response.MemberSaveResponse;
import com.example.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/member")
@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/sign-up")
    public ResponseEntity<MemberSaveResponse> memberSave(@Valid @RequestBody SignUpRequest signUpRequest) {
        final MemberSaveResponse memberSaveResponse = memberService.signUp(signUpRequest);

        return ResponseEntity.status(CREATED).body(memberSaveResponse);
    }

    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody SignInRequest signInDto) {
        return memberService.signIn(signInDto);
    }

    @ValidateUser(roles = Roles.USER)
    @DeleteMapping("/{memberId}")
    public void deleteMember(@PathVariable(value = "memberId", required = true) Long memberId) {
        memberService.deleteMember(memberId);
    }

}
