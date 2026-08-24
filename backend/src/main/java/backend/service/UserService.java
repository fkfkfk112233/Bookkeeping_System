package backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

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

    public UserService(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            TransactionRepository transactionRepository) {

        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
    }

    // =========================
    // Entity → Response
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
    // GET ALL
    // =========================

    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =========================
    // GET BY ID
    // =========================

    public UserResponse getUserById(Long id) {

        User user = userRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return toResponse(user);
    }

    // =========================
    // CREATE
    // =========================

    public UserResponse createUser(UserRequest request) {

        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setEnabled(request.getEnabled());

        User savedUser = userRepository.save(user);

        return toResponse(savedUser);
    }

    // =========================
    // UPDATE
    // =========================

    public UserResponse updateUser(
            Long id,
            UserRequest request) {

        User user = userRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setEnabled(request.getEnabled());

        User updatedUser =
                userRepository.save(user);

        return toResponse(updatedUser);
    }

    // =========================
    // DELETE
    // =========================

    public void deleteUser(Long id) {

        User user = userRepository
                .findById(id)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        boolean hasCategories =
                categoryRepository.existsByUser(user);

        boolean hasTransactions =
                transactionRepository.existsByUser(user);

        if (hasCategories || hasTransactions) {

            throw new IllegalArgumentException(
                    "User has related categories or transactions and cannot be deleted"
            );
        }

        userRepository.delete(user);
    }
}