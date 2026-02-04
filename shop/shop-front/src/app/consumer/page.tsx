"use client";

import { useEffect, useState } from "react";
import { Container } from "react-bootstrap"; // 수정: 버튼을 제거했으므로 Button은 필요없음
import { useRouter } from "next/navigation";

import Header from "@/include/Header";
import ProductModal from "@/modal/ProductModal";
import { categories } from "@/lib/Category";

const API_ROOT = "http://localhost:9999";
const API_BASE = `${API_ROOT}/api`;

type Product = {
  id: number;
  title: string;
  desc: string;
  price: number;
  primaryCategory?: number;
  secondaryCategory?: number;
  imageUrl?: string;
};

export default function Home() {
  const router = useRouter();

  const [products, setProducts] = useState<Product[]>([]);
  const [showModal, setShowModal] = useState(false);
  const [modalMode, setModalMode] = useState<"create" | "edit" | "view">("create");
  const [currentProductId, setCurrentProductId] = useState<number | undefined>(undefined);
  const [isLogin, setIsLogin] = useState<boolean | null>(null);

  // 상품 리스트 조회
  const fetchProducts = async () => {
    try {
      const res = await fetch(`${API_BASE}/products`, { cache: "no-store" });
      if (!res.ok) throw new Error("상품 리스트 불러오기 실패");
      const data = await res.json();
      console.log(data); // 상품 데이터 로깅
      setProducts(data);
    } catch (err) {
      console.error("상품 로딩 실패", err);
    }
  };

  // 로그인 상태 체크
  const checkLogin = async () => {
    try {
      const res = await fetch(`${API_BASE}/auth/me`, { credentials: "include" });
      setIsLogin(res.ok);
    } catch (err) {
      console.error("로그인 체크 실패", err);
      setIsLogin(false);
    }
  };

  // 카테고리 이름 표시
  const getCategoryName = (primaryId?: number, secondaryId?: number) => {
    console.log("primaryId:", primaryId, "secondaryId:", secondaryId); // 디버깅용

    const primary = categories.find(c => c.id === primaryId);
    const secondary = primary?.children?.find(c => c.id === secondaryId);

    console.log("Primary:", primary, "Secondary:", secondary); // 디버깅용

    // primary 또는 secondary가 없으면 "카테고리 없음" 출력
    if (!primary || !secondary) return "카테고리 없음";
    return `${primary.name} / ${secondary.name}`;
  };

  useEffect(() => {
    fetchProducts();
    checkLogin();
  }, []);

  // 모달 열기
  const openModal = (mode: "create" | "edit" | "view", productId?: number) => {
    setModalMode(mode);
    setCurrentProductId(productId);
    setShowModal(true);
  };

  return (
    <>
      <Header
        onOpenModal={() => openModal("create")}
        isLogin={isLogin}
        setIsLogin={setIsLogin}
      />

      <Container className="py-4">
        <h1>쇼핑몰 메인</h1>

        <div className="d-flex flex-wrap gap-3 mt-3">
          {products.map(p => (
            <div
              key={p.id}
              className="border p-3 d-flex flex-column justify-content-between"
              style={{ width: 200, height: 320, cursor: "pointer" }}
              onClick={() => openModal("view", p.id)} // 클릭 시 뷰 모드로 모달 열기
            >
              {p.imageUrl && (
                <img
                  src={`${API_ROOT}${p.imageUrl}`}
                  alt={p.title}
                  style={{ width: "100%", height: 140, objectFit: "cover" }}
                />
              )}
              <div>
                <h5 className="mt-2 mb-1">{p.title}</h5>
                <p style={{ fontSize: 12, marginBottom: 4 }}>
                  {getCategoryName(p.primaryCategory, p.secondaryCategory)}
                </p>
                <p style={{ fontWeight: "bold", marginBottom: 0 }}>
                  {p.price.toLocaleString()}원
                </p>
              </div>
            </div>
          ))}
        </div>
      </Container>

      {/* 상품 모달 */}
      <ProductModal
        show={showModal}
        onClose={() => setShowModal(false)}
        onSaved={() => {
          setShowModal(false);
          fetchProducts();
        }}
        productId={currentProductId}
        mode={modalMode}
      />
    </>
  );
}
