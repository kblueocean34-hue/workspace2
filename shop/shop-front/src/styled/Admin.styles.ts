import styled from "styled-components";

export const PageWrapper = styled.div`
display:flex;
`;
export const Sidebar = styled.aside`
width:250px;
backgorund-color:#1f2d3d;
color:white; 
padding:20px;
`;
export const SidebarBrand = styled.a`
display:flex; align-items:center;
font-size:24px;   font-weight: bold;
text-decoration: none; color: white;
`;

export const SidebarNav = styled.ul`
  list-style-type: none;
  padding: 0;
`;

export const NavItem = styled.a`
  display: block;
  padding: 10px;
  color: white;
  text-decoration: none;
  &:hover {
    background-color: #2a3c4c;
  }
`;

export const MainContentWrapper = styled.main`
  flex: 1;
  background-color: #f4f6f9;
  padding: 20px;
`;

export const Content = styled.div`
  max-width: 1200px;
  margin: 0 auto;
`;

export const ProductCard = styled.div`
  width: 200px;
  height: 320px;
  border: 1px solid #ddd;
  padding: 15px;
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
`;

export const ProductImage = styled.img`
  width: 100%;
  height: 140px;
  object-fit: cover;
`;

export const ProductDetails = styled.div`
  h5 {
    margin-top: 15px;
    font-size: 16px;
  }
  p {
    margin-bottom: 0;
    font-size: 12px;
    color: #555;
  }
`;

export const ButtonGroup = styled.div`
  display: flex;
  gap: 8px;
`;

export const Button = styled.button`
  padding: 8px 12px;
  font-size: 12px;
  border: none;
  cursor: pointer;
  &:first-child {
    background-color: #007bff;
    color: white;
  }
  &:last-child {
    background-color: #dc3545;
    color: white;
  }
`;