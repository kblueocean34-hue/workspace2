"use client";

import { useState, useEffect } from "react";
import { Container, Button, Form } from "react-bootstrap";
import Header from "@/include/Header"; // Header import 추가

type OrderDetails = {
  address: string;
  paymentMethod: string;
};

export default function OrderPage() {
  const [cart, setCart] = useState<any[]>([]); // 장바구니 상태
  const [isLogin, setIsLogin] = useState<boolean | null>(null);
  const [orderDetails, setOrderDetails] = useState<OrderDetails>({
    address: "",
    paymentMethod: "credit",
  }); // 주문 정보 타입 지정

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

  // 로그인 상태 확인
  useEffect(() => {
    const checkLoginStatus = () => {
      const user = localStorage.getItem("user");
      setIsLogin(user ? true : false); // 로그인 상태 확인
    };
    checkLoginStatus();
  }, []);

  // 결제 정보 처리
  const handleChange = (
    e: React.ChangeEvent<HTMLElement & { name: string; value: string }>
  ) => {
    const { name, value } = e.target;
    setOrderDetails((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  // 결제 처리
  const handlePlaceOrder = () => {
    if (!isLogin) {
      alert("로그인이 필요합니다.");
      return;
    }
    if (!orderDetails.address || !orderDetails.paymentMethod) {
      alert("주소와 결제 방법을 선택해주세요.");
      return;
    }

    // 여기서 결제 API 호출 및 처리 (가상으로 처리)
    alert("결제가 완료되었습니다.");
    // 결제 후 처리
    localStorage.setItem("cart", JSON.stringify([])); // 결제 완료 후 장바구니 비우기
    window.location.href = "/"; // 홈으로 리디렉션
  };

  return (
    <>
      <Header isLogin={isLogin} setIsLogin={setIsLogin} onOpenModal={() => {}} />

      <Container>
        <h1>주문 정보</h1>

        {/* 장바구니에 상품이 없을 경우 */}
        {cart.length === 0 ? (
          <p>장바구니에 담긴 상품이 없습니다.</p>
        ) : (
          <div>
            <h2>주문 상품</h2>
            {cart.map((item: any) => (
              <div key={item.id} className="d-flex justify-content-between align-items-center mb-3">
                <img
                  src={`http://localhost:9999${item.imageUrl}`}
                  alt={item.title}
                  style={{ width: 100, height: 100, objectFit: "cover" }}
                />
                <div>
                  <h5>{item.title}</h5>
                  <p>{item.price?.toLocaleString()}원</p>
                </div>
              </div>
            ))}
          </div>
        )}

        {/* 결제 정보 입력 폼 */}
        <h2 className="mt-4">결제 정보</h2>
        <Form>
          <Form.Group controlId="formAddress">
            <Form.Label>배송 주소</Form.Label>
            <Form.Control
              type="text"
              placeholder="배송 주소를 입력해주세요"
              name="address"
              value={orderDetails.address}
              onChange={handleChange}
            />
          </Form.Group>

          <Form.Group controlId="formPaymentMethod" className="mt-3">
            <Form.Label>결제 방법</Form.Label>
            <Form.Control
              as="select"
              name="paymentMethod"
              value={orderDetails.paymentMethod}
              onChange={handleChange}
            >
              <option value="credit">신용카드</option>
              <option value="paypal">PayPal</option>
              <option value="bank">은행 이체</option>
            </Form.Control>
          </Form.Group>

          <Button variant="success" className="mt-4" onClick={handlePlaceOrder}>
            주문하기
          </Button>
        </Form>
      </Container>
    </>
  );
}
