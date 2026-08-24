package backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.dto.auth.LoginRequest;
import backend.dto.auth.LoginResponse;
import backend.entity.User;
import backend.repository.UserRepository;
import backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	private final UserRepository userRepository;

	public AuthController(AuthService authService, UserRepository userRepository) {

		this.authService = authService;
		this.userRepository = userRepository;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {

		// =========================
		// Authentication
		// =========================

		Authentication authentication = authService.authenticate(request.getUsername(), request.getPassword());

		// =========================
		// Security Context
		// =========================

		SecurityContext context = SecurityContextHolder.createEmptyContext();

		context.setAuthentication(authentication);

		SecurityContextHolder.setContext(context);

		// =========================
		// HTTP Session
		// =========================

		HttpSession session = httpRequest.getSession(true);

		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

		// =========================
		// User Response
		// =========================

		User user = userRepository.findByUsername(authentication.getName())
				.orElseThrow(() -> new RuntimeException("User not found"));

		LoginResponse response = new LoginResponse(user.getId(), user.getUsername(), user.getEmail(), user.getRole(),
				user.getEnabled());

		return ResponseEntity.ok(response);
	}
}