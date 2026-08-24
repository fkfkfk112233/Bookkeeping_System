import { NavLink, useNavigate } from "react-router-dom";

function AdminNavbar() {
  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("currentUser");

    navigate("/login/admin");
  };

  return (
    <nav className="navbar navbar-expand-lg bg-dark navbar-dark">
      <div className="container">
        {/* Brand */}
        <NavLink className="navbar-brand" to="/admin">
          Bookkeeping Admin
        </NavLink>

        {/* Mobile Toggle */}
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#adminNavbar"
          aria-controls="adminNavbar"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        {/* Navbar Content */}
        <div className="collapse navbar-collapse" id="adminNavbar">
          {/* Left */}
          <div className="navbar-nav me-auto">
            <NavLink className="nav-link" to="/admin">
              Dashboard
            </NavLink>

            <NavLink className="nav-link" to="/admin/users">
              User Management
            </NavLink>
          </div>

          {/* Right */}
          <div className="d-flex align-items-lg-center gap-3 mt-3 mt-lg-0">
            <span className="text-white">Admin</span>

            <button
              type="button"
              className="btn btn-outline-light btn-sm"
              onClick={handleLogout}
            >
              登出
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
}

export default AdminNavbar;
