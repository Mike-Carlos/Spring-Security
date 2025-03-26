import React from 'react';
import { Navigate } from 'react-router-dom';

interface ProtectedRouteProps {
  element: React.ReactNode;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ element }) => {
  const token = localStorage.getItem('token');
  return token ? <>{element}</> : <Navigate to="/login" replace />;
};

export default ProtectedRoute;

// import React, { useEffect, useState } from 'react';
// import { Navigate } from 'react-router-dom';

// interface ProtectedRouteProps {
//   element: React.ReactNode;
// }

// const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ element }) => {
//   const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);

//   useEffect(() => {
//     const checkAuth = async () => {
//       try {
//         const response = await fetch('http://localhost:8080/api/check-auth', {
//           method: 'GET',
//           credentials: 'include', // Include cookies for authentication
//         });

//         if (response.ok) {
//           setIsAuthenticated(true);
//         } else {
//           setIsAuthenticated(false);
//         }
//       } catch (error) {
//         console.error('Authentication check failed:', error);
//         setIsAuthenticated(false);
//       }
//     };

//     checkAuth();
//   }, []);

//   if (isAuthenticated === null) {
//     return <div>Loading...</div>;
//   }

//   return isAuthenticated ? <>{element}</> : <Navigate to="/login" replace />;
// };

// export default ProtectedRoute;
