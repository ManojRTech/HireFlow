import { useParams, useNavigate } from "react-router-dom";
import { useState } from "react";
import api from "../services/api";

function ApplyJob() {

  const { id } = useParams();
  const navigate = useNavigate();

  const [file, setFile] = useState(null);

  const handleApply = async () => {
    if (!file) {
      alert("Upload resume");
      return;
    }

    const formData = new FormData();
    formData.append("resume", file);

    try {
      await api.post(`/applications/${id}`, formData, {
        headers: { "Content-Type": "multipart/form-data" }
      });

      alert("Applied successfully");
      navigate("/my-applications");

    } catch (err) {
      alert(err.response?.data?.message || "Failed");
    }
  };

  return (
    <div className="max-w-md mx-auto mt-16 bg-white p-8 rounded-xl shadow-lg">
      <h2 className="text-2xl font-bold mb-6 text-center">
        Apply for Job
      </h2>

      <input
        type="file"
        onChange={(e) => setFile(e.target.files[0])}
        className="mb-4"
      />

      <button
        onClick={handleApply}
        className="w-full bg-indigo-600 text-white py-2 rounded-lg"
      >
        Submit Application
      </button>
    </div>
  );
}

export default ApplyJob;