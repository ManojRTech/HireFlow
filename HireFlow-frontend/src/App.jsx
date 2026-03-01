import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Jobs from "./pages/Jobs";
import MyApplications from "./pages/MyApplications";
import EmployerDashboard from "./pages/EmployerDashboard";
import ProtectedRoute from "./components/ProtectedRoute";
import ApplyJob from "./pages/ApplyJob";
import AddJob from "./pages/AddJob";
import MyJobs from "./pages/MyJobs";
import EmployerApplications from "./pages/EmployerApplications";
import EditJob from "./pages/EditJobs";
import Home from "./pages/Home";
import JobDetails from "./pages/JobDetails";
import Footer from "./components/Footer";

function App() {
  return (
    <div className="min-h-screen flex flex-col flex-grow">
      <Navbar />

      <main className="flex-grow">
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          <Route
            path="/jobs"
            element={
              <ProtectedRoute>
                <Jobs />
              </ProtectedRoute>
            }
          />

          <Route
            path="/jobs/:id"
            element={<JobDetails />}
          />

          {/* Job Seeker */}
          <Route
            path="/my-applications"
            element={
              <ProtectedRoute allowedRole="JOB_SEEKER">
                <MyApplications />
              </ProtectedRoute>
            }
          />

          {/* Employer */}
          <Route
            path="/employer-dashboard"
            element={
              <ProtectedRoute allowedRole="EMPLOYER">
                <EmployerDashboard />
              </ProtectedRoute>
            }
          />

          <Route
            path="/jobs/:id/apply"
            element={
              <ProtectedRoute allowedRole="JOB_SEEKER">
                <ApplyJob />
              </ProtectedRoute>
            }
          />

          <Route
            path="/employer/add-job"
            element={
              <ProtectedRoute allowedRole="EMPLOYER">
                <AddJob />
              </ProtectedRoute>
            }
          />

          <Route
            path="/employer/my-jobs"
            element={
              <ProtectedRoute allowedRole="EMPLOYER">
                <MyJobs />
              </ProtectedRoute>
            }
          />

          <Route
            path="/employer/applications/:jobId"
            element={
              <ProtectedRoute allowedRole="EMPLOYER">
                <EmployerApplications />
              </ProtectedRoute>
            }
          />

          <Route
            path="/employer/edit-job/:id"
            element={
              <ProtectedRoute allowedRole="EMPLOYER">
                <EditJob />
              </ProtectedRoute>
            }
          />
          
        </Routes>
      </main>

      <Footer />
      
    </div>
  );
}

export default App;