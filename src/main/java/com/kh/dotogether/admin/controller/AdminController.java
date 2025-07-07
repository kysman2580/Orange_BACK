package com.kh.dotogether.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.dotogether.admin.service.AdminService;
import com.kh.dotogether.challenge.model.dto.ChallengeDTO;
import com.kh.dotogether.log.model.dto.LogDTO;
import com.kh.dotogether.member.model.dto.MemberDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // ✅ 회원 목록 조회
    @GetMapping("/users")
    public ResponseEntity<List<MemberDTO>> getAllUsers() {
        List<MemberDTO> members = adminService.findAllUsers();
        return ResponseEntity.ok(members);
    }

    // ✅ 챌린지방 목록 조회
    @GetMapping("/challenges")
    public ResponseEntity<List<ChallengeDTO>> getAllChallenges() {
        List<ChallengeDTO> challenges = adminService.findAllChallenges();
        return ResponseEntity.ok(challenges);
    }

    // ✅ 로그 기록 조회
    @GetMapping("/logs")
    public ResponseEntity<List<LogDTO>> getAllLogs() {
        List<LogDTO> logs = adminService.findAllLogs();
        return ResponseEntity.ok(logs);
    }

    // ✅ 챌린지 삭제 (또는 종료 처리)
    @DeleteMapping("/challenges/{id}")
    public ResponseEntity<?> deleteChallenge(@PathVariable Long id) {
        adminService.deleteChallenge(id);
        return ResponseEntity.ok().body("챌린지가 성공적으로 종료되었습니다.");
    }
}