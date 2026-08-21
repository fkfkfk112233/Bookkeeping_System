package backend.config;

import backend.entity.Category;
import backend.entity.Transaction;
import backend.entity.User;
import backend.enums.PaymentMethod;
import backend.enums.TransactionType;
import backend.repository.CategoryRepository;
import backend.repository.TransactionRepository;
import backend.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            TransactionRepository transactionRepository) {

        return args -> {

            // 如果已經有資料，就不建立測試資料
            if (userRepository.count() > 0) {
                return;
            }

            // =========================
            // User
            // =========================

            User user = new User();

            user.setUsername("testuser");
            user.setPassword("123456");
            user.setEmail("test@example.com");
            user.setRole("USER");
            user.setEnabled(true);

            user = userRepository.save(user);

            // =========================
            // Categories
            // =========================

            Category food =
                    createCategory(user, "飲食", TransactionType.EXPENSE);

            Category transport =
                    createCategory(user, "交通", TransactionType.EXPENSE);

            Category entertainment =
                    createCategory(user, "娛樂", TransactionType.EXPENSE);

            Category shopping =
                    createCategory(user, "購物", TransactionType.EXPENSE);

            Category salary =
                    createCategory(user, "薪資", TransactionType.INCOME);

            Category bonus =
                    createCategory(user, "獎金", TransactionType.INCOME);

            categoryRepository.save(food);
            categoryRepository.save(transport);
            categoryRepository.save(entertainment);
            categoryRepository.save(shopping);
            categoryRepository.save(salary);
            categoryRepository.save(bonus);

            // =========================
            // Transactions
            // =========================

            createTransaction(
                    transactionRepository,
                    user,
                    salary,
                    TransactionType.INCOME,
                    "30000",
                    PaymentMethod.CASH,
                    "八月薪資",
                    LocalDateTime.of(2026, 8, 1, 9, 0));

            createTransaction(
                    transactionRepository,
                    user,
                    bonus,
                    TransactionType.INCOME,
                    "5000",
                    PaymentMethod.CASH,
                    "績效獎金",
                    LocalDateTime.of(2026, 8, 5, 10, 30));

            createTransaction(
                    transactionRepository,
                    user,
                    food,
                    TransactionType.EXPENSE,
                    "120",
                    PaymentMethod.CASH,
                    "午餐",
                    LocalDateTime.of(2026, 8, 18, 12, 30));

            createTransaction(
                    transactionRepository,
                    user,
                    transport,
                    TransactionType.EXPENSE,
                    "50",
                    PaymentMethod.CASH,
                    "捷運",
                    LocalDateTime.of(2026, 8, 18, 18, 0));

            createTransaction(
                    transactionRepository,
                    user,
                    food,
                    TransactionType.EXPENSE,
                    "180",
                    PaymentMethod.CREDIT_CARD,
                    "晚餐",
                    LocalDateTime.of(2026, 8, 19, 19, 0));

            createTransaction(
                    transactionRepository,
                    user,
                    entertainment,
                    TransactionType.EXPENSE,
                    "350",
                    PaymentMethod.CREDIT_CARD,
                    "電影",
                    LocalDateTime.of(2026, 8, 19, 20, 30));

            createTransaction(
                    transactionRepository,
                    user,
                    shopping,
                    TransactionType.EXPENSE,
                    "1200",
                    PaymentMethod.CREDIT_CARD,
                    "購買衣服",
                    LocalDateTime.of(2026, 8, 20, 15, 0));

            createTransaction(
                    transactionRepository,
                    user,
                    transport,
                    TransactionType.EXPENSE,
                    "50",
                    PaymentMethod.CASH,
                    "捷運",
                    LocalDateTime.of(2026, 8, 21, 8, 30));

            createTransaction(
                    transactionRepository,
                    user,
                    food,
                    TransactionType.EXPENSE,
                    "150",
                    PaymentMethod.CASH,
                    "午餐",
                    LocalDateTime.of(2026, 8, 21, 12, 0));

            System.out.println("====================================");
            System.out.println("Test data initialized successfully.");
            System.out.println("====================================");
        };
    }

    // =========================
    // Category 建立方法
    // =========================

    private Category createCategory(
            User user,
            String name,
            TransactionType type) {

        Category category = new Category();

        category.setUser(user);
        category.setName(name);
        category.setType(type);

        return category;
    }

    // =========================
    // Transaction 建立方法
    // =========================

    private void createTransaction(
            TransactionRepository transactionRepository,
            User user,
            Category category,
            TransactionType type,
            String amount,
            PaymentMethod paymentMethod,
            String description,
            LocalDateTime transactionDate) {

        Transaction transaction = new Transaction();

        transaction.setUser(user);
        transaction.setCategory(category);
        transaction.setType(type);
        transaction.setAmount(new BigDecimal(amount));
        transaction.setPaymentMethod(paymentMethod);
        transaction.setDescription(description);
        transaction.setTransactionDate(transactionDate);

        transactionRepository.save(transaction);
    }
}