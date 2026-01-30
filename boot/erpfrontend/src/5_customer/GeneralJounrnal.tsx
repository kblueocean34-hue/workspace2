import axios from "axios";
import {
  Container,
  Row,
  Col,
  Table,
  Button,
  Modal,
  Form,
} from "react-bootstrap";
import Top from "../include/Top";
import Header from "../include/Header";
import SideBar from "../include/SideBar";
import {
  Left,
  Right,
  Flex,
  TopWrap,
  RoundRect,
} from "../stylesjs/Content.styles";
import { useState, useEffect } from "react";
import { JustifyContent, W70, W30 } from "../stylesjs/Util.styles";
import { TableTitle } from "../stylesjs/Text.styles";
import {
  InputGroup,
  Search,
  Radio,
  Label,
  MidLabel,
} from "../stylesjs/Input.styles";
import { WhiteBtn, MainSubmitBtn, BtnRight } from "../stylesjs/Button.styles";

type ColumnDef = {key: string; label:string;}

const API_BASE = "http://localhost:8888/api/acc/journals";

//일반전표타입 예시
type JournalStatus = "DRAFT" | "POSTED";

type JournalLine = {
id?:number; accountCode:string; accountName?:string;
dcType:"DEBIT"|"CREDIT";
amount:number; lineRemark?:string;
}

type Journal = {
   id?: number;
  journalNo: string;       // 전표번호
  journalDate: string;     // YYYY-MM-DD
  customerId?: number | null;
  customerName?: string;   // 표시용
  remark?: string;         // 전표 적요
  status: JournalStatus;
  lines: JournalLine[];   
}

//👉 새 전표를 만들 때 사용할 “빈 전표 기본값 생성기”
const emptyJournal = () : Journal => ({
  journalNo: "",
  journalDate: new Date().toISOString().slice(0, 10),
  customerId: null,
  customerName: "",
  remark: "",
  status: "DRAFT",
  lines: [
    { accountCode: "", dcType: "DEBIT", amount: 0, lineRemark: "" },
    { accountCode: "", dcType: "CREDIT", amount: 0, lineRemark: "" },
  ],
});

const GeneralJournal = () => {

const [show, setShow] = useState(false);
//선택된 거래처 iD
const [selectedId, setSelectedId] = useState<number | null>(null);

//검색(간단)
const [keyword, setKeyword] = useState("");

//목록
const [journalList, setJournalList] = useState<any[]>([]);

//편집대상 (전표)
const [journal, setJournal] = useState<Journal>(emptyJournal());

//테이블 컬럼
const columns: ColumnDef[] = [
    { key: "journalNo", label: "전표번호" },
    { key: "journalDate", label: "전표일자" },
    { key: "customerName", label: "거래처" },
    { key: "remark", label: "적요" },
    { key: "debitTotal", label: "차변합" },
    { key: "creditTotal", label: "대변합" },
    { key: "status", label: "상태" },
];

//합계 계산(모달/목록 표시용)
const totals = useMemo(() => {
    const debitTotal = (journal.lines || [])
    .filter((1))
})

const fetchCustomers = async () => {//async () =>
 try {//에러처리용 구조
    //정상실행 코드
const res = await axios.get(
    //axios.get(주소, 옵션) HTTP 요청 라이브러리
"http://localhost:8888/api/acc/customers", //요청 url
 {params:{page:0, size:20}}  //페이징을 한페이지에 20개 
);
 } catch(e) {//에러 발생시 실행
console.error("거래처 조회 실패", e);
 }
};

useEffect(() => { //컴포넌트의 생명주기 관리
    //이화면이 처음 뜰때 이코드를 실행해 처음 딱 한번만 실행
    fetchCustomers();
},[]);

const handleClose = () => {
    setShow(false);
    setSelectedId(null);
    setCustomer({
    customerCode:"",
    customerName:"",
    ceoName:"",
    phone:"",
    email:"",
    address:"",
    customerType:"SALES",
    remark:"",
    });
}

//쓰기 after 분기(신규/수정)
const saveCustomer = async () => {
try{
    if(selectedId) {//수정
        await axios.put(
            `http://localhost:8888/api/acc/customers/${selectedId}`,
            customer
        );
    }else{//신규
        await axios.post(
            "http://localhost:8888/api/acc/customers",
            customer
        );
}
fetchCustomers()
//setShow(false);
handleClose();
}catch (e) {
    console.error("저장 실패", e);
}
};

const deleteCustomer = async () => {
    if (!selectedId) return;
    if (!window.confirm("정말 삭제하시겠습니까?")) return;

try{
await axios.delete(`http://localhost:8888/api/acc/customers/${selectedId}`);
fetchCustomers(); handleClose();
}catch (e) {console.error("거래처 저장 실패", e);}}

    return(
        <>
        <div className="fixed-top">
            <Top/>
            <Header/>
        </div>
        <SideBar/>
        <Container fluid>
            <Row>
                <Col>
                <Flex>
                    <Left/>
                    <Right>
                        <TopWrap/>
                        <JustifyContent>
                            <TableTitle>
                                거래처 기초등록
                            </TableTitle>
                            <InputGroup>
                            <WhiteBtn className="mx-2">사용중단포함</WhiteBtn>
                                <Search type="search" placeholder="거래처 검색"/>
                                <MainSubmitBtn className="mx-2">
                                    Search(F3)
                                </MainSubmitBtn>
                            </InputGroup>
                        </JustifyContent>

                        <Table responsive>
                            <thead>
                                <tr>
                                    {columns.map((c) => (
                                        <th key={c.key}>{c.label}</th>
                                    ))}
                                </tr>
                            </thead>
                            <tbody>
                                {customerList.length === 0 && (
                                    <tr>
                                        <td
                                        colSpan={columns.length}
                                        className="text-center"
                                        >
                                        등록된 거래처가 없습니다
                                        </td>
                                    </tr>
                                )}
                                {customerList.map((c, idx) =>(
                                    <tr key={idx}
                                        onClick={() => {
                                        setCustomer(c);
                                        setSelectedId(c.id);
                                        setShow(true);    
                                        }}                                                                       
                                    >
                                        {columns.map((col) => (
<td key={col.key}>{c[col.key] ?? "-"}</td>                                            
                                        ))}
                                    </tr>
                                ))}
                            </tbody>
                        </Table>

                        <BtnRight>
                            <MainSubmitBtn onClick={() => {
                            setSelectedId(null);
                            setCustomer({
                                customerCode:"",
                                customerName:"",
                                ceoName:"",
                                phone:"",
                                email:"",
                                address:"",
                                customerType:"SALES",
                                remark:"",
                            })                                
                            setShow(true);}}>
                                신규(F2)
                            </MainSubmitBtn>
                        </BtnRight>
                    </Right>
                </Flex>
                </Col>
            </Row>
        </Container>

        {/*등록 모달 */}
        <Modal show={show} onHide={() => setShow(false)} size="lg" centered>
            <Modal.Header closeButton>
                <Modal.Title>거래처 등록</Modal.Title>
            </Modal.Header>

            <Modal.Body>
                <RoundRect>
                    <InputGroup>
                    <W30>
                        <MidLabel>거래처 코드</MidLabel>
                    </W30>
                    <W70>
                        <Form.Control
                        value={customer.customerCode}
                        onChange={(e) => 
                            setCustomer({
                                ...customer,
                                customerCode:e.target.value,
                            })
                        }
                        />
                    </W70>
                    </InputGroup>

                    <InputGroup className="my-3">
                        <W30>
                            <MidLabel>거래처명</MidLabel>
                        </W30>
                        <Form.Control
                        value={customer.customerName}
                        onChange={(e) => 
                            setCustomer({
                                ...customer,
                                customerName:e.target.value,
                            })
                        }
                        />
                    </InputGroup>

                    <InputGroup className="my-3">
                        <W30>
                            <MidLabel>대표자명</MidLabel>
                        </W30>
                        <Form.Control
                        value={customer.ceoName}
                        onChange={(e) =>
                            setCustomer({
                                ...customer,
                                ceoName:e.target.value,
                            })
                        }
                        />
                    </InputGroup>

                    <InputGroup className="my-3">
                        <W30>
                            <MidLabel>전화번호</MidLabel>
                        </W30>
                        <Form.Control
                        value={customer.phone}
                        onChange={(e) =>
                            setCustomer({
                                ...customer,
                                phone:e.target.value,
                            })
                        }
                        />
                    </InputGroup>

                    <InputGroup className="my-3">
                        <W30>
                            <MidLabel>Email</MidLabel>
                        </W30>
                        <Form.Control
                        value={customer.email}
                        onChange={(e) =>
                            setCustomer({
                                ...customer,
                                email:e.target.value,
                            })
                        }
                        />
                    </InputGroup>
                    
                    <InputGroup className="my-3">
                        <W30>
                            <MidLabel>주소</MidLabel>
                        </W30>
                        <Form.Control
                        value={customer.address}
                        onChange={(e) =>
                            setCustomer({
                                ...customer,
                                address:e.target.value,
                            })
                        }
                        />
                    </InputGroup>
                    
                    <InputGroup className="my-3">
                        <W30>
                            <MidLabel>적요</MidLabel>
                        </W30>
                        <Form.Control
                        as="textarea"
                        rows={2}
                        value={customer.remark}
                        onChange={(e) =>
                            setCustomer({
                                ...customer,
                                remark:e.target.value,
                            })
                        }
                        />
                    </InputGroup>

                    <Flex className="my-3">
                        <W30>
                            <MidLabel>상/구분</MidLabel>
                        </W30>
                        <W70>
                            {[
                                ["SALES", "매출처"],
                                ["PURCHASE", "매입처"],
                                ["BOTH", "매입·매출"],   
                            ].map(([v, l]) => (
                                <span key={v}>
                                    <Radio
                                        checked={customer.customerType === v}      
                                        onChange={() =>
                                            setCustomer({
                                                ...customer,
                                                customerType:v,
                                            })
                                        }                              
                                    />
                                    <Label className="mx-2">{l}</Label>
                                </span>
                            ))}
                        </W70>
                    </Flex>
                    
                    <InputGroup className="my-3">
                        <W30>
                            <MidLabel></MidLabel>
                        </W30>
                        <Form.Control/>
                    </InputGroup>
                </RoundRect>
            </Modal.Body>
            <Modal.Footer>
                
                <Button variant="secondary" onClick={handleClose}>
                    close
                </Button>
                
                {selectedId && (
                    <Button variant="danger" onClick={deleteCustomer}>
                        Delete
                    </Button>
                )}
                                
                <Button variant="primary" onClick={saveCustomer}>
                    {selectedId ? "Update" : "Save"}
                </Button>
            </Modal.Footer>
        </Modal>
        </>
    )
}

export default Customer;