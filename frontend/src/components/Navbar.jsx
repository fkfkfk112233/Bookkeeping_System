import { NavLink } from "react-router-dom";

function Navbar() {
  return (
    <nav className="navbar navbar-expand-lg bg-white border-bottom">
      <div className="container-fluid px-4 px-lg-5">
        {/* Logo / Brand */}
        <NavLink className="navbar-brand fw-semibold" to="/">
          Bookkeeping System
        </NavLink>

        {/* Mobile Toggle */}
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarContent"
          aria-controls="navbarContent"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        {/* Navbar Content */}
        <div className="collapse navbar-collapse" id="navbarContent">
          {/* Center Navigation */}
          <ul className="navbar-nav mx-auto mb-2 mb-lg-0 gap-lg-4">
            <li className="nav-item">
              <NavLink className="nav-link" to="/">
                Dashboard
              </NavLink>
            </li>

            <li className="nav-item">
              <NavLink className="nav-link" to="/transactions">
                記帳
              </NavLink>
            </li>

            <li className="nav-item">
              <NavLink className="nav-link" to="/categories">
                分類管理
              </NavLink>
            </li>
          </ul>

          {/* Right Side */}
          <div className="d-flex align-items-center gap-3">
            <span className="text-body-secondary">Admin</span>

            <button type="button" className="btn btn-primary px-4">
              登出
            </button>
          </div>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
