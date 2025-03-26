import React from 'react';
import { Navigate } from 'react-router-dom';

interface PublicRouteProps {
  element: React.ReactNode;
}

const PublicRoute: React.FC<PublicRouteProps> = ({ element }) => {
  const token = localStorage.getItem('token');
  return token ? <Navigate to="/home" replace /> : <>{element}</>;
};

export default PublicRoute;

// import React, { useEffect, useState } from 'react';
// import { Navigate } from 'react-router-dom';

// interface PublicRouteProps {
//   element: React.ReactNode;
// }

// const PublicRoute: React.FC<PublicRouteProps> = ({ element }) => {
//   const [isAuthenticated, setIsAuthenticated] = useState<boolean | null>(null);

//   useEffect(() => {
//     const checkAuth = async () => {
//       try {
//         const response = await fetch('http://localhost:8080/api/check-auth', {
//           method: 'GET',
//           credentials: 'include', // Ensures cookies are sent with the request
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

//   return isAuthenticated ? <Navigate to="/home" replace /> : <>{element}</>;
// };

// export default PublicRoute;
