"use client";
import { useEffect, useState } from "react";
import {Container, Row, Col} from "react-bootstrap";
import {useRouter} from "next/navigation";

import Header from "@/include/Header";
import ProductModal from "@/modal/ProductModal";
import {categories } from "@/lib/Category";

const API_ROOT = "http://localhost:9999";
const API_BASE = `${API_ROOT}/api`;

type Product = {
id:number; title:string;
desc:string; price:number;
primaryCategory?:number; secondaryCategory?:number;
imageUrl?:string;
}

export default function Consumer() {
const router = useRouter();
const [products, setProducts] =useState<Product[]>([]);
const [modalMode, setModalMode] = useState<"create" | "edit" | "view">("create");
const [currentProductId, setCurrentProductId ] = useState<number | undefined>(undefined);
const [isLogin, setIsLogin] = useState<boolean | null>(null);

//상품리스트 조회
const fetchProducts = async () => {
    try{
const res = await fetch(`${API_BASE}/products`, {cache: "no-store"});
if(!res.ok) throw new Error("상품 리스트 불러오기 실패");
const data = await res.json();
    }catch(err){

    }
}

const openModal = () => {
    setModalMode(mode);
    setCurrentProductId(productId);
    setShowModal(true);
}

    return(
        <>
<Header 
onOpenModal={() => openModal("create")}
/>

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
    )
}