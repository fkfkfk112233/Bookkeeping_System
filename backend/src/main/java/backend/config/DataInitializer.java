package backend.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import backend.entity.Category;
import backend.entity.User;
import backend.enums.TransactionType;
import backend.repository.CategoryRepository;
import backend.repository.UserRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
            CategoryRepository categoryRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createAdmin();
        createDefaultCategories();
    }

    private void createAdmin() {
        if (userRepository.findByUsername("admin").isPresent()) {
            return;
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setEmail("admin@bookkeeping.com");
        admin.setRole("ADMIN");
        admin.setEnabled(true);
        userRepository.save(admin);

        System.out.println("Default admin account created.");
    }

    private void createDefaultCategories() {
        if (categoryRepository.existsByUserIsNull()) {
            return;
        }

        List<Category> defaults = List.of(
                createDefaultCategory("薪資", TransactionType.INCOME),
                createDefaultCategory("獎金", TransactionType.INCOME),
                createDefaultCategory("飲食", TransactionType.EXPENSE),
                createDefaultCategory("交通", TransactionType.EXPENSE),
                createDefaultCategory("購物", TransactionType.EXPENSE),
                createDefaultCategory("娛樂", TransactionType.EXPENSE),
                createDefaultCategory("居家", TransactionType.EXPENSE),
                createDefaultCategory("3C", TransactionType.EXPENSE),
                createDefaultCategory("醫療", TransactionType.EXPENSE)
        );

        categoryRepository.saveAll(defaults);
        System.out.println("Default categories created.");
    }

    private Category createDefaultCategory(String name, TransactionType type) {
        Category category = new Category();
        category.setUser(null);
        category.setName(name);
        category.setType(type);
        return category;
    }
}
