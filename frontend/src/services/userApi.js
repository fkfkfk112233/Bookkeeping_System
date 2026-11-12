import api from "./api";

// Admin
export const getUsers = async () => {
  const response = await api.get("/admin/users");
  return response.data;
};

export const createUser = async (userData) => {
  const response = await api.post("/admin/users", userData);
  return response.data;
};

export const updateUser = async (id, userData) => {
  const response = await api.put(`/admin/users/${id}`, userData);
  return response.data;
};

export const deleteUser = async (id) => {
  await api.delete(`/admin/users/${id}`);
};

// Current user's profile.
// The backend identifies the current user through the authenticated Session;
// never send a user ID supplied by the browser for self-service operations.
export const getUserProfile = async () => {
  const response = await api.get("/users/me");
  return response.data;
};

export const updateUserProfile = async (userData) => {
  const response = await api.put("/users/me", userData);
  return response.data;
};

export const disableUser = async () => {
  await api.patch("/users/me/disable");
};
