"use client";

import { useState, useEffect, useMemo } from "react";
import { Modal, Button, Form } from "react-bootstrap";

const API_BASE = "http://localhost:9999/api";

type CategoryNode = {
  id: number;
  name: string;
  children?: CategoryNode[];
};

type Props = {
  show: boolean;
  onClose: () => void;
  onSaved: () => void;
  productId?: number;
  mode?: "create" | "edit" | "view";
  isLogin: boolean;
  categoryList?: CategoryNode[];
};

export default function ProductModal({
  show,
  onClose,
  onSaved,
  productId,
  mode = "create",
  isLogin,
  categoryList = [], // 안전 기본값
}: Props) {
  const [form, setForm] = useState({ title: "", desc: "", price: "" });
  const [imageFile, setImageFile] = useState<File | null>(null);
  const [imageUrl, setImageUrl] = useState<string | null>(null);

  // 🟢 카테고리는 null 기반으로 안전하게
  const [category1, setCategory1] = useState<number | null>(null);
  const [category2, setCategory2] = useState<number | null>(null);

  const [saving, setSaving] = useState(false);

  const isViewMode = mode === "view";

  const unitPrice = useMemo(() => {
    const n = Number(form.price);
    return Number.isFinite(n) ? n : 0;
  }, [form.price]);

  // -----------------------------
  // 상품 불러오기
  // -----------------------------
  useEffect(() => {
    if (!show) return;

    if (mode === "create") {
      setForm({ title: "", desc: "", price: "" });
      setCategory1(null);
      setCategory2(null);
      setImageFile(null);
      setImageUrl(null);
      return;
    }

    if ((mode === "edit" || mode === "view") && productId) {
      (async () => {
        try {
          const res = await fetch(`${API_BASE}/products/${productId}`, {
            credentials: "include",
          });

          if (!res.ok) throw new Error("상품 조회 실패");

          const data = await res.json();

          setForm({
            title: data.title ?? "",
            desc: data.desc ?? "",
            price: data.price?.toString() ?? "",
          });

          // 객체 or 숫자 모두 대응
          const primaryId =
            typeof data.primaryCategory === "object"
              ? data.primaryCategory?.id
              : data.primaryCategory;

          const secondaryId =
            typeof data.secondaryCategory === "object"
              ? data.secondaryCategory?.id
              : data.secondaryCategory;

          setCategory1(primaryId ? Number(primaryId) : null);
          setCategory2(secondaryId ? Number(secondaryId) : null);

          setImageUrl(
            data.imageUrl ? `http://localhost:9999${data.imageUrl}` : null
          );
          setImageFile(null);
        } catch (err) {
          alert("상품 정보를 불러오지 못했습니다.");
          onClose();
        }
      })();
    }
  }, [show, mode, productId]);

  // -----------------------------
  // 입력 변경
  // -----------------------------
  const onChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>
  ) => {
    if (isViewMode) return;

    const { name, value } = e.target;

    if (name === "category1") {
      setCategory1(value === "" ? null : Number(value));
      setCategory2(null);
    } else if (name === "category2") {
      setCategory2(value === "" ? null : Number(value));
    } else {
      setForm((prev) => ({ ...prev, [name]: value }));
    }
  };

  // -----------------------------
  // 저장
  // -----------------------------
  const handleSave = async () => {
    if (isViewMode) return;

    if (!form.title.trim()) return alert("상품명을 입력하세요.");
    if (!form.price.trim()) return alert("가격을 입력하세요.");

    // 🛑 가장 중요한 체크
    if (category1 == null || category2 == null) {
      return alert("카테고리를 선택하세요.");
    }

    if (mode === "create" && !imageFile) {
      return alert("이미지를 선택하세요.");
    }

    setSaving(true);

    const fd = new FormData();
    fd.append("title", form.title);
    fd.append("desc", form.desc);
    fd.append("price", Number(form.price).toString());

    // ✅ 스프링 컨트롤러 파라미터 이름과 완전 일치
    fd.append("primaryCategoryId", String(category1));
    fd.append("secondaryCategoryId", String(category2));

    if (imageFile) {
      fd.append("image", imageFile);
    }

    try {
      const res = await fetch(
        mode === "create"
          ? `${API_BASE}/products`
          : `${API_BASE}/products/${productId}`,
        {
          method: mode === "create" ? "POST" : "PUT",
          body: fd,
          credentials: "include",
        }
      );

      if (!res.ok) {
        throw new Error(
          mode === "create" ? "상품 등록 실패" : "상품 수정 실패"
        );
      }

      alert(mode === "create" ? "등록 완료!" : "수정 완료!");
      onSaved();
      onClose();
    } catch (err: any) {
      alert(err?.message || "저장 중 오류 발생");
    } finally {
      setSaving(false);
    }
  };

  // -----------------------------
  // 카테고리 표시용 (안전하게)
  // -----------------------------
  const primaryObj = categoryList.find((c) => c.id === category1);
  const secondaryObj = primaryObj?.children?.find(
    (c) => c.id === category2
  );

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
            disabled={saving || isViewMode}
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>카테고리</Form.Label>

          {isViewMode ? (
            <div>
              {primaryObj?.name ?? "없음"} /{" "}
              {secondaryObj?.name ?? "없음"}
            </div>
          ) : (
            <>
              <Form.Select
                name="category1"
                value={category1 ?? ""}
                onChange={onChange}
                disabled={saving}
              >
                <option value="">1차 카테고리 선택</option>
                {categoryList.map((c) => (
                  <option key={c.id} value={c.id}>
                    {c.name}
                  </option>
                ))}
              </Form.Select>

              <Form.Select
                name="category2"
                className="mt-2"
                value={category2 ?? ""}
                onChange={onChange}
                disabled={!category1 || saving}
              >
                <option value="">2차 카테고리 선택</option>
                {primaryObj?.children?.map((c) => (
                  <option key={c.id} value={c.id}>
                    {c.name}
                  </option>
                ))}
              </Form.Select>
            </>
          )}
        </Form.Group>

        <Form.Group>
          <Form.Label>이미지</Form.Label>

          {imageUrl && (
            <img
              src={imageUrl}
              alt="상품 이미지"
              style={{
                width: "100%",
                height: 150,
                objectFit: "cover",
                marginBottom: 10,
              }}
            />
          )}

          {!isViewMode && (
            <Form.Control
              type="file"
              accept="image/*"
              onChange={(e) => {
                const target = e.target as HTMLInputElement;
                setImageFile(
                  target.files && target.files.length > 0
                    ? target.files[0]
                    : null
                );
              }}
              disabled={saving}
            />
          )}
        </Form.Group>
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={onClose} disabled={saving}>
          닫기
        </Button>

        {!isViewMode && (
          <Button
            variant="primary"
            onClick={handleSave}
            disabled={saving}
          >
            {saving
              ? mode === "create"
                ? "등록 중..."
                : "수정 중..."
              : mode === "create"
              ? "등록"
              : "수정"}
          </Button>
        )}
      </Modal.Footer>
    </Modal>
  );
}
