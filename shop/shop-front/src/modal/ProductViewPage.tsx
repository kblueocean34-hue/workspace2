"use client";

import { useState, useEffect } from "react";
import { Modal, Button } from "react-bootstrap";

const API_BASE = "http://localhost:9999/api";

type Props = {
  show: boolean;
  onClose: () => void;
  productId: number;
};

export default function ProductViewModal({ show, onClose, productId }: Props) {
  const [product, setProduct] = useState<any>(null);

  // 상품 정보 가져오기
  useEffect(() => {
    if (!show) return; // 모달이 열릴 때만 API 호출
    const fetchProduct = async () => {
      try {
        const res = await fetch(`${API_BASE}/products/${productId}`, { credentials: "include" });
        if (!res.ok) throw new Error("상품 정보 불러오기 실패");
        const data = await res.json();
        setProduct(data);
      } catch (error) {
        console.error("상품 정보 불러오기 실패", error);
        alert("상품 정보를 불러오지 못했습니다.");
        onClose();
      }
    };
    fetchProduct();
  }, [show, productId, onClose]);

  if (!product) return null; // 상품 정보가 없으면 null 반환하여 모달 렌더링 안 함

  return (
    <Modal show={show} onHide={onClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>상품 상세</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p><strong>상품명:</strong> {product.title}</p>
        <p><strong>설명:</strong> {product.desc}</p>
        <p><strong>가격:</strong> {product.price?.toLocaleString()}원</p>
        <p><strong>카테고리:</strong> {product.primaryCategory} / {product.secondaryCategory}</p>
        {product.imageUrl && <img src={`http://localhost:9999${product.imageUrl}`} alt={product.title} style={{ width: "100%", height: 150, objectFit: "cover" }} />}
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onClose}>닫기</Button>
      </Modal.Footer>
    </Modal>
  );
}
