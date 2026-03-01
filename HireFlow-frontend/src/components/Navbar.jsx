import { Link, useNavigate } from "react-router-dom";
import { useLocation } from "react-router-dom";
import { useEffect, useState } from "react";

function Navbar() {
  const navigate = useNavigate();
  const token = localStorage.getItem("token");
  const role = localStorage.getItem("role");

  const location = useLocation();
  const isLoggedIn = !!localStorage.getItem("token");
  const [isLoggedInState, setIsLoggedIn] = useState(isLoggedIn);

  useEffect(() => {
    const token = localStorage.getItem("token");
    setIsLoggedIn(!!token);
  }, [location]);

  const handleLogout = () => {
    localStorage.clear();
    navigate("/login");
  };

  return (
    <nav className="bg-white shadow-md">
      <div className="max-w-6xl mx-auto px-4 py-4 flex justify-between items-center">
        <Link to="/" className="text-3xl font-bold text-indigo-600">
          HireFlow
        </Link>

        <div className="space-x-6">

          {location.pathname !== "/" && (
            <Link to="/" className="px-4 py-2 text-indigo-600 font-medium">
              Home
            </Link>
          )}

          {role === "JOB_SEEKER" && (
            <>
              <Link to="/jobs" className="hover:text-indigo-600">
                Jobs
              </Link>
              <Link to="/my-applications" className="hover:text-indigo-600">
                My Applications
              </Link>
            </>
          )}

          {role === "EMPLOYER" && (
            <>
              <Link to="/employer-dashboard" className="hover:text-indigo-600">
                Dashboard
              </Link>

              <Link to="/employer/my-jobs" className="hover:text-indigo-600">
                My Jobs
              </Link>

              <Link to="/employer/add-job" className="hover:text-indigo-600">
                Add Job
              </Link>
            </>
          )}

          {location.pathname !== "/" && !isLoggedIn && (
            <>
              <Link to="/login" className="px-4 py-2 text-indigo-600">
                Login
              </Link>

              <Link
                to="/register"
                className="bg-indigo-600 text-white px-4 py-2 rounded-xl"
              >
                Register
              </Link>
            </>
          )}

          {role && (
            <button
              onClick={handleLogout}
              className="text-red-500 hover:text-red-600"
            >
              Logout
            </button>
          )}
        </div>
      </div>
    </nav>
  );
}

export default Navbar;