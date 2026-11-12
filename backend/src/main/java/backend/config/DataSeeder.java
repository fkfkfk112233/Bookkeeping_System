package backend.config;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import backend.entity.Category;
import backend.entity.Transaction;
import backend.entity.User;
import backend.enums.PaymentMethod;
import backend.enums.TransactionType;
import backend.repository.CategoryRepository;
import backend.repository.TransactionRepository;
import backend.repository.UserRepository;

/**
 * Generates repeatable demo transactions for the dev profile only.
 * Existing manually-created transactions are preserved. Only rows belonging
 * to the test user with a [TEST-SEED] description prefix are replaced.
 */
@Component
@Profile("dev")
@Order(2)
public class DataSeeder implements CommandLineRunner {

    private static final String TEST_USERNAME = "test";
    private static final String TEST_EMAIL = "test@user.com";
    private static final String TEST_PASSWORD = "test123";
    private static final String SEED_PREFIX = "[TEST-SEED]";
    private static final int RANDOM_SEED = 20261111;
    private static final int START_MONTH = 1;
    private static final int END_MONTH = 11;
    private static final int END_DAY = 11;
    private static final int ROWS_PER_MONTH = 20;
    private static final int INCOME_ROWS_PER_MONTH = 4;

    private static final List<String> INCOME_CATEGORY_NAMES = List.of("薪資", "獎金");
    private static final List<String> EXPENSE_CATEGORY_NAMES = List.of(
            "飲食", "交通", "購物", "娛樂", "居家", "3C", "醫療");

    private static final Map<String, List<String>> EXPENSE_NOTES = Map.of(
            "飲食", List.of("早餐", "午餐", "晚餐", "咖啡與點心", "超市採買"),
            "交通", List.of("捷運與公車", "計程車", "加油", "停車費"),
            "購物", List.of("生活用品", "服飾", "網路購物", "日用品補貨"),
            "娛樂", List.of("電影", "聚餐活動", "展覽門票", "影音訂閱"),
            "居家", List.of("水電瓦斯", "居家清潔", "家具用品", "房屋維護"),
            "3C", List.of("周邊設備", "軟體訂閱", "電子配件"),
            "醫療", List.of("門診", "藥品", "健康檢查"));

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            TransactionRepository transactionRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        User testUser = getOrCreateTestUser();
        Map<TransactionType, List<Category>> categories = getRequiredCategories();

        // Re-running the dev application replaces only this seeder's own rows.
        transactionRepository.deleteByUserAndDescriptionStartingWith(testUser, SEED_PREFIX);

        List<Transaction> transactions = generateTransactions(testUser, categories);
        transactionRepository.saveAll(transactions);

        System.out.printf(
                "[DataSeeder] Generated %d demo transactions for user '%s'. Date range: 2026-01-01 to 2026-11-11.%n",
                transactions.size(), TEST_USERNAME);
    }

    private User getOrCreateTestUser() {
        User user = userRepository.findByUsername(TEST_USERNAME).orElseGet(User::new);
        boolean isNewUser = user.getId() == null;

        user.setUsername(TEST_USERNAME);
        user.setPassword(passwordEncoder.encode(TEST_PASSWORD));
        user.setEmail(TEST_EMAIL);
        user.setRole("USER");
        user.setEnabled(true);

        User savedUser = userRepository.save(user);
        if (isNewUser) {
            System.out.println("[DataSeeder] Created test account: username=test, password=test123");
        } else {
            System.out.println("[DataSeeder] Reset development test account credentials: username=test, password=test123");
        }
        return savedUser;
    }

    private Map<TransactionType, List<Category>> getRequiredCategories() {
        List<Category> allCategories = categoryRepository.findAll();
        Map<TransactionType, List<Category>> result = new EnumMap<>(TransactionType.class);
        result.put(TransactionType.INCOME, allCategories.stream()
                .filter(category -> category.getUser() == null)
                .filter(category -> INCOME_CATEGORY_NAMES.contains(category.getName()))
                .filter(category -> category.getType() == TransactionType.INCOME)
                .toList());
        result.put(TransactionType.EXPENSE, allCategories.stream()
                .filter(category -> category.getUser() == null)
                .filter(category -> EXPENSE_CATEGORY_NAMES.contains(category.getName()))
                .filter(category -> category.getType() == TransactionType.EXPENSE)
                .toList());

        if (result.get(TransactionType.INCOME).size() != INCOME_CATEGORY_NAMES.size()
                || result.get(TransactionType.EXPENSE).size() != EXPENSE_CATEGORY_NAMES.size()) {
            throw new IllegalStateException(
                    "Default categories are incomplete. Ensure DataInitializer runs before DataSeeder.");
        }
        return result;
    }

    private List<Transaction> generateTransactions(
            User user, Map<TransactionType, List<Category>> categories) {
        Random random = new Random(RANDOM_SEED);
        List<Transaction> result = new ArrayList<>();
        List<PaymentMethod> paymentMethods = List.of(PaymentMethod.values());

        for (int month = START_MONTH; month <= END_MONTH; month++) {
            LocalDate firstDay = LocalDate.of(2026, month, 1);
            int lastDay = month == END_MONTH ? END_DAY : firstDay.lengthOfMonth();

            List<TransactionType> monthlyTypes = new ArrayList<>();
            for (int i = 0; i < INCOME_ROWS_PER_MONTH; i++) {
                monthlyTypes.add(TransactionType.INCOME);
            }
            for (int i = INCOME_ROWS_PER_MONTH; i < ROWS_PER_MONTH; i++) {
                monthlyTypes.add(TransactionType.EXPENSE);
            }
            Collections.shuffle(monthlyTypes, random);

            for (TransactionType type : monthlyTypes) {
                int day = random.nextInt(lastDay) + 1;
                int hour = random.nextInt(16) + 7; // 07:00-22:59
                int minute = List.of(0, 10, 15, 20, 30, 40, 45, 50)
                        .get(random.nextInt(8));
                int second = random.nextInt(60);
                LocalDateTime transactionDate = LocalDate.of(2026, month, day)
                        .atTime(hour, minute, second);

                List<Category> availableCategories = categories.get(type);
                Category category = availableCategories.get(random.nextInt(availableCategories.size()));
                BigDecimal amount;
                String note;

                if (type == TransactionType.INCOME) {
                    if ("薪資".equals(category.getName())) {
                        amount = BigDecimal.valueOf(random.nextInt(2301) + 4200L)
                                .multiply(BigDecimal.TEN);
                        note = randomChoice(random, List.of("每月薪資", "薪資入帳", "兼職收入"));
                    } else {
                        amount = BigDecimal.valueOf(random.nextInt(1701) + 300L)
                                .multiply(BigDecimal.TEN);
                        note = randomChoice(random, List.of("績效獎金", "專案獎金", "節慶獎金"));
                    }
                } else {
                    amount = BigDecimal.valueOf(random.nextInt(1191) + 10L)
                            .multiply(BigDecimal.TEN);
                    note = randomChoice(random, EXPENSE_NOTES.get(category.getName()));
                }

                String description = SEED_PREFIX + " " + category.getName() + " - " + note;
                PaymentMethod paymentMethod = paymentMethods.get(random.nextInt(paymentMethods.size()));
                result.add(new Transaction(
                        user, category, type, amount, paymentMethod, description, transactionDate));
            }
        }
        return result;
    }

    private String randomChoice(Random random, List<String> values) {
        return values.get(random.nextInt(values.size()));
    }
}
