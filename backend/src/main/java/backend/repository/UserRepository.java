package backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import backend.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    long countByRole(String role);

    long countByEnabledFalse();

    long countByLastLoginAtGreaterThanEqualAndLastLoginAtLessThan(
            java.time.LocalDateTime start,
            java.time.LocalDateTime end
    );
}