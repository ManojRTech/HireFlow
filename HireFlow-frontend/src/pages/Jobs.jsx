import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";

function Jobs() {

  const navigate = useNavigate();
  const [jobs, setJobs] = useState([]);
  const [totalJobs, setTotalJobs] = useState(0);
  const [page, setPage] = useState(0);
  const [totalPages, setTotalPages] = useState(0);
  const [keyword, setKeyword] = useState("");
  const [sort, setSort] = useState("latest");

  useEffect(() => {
    fetchJobs();
  }, [page, keyword, sort]);

  const fetchJobs = async () => {
    try {
      const res = await api.get(`/jobs?keyword=${keyword}&sort=${sort}&page=${page}&size=6`);
      console.log("Jobs response:", res.data);
      setJobs(res.data.content);
      setTotalJobs(res.data.totalElements);
      setTotalPages(res.data.totalPages);
    } catch (err) {
      console.error("Failed to fetch jobs", err);
    }
  };

  return (
    <div className="max-w-6xl mx-auto p-8">

      <div className="bg-white p-4 shadow rounded-lg mb-6 flex flex-wrap gap-4">

        <input
          type="text"
          placeholder="Search by job, company or skill..."
          className="flex-1 p-2 border rounded"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
        />

        <select
          value={sort}
          onChange={(e) => setSort(e.target.value)}
          className="border p-2 rounded"
        >
          <option value="latest">Latest</option>
          <option value="salary_desc">Salary: High to Low</option>
          <option value="salary_asc">Salary: Low to High</option>
        </select>

      </div>

      <h2 className="text-3xl font-bold mb-6">
        Available Jobs
      </h2>

      {jobs.length === 0 && (
        <p className="text-gray-500">No jobs available</p>
      )}

      <div className="bg-indigo-50 border border-indigo-200 rounded-lg p-4 mb-6">
        <p className="text-indigo-700 font-semibold">
          Total Open Positions: {totalJobs}
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">

        {jobs.map((job) => (
          <div
            key={job.id}
            className="bg-white p-6 rounded-xl shadow-md hover:shadow-xl transition-all duration-300"
          >
            <h3 className="text-xl font-semibold">
              {job.title}
            </h3>

            <p className="font-medium text-indigo-600">
              {job.company}
            </p>

            <p className="font-medium text-indigo-600">
              ₹ {job.salary}
            </p>

            <button
              onClick={() => navigate(`/jobs/${job.id}`)}
              className="bg-indigo-600 text-white px-4 py-2 rounded"
            >
              View Job
            </button>

          </div>
        ))}

      </div>

      <div className="flex justify-center gap-4 mt-6">
        <button
          disabled={page === 0}
          onClick={() => setPage(page - 1)}
          className="px-4 py-2 bg-gray-300 rounded"
        >
          Prev
        </button>

        <span>Page {page + 1} of {totalPages}</span>

        <button
          disabled={page === totalPages - 1}
          onClick={() => setPage(page + 1)}
          className="px-4 py-2 bg-gray-300 rounded"
        >
          Next
        </button>
      </div>
    </div>
  );
}

export default Jobs;