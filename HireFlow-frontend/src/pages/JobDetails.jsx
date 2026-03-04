import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import api from "../services/api";

function JobDetails() {

  const { id } = useParams();
  const [job, setJob] = useState(null);

  useEffect(() => {
    fetchJob();
  }, [id]);

  const fetchJob = async () => {
    try {
      const res = await api.get(`/jobs/${id}`);
      setJob(res.data);
    } catch (err) {
      console.error("Failed to fetch job", err);
    }
  };

  const role = localStorage.getItem("role");

  return (
    <div className="max-w-2xl mt-20 mx-auto p-8 bg-white shadow-md rounded-xl">
      {job && (
        <>
          <h2 className="text-2xl font-bold">{job.title}</h2>
          <p className="text-gray-600">{job.company}</p>
          <p className="text-gray-600">{job.location}</p>
          <p className="mt-2">{job.description}</p>
          <p className="mt-4 font-semibold">₹ {job.salary}</p>

          {role === "JOB_SEEKER" && (
            <button
              onClick={() => Navigate(`/jobs/${id}/apply`)}
              className="mt-6 bg-green-600 text-white px-4 py-2 rounded"
            >
              Apply Now
            </button>
          )}
        </>
      )}
    </div>
  );
}

export default JobDetails;