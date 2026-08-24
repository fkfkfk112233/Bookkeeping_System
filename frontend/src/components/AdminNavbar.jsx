import { Link } from "react-router-dom";

function AdminNavbar() {
  return (
    <nav className="navbar navbar-expand-lg bg-dark navbar-dark">
      <div className="container">
        <Link className="navbar-brand" to="/admin">
          Bookkeeping Admin
        </Link>

        <div className="navbar-nav">
          <Link className="nav-link" to="/admin">
            Dashboard
          </Link>

          <Link className="nav-link" to="/admin/users">
            User Management
          </Link>
        </div>
      </div>
    </nav>
  );
}

export default AdminNavbar;
