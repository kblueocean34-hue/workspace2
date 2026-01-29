"use client"; 
//👉 Next.js에서 “이 파일은 브라우저에서 실행되는 컴포넌트다” 라고 알려주는 선언
import {useEffect, useMemo, useState} from "react";

import {Container, Navbar, Nav, Button, Card, Row, Col} from "react-bootstrap";

//자료형 타입(type) 정의
type ProductMenu = {
  id:number; title:string; desc:string; price:number;
  imageKey:string; //이미지 API의 key와
}

type ImageItem = {
  key:string; url:string;
}

export default function Home(){
//상태(state) 선언 📌 왜 state로 관리하나? 데이터가 나중에 도착함 (fetch) 도착하면 화면을 다시 그려야 하니까   
const [menus, setMenus] = useState<ProductMenu[]>([]);
const [images, setImages] = useState<ImageItem[]>([]);
const[loading, setLoading] = useState(true);
const[error, setError] = useState<string>("");

useEffect(() => {
//useMemo – 왜 굳이 Map을 쓰나? .find를 쓰면 상품이 많아지면 느려짐
const run = async () => {
  try{
setLoading(true);
setError("");

//상품메뉴 json text 읽기
const menuRes = await fetch("/product-menu.json", )
if(!menuRes.ok) throw new Error("상품메뉴 로딩 실패");
const menuData:ProductMenu[] = await menuRes.json();

//이미지 json 읽기 //👉 Spring API 실제 이미지 URL 관리
const imgRes = await fetch("/api/product-images",{cache:"no-store"});
if(!imgRes.ok) throw new Error("이미지 API로딩 실패")
const imgData : ImageItem[] = await imgRes.json(); 

//둘 다 성공하면 상태 저장
  setMenus(menuData);
  setImages(imgData);
  }catch (e:any) {
  setError(e?.message || "로딩 중 오류");
  } finally {
  setLoading(false);
  }
};

run();
},[]);

//key -> url 빠르게 찾기 위해 map으로 변환
const imageMap = useMemo(() => {
  const m = new Map<string, string>();
  images.forEach((it) => m.set(it.key, it.url));
  return m;
},[images]);

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
        <Button variant ="outline-light">
          로그인
        </Button>
      </Container>
    </Navbar>

    <Container className="py-4">
      <h1 className="mb-4">쇼핑몰 메인</h1>

{loading && <p>로딩 중...</p>}
{error && <p style={{whiteSpace:"pre-wrap"}}>{error}</p>}

 <Row className="g-3">
  {menus.map((p) => {
    const imgUrl = imageMap.get(p.imageKey);

    return (
      <Col key={p.id} md={3}>
        <Card>
          {imgUrl ? (
            <Card.Img variant="top" src={imgUrl} alt={p.title} />
          ) : (
            <div style={{ height: 200, display: "flex", alignItems: "center", justifyContent: "center" }}>
              이미지 없음
            </div>
          )}

          <Card.Body>
            <Card.Title>{p.title}</Card.Title>
            <Card.Text>{p.desc}</Card.Text>
            <Card.Text className="fw-bold">{p.price.toLocaleString()}원</Card.Text>
            <Button variant="primary">상세보기</Button>
          </Card.Body>
        </Card>
      </Col>
    );
  })}
</Row>

    </Container>
    </>
  );
}