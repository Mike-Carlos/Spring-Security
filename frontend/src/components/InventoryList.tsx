import React, { useEffect, useState } from "react";
import {
  fetchInventory,
  fetchInventoryByCategory,
  searchInventoryByName,
  deleteInventory,
} from "../config/InventoryApi";
import { useNavigate } from "react-router-dom";

const InventoryList = () => {
  const navigate = useNavigate();
  const [inventory, setInventory] = useState<any[]>([]);
  const [category, setCategory] = useState("");
  const [searchTerm, setSearchTerm] = useState("");

  const loadInventory = async () => {
    const data = await fetchInventory();
    setInventory(data);
  };

  const handleCategoryFilter = async () => {
    if (!category) {
      loadInventory();
    } else {
      const data = await fetchInventoryByCategory(category);
      setInventory(data);
    }
  };

  const handleSearch = async () => {
    const data = await searchInventoryByName(searchTerm);
    setInventory(data);
  };

  const handleDelete = async (id: number) => {
    await deleteInventory(id);
    loadInventory();
  };

  useEffect(() => {
    loadInventory();
  }, []);

  return (
    <div className="container mt-5">
      <h2>Inventory Management</h2>

      <div className="mb-3">
        <input
          type="text"
          className="form-control mb-2"
          placeholder="Search by Name"
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <button className="btn btn-primary me-2" onClick={handleSearch}>
          Search
        </button>

        <input
          type="text"
          className="form-control mb-2"
          placeholder="Filter by Category"
          value={category}
          onChange={(e) => setCategory(e.target.value)}
        />
        <button className="btn btn-secondary" onClick={handleCategoryFilter}>
          Filter
        </button>
      </div>

      <div className="row mb-3">
        <div className="col-12">
          <button
            className="btn btn-primary w-100"
            onClick={() => navigate("/AddInventory")}
          >
            Add Product
          </button>
        </div>
      </div>
      <table className="table table-bordered">
        <thead>
          <tr>
            <th>Name</th>
            <th>Description</th>
            <th>Quantity</th>
            <th>Price</th>
            <th>Category</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {inventory.map((item) => (
            <tr key={item.id}>
              <td>{item.name}</td>
              <td>{item.description}</td>
              <td>{item.quantity}</td>
              <td>${item.price.toFixed(2)}</td>
              <td>{item.category}</td>
              <td>
                <button className="btn btn-primary me-2" onClick={() => navigate(`/edit/${item.id}`)}>Edit</button>
              </td>
              <td>
                <button
                  className="btn btn-danger me-2"
                  onClick={() => handleDelete(item.id)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default InventoryList;
