"use client";

import { useState, useEffect } from "react";
import { Container, Button } from "react-bootstrap";
import Header from "@/include/Header"; // Header import 추가

export default function CartPage() {
  const [cart, setCart] = useState<any[]>([]); // cart 상태 추가
  const [isLogin, setIsLogin] = useState<boolean | null>(null);

  // 장바구니 로드
  useEffect(() => {
    const savedCart = localStorage.getItem("cart");
    if (savedCart) {
      try {
        setCart(JSON.parse(savedCart)); // JSON 파싱하여 cart에 저장
      } catch (err) {
        console.error("장바구니 로드 실패:", err);
        setCart([]); // 실패 시 빈 배열로 초기화
      }
    } else {
      setCart([]); // 장바구니가 없을 경우 빈 배열
    }
  }, []);

  // 장바구니에서 상품 삭제
  const handleRemoveFromCart = (productId: number) => {
    const updatedCart = cart.filter((item: any) => item.id !== productId);
    localStorage.setItem("cart", JSON.stringify(updatedCart));
    setCart(updatedCart);
  };

  // 결제 페이지로 이동
  const handleCheckout = () => {
    if (!isLogin) {
      alert("로그인이 필요합니다.");
      return;
    }
    alert("결제 페이지로 이동");
  };

  // 로그인 상태 확인
  useEffect(() => {
    const checkLoginStatus = () => {
      const user = localStorage.getItem("user");
      setIsLogin(user ? true : false); // 로그인 상태 확인
    };
    checkLoginStatus();
  }, []);

  return (
    <>
      {/* Header 컴포넌트 추가, onOpenModal을 빈 함수로 전달 */}
      <Header isLogin={isLogin} setIsLogin={setIsLogin} onOpenModal={() => {}} />

      <Container>
        <h1>장바구니</h1>
        {cart.length === 0 ? (
          <p>장바구니에 담긴 상품이 없습니다.</p>
        ) : (
          <div>
            {cart.map((item: any) => (
              <div key={item.id} className="d-flex justify-content-between align-items-center">
                <img
                  src={`http://localhost:9999${item.imageUrl}`}
                  alt={item.title}
                  style={{ width: 100, height: 100, objectFit: "cover" }}
                />
                <div>
                  <h5>{item.title}</h5>
                  <p>{item.price?.toLocaleString()}원</p>
                </div>
                <Button
                  variant="danger"
                  onClick={() => handleRemoveFromCart(item.id)}
                >
                  삭제
                </Button>
              </div>
            ))}
            <div className="mt-4">
              <Button variant="success" onClick={handleCheckout}>
                결제하기
              </Button>
            </div>
          </div>
        )}
      </Container>
    </>
  );
}
