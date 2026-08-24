import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
  getUserProfile,
  updateUserProfile,
  disableUser,
} from "../services/userApi";

function Profile() {
  const navigate = useNavigate();

  const [user, setUser] = useState(null);

  const [formData, setFormData] = useState({
    username: "",
    password: "",
    email: "",
  });

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    const currentUser = JSON.parse(localStorage.getItem("currentUser"));

    if (!currentUser) {
      navigate("/login");
      return;
    }

    fetchProfile(currentUser.id);
  }, [navigate]);

  const fetchProfile = async (id) => {
    try {
      const data = await getUserProfile(id);

      setUser(data);

      setFormData({
        username: data.username,
        password: "",
        email: data.email,
      });
    } catch (error) {
      console.error("取得 Profile 失敗:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!user) {
      return;
    }

    try {
      setSaving(true);

      const updatedUser = await updateUserProfile(user.id, formData);

      setUser(updatedUser);

      setFormData({
        username: updatedUser.username,
        password: "",
        email: updatedUser.email,
      });

      // 更新目前登入者
      localStorage.setItem("currentUser", JSON.stringify(updatedUser));

      alert("資料更新成功");
    } catch (error) {
      console.error("更新 Profile 失敗:", error);

      alert("資料更新失敗");
    } finally {
      setSaving(false);
    }
  };

  const handleDisableAccount = async () => {
    if (!user) {
      return;
    }

    const confirmed = window.confirm(
      "確定要停用自己的帳號嗎？\n停用後將無法登入。",
    );

    if (!confirmed) {
      return;
    }

    try {
      await disableUser(user.id);

      localStorage.removeItem("currentUser");

      navigate("/login");
    } catch (error) {
      console.error("停用帳號失敗:", error);

      alert("停用帳號失敗");
    }
  };

  if (loading) {
    return (
      <div className="container py-4">
        <p>Loading...</p>
      </div>
    );
  }

  if (!user) {
    return (
      <div className="container py-4">
        <p>找不到 User 資料。</p>
      </div>
    );
  }

  return (
    <div className="container py-4">
      <div className="mb-4">
        <h1>My Profile</h1>

        <p className="text-body-secondary">管理自己的帳號資料</p>
      </div>

      <div className="row">
        <div className="col-12 col-lg-8">
          <div className="card shadow-sm">
            <div className="card-body">
              <form onSubmit={handleSubmit}>
                {/* Username */}
                <div className="mb-3">
                  <label className="form-label">Username</label>

                  <input
                    type="text"
                    className="form-control"
                    name="username"
                    value={formData.username}
                    onChange={handleChange}
                    required
                  />
                </div>

                {/* Email */}
                <div className="mb-3">
                  <label className="form-label">Email</label>

                  <input
                    type="email"
                    className="form-control"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    required
                  />
                </div>

                {/* Password */}
                <div className="mb-4">
                  <label className="form-label">Password</label>

                  <input
                    type="password"
                    className="form-control"
                    name="password"
                    value={formData.password}
                    onChange={handleChange}
                    placeholder="留空表示不修改密碼"
                  />
                </div>

                {/* Role */}
                <div className="mb-3">
                  <label className="form-label">Role</label>

                  <input
                    type="text"
                    className="form-control"
                    value={user.role}
                    disabled
                  />
                </div>

                {/* Status */}
                <div className="mb-4">
                  <label className="form-label">Status</label>

                  <input
                    type="text"
                    className="form-control"
                    value={user.enabled ? "Enabled" : "Disabled"}
                    disabled
                  />
                </div>

                <button
                  type="submit"
                  className="btn btn-primary"
                  disabled={saving}
                >
                  {saving ? "儲存中..." : "儲存修改"}
                </button>
              </form>
            </div>
          </div>
        </div>

        {/* Danger Zone */}
        <div className="col-12 col-lg-4 mt-4 mt-lg-0">
          <div className="card border-danger">
            <div className="card-body">
              <h5 className="text-danger">Danger Zone</h5>

              <p className="text-body-secondary">
                停用帳號後將無法登入。 你的分類與交易資料仍會保留。
              </p>

              <button
                type="button"
                className="btn btn-outline-danger"
                onClick={handleDisableAccount}
              >
                停用我的帳號
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Profile;
