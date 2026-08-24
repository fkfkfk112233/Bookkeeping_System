import api from "./api";

export const getUsers = async () => {
  const response = await api.get("/admin/users");

  return response.data;
};
