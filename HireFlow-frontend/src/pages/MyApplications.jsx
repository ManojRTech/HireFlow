import { useEffect, useState } from "react";
import api from "../services/api";

function MyApplications() {

  const [applications, setApplications] = useState([]);

  useEffect(() => {
    fetchMyApplications();
  }, []);

  const fetchMyApplications = async () => {
    try {
      const res = await api.get("/applications/me");
      setApplications(res.data);
    } catch (err) {
      console.error("Failed to fetch applications");
    }
  };

  const cancelApplication = async (id) => {
    try {
      await api.delete(`/applications/${id}`);
      fetchMyApplications();
    } catch (err) {
      alert("Failed to cancel");
    }
  };

  return (
    <div className="max-w-4xl mx-auto p-8">
      <h2 className="text-3xl font-bold mb-6">My Applications</h2>

      <div className="grid gap-6">
        {applications.length === 0 ? (
            <div className="bg-gray-100 p-6 rounded-lg text-center text-gray-600">
              You have not applied to any jobs yet.
            </div>
          ) : (
          applications.map((app) => (
          <div key={app.applicationId} className="bg-white p-6 rounded-xl shadow-md">
            <h3 className="text-xl font-semibold">{app.jobTitle}</h3>
            <p className="text-gray-600">
              Employer: {app.employerName}
            </p>

            <p className="mt-3">
              <strong>Status:</strong>{" "}
              <span
                className={`px-3 py-1 rounded-full text-sm font-semibold ${
                  app.status === "SHORTLISTED"
                    ? "bg-green-100 text-green-700"
                    : app.status === "REJECTED"
                    ? "bg-red-100 text-red-700"
                    : "bg-yellow-100 text-yellow-700"
                }`}
              >
                {app.status}
              </span>
            </p>

            <p className="mt-2 text-gray-600">
              {app.statusMessage}
            </p>

            <button
              onClick={() => cancelApplication(app.applicationId)}
              className="mt-3 bg-gray-600 text-white px-3 py-1 rounded"
            >
              Cancel
            </button>
          </div>
        )))}
      </div>
    </div>
  );
}

export default MyApplications;
