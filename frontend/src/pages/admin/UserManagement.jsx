import { useEffect, useState } from "react";

import {
  getUsers,
  createUser,
  updateUser,
  deleteUser,
} from "../../services/userApi";

import UserModal from "../../components/UserModal";

function UserManagement() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [editingUser, setEditingUser] = useState(null);
  const [searchText, setSearchText] = useState("");
  const [roleFilter, setRoleFilter] = useState("ALL");
  const [statusFilter, setStatusFilter] = useState("ALL");

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      setLoading(true);

      const data = await getUsers();

      setUsers(data);
    } catch (error) {
      console.error("取得 User 失敗:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateUser = async (userData) => {
    try {
      await createUser(userData);

      setShowModal(false);

      await fetchUsers();
    } catch (error) {
      console.error("新增 User 失敗:", error);
    }
  };

  const handleUpdateUser = async (userData) => {
    try {
      await updateUser(editingUser.id, userData);

      setShowModal(false);
      setEditingUser(null);

      await fetchUsers();
    } catch (error) {
      console.error("更新 User 失敗:", error);
    }
  };

  const handleDeleteUser = async (user) => {
    const confirmed = window.confirm(`確定要刪除 User「${user.username}」嗎？`);

    if (!confirmed) {
      return;
    }

    try {
      await deleteUser(user.id);

      await fetchUsers();
    } catch (error) {
      console.error("刪除 User 失敗:", error);

      const message = error.response?.data?.message || "刪除 User 失敗";

      window.alert(message);
    }
  };

  const formatDateTime = (dateTime) => {
    if (!dateTime) {
      return "-";
    }

    const date = new Date(dateTime);

    return date.toLocaleString("zh-TW", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  const filteredUsers = users.filter((user) => {
    const keyword = searchText.toLowerCase();

    const matchesSearch =
      user.username.toLowerCase().includes(keyword) ||
      user.email.toLowerCase().includes(keyword);

    const matchesRole = roleFilter === "ALL" || user.role === roleFilter;

    const matchesStatus =
      statusFilter === "ALL" ||
      (statusFilter === "ENABLED" && user.enabled) ||
      (statusFilter === "DISABLED" && !user.enabled);

    return matchesSearch && matchesRole && matchesStatus;
  });

  return (
    <div className="container py-4">
      <div className="d-flex flex-column flex-md-row justify-content-between align-items-md-center gap-3 mb-4">
        <div>
          <h1 className="mb-1">User Management</h1>

          <p className="text-body-secondary mb-0">管理系統使用者</p>
        </div>

        <button
          className="btn btn-primary align-self-md-auto"
          onClick={() => {
            setEditingUser(null);
            setShowModal(true);
          }}
        >
          新增 User
        </button>
      </div>

      <div className="row g-2 mb-4">
        <div className="card shadow-sm mb-4">
          <div className="card-body">
            <div className="row g-3">
              {/* Search */}
              <div className="col-12 col-md-6 col-lg-5">
                <label className="form-label">搜尋</label>

                <input
                  type="text"
                  className="form-control"
                  placeholder="Username 或 Email"
                  value={searchText}
                  onChange={(e) => setSearchText(e.target.value)}
                />
              </div>

              {/* Role */}
              <div className="col-12 col-md-3 col-lg-2">
                <label className="form-label">Role</label>

                <select
                  className="form-select"
                  value={roleFilter}
                  onChange={(e) => setRoleFilter(e.target.value)}
                >
                  <option value="ALL">全部角色</option>

                  <option value="USER">USER</option>

                  <option value="ADMIN">ADMIN</option>
                </select>
              </div>

              {/* Status */}
              <div className="col-12 col-md-3 col-lg-2">
                <label className="form-label">Status</label>

                <select
                  className="form-select"
                  value={statusFilter}
                  onChange={(e) => setStatusFilter(e.target.value)}
                >
                  <option value="ALL">全部狀態</option>

                  <option value="ENABLED">Enabled</option>

                  <option value="DISABLED">Disabled</option>
                </select>
              </div>
            </div>
          </div>
        </div>

        {/* Status */}
        <div className="col-12 col-md-3 col-lg-2">
          <select
            className="form-select"
            value={statusFilter}
            onChange={(e) => setStatusFilter(e.target.value)}
          >
            <option value="ALL">全部狀態</option>
            <option value="ENABLED">Enabled</option>
            <option value="DISABLED">Disabled</option>
          </select>
        </div>
      </div>

      {loading ? (
        <p>Loading...</p>
      ) : (
        <div className="card shadow-sm">
          <div className="card-body p-0">
            <div className="table-responsive">
              <table className="table table-hover align-middle mb-0">
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Role</th>
                    <th>Status</th>
                    <th>Created At</th>
                    <th>Action</th>
                  </tr>
                </thead>

                <tbody>
                  {filteredUsers.length === 0 ? (
                    <tr>
                      <td
                        colSpan="7"
                        className="text-center py-4 text-body-secondary"
                      >
                        沒有符合條件的 User
                      </td>
                    </tr>
                  ) : (
                    filteredUsers.map((user) => (
                      <tr key={user.id}>
                        <td>{user.id}</td>

                        <td>{user.username}</td>

                        <td>{user.email}</td>

                        <td>
                          <span className="badge bg-secondary">
                            {user.role}
                          </span>
                        </td>

                        <td>
                          {user.enabled ? (
                            <span className="badge bg-success">Enabled</span>
                          ) : (
                            <span className="badge bg-danger">Disabled</span>
                          )}
                        </td>

                        <td>{formatDateTime(user.createdAt)}</td>

                        <td>
                          <button
                            className="btn btn-sm btn-outline-primary me-2"
                            onClick={() => {
                              setEditingUser(user);
                              setShowModal(true);
                            }}
                          >
                            編輯
                          </button>

                          <button
                            className="btn btn-sm btn-outline-danger"
                            onClick={() => handleDeleteUser(user)}
                          >
                            刪除
                          </button>
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}

      <UserModal
        show={showModal}
        editingUser={editingUser}
        onClose={() => {
          setShowModal(false);
          setEditingUser(null);
        }}
        onSubmit={editingUser ? handleUpdateUser : handleCreateUser}
      />
    </div>
  );
}

export default UserManagement;
