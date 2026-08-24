import { useState } from "react";
import { useNavigate } from "react-router-dom";

import { login } from "../services/authApi";

function Login() {
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const user = await login(username, password);

      // 保存目前登入者的基本資料
      localStorage.setItem("currentUser", JSON.stringify(user));

      // User 登入
      if (user.role === "USER") {
        navigate("/dashboard");
        return;
      }

      // Admin 不從 User Login 進入
      if (user.role === "ADMIN") {
        alert("Admin 請從管理員登入入口登入。");

        localStorage.removeItem("currentUser");

        return;
      }
    } catch (error) {
      console.error("Login failed:", error);

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
              <h2 className="text-center mb-4">Login</h2>

              <form onSubmit={handleLogin}>
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

                <button type="submit" className="btn btn-primary w-100">
                  登入
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;
