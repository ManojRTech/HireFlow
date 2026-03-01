import { useState } from "react";
import api from "../services/api";

function AddJob() {

  const [form, setForm] = useState({
    title: "",
    description: "",
    company: "",
    location: "",
    salary: ""
  });

  const handleSubmit = async () => {
    try {

      if (!form.company.trim()) {
        alert("Company is required");
        return;
      }

      await api.post("/jobs", {
        ...form,
        salary: Number(form.salary)
      });

      alert("Job created successfully");

    } catch (err) {
      alert(err.response?.data?.message || "Failed to create job");
    }
  };

  return (
    <div className="max-w-md mt-20 mx-auto bg-white p-8 shadow-xl rounded-xl">
      <h2 className="text-2xl font-bold mb-6">Add New Job</h2>

      <input
        type="text"
        placeholder="Title"
        className="w-full mb-4 p-2 border rounded"
        value={form.title}
        onChange={(e) => setForm({ ...form, title: e.target.value })}
      />

      <textarea
        placeholder="Description"
        className="w-full mb-4 p-2 border rounded"
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
        type="text"
        placeholder="Location"
        className="w-full mb-4 p-2 border rounded"
        value={form.location}
        onChange={(e) => setForm({ ...form, location: e.target.value })}
      />

      <input
        type="number"
        placeholder="Salary"
        className="w-full mb-4 p-2 border rounded"
        value={form.salary}
        onChange={(e) => setForm({ ...form, salary: e.target.value })}
      />

      <button
        onClick={handleSubmit}
        className="bg-indigo-600 text-white px-4 py-2 rounded"
      >
        Create Job
      </button>
    </div>
  );
}

export default AddJob;