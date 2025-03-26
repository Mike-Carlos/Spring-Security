import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

const EditInventory = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const token = localStorage.getItem('token');

  const [inventory, setInventory] = useState({
    name: '',
    category: '',
    quantity: 0,
    price: 0,
  });

  useEffect(() => {
    if (!token) {
      alert('You are not authorized. Please log in.');
      navigate('/login');
      return;
    }

    const fetchInventory = async () => {
      try {
        const response = await fetch(`http://localhost:8080/api/inventory/${id}`, {
          method: 'GET',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (!response.ok) throw new Error('Failed to fetch inventory item');

        const data = await response.json();
        setInventory(data);
      } catch (error: any) {
        alert(error.message);
      }
    };

    fetchInventory();
  }, [id, navigate, token]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setInventory((prev) => ({
      ...prev,
      [name]: name === 'quantity' || name === 'price' ? Number(value) : value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    try {
      const response = await fetch(`http://localhost:8080/api/inventory/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(inventory),
      });

      if (!response.ok) throw new Error('Failed to update inventory');

      alert('Product updated successfully!');
      navigate('/home');
    } catch (error: any) {
      alert(error.message);
    }
  };

  return (
    <div className="container mt-5">
      <h2>Edit Product</h2>
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label className="form-label">Product Name</label>
          <input
            type="text"
            className="form-control"
            name="name"
            value={inventory.name}
            onChange={handleChange}
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Category</label>
          <input
            type="text"
            className="form-control"
            name="category"
            value={inventory.category}
            onChange={handleChange}
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Quantity</label>
          <input
            type="number"
            className="form-control"
            name="quantity"
            value={inventory.quantity}
            onChange={handleChange}
            required
          />
        </div>

        <div className="mb-3">
          <label className="form-label">Price</label>
          <input
            type="number"
            className="form-control"
            name="price"
            value={inventory.price}
            onChange={handleChange}
            required
          />
        </div>

        <button type="submit" className="btn btn-primary">Update Product</button>
        <button type="button" className="btn btn-secondary ms-2" onClick={() => navigate('/home')}>Cancel</button>
      </form>
    </div>
  );
};

export default EditInventory;
