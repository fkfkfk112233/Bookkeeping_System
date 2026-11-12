package backend.dto.dashboard;

public class AdminDashboardSummaryResponse {

    private long adminCount;
    private long userCount;
    private long todayLoginCount;
    private long disabledUserCount;

    public AdminDashboardSummaryResponse() {
    }

    public AdminDashboardSummaryResponse(
            long adminCount,
            long userCount,
            long todayLoginCount,
            long disabledUserCount) {
        this.adminCount = adminCount;
        this.userCount = userCount;
        this.todayLoginCount = todayLoginCount;
        this.disabledUserCount = disabledUserCount;
    }

    public long getAdminCount() {
        return adminCount;
    }

    public void setAdminCount(long adminCount) {
        this.adminCount = adminCount;
    }

    public long getUserCount() {
        return userCount;
    }

    public void setUserCount(long userCount) {
        this.userCount = userCount;
    }

    public long getTodayLoginCount() {
        return todayLoginCount;
    }

    public void setTodayLoginCount(long todayLoginCount) {
        this.todayLoginCount = todayLoginCount;
    }

    public long getDisabledUserCount() {
        return disabledUserCount;
    }

    public void setDisabledUserCount(long disabledUserCount) {
        this.disabledUserCount = disabledUserCount;
    }
}
