import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import api from "../services/api";

function EditJob() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [form, setForm] = useState({
    title: "",
    description: "",
    company: "",
    location: "",
    salary: ""
  });

  useEffect(() => {
    fetchJob();
  }, []);

  const fetchJob = async () => {
    const res = await api.get(`/jobs`);
    const job = res.data.content.find(j => j.id === Number(id));
    setForm(job);
  };

  const handleUpdate = async () => {
    await api.put(`/jobs/${id}`, form);
    alert("Job updated successfully");
    navigate("/employer/my-jobs");
  };

  return (
    <div className="max-w-xl mx-auto p-8 bg-white shadow rounded-xl">
      <h2 className="text-2xl font-bold mb-6">Edit Job</h2>

      <input
        className="w-full mb-3 p-2 border rounded"
        value={form.title}
        onChange={(e) => setForm({ ...form, title: e.target.value })}
      />

      <textarea
        className="w-full mb-3 p-2 border rounded"
        value={form.description}
        onChange={(e) => setForm({ ...form, description: e.target.value })}
      />

      <input
        type="text"
        placeholder="Company Name"
        className="w-full mb-3 p-2 border rounded"
        value={form.company}
        onChange={(e) => setForm({ ...form, company: e.target.value })}
        />

      <input
        className="w-full mb-3 p-2 border rounded"
        value={form.location}
        onChange={(e) => setForm({ ...form, location: e.target.value })}
      />

      <input
        type="number"
        className="w-full mb-3 p-2 border rounded"
        value={form.salary}
        onChange={(e) => setForm({ ...form, salary: e.target.value })}
      />

      <button
        onClick={handleUpdate}
        className="bg-indigo-600 text-white px-4 py-2 rounded"
      >
        Update Job
      </button>
    </div>
  );
}

export default EditJob;