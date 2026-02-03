"use client";
import { useEffect, useState } from "react";
import { Container } from "react-bootstrap";

import Header from "@/include/Header";
import ProductModal from "@/modal/ProductModal";
import ProductList from "@/components/ProductList";

const API_BASE = "http://localhost:9999/api";

type Product = {
  id: number;
  title: string;
  desc: string;
  price: number;
  imageUrl?: string;
};

export default function Home() {
  const [products, setProducts] = useState<Product[]>([]);
  const [showModal, setShowModal] = useState(false);

  const fetchProducts = async () => {
    const res = await fetch(`${API_BASE}/products`, { cache: "no-store" });
    const data = await res.json();
    setProducts(data);
  };

  const handleDelete = async (id: number) => {
    if (!confirm("삭제할까요?")) return;
    await fetch(`${API_BASE}/products/${id}`, { method: "DELETE" });
    fetchProducts();
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  return (
    <>
      <Header onOpenModal={() => setShowModal(true)} />

      <Container className="py-4">
        <h1>쇼핑몰 메인</h1>
        <ProductList products={products} onDelete={handleDelete} />
      </Container>

      <ProductModal
        show={showModal}
        onClose={() => setShowModal(false)}
        onCreated={fetchProducts}
      />
    </>
  );
}
