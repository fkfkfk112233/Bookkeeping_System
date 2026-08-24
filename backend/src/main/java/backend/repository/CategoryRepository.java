package backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import backend.entity.Category;
import backend.entity.User;

public interface CategoryRepository extends JpaRepository<Category, Long> {

	boolean existsByUser(User user);
}