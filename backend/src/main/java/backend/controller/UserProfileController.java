
package backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.dto.user.UserProfileRequest;
import backend.dto.user.UserProfileResponse;
import backend.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }

    // =========================
    // CURRENT USER PROFILE
    // =========================

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getMyProfile(
            Authentication authentication) {

        UserProfileResponse response =
                userService.getUserProfileByUsername(
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateMyProfile(
            Authentication authentication,
            @RequestBody UserProfileRequest request) {

        UserProfileResponse response =
                userService.updateUserProfileByUsername(
                        authentication.getName(),
                        request
                );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/me/disable")
    public ResponseEntity<Void> disableMyAccount(
            Authentication authentication) {

        userService.disableUserByUsername(
                authentication.getName()
        );

        return ResponseEntity.noContent().build();
    }

    // =========================
    // ADMIN ONLY PROFILE APIs
    // =========================

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getProfile(
            @PathVariable Long id) {

        UserProfileResponse response =
                userService.getUserProfile(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserProfileResponse> getProfileByUsername(
            @PathVariable String username) {

        UserProfileResponse response =
                userService.getUserProfileByUsername(username);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @PathVariable Long id,
            @RequestBody UserProfileRequest request) {

        UserProfileResponse response =
                userService.updateUserProfile(id, request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<Void> disableAccount(
            @PathVariable Long id) {

        userService.disableUser(id);

        return ResponseEntity.noContent().build();
    }
}
