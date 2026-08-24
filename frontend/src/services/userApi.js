import api from "./api";

export const getUsers = async () => {
  const response = await api.get("/admin/users");

  return response.data;
};

export const createUser = async (userData) => {
  const response = await api.post("/admin/users", userData);

  return response.data;
};
