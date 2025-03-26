import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const Register = () => {
    const [formData, setFormData] = useState({
      username: "",
      password: "",
      role: "USER",
      adminUsername: "", // Add admin credentials
      adminPassword: ""
    });
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);
    const navigate = useNavigate();
  
    const handleRegister = async (e: React.FormEvent<HTMLFormElement>) => {
      e.preventDefault();
      setError(null);
      setSuccess(null);
  
      try {
        // First, login as admin
        const loginResponse = await fetch("http://localhost:8080/api/authenticate", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ 
            username: formData.adminUsername, 
            password: formData.adminPassword 
          }),
        });
  
        if (!loginResponse.ok) {
          throw new Error("Admin authentication failed");
        }
  
        const token = await loginResponse.text();
  
        // Then register new user with admin token
        const registerResponse = await fetch("http://localhost:8080/api/register", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
          },
          body: JSON.stringify({ 
            username: formData.username, 
            password: formData.password,
            role: formData.role 
          }),
        });
  
        if (!registerResponse.ok) {
          const errorMessage = await registerResponse.text();
          throw new Error(errorMessage || "Registration failed");
        }
  
        setSuccess("Registration successful!");
        setTimeout(() => navigate("/login"), 2000);
      } catch (err: any) {
        setError(err.message);
      }
    };
  
    return (
      <div className="container mt-5">
        <h2>Register New User (Admin Only)</h2>
        {error && <div className="alert alert-danger">{error}</div>}
        {success && <div className="alert alert-success">{success}</div>}
        
        <form onSubmit={handleRegister}>
          <h4 className="mt-4">Admin Credentials</h4>
          <div className="mb-3">
            <label className="form-label">Admin Username</label>
            <input
              type="text"
              className="form-control"
              value={formData.adminUsername}
              onChange={(e) => setFormData({...formData, adminUsername: e.target.value})}
              required
            />
          </div>
          <div className="mb-3">
            <label className="form-label">Admin Password</label>
            <input
              type="password"
              className="form-control"
              value={formData.adminPassword}
              onChange={(e) => setFormData({...formData, adminPassword: e.target.value})}
              required
            />
          </div>
  
          <h4 className="mt-4">New User Details</h4>
          <div className="mb-3">
            <label className="form-label">Username</label>
            <input
              type="text"
              className="form-control"
              value={formData.username}
              onChange={(e) => setFormData({...formData, username: e.target.value})}
              required
            />
          </div>
          <div className="mb-3">
            <label className="form-label">Password</label>
            <input
              type="password"
              className="form-control"
              value={formData.password}
              onChange={(e) => setFormData({...formData, password: e.target.value})}
              required
            />
          </div>
          <div className="mb-3">
            <label className="form-label">Role</label>
            <select
              className="form-select"
              value={formData.role}
              onChange={(e) => setFormData({...formData, role: e.target.value})}
            >
              <option value="USER">User</option>
              <option value="ADMIN">Admin</option>
            </select>
          </div>
          <button type="submit" className="btn btn-primary">
            Register
          </button>
        </form>
      </div>
    );
  };

export default Register;
