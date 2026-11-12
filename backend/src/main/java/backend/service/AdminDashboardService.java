package backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.stereotype.Service;

import backend.dto.dashboard.AdminDashboardSummaryResponse;
import backend.repository.UserRepository;

@Service
public class AdminDashboardService {

    private static final ZoneId TAIPEI_ZONE = ZoneId.of("Asia/Taipei");

    private final UserRepository userRepository;

    public AdminDashboardService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AdminDashboardSummaryResponse getSummary() {
        LocalDate today = LocalDate.now(TAIPEI_ZONE);
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime startOfNextDay = today.plusDays(1).atStartOfDay();

        return new AdminDashboardSummaryResponse(
                userRepository.countByRole("ADMIN"),
                userRepository.countByRole("USER"),
                userRepository.countByLastLoginAtGreaterThanEqualAndLastLoginAtLessThan(
                        startOfDay,
                        startOfNextDay
                ),
                userRepository.countByEnabledFalse()
        );
    }
}
