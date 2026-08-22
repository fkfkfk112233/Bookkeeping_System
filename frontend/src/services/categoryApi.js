import api from "./api";

export function getCategories() {
  return api.get("/categories");
}

export function createCategory(category) {
  return api.post("/categories", category);
}

export function updateCategory(id, data) {
  return api.put(`/categories/${id}`, data);
}

export function deleteCategory(id) {
  return api.delete(`/categories/${id}`);
}
