import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import securityImage2 from "../assets/salute.png";
import securityImage1 from "../assets/image1.png";
import securityImage from "../assets/security.jpg";

const Login = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setError(null);
    setLoading(true);

    try {
      const response = await fetch("http://localhost:8080/api/auth/authenticate", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ username, password }),
      });

      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(errorMessage || "Failed to login");
      }

      const token = await response.text();

      // Store the token securely
      localStorage.setItem("token", token);
      alert("Login successful!");
      setTimeout(() => {
        navigate("/home");
      }, 2000);
    } catch (err: any) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const getCurrentImage = () => {
    if (loading) return securityImage2;
    if (error) return securityImage1;
    return securityImage;
  };

  return (
    <div className="container mt-5">
      <div className="mt-4 text-center">
        <img 
          src={getCurrentImage()}
          alt="Security Image"
          className="img-fluid rounded"
          style={{ maxHeight: "300px" }}
        />
      </div>
      <h2>Login</h2>
      {error && <div className="alert alert-danger">{error}</div>}
      <form onSubmit={handleLogin}>
        <div className="mb-3">
          <label className="form-label">Username</label>
          <input
            type="text"
            className="form-control"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            required
          />
        </div>
        <div className="mb-3">
          <label className="form-label">Password</label>
          <input
            type="password"
            className="form-control"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>
        <button type="submit" className="btn btn-primary" disabled={loading}>
          {loading ? "Logging in..." : "Login"}
        </button>
      </form>

      <p className="mt-3">
        Don't have an account? <a href="/register">Register here</a>
      </p>
    </div>
  );
};

export default Login;
