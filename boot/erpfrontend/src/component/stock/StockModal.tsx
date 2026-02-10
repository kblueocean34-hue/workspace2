import {Modal, Button, Form, Row, Col} from "react-bootstrap";

export type StockForm = { //재고 입력 폼의 타입(형식)을 정의 시작.
id?:number; //id는 숫자 타입, ?는 선택값(없어도 됨) 이라는 의미.
itemCode:string;//품목 코드 (문자열) 
itemName:string; 
stockQty:number;
unitPrice:number;
}

type Props = { //모달 컴포넌트가 받을 속성(props) 타입 시작.
show:boolean; //모달을 보여줄지(true) 숨길지(false)
mode:"create" | "edit";//"create" → 등록 "edit" → 수정
form:StockForm; //폼 데이터 전체 객체
onClose: () => void; //닫기 버튼 눌렀을 때 실행될 함수
onChange:(patch: Partial<StockForm>) => void;
//입력값 변경 시 실행될 함수 Partial<StockForm> = 일부 필드만 전달 가능
onSave:() => void; //저장 버튼 클릭 시 실행될 함수
onDelete?:() => void; //삭제 버튼 함수 (선택값, 수정 모드일 때만 사용)
};

export default function StockModal({//컴포넌트 시작
show, mode, form, onClose, onChange, onSave, onDelete,    
} : Props){
    //재고금액 계산
    const totalAmount =
    (Number(form.stockQty) || 0) * (Number(form.unitPrice) || 0);
    //재고수량 × 단가 계산 Number()로 숫자 변환 값이 없으면 0 처리
    return(
        <Modal show={show} onHide={onClose} centered size ="lg">
        {/*show → 보이기 여부  onHide → 닫기 함수 */} 
            
            <Modal.Header closeButton>
                <Modal.Title>
                    {mode === "create" ? "재고 등록":"재고 수정"}
                </Modal.Title>
            </Modal.Header>  

            <Modal.Body>
            <Row className="g-3">

                <Col md={6}>
                    <Form.Label>품목코드</Form.Label>
                    <Form.Control
                        value={form.itemCode}
                        onChange={(e) => onChange({itemCode:e.target.value})}
                        placeholder="예) A001"
                    />{/*입력값을 form.itemCode에 연결  입력 시 onChange 실행*/}
                </Col>

                <Col md={6}>
                    <Form.Label>품목명</Form.Label>
                    <Form.Control
                        value={form.itemName}
                        onChange={(e) => onChange({itemName:e.target.value})}
                        placeholder="예) 샘플품목"
                    />
                </Col>

                <Col md={6}>
                    <Form.Label>재고 수량</Form.Label>
                    <Form.Control
                        type="number"
                        value={form.stockQty}
                        onChange={(e) => onChange({stockQty:Number(e.target.value)})}
                       min={0}
                    />
                </Col>

                <Col md={6}>
                    <Form.Label>단가</Form.Label>
                    <Form.Control
                    type="number"
                    value={form.unitPrice}
                    onChange={(e) => onChange({unitPrice:Number(e.target.value)})}
                    min={0}     
                    />
                </Col>

                <Col md={12}>
                    <Form.Label>재고금액</Form.Label>
                    <Form.Control
                    value={totalAmount.toLocaleString()}
                    readOnly
                    />
                </Col>

            </Row>
            </Modal.Body>    

            <Modal.Footer>
            {mode === "edit" && onDelete &&(
                <Button variant="danger" onClick={onDelete}>
                    삭제
                </Button>
            )}
            <Button variant="secondary" onClick={onClose}>
                닫기
            </Button>
            <Button onClick={onSave}>
                {mode === "create" ? "저장":"수정"}
            </Button>
            </Modal.Footer>     
        </Modal>
    )
}