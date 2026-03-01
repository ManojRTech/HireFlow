import { useEffect, useState } from "react";
import api from "../services/api";
import { useNavigate } from "react-router-dom";

function MyJobs() {

  const [jobs, setJobs] = useState([]);
  const navigate = useNavigate();
  
  useEffect(() => {
    fetchJobs();
  }, []);

  const fetchJobs = async () => {
    try {
      const res = await api.get("/jobs/me");
      setJobs(res.data);
    } catch (err) {
      console.error("Failed to fetch jobs");
    }
  };

  const deleteJob = async (jobId) => {
    try {
      await api.delete(`/jobs/${jobId}`);
      alert("Job deleted successfully");
      fetchJobs();
    } catch (err) {
      alert(
        err.response?.data?.message ||
        "Cannot delete job. Applications may exist."
      );
    }
  };

    const fetchApplications = async (jobId) => {
    try {
      const res = await api.get(`/applications/job/${jobId}`);
      setApplications(res.data);
      setSelectedJob(jobId);
    } catch (err) {
      console.error("Failed to fetch applications");
    }
  };

  return (
    <div className="max-w-4xl mx-auto p-8">
      <h2 className="text-2xl font-bold mb-6">My Posted Jobs</h2>

      <div className="grid gap-6">
        {jobs.length === 0 ? (
            <div className="bg-gray-100 p-6 rounded-lg text-center text-gray-600">
              You have not posted any jobs yet.
            </div>
          ) : (jobs.map((job) => (
          <div key={job.id} className="bg-white p-6 shadow-md rounded-xl">
            <h3 className="font-semibold text-lg">{job.title}</h3>
            <p className="text-gray-600">{job.company}</p>
            <p className="text-gray-600">{job.location}</p>
            <p className="mt-2">₹ {job.salary}</p>

            <div className="flex gap-3 mt-4">
              <button
                onClick={() => navigate(`/jobs/${job.id}`)}
                className="bg-blue-500 text-white px-3 py-1 rounded"
              >
                View Job
              </button>

              <button
                onClick={() => navigate(`/employer/applications/${job.id}`)}
                className="bg-indigo-600 text-white px-3 py-1 rounded"
              >
                View Applications
              </button>

              <button
                onClick={() => deleteJob(job.id)}
                className="bg-red-500 text-white px-3 py-1 rounded"
              >
                Delete
              </button>
            </div>
          </div>
        )))}

      </div>
    </div>
  );
}

export default MyJobs;