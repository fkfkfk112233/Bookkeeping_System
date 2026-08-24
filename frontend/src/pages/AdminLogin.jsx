import { useState } from "react";
import { useNavigate } from "react-router-dom";

import { login } from "../services/authApi";

function AdminLogin() {
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const user = await login(username, password);

      // 必須是 ADMIN
      if (user.role !== "ADMIN") {
        alert("此入口僅限管理員登入");

        return;
      }

      localStorage.setItem("currentUser", JSON.stringify(user));

      navigate("/admin");
    } catch (error) {
      console.error("Admin Login failed:", error);

      if (error.response?.status === 401) {
        alert("帳號或密碼錯誤");
      } else if (error.response?.status === 403) {
        alert("此帳號沒有登入權限");
      } else {
        alert("登入失敗");
      }
    }
  };

  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-12 col-sm-10 col-md-6 col-lg-4">
          <div className="card shadow-sm">
            <div className="card-body">
              <h2 className="text-center mb-2">Admin Login</h2>

              <p className="text-center text-body-secondary mb-4">
                後台管理系統
              </p>

              <form onSubmit={handleLogin}>
                {/* Username */}
                <div className="mb-3">
                  <label className="form-label">Username</label>

                  <input
                    type="text"
                    className="form-control"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                  />
                </div>

                {/* Password */}
                <div className="mb-4">
                  <label className="form-label">Password</label>

                  <input
                    type="password"
                    className="form-control"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                  />
                </div>

                <button type="submit" className="btn btn-dark w-100">
                  Admin 登入
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default AdminLogin;
