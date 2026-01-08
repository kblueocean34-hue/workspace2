import Lnb from "../include/Lnb";
import Top from "../include/Top";
import {Wrapper, DflexColumn, DflexColumn2,Content, Ctap}
from "../styled/Sales.styles";
import {Container, Row, Col, Tab, Tabs, Table, Button} from "react-bootstrap";
//import { BaseTable, Thead,Tbody,Tfoot, Tr, Th, Td } from "../styled/Table.styles";
//import { ColGroup } from "../commons/ColGroup";
import { Group, Left, Right,  Text6, Span, Dflex, DflexEnd} from "../styled/Component.styles";
import {Time, Select, Search, Submit,} from "../styled/Input.styles";

const SalesManagement = () => {

const tableData = {
    headers:[
"수주일자","거래처코드","A거래처","품목코드","품목명","규격","수주 잔량",
"단가","금액","납품 예정일","남품 여부","비고","상세보기"
    ],
    row:[
"2025-12-30","2001","A거래처","0000000025","다마내기","1","100","100,000",
"10,000,000","미납"
    ]
}

return(
        <>
<Wrapper>
    <Lnb/>
    <DflexColumn>
        <Content>
            <Top/>
        </Content>
        <Container fluid className="p-0">
            <Row>
                <Col>

<Ctap>
<h5>영업관리</h5>

<DflexColumn2 className="mt-4 mb-3">
    <Left >
<Dflex>
    <Group>
        <Text6>수주일자조회기간</Text6>
        <Dflex>
        <Time type="time"/><span className="mx-2">-</span><Time type="time"/>
        </Dflex>
    </Group>
            
    <Group className="mx-3">
        <Text6>거래처</Text6>
        <Search type="search"/>
    </Group>

    <Group>
        <Text6>품목</Text6>
        <Search type="search"/>
    </Group>

    <Group className="mx-2">
        <Text6>납품여부</Text6>
        <Select>
            <option>전체</option>
            <option>아님</option>
        </Select>
    </Group>



</Dflex>
</Left>

    <Right>
        <Right>
            <Group>
            <DflexEnd>
                <Button variant="success">엑셀 다운</Button>
                <Button variant="primary" className="mx-3">일괄 납품</Button>
                <Button variant="secondary">수주 등록</Button>
            </DflexEnd>
            </Group>
        </Right>
    </Right>
</DflexColumn2>

<Tabs 
defaultActiveKey="orders"
id=""
className="mb-3"
fill
>
<Tab eventKey="orders" title="수주관리">
<Table responsive>
    <thead>
        <tr >
            <th className="bg-secondary text-white">#</th> 
            {tableData.headers.map((title, index) => (
                <th key={index} className="bg-secondary text-white">{title}</th>
            ))}           
        </tr>
    </thead>
    <tbody>
        <tr>
            <th>1</th> 
            {tableData.row.map((cell, index) => (
                <th key={index}>{cell}</th>
            ))}   
        </tr>
        <tr>
            <th>2</th> 
            {tableData.row.map((cell, index) => (
                <th key={index}>{cell}</th>
            ))}   
        </tr>
    </tbody>
    <tfoot>
        <tr>
<th className="bg-secondary text-white text-center" colSpan={6}>합계</th> 
    <th className="bg-secondary text-warning fw-bold">200</th>
    <th className="bg-secondary text-warning fw-bold">200</th>
    <th className="bg-secondary text-white">&nbsp;</th>
    <th className="bg-secondary text-white"></th>
    <th className="bg-secondary text-warning fw-bold">200</th>
    <th className="bg-secondary text-white" colSpan={5}></th> 
</tr>
    </tfoot>
</Table>


</Tab>
<Tab eventKey="delivery" title="납품관리">
    
</Tab>
<Tab eventKey="search" title="수주내역조회">
    
</Tab>
<Tab eventKey="dsearch" title="납품내역조회">
    
</Tab>
</Tabs>
</Ctap>
                </Col>
            </Row>
        </Container>
    </DflexColumn>
</Wrapper>
        </>
    )
}

export default SalesManagement;

/*
<BaseTable>
    <ColGroup columns={[]}/>
    <Thead variant="dark">
        <Tr>
            <Th>수주일자</Th><Th>거래처코드</Th><Th>거래처명</Th>
            <Th>품목코드</Th><Th>품목명</Th><Th>규격</Th>
            <Th>수주수량</Th><Th>수주잔량</Th><Th>단가</Th>
            <Th>금액</Th><Th>남품예정일</Th><Th>납품여부</Th>
            <Th>비고</Th><Th>상세보기</Th>
        </Tr>
    </Thead>
    <Tbody>
<Tr>
<Td></Td><Td></Td><Td></Td><Td></Td><Td></Td>
<Td></Td><Td></Td><Td></Td><Td></Td><Td></Td>
<Td></Td><Td></Td><Td></Td><Td></Td>
</Tr>
<Tr>
<Td></Td><Td></Td><Td></Td><Td></Td><Td></Td>
<Td></Td><Td></Td><Td></Td><Td></Td><Td></Td>
<Td></Td><Td></Td><Td></Td><Td></Td>
</Tr>
    </Tbody>
    <Tfoot variant="dark">
        <Tr>
            <Th>수주일자</Th><Th>거래처코드</Th><Th>거래처명</Th>
            <Th>품목코드</Th><Th>품목명</Th><Th>규격</Th>
            <Th>수주수량</Th><Th>수주잔량</Th><Th>단가</Th>
            <Th>금액</Th><Th>남품예정일</Th><Th>납품여부</Th>
            <Th>비고</Th><Th>상세보기</Th>
        </Tr>
    </Tfoot>
</BaseTable>

{Array.from({length:14}).map((_, index) => (
    <th key={index}>cell</th>
))}     
*/