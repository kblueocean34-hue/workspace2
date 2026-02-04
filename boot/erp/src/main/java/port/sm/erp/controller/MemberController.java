package port.sm.erp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import port.sm.erp.dto.LoginRequestDTO;
import port.sm.erp.dto.LoginResponse;
import port.sm.erp.dto.MemberRequestDTO;
import port.sm.erp.entity.Member;
import port.sm.erp.service.MemberService;
import port.sm.erp.security.SecurityJwtConfig; // JwtUtil 통합 버전

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class MemberController {

    private final MemberService memberService;
    private final SecurityJwtConfig securityJwtConfig;

    // =======================
    // 1️⃣ 모든 회원 조회
    // =======================
    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    // =======================
    // 2️⃣ 특정 회원 조회
    // =======================
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<?> getMemberById(@PathVariable Long id) {
        Member member = memberService.getMemberById(id);
        if (member == null) {
            return ResponseEntity.status(404).body(Map.of("error", "Member not found"));
        }
        return ResponseEntity.ok(member);
    }

    // =======================
    // 3️⃣ 회원가입
    // =======================
    @PostMapping
    public ResponseEntity<?> registerMember(@RequestBody MemberRequestDTO dto) {
        try {
            Member savedMember = memberService.register(dto);
            return ResponseEntity.ok(Map.of(
                    "message", "회원가입 완료",
                    "id", savedMember.getId()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // =======================
    // 4️⃣ 회원 삭제
    // =======================
    @DeleteMapping("/{id:[0-9]+}")
    public ResponseEntity<?> deleteMember(@PathVariable Long id) {
        try {
            memberService.deleteMember(id);
            return ResponseEntity.ok(Map.of("message", "회원 삭제 완료"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // =======================
    // 5️⃣ 로그인 (JWT 발급)
    // =======================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO req) {
        try {
            Member member = memberService.login(req.getEmail(), req.getPassword());
            if (member == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
            }

            String token = securityJwtConfig.generateToken(member.getId(), member.getEmail());

            return ResponseEntity.ok(new LoginResponse("success", token));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Login failed"));
        }
    }
}
