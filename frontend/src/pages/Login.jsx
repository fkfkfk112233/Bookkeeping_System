import { useState } from "react";
import { useNavigate } from "react-router-dom";

function Login() {
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      const user = await getUserByUsername(username);

      if (!user.enabled) {
        alert("此帳號已停用");
        return;
      }

      localStorage.setItem("currentUser", JSON.stringify(user));

      navigate("/dashboard");
    } catch (error) {
      console.error("Login failed:", error);

      alert("找不到此 User");
    }
  };

  return (
    <div className="container py-5">
      <div className="row justify-content-center">
        <div className="col-12 col-md-6 col-lg-4">
          <div className="card shadow-sm">
            <div className="card-body p-4">
              <h2 className="text-center mb-4">User Login</h2>

              <form onSubmit={handleLogin}>
                <div className="mb-3">
                  <label className="form-label">Username</label>

                  <input
                    type="text"
                    className="form-control"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                  />
                </div>

                <div className="mb-4">
                  <label className="form-label">Password</label>

                  <input
                    type="password"
                    className="form-control"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
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
