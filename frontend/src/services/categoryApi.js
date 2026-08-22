import api from "./api";

export function getCategories() {
  return api.get("/categories");
}

export function createCategory(category) {
  return api.post("/categories", category);
}
