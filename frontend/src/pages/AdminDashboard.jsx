function AdminDashboard() {
  return (
    <div className="container py-4">
      {/* Page Header */}
      <div className="mb-4">
        <h1 className="mb-1">Admin Dashboard</h1>

        <p className="text-body-secondary mb-0">後台管理系統</p>
      </div>

      {/* Content */}
      <div className="row g-4">
        <div className="col-12 col-md-6 col-lg-4">
          <div className="card h-100 shadow-sm">
            <div className="card-body">
              <h5 className="card-title">User Management</h5>

              <p className="card-text text-body-secondary">管理系統使用者。</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default AdminDashboard;
