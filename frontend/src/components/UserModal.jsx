import { useState } from "react";

function UserModal({ show, onClose, onSubmit }) {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    email: "",
    role: "USER",
    enabled: true,
  });

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

  if (!show) {
    return null;
  }

  return (
    <div
      className="modal d-block"
      tabIndex="-1"
      style={{ backgroundColor: "rgba(0, 0, 0, 0.5)" }}
    >
      <div className="modal-dialog">
        <div className="modal-content">
          <div className="modal-header">
            <h5 className="modal-title">新增 User</h5>

            <button type="button" className="btn-close" onClick={onClose} />
          </div>

          <form onSubmit={handleSubmit}>
            <div className="modal-body">
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

              {/* Password */}
              <div className="mb-3">
                <label className="form-label">Password</label>

                <input
                  type="password"
                  className="form-control"
                  name="password"
                  value={formData.password}
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

              {/* Role */}
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

              {/* Enabled */}
              <div className="form-check">
                <input
                  type="checkbox"
                  className="form-check-input"
                  id="enabled"
                  name="enabled"
                  checked={formData.enabled}
                  onChange={handleChange}
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
                新增
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default UserModal;
