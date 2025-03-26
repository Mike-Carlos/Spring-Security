const API_URL = 'http://localhost:8080/api/inventory';

const getToken = () => localStorage.getItem('token');

const headers = () => ({
  'Authorization': `Bearer ${getToken()}`,
  'Content-Type': 'application/json',
});

export const fetchInventory = async () => {
  const response = await fetch(API_URL, { headers: headers() });
  return response.json();
};

export const fetchInventoryByCategory = async (category: string) => {
  const response = await fetch(`${API_URL}/category/${category}`, { headers: headers() });
  return response.json();
};

export const searchInventoryByName = async (name: string) => {
  const response = await fetch(`${API_URL}/search?name=${name}`, { headers: headers() });
  return response.json();
};

export const createInventory = async (inventory: any) => {
  const response = await fetch(API_URL, {
    method: 'POST',
    headers: headers(),
    body: JSON.stringify(inventory),
  });
  return response.json();
};

export const updateInventory = async (id: number, inventory: any) => {
  const response = await fetch(`${API_URL}/${id}`, {
    method: 'PUT',
    headers: headers(),
    body: JSON.stringify(inventory),
  });
  return response.json();
};

export const deleteInventory = async (id: number) => {
  await fetch(`${API_URL}/${id}`, {
    method: 'DELETE',
    headers: headers(),
  });
};
