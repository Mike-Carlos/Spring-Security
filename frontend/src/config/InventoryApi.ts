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

// const API_URL = 'http://localhost:8080/api/inventory';

// const headers = {
//   'Content-Type': 'application/json',
// };

// const handleResponse = async (response: Response) => {
//   if (!response.ok) {
//     const error = await response.json();
//     throw new Error(error.message || 'An error occurred');
//   }
//   return response.json();
// };

// export const fetchInventory = async () => {
//   try {
//     const response = await fetch(API_URL, {
//       method: 'GET',
//       headers,
//       credentials: 'include', // Send cookies with the request
//     });
//     return await handleResponse(response);
//   } catch (error) {
//     console.error('Fetch inventory failed:', error);
//     throw error;
//   }
// };

// export const fetchInventoryByCategory = async (category: string) => {
//   try {
//     const response = await fetch(`${API_URL}/category/${category}`, {
//       method: 'GET',
//       headers,
//       credentials: 'include',
//     });
//     return await handleResponse(response);
//   } catch (error) {
//     console.error('Fetch by category failed:', error);
//     throw error;
//   }
// };

// export const searchInventoryByName = async (name: string) => {
//   try {
//     const response = await fetch(`${API_URL}/search?name=${name}`, {
//       method: 'GET',
//       headers,
//       credentials: 'include',
//     });
//     return await handleResponse(response);
//   } catch (error) {
//     console.error('Search failed:', error);
//     throw error;
//   }
// };

// export const createInventory = async (inventory: any) => {
//   try {
//     const response = await fetch(API_URL, {
//       method: 'POST',
//       headers,
//       credentials: 'include',
//       body: JSON.stringify(inventory),
//     });
//     return await handleResponse(response);
//   } catch (error) {
//     console.error('Create inventory failed:', error);
//     throw error;
//   }
// };

// export const updateInventory = async (id: number, inventory: any) => {
//   try {
//     const response = await fetch(`${API_URL}/${id}`, {
//       method: 'PUT',
//       headers,
//       credentials: 'include',
//       body: JSON.stringify(inventory),
//     });
//     return await handleResponse(response);
//   } catch (error) {
//     console.error('Update inventory failed:', error);
//     throw error;
//   }
// };

// export const deleteInventory = async (id: number) => {
//   try {
//     const response = await fetch(`${API_URL}/${id}`, {
//       method: 'DELETE',
//       headers,
//       credentials: 'include',
//     });
//     if (!response.ok) throw new Error('Failed to delete inventory');
//   } catch (error) {
//     console.error('Delete failed:', error);
//     throw error;
//   }
// };
