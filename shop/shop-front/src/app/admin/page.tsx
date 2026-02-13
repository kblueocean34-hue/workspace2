"use client";

import { useEffect, useState } from "react";
import { Button,} from "react-bootstrap";
import { useRouter } from "next/navigation";
import Header from "@/include/Header";
import ProductModal from "@/modal/ProductModal";
import { categories } from "@/lib/Category";
import { PageWrapper,MainContentWrapper, Content,
ProductCard, ProductDetails, ButtonGroup, H1,H5, ProductImage,
ContentInner,P,Pprice,} from "@/styled/Admin.styles";
import Link from "next/link";
import SideBar from "./include/SideBar";


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

export default function Admin() {
  const router = useRouter();

  const [products, setProducts] = useState<Product[]>([]);
  const [showModal, setShowModal] = useState(false);
  const [modalMode, setModalMode] = useState<"create" | "edit" | "view">("create");
  const [currentProductId, setCurrentProductId] = useState<number | undefined>(undefined);
  const [isLogin, setIsLogin] = useState<boolean>(false);

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

  // 삭제 처리
  const handleDelete = async (id: number) => {
    if (!confirm("삭제할까요?")) return;
    try {
      await fetch(`${API_BASE}/products/${id}`, { method: "DELETE" });
      fetchProducts();
    } catch (err) {
      console.error("삭제 실패", err);
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
<PageWrapper>

<SideBar/>

    <MainContentWrapper>
        
        <Header 
        onOpenModal={() => openModal("create")}
        isLogin={isLogin}
        setIsLogin={setIsLogin}/>

        <Content>
            <H1>쇼핑몰 관리</H1>
            <ContentInner>
{products.map((p) =>(
  <ProductCard key={p.id} onClick={() => openModal("view", p.id)}>
    {p.imageUrl && <ProductImage src={`${API_ROOT}${p.imageUrl}`} alt={p.title}/>}
    <ProductDetails>
      <H5>{p.title}</H5>
      <P>{getCategoryName(p.primaryCategory, p.secondaryCategory)}</P>
      <Pprice>{p.price.toLocaleString()}원</Pprice>
    </ProductDetails>
    <ButtonGroup>
<Button
variant="primary"
size="sm"
onClick={(e) =>
{e.stopPropagation(); openModal("edit", p.id);}}
>수정</Button>  
<Button
variant="danger"
size="sm"
onClick={(e) =>
{e.stopPropagation(); handleDelete(p.id);}}
>삭제</Button>     
    </ButtonGroup>
  </ProductCard>
))}
            </ContentInner>
        </Content>

<ProductModal
show={showModal}
onClose={() => setShowModal(false)}
onSaved={() => {setShowModal(false); fetchProducts();}}
productId={currentProductId}
mode={modalMode}
isLogin={isLogin ?? false}
/>
    </MainContentWrapper>
</PageWrapper>    
    </>
  );
}
