"use client";
import { useState } from "react";
import { Modal, Button, Form } from "react-bootstrap";

const API_BASE = "http://localhost:9999/api";

type Props = {
  show: boolean;
  onClose: () => void;
  onCreated: () => void;
};

export default function ProductModal({ show, onClose, onCreated }: Props) {
  const [form, setForm] = useState({
    title: "",
    desc: "",
    price: "",
  });

  const [imageFile, setImageFile] = useState<File | null>(null);
  const [saving, setSaving] = useState(false);

  // 입력값 변경
  const onChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  // 상품 등록
  const handleCreate = async () => {
    try {
      if (!form.title.trim()) return alert("상품명 입력");
      if (!form.price.trim()) return alert("가격 입력");
      if (!imageFile) return alert("이미지 선택");

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

      if (!res.ok) throw new Error("상품 등록 실패");

      // 초기화
      setForm({ title: "", desc: "", price: "" });
      setImageFile(null);

      onClose();
      onCreated();
      alert("등록 완료!");
    } catch (e: any) {
      alert(e?.message || "등록 중 오류");
    } finally {
      setSaving(false);
    }
  };

  return (
    <Modal show={show} onHide={onClose} centered>
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
          />
        </Form.Group>

        <Form.Group>
          <Form.Label>이미지</Form.Label>
          <Form.Control
            type="file"
            accept="image/*"
            onChange={(e) => {
              const target = e.target as HTMLInputElement;
              setImageFile(target.files?.[0] || null);
            }}
          />
          <div className="text-muted mt-2" style={{ fontSize: 12 }}>
            이미지는 스프링 서버로 업로드됩니다.
          </div>
        </Form.Group>
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={onClose}>
          닫기
        </Button>
        <Button variant="primary" onClick={handleCreate} disabled={saving}>
          {saving ? "등록 중..." : "등록"}
        </Button>
      </Modal.Footer>
    </Modal>
  );
}
