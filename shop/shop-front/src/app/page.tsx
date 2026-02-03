"use client";
//👉 Next.js에서 “이 파일은 브라우저에서 실행되는 컴포넌트다” 라고 알려주는 선언
import { useEffect, useState } from "react";
import Header from "./include/Header";

import {
  Container,
  Navbar,
  Nav,
  Button,
  Card,
  Row,
  Col,
  Modal,
  Form,
} from "react-bootstrap";

// ✅ 자료형 타입(type) 정의
type Product = {
  id: number;
  title: string;
  desc: string;
  price: number;
  imageUrl?: string; // ✅ Spring에서 내려주는 이미지 URL
};

const API_BASE = "http://localhost:9999/api";

export default function Home() {
  //상태(state) 선언 📌 왜 state로 관리하나? 데이터가 나중에 도착함 (fetch) 도착하면 화면을 다시 그려야 하니까
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>("");




  

  return (
    <>
<Header/>

      <Container className="py-4">
        <h1 className="mb-4">쇼핑몰 메인</h1>

        {loading && <p>로딩 중...</p>}
        {error && <p style={{ whiteSpace: "pre-wrap" }}>{error}</p>}

        <Row className="g-3">
          {products.map((p) => (
            <Col key={p.id} md={3}>
              <Card>
                {p.imageUrl ? (
                  <Card.Img
                    variant="top"
                    // ✅ 캐시 방지: 등록 직후에도 새 이미지 보이게
  src={`http://localhost:9999${p.imageUrl}?v=${Date.now()}`}
  alt={p.title}
                  />
                ) : (
                  <div
                    style={{
                      height: 200,
                      display: "flex",
                      alignItems: "center",
                      justifyContent: "center",
                    }}
                  >
                    이미지 없음
                  </div>
                )}

                <Card.Body>
                  <Card.Title>{p.title}</Card.Title>
                  <Card.Text>{p.desc}</Card.Text>
                  <Card.Text className="fw-bold">
                    {p.price.toLocaleString()}원
                  </Card.Text>

                  <div className="d-flex gap-2">
                    <Button variant="primary">상세보기</Button>
                    <Button
                      variant="outline-danger"
                      onClick={() => handleDelete(p.id)}
                    >
                      삭제
                    </Button>
                  </div>
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
      </Container>

     
    </>
  );
}
