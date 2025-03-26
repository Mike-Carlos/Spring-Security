import React, { useState } from "react";
import { createInventory, updateInventory } from "../config/InventoryApi";

interface InventoryFormProps {
  onInventoryChange: () => void;
}

const InventoryForm: React.FC<InventoryFormProps> = ({ onInventoryChange }) => {
  const [formData, setFormData] = useState<any>({
    name: "",
    description: "",
    quantity: "",
    price: "",
    category: "",
  });

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (formData.id) {
      await updateInventory(formData.id, formData);
    } else {
      await createInventory(formData);
    }
    onInventoryChange();
    setFormData({
      name: "",
      description: "",
      quantity: "",
      price: "",
      category: "",
    });
  };

  return (
    <form onSubmit={handleSubmit} className="mb-4">
      <h4>{formData.id ? "Update Inventory" : "Add New Inventory"}</h4>
      <input
        className="form-control mb-2"
        placeholder="Name"
        name="name"
        value={formData.name}
        onChange={handleChange}
      />
      <textarea
        className="form-control mb-2"
        placeholder="Description"
        name="description"
        value={formData.description}
        onChange={handleChange}
      />
      <input
        className="form-control mb-2"
        placeholder="Quantity"
        name="quantity"
        type="number"
        value={formData.quantity}
        onChange={handleChange}
      />
      <input
        className="form-control mb-2"
        placeholder="Price"
        name="price"
        type="number"
        step="0.01"
        value={formData.price}
        onChange={handleChange}
      />
      <input
        className="form-control mb-2"
        placeholder="Category"
        name="category"
        value={formData.category}
        onChange={handleChange}
      />
      <button className="btn btn-success" type="submit">
        {formData.id ? "Update" : "Add"}
      </button>
    </form>
  );
};

export default InventoryForm;
