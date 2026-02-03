"use client";

import { useState, useEffect } from "react";
import { useParams, useRouter } from "next/navigation";
import ProductModal from "@/modal/ProductModal";

export default function ProductDetailPage() {
  const params = useParams();
  const productId = Number(params.id);
  const router = useRouter();

  const [showModal, setShowModal] = useState(true);

  if (isNaN(productId)) {
    return <div>잘못된 상품 ID입니다.</div>;
  }

  return (
    <ProductModal
      show={showModal}
      onClose={() => router.push("/")} // 모달 닫으면 홈으로
      onSaved={() => router.push("/")} // 상세 페이지에서는 수정 후 홈으로
      productId={productId}
      mode="view" // 상세보기 모드
    />
  );
}
