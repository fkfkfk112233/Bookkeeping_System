import api from "./api";

export function getDashboardSummary(startDate, endDate) {
  return api.get("/dashboard/summary", {
    params: {
      startDate,
      endDate,
    },
  });
}
