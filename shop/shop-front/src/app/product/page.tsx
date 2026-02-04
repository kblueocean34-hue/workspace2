"use client";

import { useState, useEffect } from "react";
import { useParams, useRouter } from "next/navigation";
import {Button, Container} from "react-bootstrap";
import ProductModal from "@/modal/ProductModal";

const API_ROOT = "http://localhost:9999";
const API_BASE = `${API_ROOT}/api`;

export default function ProductDetailPage() {
  const params = useParams();
  const productId = Number(params.id);
  const router = useRouter();

const [showModal, setShowModal] = useState(true);
const[product, setProduct] = useState<any>(null);
const[isLogin, setIsLogin] = useState<boolean | null>(null);
const[userRole, setUserRole] = useState<"consumer" | "developer" | null>(null);

//로그인 상태 및 사용자 역활 체크
const checkUserRole = async () => {
  try{
const res = await fetch(`${API_BASE}/auth/me`, {credentials:"include"});
if (!res.ok) throw new Error("로그인 체크 실패");
const data = await res.json();
setIsLogin(true);
setUserRole(data.role);//소비자 또는 개발자 역할 정보를 받아옴
  }catch(err) {
setIsLogin(false); 
setUserRole("consumer");//기본값으로 소비자 역할 설정
  }
}

//상품정보 가져오기
const fetchProductDetails = async () => {
  try{
    const res = await fetch(`${API_BASE}/products/${productId}`);
    if (!res.ok) throw new Error("상품 정보 불러오기 실패");
    const data = await res.json();
    setProduct(data);
  } catch (err) {
    console.error("상품 정보 불러오기 실패", err);
  }
};

//장바구니에 상품추가
const handleAddToCart = ()  => {
  alert("장바구니에 상품을 추가 했습니다");
}

//결제처리
const handleCheckout = () => {
  router.push("/checkout");
};

useEffect(() => {
  fetchProductDetails();
checkUserRole();
},[productId]);

  if (isNaN(productId)) {
    return <div>잘못된 상품 ID입니다.</div>;
  }

  return (
     <Container>
      <h1>상품 상세</h1>
      <div className="d-flex flex-column align-items-center mt-3">
        {product && (
          <>
            <img
              src={`${API_ROOT}${product.imageUrl}`}
              alt={product.title}
              style={{ width: "100%", height: 300, objectFit: "cover" }}
            />
            <h3 className="mt-3">{product.title}</h3>
            <p>{product.desc}</p>
            <p><strong>{product.price.toLocaleString()}원</strong></p>

            {userRole === "consumer" && (
              <div className="d-flex gap-2 mt-3">
                <Button variant="primary" onClick={handleAddToCart}>장바구니에 담기</Button>
                <Button variant="success" onClick={handleCheckout}>결제하기</Button>
              </div>
            )}

            {userRole === "developer" && (
              <div className="d-flex gap-2 mt-3">
                <Button variant="warning" onClick={() => router.push(`/products/edit/${product.id}`)}>상품 수정</Button>
                <Button variant="danger" onClick={() => { if (confirm("정말 삭제하시겠습니까?")) { /* 삭제 로직 */ } }}>상품 삭제</Button>
              </div>
            )}
          </>
        )}
      </div>
          <ProductModal
      show={showModal}
      onClose={() => router.push("/")} // 모달 닫으면 홈으로
      onSaved={() => router.push("/")} // 상세 페이지에서는 수정 후 홈으로
      productId={productId}
      mode="view" // 상세보기 모드
    />
</Container>

  );
}
