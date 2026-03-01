import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../services/api";

function EmployerApplications() {

  const { jobId } = useParams();
  const [applications, setApplications] = useState([]);
  const [selectedJob, setSelectedJob] = useState(null);

  useEffect(() => {
    setSelectedJob(jobId);
    fetchApplications();
  }, []);

  const fetchApplications = async () => {
    try {
      const res = await api.get(`/applications/job/${jobId}`);
      setApplications(res.data);
    } catch (err) {
      console.error("Failed");
    }
  };

  const updateStatus = async (applicationId, status) => {
    try {
      await api.put(
        `/applications/${applicationId}/status?status=${status}`
      );
      fetchApplications(selectedJob);
    } catch (err) {
      console.error("Failed to update status");
    }
  };

  return (
    <div className="max-w-4xl mx-auto p-8">
      <h3 className="text-2xl font-semibold mb-4">
            Applications
      </h3>

      {applications.length === 0 ? (
            <div className="bg-gray-100 p-6 rounded-lg text-center text-gray-600">
              No applications yet for this job.
            </div>
          ) : (
            applications.map((app) => (
              <div
                key={app.applicationId}
                className="bg-white p-5 rounded-xl shadow-md mb-4 border"
              >
                <p><strong>Name:</strong> {app.applicantName}</p>
                <p><strong>Email:</strong> {app.applicantEmail}</p>

                <p className="mt-2">
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

                <a
                  href={`http://localhost:8080/uploads/${app.resumeUrl}`}
                  target="_blank"
                  rel="noreferrer"
                  className="inline-block mt-3 text-indigo-600 underline"
                >
                  View Resume
                </a>

                <div className="mt-4 flex gap-3">
                  <button
                    onClick={() => updateStatus(app.applicationId, "SHORTLISTED")}
                    className="bg-green-600 text-white px-4 py-1 rounded-lg"
                  >
                    Shortlist
                  </button>

                  <button
                    onClick={() => updateStatus(app.applicationId, "REJECTED")}
                    className="bg-red-600 text-white px-4 py-1 rounded-lg"
                  >
                    Reject
                  </button>
                </div>
              </div>
            ))
          )}
    </div>
  );
}

export default EmployerApplications;


