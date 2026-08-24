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

  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <div>
          <h1>User Management</h1>

          <p className="text-body-secondary mb-0">管理系統使用者</p>
        </div>

        <button
          className="btn btn-primary"
          onClick={() => {
            setEditingUser(null);
            setShowModal(true);
          }}
        >
          新增 User
        </button>
      </div>

      {loading ? (
        <p>Loading...</p>
      ) : (
        <div className="table-responsive">
          <table className="table table-hover align-middle">
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
              {users.map((user) => (
                <tr key={user.id}>
                  <td>{user.id}</td>

                  <td>{user.username}</td>

                  <td>{user.email}</td>

                  <td>
                    <span className="badge bg-secondary">{user.role}</span>
                  </td>

                  <td>
                    {user.enabled ? (
                      <span className="badge bg-success">Enabled</span>
                    ) : (
                      <span className="badge bg-danger">Disabled</span>
                    )}
                  </td>

                  <td>{user.createdAt ?? "-"}</td>

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
              ))}
            </tbody>
          </table>
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
