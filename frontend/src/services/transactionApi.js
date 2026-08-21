import api from "./api";

export function getTransactions(startDate, endDate) {
  return api.get("/transactions", {
    params: {
      startDate,
      endDate,
    },
  });
}

export function getTransaction(id) {
  return api.get(`/transactions/${id}`);
}

export function createTransaction(data) {
  return api.post("/transactions", data);
}

export function updateTransaction(id, data) {
  return api.put(`/transactions/${id}`, data);
}

export function deleteTransaction(id) {
  return api.delete(`/transactions/${id}`);
}
