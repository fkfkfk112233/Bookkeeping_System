import { useEffect, useState } from "react";

function UserModal({ show, onClose, onSubmit, editingUser }) {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    email: "",
    role: "USER",
    enabled: true,
  });

  // 編輯模式：把資料帶進 Modal
  useEffect(() => {
    if (editingUser) {
      setFormData({
        username: editingUser.username,
        password: "",
        email: editingUser.email,
        role: editingUser.role,
        enabled: editingUser.enabled,
      });
    } else {
      // 新增模式：清空表單
      setFormData({
        username: "",
        password: "",
        email: "",
        role: "USER",
        enabled: true,
      });
    }
  }, [editingUser, show]);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    onSubmit(formData);
  };

  if (!show) return null;

  const isEdit = editingUser !== null;

  return (
    <div
      className="modal d-block"
      style={{ backgroundColor: "rgba(0,0,0,.5)" }}
    >
      <div className="modal-dialog">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">
              {isEdit ? "編輯 User" : "新增 User"}
            </h5>

            <button className="btn-close" onClick={onClose} />
          </div>

          <form onSubmit={handleSubmit}>
            <div className="modal-body">
              <div className="mb-3">
                <label className="form-label">Username</label>

                <input
                  className="form-control"
                  name="username"
                  value={formData.username}
                  onChange={handleChange}
                  required
                />
              </div>

              <div className="mb-3">
                <label className="form-label">Password</label>

                <input
                  type="password"
                  className="form-control"
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
                  placeholder={isEdit ? "留空表示不修改密碼" : ""}
                />
              </div>

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

              <div className="mb-3">
                <label className="form-label">Role</label>

                <select
                  className="form-select"
                  name="role"
                  value={formData.role}
                  onChange={handleChange}
                >
                  <option value="USER">USER</option>

                  <option value="ADMIN">ADMIN</option>
                </select>
              </div>

              <div className="form-check">
                <input
                  className="form-check-input"
                  type="checkbox"
                  name="enabled"
                  checked={formData.enabled}
                  onChange={handleChange}
                  id="enabled"
                />

                <label className="form-check-label" htmlFor="enabled">
                  Enabled
                </label>
              </div>
            </div>

            <div className="modal-footer">
              <button
                type="button"
                className="btn btn-secondary"
                onClick={onClose}
              >
                取消
              </button>

              <button type="submit" className="btn btn-primary">
                {isEdit ? "儲存修改" : "新增"}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default UserModal;
