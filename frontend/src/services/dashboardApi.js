import api from "./api";

export function getDashboardSummary(startDate, endDate) {
  return api.get("/dashboard/summary", {
    params: {
      startDate,
      endDate,
    },
  });
}

export function getDashboardIncome(startDate, endDate) {
  return api.get("/dashboard/income", {
    params: {
      startDate,
      endDate,
    },
  });
}

export function getDashboardExpense(startDate, endDate) {
  return api.get("/dashboard/expense", {
    params: {
      startDate,
      endDate,
    },
  });
}

export function getDashboardIncomeTrend(startDate, endDate) {
  return api.get("/dashboard/income/trend", {
    params: {
      startDate,
      endDate,
    },
  });
}

export function getDashboardExpenseTrend(startDate, endDate) {
  return api.get("/dashboard/expense/trend", {
    params: {
      startDate,
      endDate,
    },
  });
}

export function getDashboardFrequency(startDate, endDate) {
  return api.get("/dashboard/frequency", {
    params: {
      startDate,
      endDate,
    },
  });
}
