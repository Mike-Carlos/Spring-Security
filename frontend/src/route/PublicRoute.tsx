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
