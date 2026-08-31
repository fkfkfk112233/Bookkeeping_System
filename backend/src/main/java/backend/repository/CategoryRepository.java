package backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.entity.Category;
import backend.entity.User;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByUser(User user);

    List<Category> findByUserIsNullOrUserOrderByTypeAscNameAsc(User user);

    Optional<Category> findByIdAndUser(Long id, User user);

    Optional<Category> findByIdAndUserIsNull(Long id);

    boolean existsByUserIsNull();
}
