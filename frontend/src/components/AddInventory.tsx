import React from 'react';
import InventoryForm from './InventoryForm';
import { useNavigate } from 'react-router-dom';

const AddInventory = () => {
  const navigate = useNavigate();

  const handleBack = () => {
    navigate('/home');
  };

  return (
    <div className="container mt-5">
      <h2>Add New Product</h2>
      <button className="btn btn-secondary mb-3" onClick={handleBack}>Back to Home</button>
      <InventoryForm onInventoryChange={handleBack} />
    </div>
  );
};

export default AddInventory;