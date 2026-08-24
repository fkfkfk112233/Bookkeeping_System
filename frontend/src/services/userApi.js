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

// User Login
export const getUserByUsername = async (username) => {
  const response = await api.get(`/users/username/${username}`);

  return response.data;
};

// User Profile
export const getUserProfile = async (id) => {
  const response = await api.get(`/users/${id}`);

  return response.data;
};

export const updateUserProfile = async (id, userData) => {
  const response = await api.put(`/users/${id}`, userData);

  return response.data;
};

export const disableUser = async (id) => {
  await api.patch(`/users/${id}/disable`);
};
