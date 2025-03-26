import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./components/Login";
import Home from "./components/Home";
import Register from "./components/Register";
import AddInventory from "./components/AddInventory";
import ProtectedRoute from "./route/ProtectedRoute";
import PublicRoute from "./route/PublicRoute";
import EditInventory from "./components/EditInventory";

function App() {
  return (
    <Router>
      <Routes>
        {/* Public Routes */}
        <Route path="/" element={<PublicRoute element={<Login />} />} />
        <Route path="/login" element={<PublicRoute element={<Login />} />} />
        <Route
          path="/register"
          element={<PublicRoute element={<Register />} />}
        />

        {/* <Route path="/"element={<Login />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} /> */}

        {/* Protected Routes */}
        <Route path="/home" element={<ProtectedRoute element={<Home />} />} />
        <Route
          path="/AddInventory"
          element={<ProtectedRoute element={<AddInventory />} />}
        />
        <Route
          path="/edit/:id"
          element={<ProtectedRoute element={<EditInventory />} />}
        />
        
        {/* <Route path="/home" element={<Home />} />
        <Route path="/AddInventory" element={<AddInventory />} />
        <Route path="/edit/:id" element={<EditInventory />} /> */}
      </Routes>
    </Router>
  );
}

export default App;
