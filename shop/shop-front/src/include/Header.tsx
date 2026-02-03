"use client";
//👉 Next.js에서 “이 파일은 브라우저에서 실행되는 컴포넌트다” 라고 알려주는 선언
import {Navbar,Nav,Container, Button,} from "react-bootstrap";

type Props = {
onOpenModal: () => void;
};

export default function Header({onOpenModal}: Props) {
return(
        <>
      <Navbar bg="secondary" expand="lg">
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
            onClick={onOpenModal}
          >
            상품 등록
          </Button>
          <Button variant="outline-light">로그인</Button>
        </Container>
      </Navbar>

        </>
    )
}