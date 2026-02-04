package port.sm.erp.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import port.sm.erp.security.SecurityJwtConfig; // JwtUtil 통합 버전 사용 시
import port.sm.erp.dto.MemberRequestDTO;
import port.sm.erp.entity.Member;
import port.sm.erp.service.MemberService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true") // axios withCredentials=true일 때 필요
public class AuthController {

    private final MemberService memberService;
    private final SecurityJwtConfig securityJwtConfig; // JwtUtil 통합된 클래스 사용

    public AuthController(MemberService memberService, SecurityJwtConfig securityJwtConfig) {
        this.memberService = memberService;
        this.securityJwtConfig = securityJwtConfig;
    }

    // =======================
    // 로그인
    // =======================
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        // 로그인 시도
        Member member = memberService.login(email, password);
        if (member == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
        }

        // JWT 생성
        String token = securityJwtConfig.generateToken(member.getId(), member.getEmail());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "id", member.getId(),
                "email", member.getEmail()
        ));
    }

    // =======================
    // 회원가입
    // =======================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody MemberRequestDTO dto) {
        try {
            Member member = memberService.register(dto);
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
