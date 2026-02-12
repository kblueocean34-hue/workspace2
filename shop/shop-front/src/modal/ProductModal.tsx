"use client";

import { useState, useEffect, useMemo } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import { categories } from "@/lib/Category";

const API_BASE = "http://localhost:9999/api";

type Props = {
  show: boolean;
  onClose: () => void;
  onSaved: () => void;
  productId?: number;
  mode?: "create" | "edit" | "view";
  isLogin: boolean;
};

type CartItem = {
  id: number;
  title: string;
  price: number;
  imageUrl?: string | null;
  qty: number;
};

export default function ProductModal({
  show,
  onClose,
  onSaved,
  productId,
  mode = "create",
  isLogin,
}: Props) {
  const [form, setForm] = useState({ title: "", desc: "", price: "" });
  const [imageFile, setImageFile] = useState<File | null>(null);
  const [imageUrl, setImageUrl] = useState<string | null>(null);
  const [category1, setCategory1] = useState<number | "">("");
  const [category2, setCategory2] = useState<number | "">("");
  const [saving, setSaving] = useState(false);
    // ✅ 결제 모달 상태
  const [showCheckout, setShowCheckout] = useState(false);

  // ✅ 상세(뷰)에서 쓸 수량
  const [qty, setQty] = useState<number>(1);

  const isViewMode = mode === "view";

  // ✅ 가격 숫자화
  const unitPrice = useMemo(() => {
    const n = Number(form.price);
    return Number.isFinite(n) ? n : 0;
  }, [form.price]);

  // ✅ 상세 합계
  const subtotal = useMemo(() => unitPrice * qty, [unitPrice, qty]);

  useEffect(() => {
    if (!show) return;

    // 모달 열릴 때 기본 수량 리셋
    setQty(1);

    if ((mode === "edit" || mode === "view") && productId) {
      (async () => {
        try {
          const res = await fetch(`${API_BASE}/products/${productId}`, {
            credentials: "include",
          });
          if (!res.ok) throw new Error("상품 정보 불러오기 실패");

          const data = await res.json();
          setForm({
            title: data.title || "",
            desc: data.desc || "",
            price: data.price?.toString() || "",
          });
          setCategory1(data.primaryCategory || "");
          setCategory2(data.secondaryCategory || "");
          setImageUrl(data.imageUrl ? `http://localhost:9999${data.imageUrl}` : null);
          setImageFile(null);
        } catch {
          alert("상품 정보를 불러오지 못했습니다.");
          onClose();
        }
      })();
    } else if (mode === "create") {
      setForm({ title: "", desc: "", price: "" });
      setCategory1("");
      setCategory2("");
      setImageFile(null);
      setImageUrl(null);
    }
  }, [show, mode, productId, onClose]);

  const onChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
  ) => {
    if (isViewMode) return;
    const { name, value } = e.target;

    if (name === "category1") {
      setCategory1(Number(value));
      setCategory2("");
    } else if (name === "category2") {
      setCategory2(Number(value));
    } else {
      setForm((prev) => ({ ...prev, [name]: value }));
    }
  };

  const handleSave = async () => {
    if (isViewMode) return;
    if (!form.title.trim()) return alert("상품명 입력");
    if (!form.price.trim()) return alert("가격 입력");
    if (!category1 || !category2) return alert("카테고리를 선택해 주세요");
    if (mode === "create" && !imageFile) return alert("이미지 선택");

    setSaving(true);

    const fd = new FormData();
    fd.append("title", form.title);
    fd.append("desc", form.desc);
    fd.append("price", Number(form.price).toString());
    fd.append("primaryCategory", category1.toString());
    fd.append("secondaryCategory", category2.toString());
    if (imageFile) fd.append("image", imageFile);

    try {
      const res = await fetch(
        mode === "create" ? `${API_BASE}/products` : `${API_BASE}/products/${productId}`,
        { method: mode === "create" ? "POST" : "PUT", body: fd, credentials: "include" }
      );

      if (!res.ok) throw new Error(mode === "create" ? "상품 등록 실패" : "상품 수정 실패");

      alert(mode === "create" ? "등록 완료!" : "수정 완료!");
      onSaved();
      onClose();
    } catch (e: any) {
      alert(e?.message || "저장 중 오류");
    } finally {
      setSaving(false);
    }
  };

  // ✅ 장바구니 담기(수량 반영 + 기존 있으면 qty 누적)
  const handleAddToCart = () => {
    if (!productId) return;

    const item: CartItem = {
      id: productId,
      title: form.title,
      price: unitPrice,
      imageUrl,
      qty: qty,
    };

    const saved = localStorage.getItem("cart");
    let cartArr: CartItem[] = [];

    if (saved) {
      try {
        cartArr = JSON.parse(saved) as CartItem[];
      } catch {
        cartArr = [];
      }
    }

    const idx = cartArr.findIndex((c) => c.id === item.id);

    if (idx >= 0) {
      const prevQty = cartArr[idx].qty ?? 1;
      cartArr[idx] = { ...cartArr[idx], qty: prevQty + qty };
    } else {
      cartArr.push(item);
    }

    localStorage.setItem("cart", JSON.stringify(cartArr));
    alert("장바구니에 담겼습니다.");
  };

  // ✅ 구매하기(일단 로컬에 'checkout' 같은 값 저장 후 결제 페이지로 이동하도록 자리 마련)
   const handleCheckout = () => {
    if (isLogin !== true) {
      alert("로그인이 필요합니다.");
      return;
    }
    setShowCheckout(true);
  };

  return (
    <Modal show={show} onHide={onClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>
          {mode === "create" && "상품 등록"}
          {mode === "edit" && "상품 수정"}
          {mode === "view" && "상품 상세"}
        </Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <Form.Group className="mb-3">
          <Form.Label>상품명</Form.Label>
          <Form.Control
            name="title"
            value={form.title}
            onChange={onChange}
            placeholder="예) 아메리카노"
            disabled={saving || isViewMode}
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
            disabled={saving || isViewMode}
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>가격</Form.Label>
          <Form.Control
            name="price"
            value={form.price}
            onChange={onChange}
            inputMode="numeric"
            placeholder="예) 12900"
            disabled={saving || isViewMode}
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>카테고리</Form.Label>
          <Form.Select name="category1" value={category1} onChange={onChange} disabled={saving || isViewMode}>
            <option value="">1차 카테고리 선택</option>
            {categories.map((c) => (
              <option key={c.id} value={c.id}>
                {c.name}
              </option>
            ))}
          </Form.Select>

          <Form.Select
            name="category2"
            className="mt-2"
            value={category2}
            onChange={onChange}
            disabled={!category1 || saving || isViewMode}
          >
            <option value="">2차 카테고리 선택</option>
            {categories
              .find((c) => c.id === category1)
              ?.children?.map((c) => (
                <option key={c.id} value={c.id}>
                  {c.name}
                </option>
              ))}
          </Form.Select>
        </Form.Group>

        <Form.Group>
          <Form.Label>이미지</Form.Label>

          {imageUrl && (
            <img
              src={imageUrl}
              alt="상품 이미지"
              style={{ width: "100%", height: 150, objectFit: "cover", marginBottom: 10 }}
            />
          )}

          {!isViewMode && (
            <>
              <Form.Control
                type="file"
                accept="image/*"
                onChange={(e) => {
                  const target = e.target as HTMLInputElement;
                  setImageFile(target.files && target.files.length > 0 ? target.files[0] : null);
                }}
                disabled={saving}
              />
              <div className="text-muted mt-2" style={{ fontSize: 12 }}>
                이미지는 스프링 서버로 업로드됩니다.
              </div>
            </>
          )}
        </Form.Group>

        {/* ✅ 상세(뷰)에서만 수량/합계 UI 표시 */}
        {isViewMode && (
          <div className="mt-4 border-top pt-3">
            <div className="d-flex align-items-center justify-content-between">
              <div style={{ fontWeight: 600 }}>수량</div>

              <div className="d-flex align-items-center gap-2">
                <Button
                  size="sm"
                  variant="secondary"
                  onClick={() => setQty((v) => Math.max(1, v - 1))}
                >
                  -
                </Button>
                <div style={{ minWidth: 30, textAlign: "center" }}>{qty}</div>
                <Button size="sm" variant="secondary" onClick={() => setQty((v) => v + 1)}>
                  +
                </Button>
              </div>
            </div>

            <div className="d-flex align-items-center justify-content-between mt-2">
              <div style={{ fontWeight: 600 }}>합계</div>
              <div>{subtotal.toLocaleString()}원</div>
            </div>
          </div>
        )}
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={onClose} disabled={saving}>
          닫기
        </Button>

        {/* ✅ 소비자 로그인 상태에서만 (뷰 모드) 구매/장바구니 버튼 노출 */}
        {isViewMode && isLogin && (
          <>
            <Button variant="outline-primary" onClick={handleAddToCart} disabled={!productId}>
              장바구니
            </Button>

            <Button variant="primary" onClick={handleCheckout} disabled={!productId}>
              결제하기
            </Button>
          </>
        )}

        {/* 기존 등록/수정 버튼 */}
        {!isViewMode && (
          <Button variant="primary" onClick={handleSave} disabled={saving}>
            {saving ? (mode === "create" ? "등록 중..." : "수정 중...") : mode === "create" ? "등록" : "수정"}
          </Button>
        )}
      </Modal.Footer>
    </Modal>
  );
}
