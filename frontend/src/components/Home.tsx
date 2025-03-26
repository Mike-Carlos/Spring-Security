import React from "react";
import { useNavigate } from "react-router-dom";
import InventoryList from "./InventoryList";

const Home = () => {
  const navigate = useNavigate();

  const handleLogout = async () => {
    const token = localStorage.getItem("token");
    if (!token) {
      alert("No valid token found. Please login.");
      navigate("/login");
      return;
    }

    try {
      const response = await fetch("http://localhost:8080/api/auth/logout", {
        method: "POST",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(errorMessage || "Logout failed");
      }

      alert("Logout successful");
      localStorage.removeItem("token"); // Remove token from storage
      navigate("/login");
    } catch (error: any) {
      alert(error.message);
    }
  };

  return (
    <div className="container mt-5">
      <h1>Welcome to Home Page</h1>

      <button className="btn btn-danger" onClick={handleLogout}>
        Logout
      </button>
      <InventoryList />
    </div>
  );
};

export default Home;
