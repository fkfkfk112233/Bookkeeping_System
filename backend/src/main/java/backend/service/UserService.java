
package backend.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import backend.dto.user.UserProfileRequest;
import backend.dto.user.UserProfileResponse;
import backend.dto.user.UserRequest;
import backend.dto.user.UserResponse;
import backend.entity.User;
import backend.repository.CategoryRepository;
import backend.repository.TransactionRepository;
import backend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            TransactionRepository transactionRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================
    // Entity -> Profile Response
    // =========================

    private UserProfileResponse toProfileResponse(User user) {

        UserProfileResponse response = new UserProfileResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setEnabled(user.getEnabled());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }

    // =========================
    // Entity -> User Response
    // =========================

    private UserResponse toResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setEnabled(user.getEnabled());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());
        response.setLastLoginAt(user.getLastLoginAt());

        return response;
    }

    // =========================
    // GET PROFILE
    // =========================

    public UserProfileResponse getUserProfile(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return toProfileResponse(user);
    }

    public UserProfileResponse getUserProfileByUsername(
            String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return toProfileResponse(user);
    }

    // =========================
    // UPDATE OWN PROFILE
    // =========================

    @Transactional
    public UserProfileResponse updateUserProfileByUsername(
            String username,
            UserProfileRequest request) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        updateProfileFields(user, request);

        User updatedUser = userRepository.save(user);

        return toProfileResponse(updatedUser);
    }

    // =========================
    // UPDATE PROFILE BY ID
    // ADMIN USE
    // =========================

    @Transactional
    public UserProfileResponse updateUserProfile(
            Long id,
            UserProfileRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        updateProfileFields(user, request);

        User updatedUser = userRepository.save(user);

        return toProfileResponse(updatedUser);
    }

    private void updateProfileFields(
            User user,
            UserProfileRequest request) {

        if (request.getUsername() != null
                && !request.getUsername().isBlank()) {

            user.setUsername(request.getUsername().trim());
        }

        if (request.getEmail() != null
                && !request.getEmail().isBlank()) {

            user.setEmail(request.getEmail().trim());
        }

        if (request.getPassword() != null
                && !request.getPassword().isBlank()) {

            user.setPassword(
                    passwordEncoder.encode(request.getPassword())
            );
        }
    }

    // =========================
    // DISABLE OWN ACCOUNT
    // =========================

    @Transactional
    public void disableUserByUsername(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setEnabled(false);

        userRepository.save(user);
    }

    // =========================
    // DISABLE USER BY ID
    // ADMIN USE
    // =========================

    @Transactional
    public void disableUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setEnabled(false);

        userRepository.save(user);
    }

    // =========================
    // GET ALL USERS
    // =========================

    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================
    // GET USER BY ID
    // =========================

    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return toResponse(user);
    }

    // =========================
    // CREATE USER
    // =========================

    @Transactional
    public UserResponse createUser(UserRequest request) {

        User user = new User();

        user.setUsername(request.getUsername());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setEnabled(request.getEnabled());

        User savedUser = userRepository.save(user);

        return toResponse(savedUser);
    }

    // =========================
    // UPDATE USER
    // =========================

    @Transactional
    public UserResponse updateUser(
            Long id,
            UserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setEnabled(request.getEnabled());

        if (request.getPassword() != null
                && !request.getPassword().isBlank()) {

            user.setPassword(
                    passwordEncoder.encode(request.getPassword())
            );
        }

        User updatedUser = userRepository.save(user);

        return toResponse(updatedUser);
    }

    // =========================
    // DELETE USER
    // =========================

    @Transactional
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        boolean hasCategories =
                categoryRepository.existsByUser(user);

        boolean hasTransactions =
                transactionRepository.existsByUser(user);

        if (hasCategories || hasTransactions) {

            throw new IllegalArgumentException(
                    "User has related categories or transactions "
                    + "and cannot be deleted"
            );
        }

        userRepository.delete(user);
    }
}
