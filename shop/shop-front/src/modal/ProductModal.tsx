"use client";

import { useState, useEffect } from "react";
import { Modal, Button, Form } from "react-bootstrap";
import { categories } from "@/lib/Category";

const API_BASE = "http://localhost:9999/api";

type Props = {
  show: boolean;
  onClose: () => void;
  onSaved: () => void;
  productId?: number;
  mode?: "create" | "edit" | "view"; // ✅ view 추가
};

export default function ProductModal({
  show,
  onClose,
  onSaved,
  productId,
  mode = "create",
}: Props) {
  const [form, setForm] = useState({ title: "", desc: "", price: "" });
  const [imageFile, setImageFile] = useState<File | null>(null);
  const [imageUrl, setImageUrl] = useState<string | null>(null);
  const [category1, setCategory1] = useState<number | "">("");
  const [category2, setCategory2] = useState<number | "">("");
  const [saving, setSaving] = useState(false);

  const isViewMode = mode === "view"; // ✅ view 모드 체크

  useEffect(() => {
    if (!show) return;

    if ((mode === "edit" || mode === "view") && productId) {
      (async () => {
        try {
          const res = await fetch(`${API_BASE}/products/${productId}`, { credentials: "include" });
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

  // 폼 입력 비활성화 처리
  const onChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    if (isViewMode) return;
    const { name, value } = e.target;
    if (name === "category1") {
      setCategory1(Number(value));
      setCategory2("");
    } else if (name === "category2") {
      setCategory2(Number(value));
    } else {
      setForm(prev => ({ ...prev, [name]: value }));
    }
  };

  const handleSave = async () => {
    if (isViewMode) return; // ✅ view 모드에서는 저장 안 함
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

  return (
    <Modal show={show} onHide={onClose} centered>
      <Modal.Header closeButton>
        <Modal.Title>
          {mode === "create" && "상품 등록"}
          {mode === "edit" && "상품 수정"}
          {mode === "view" && "상품 상세"} {/* ✅ view 제목 */}
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
          <Form.Select
            name="category1"
            value={category1}
            onChange={onChange}
            disabled={saving || isViewMode}
          >
            <option value="">1차 카테고리 선택</option>
            {categories.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
          </Form.Select>
          <Form.Select
            name="category2"
            className="mt-2"
            value={category2}
            onChange={onChange}
            disabled={!category1 || saving || isViewMode}
          >
            <option value="">2차 카테고리 선택</option>
            {categories.find(c => c.id === category1)?.children?.map(c => (
              <option key={c.id} value={c.id}>{c.name}</option>
            ))}
          </Form.Select>
        </Form.Group>

        <Form.Group>
          <Form.Label>이미지</Form.Label>
          {imageUrl && <img src={imageUrl} alt="기존 이미지" style={{ width: "100%", height: 150, objectFit: "cover", marginBottom: 10 }} />}
          {!isViewMode && (
            <>
              <Form.Control
                type="file"
                accept="image/*"
                onChange={e => {
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
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={onClose} disabled={saving}>
          닫기
        </Button>
        {!isViewMode && (
          <Button variant="primary" onClick={handleSave} disabled={saving}>
            {saving ? (mode === "create" ? "등록 중..." : "수정 중...") : mode === "create" ? "등록" : "수정"}
          </Button>
        )}
      </Modal.Footer>
    </Modal>
  );
}
