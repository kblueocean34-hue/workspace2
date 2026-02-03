"use client";
//👉 Next.js에서 “이 파일은 브라우저에서 실행되는 컴포넌트다” 라고 알려주는 선언
import { useEffect, useState } from "react";

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

const API_BASE = "http://localhost:9999/api";

// ✅ 자료형 타입(type) 정의
type Product = {
  id: number;
  title: string;
  desc: string;
  price: number;
  imageUrl?: string; // ✅ Spring에서 내려주는 이미지 URL
};

export default function Header() {

      //등록모달상태
  const [show, setShow] = useState(false);
   const [form, setForm] = useState({ title: "", desc: "", price: "" });
     //등록폼상태
 
  const [imageFile, setImageFile] = useState<File | null>(null);
  const [saving, setSaving] = useState(false);
    const [products, setProducts] = useState<Product[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string>("");

  // ✅ 상품 등록(이미지 포함) - multipart/form-data
    const handleCreate = async () => {
      try {
        if (!form.title.trim()) return alert("상품명을 입력하세요");
        if (!form.price.trim()) return alert("가격을 입력하세요");
        if (!imageFile) return alert("이미지를 선택하세요");
  
        setSaving(true);
  
        const fd = new FormData();
        fd.append("title", form.title);
        fd.append("desc", form.desc);
        fd.append("price", form.price);
        fd.append("image", imageFile);
  
        const res = await fetch(`${API_BASE}/products`, {
          method: "POST",
          body: fd,
        });
  
        if (!res.ok) {
          const raw = await res.text().catch(() => "");
          throw new Error(raw || "상품 등록 실패");
        }
  
        setShow(false);
        setForm({ title: "", desc: "", price: "" });
        setImageFile(null);
  
        await fetchProducts();
        alert("등록 완료!");
      } catch (e: any) {
        alert(e?.message || "등록 중 오류");
      } finally {
        setSaving(false);
      }
    };
  
    // ✅ 상품 목록 조회
  const fetchProducts = async () => {
    try {
      setLoading(true);
      setError("");

      const res = await fetch(`${API_BASE}/products`, { cache: "no-store" });
      if (!res.ok) throw new Error("상품 목록 로딩 실패");

      const data: Product[] = await res.json();
      setProducts(data);
    } catch (e: any) {
      setError(e?.message || "로딩 중 오류");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  // ✅ 폼입력 변경
  const onChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  
  // ✅ 상품 삭제
  const handleDelete = async (id: number) => {
    const ok = window.confirm("정말 삭제할까요?");
    if (!ok) return;

    const res = await fetch(`${API_BASE}/products/${id}`, { method: "DELETE" });

    if (!res.ok) {
      const raw = await res.text().catch(() => "");
      alert(raw || "삭제 실패");
      return;
    }

    fetchProducts();
  };

    return(
        <>
      <Navbar bg="dark" variant="dark" expand="lg">
        <Container>
          <Navbar.Brand href="/">My shop</Navbar.Brand>
          <Nav className="me-auto">
            <Nav.Link href="/products">상품</Nav.Link>
            <Nav.Link href="/cart">장바구니</Nav.Link>
            <Nav.Link href="/orders">주문</Nav.Link>
          </Nav>

          <Button
            variant="outline-light"
            className="me-2"
            onClick={() => setShow(true)}
          >
            상품 등록
          </Button>
          <Button variant="outline-light">로그인</Button>
        </Container>
      </Navbar>

 {/* ✅ 상품 등록 모달 */}
      <Modal show={show} onHide={() => setShow(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>상품 등록</Modal.Title>
        </Modal.Header>

        <Modal.Body>
          <Form.Group className="mb-3">
            <Form.Label>상품명</Form.Label>
            <Form.Control
              name="title"
              value={form.title}
              onChange={onChange}
              placeholder="예) 아메리카노"
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>설명</Form.Label>
            <Form.Control
              as="textarea"
              rows={3}
              name="desc"
              value={form.desc}
              onChange={onChange}
              placeholder="상품 설명을 입력하세요"
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>가격</Form.Label>
            <Form.Control
              name="price"
              value={form.price}
              onChange={onChange}
              placeholder="예) 12900"
              inputMode="numeric"
            />
          </Form.Group>

          <Form.Group>
            <Form.Label>이미지</Form.Label>
            <Form.Control
              type="file"
              accept="image/*"
              onChange={(e) => setImageFile(e.target.files?.[0] || null)}
            />
            <div className="text-muted mt-2" style={{ fontSize: 12 }}>
              이미지는 스프링으로 업로드되고, 상품에 imageUrl로 저장됩니다.
            </div>
          </Form.Group>
        </Modal.Body>

        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShow(false)}>
            닫기
          </Button>
          <Button variant="primary" onClick={handleCreate} disabled={saving}>
            {saving ? "등록 중..." : "등록"}
          </Button>
        </Modal.Footer>
      </Modal>
        </>
    )
}