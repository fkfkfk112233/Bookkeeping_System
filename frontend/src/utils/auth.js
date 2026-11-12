import { logout as logoutApi } from "../services/authApi";

export const logout = async () => {
  try {
    await logoutApi();
  } finally {
    localStorage.removeItem("currentUser");
  }
};
