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
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class DataInitializer {

    private static final String DATA_DB_URL = "jdbc:sqlite:data.db";
    private static final String TEST_USERNAME = "testuser";

    @Bean
    CommandLineRunner initDatabase(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            TransactionRepository transactionRepository) {

        return args -> {

            Path dataDbPath = Path.of("data.db");

            // 沒有 data.db 就直接略過測試資料匯入
            if (!Files.exists(dataDbPath)) {
                System.out.println("data.db not found. Skip test data import.");
                return;
            }

            // 已經有交易資料，就不要重複匯入
            if (transactionRepository.count() > 0) {
                System.out.println("Test data already exists. Skip import.");
                return;
            }

            System.out.println("====================================");
            System.out.println("Importing test data from data.db...");
            System.out.println("====================================");

            try (Connection connection = DriverManager.getConnection(DATA_DB_URL)) {

                User user = findOrCreateUser(
                        connection,
                        userRepository);

                Map<Long, Category> categoryMap = findOrCreateCategories(
                        connection,
                        user,
                        categoryRepository);

                int importedCount = importTransactions(
                        connection,
                        user,
                        categoryMap,
                        transactionRepository);

                System.out.println("====================================");
                System.out.println(
                        "Test data imported successfully: "
                                + importedCount
                                + " transactions.");
                System.out.println("====================================");
            }
        };
    }

    // =========================
    // User
    // =========================

    private User findOrCreateUser(
            Connection connection,
            UserRepository userRepository) throws SQLException {

        List<User> users = userRepository.findAll();

        for (User user : users) {
            if (TEST_USERNAME.equals(user.getUsername())) {
                return user;
            }
        }

        String sql = """
                SELECT username, password, email, role, enabled
                FROM users
                WHERE username = ?
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, TEST_USERNAME);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    throw new IllegalStateException(
                            "User '" + TEST_USERNAME + "' not found in data.db.");
                }

                User user = new User();
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                user.setEmail(resultSet.getString("email"));
                user.setRole(resultSet.getString("role"));
                user.setEnabled(resultSet.getBoolean("enabled"));

                return userRepository.save(user);
            }
        }
    }

    // =========================
    // Categories
    // =========================

    private Map<Long, Category> findOrCreateCategories(
            Connection connection,
            User user,
            CategoryRepository categoryRepository) throws SQLException {

        Map<Long, Category> categoryMap = new HashMap<>();

        String sql = """
                SELECT id, name, type, category_id
                FROM categories
                ORDER BY id
                """;

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            // 第一階段：先建立所有 Category
            while (resultSet.next()) {
                Long sourceId = resultSet.getLong("id");

                String name = resultSet.getString("name");
                TransactionType type = TransactionType.valueOf(
                        resultSet.getString("type"));

                Category category = findExistingCategory(
                        user,
                        name,
                        type,
                        categoryRepository);

                if (category == null) {
                    category = new Category();
                    category.setUser(user);
                    category.setName(name);
                    category.setType(type);
                    category = categoryRepository.save(category);
                }

                categoryMap.put(sourceId, category);
            }
        }

        // 第二階段：處理父分類關係
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long sourceId = resultSet.getLong("id");
                long parentIdValue = resultSet.getLong("category_id");
                Long parentId = resultSet.wasNull() ? null : parentIdValue;

                if (parentId == null) {
                    continue;
                }

                Category category = categoryMap.get(sourceId);
                Category parentCategory = categoryMap.get(parentId);

                if (category != null && parentCategory != null) {
                    category.setCategory(parentCategory);
                    categoryRepository.save(category);
                }
            }
        }

        return categoryMap;
    }

    private Category findExistingCategory(
            User user,
            String name,
            TransactionType type,
            CategoryRepository categoryRepository) {

        return categoryRepository.findAll()
                .stream()
                .filter(category -> category.getUser().getId().equals(user.getId()))
                .filter(category -> category.getName().equals(name))
                .filter(category -> category.getType() == type)
                .findFirst()
                .orElse(null);
    }

    // =========================
    // Transactions
    // =========================

    private int importTransactions(
            Connection connection,
            User user,
            Map<Long, Category> categoryMap,
            TransactionRepository transactionRepository) throws SQLException {

        String sql = """
                SELECT amount,
                       payment_method,
                       description,
                       transaction_date,
                       type,
                       category_id
                FROM transactions
                ORDER BY id
                """;

        int count = 0;

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                BigDecimal amount = resultSet.getBigDecimal("amount");

                PaymentMethod paymentMethod = PaymentMethod.valueOf(
                        resultSet.getString("payment_method"));

                TransactionType type = TransactionType.valueOf(
                        resultSet.getString("type"));

                long categoryIdValue = resultSet.getLong("category_id");
                Long categoryId = resultSet.wasNull() ? null : categoryIdValue;
                Category category = categoryMap.get(categoryId);

                long timestamp = resultSet.getLong("transaction_date");
                LocalDateTime transactionDate = Instant
                        .ofEpochMilli(timestamp)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                Transaction transaction = new Transaction();
                transaction.setUser(user);
                transaction.setCategory(category);
                transaction.setType(type);
                transaction.setAmount(amount);
                transaction.setPaymentMethod(paymentMethod);
                transaction.setDescription(resultSet.getString("description"));
                transaction.setTransactionDate(transactionDate);

                transactionRepository.save(transaction);
                count++;
            }
        }

        return count;
    }
}
