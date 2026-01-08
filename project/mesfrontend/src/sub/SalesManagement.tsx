import Lnb from "../include/Lnb";
import Top from "../include/Top";
import {Wrapper, DflexColumn, Content, Ctap}
from "../styled/Sales.styles";
import {Container, Row, Col, Tab, Tabs, Table} from "react-bootstrap";
//import { BaseTable, Thead,Tbody,Tfoot, Tr, Th, Td } from "../styled/Table.styles";
//import { ColGroup } from "../commons/ColGroup";
import { Group, Left, Right,  Text6} from "../styled/Component.styles";
import {Time, Select, Search, Submit,} from "../styled/Input.styles";

const SalesManagement = () => {
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

<DflexColumn>
    <Left>
<Group>
<Text6>수주일자조회기간</Text6>
<Time type="time"/>
</Group>
    </Left>
    <Right>

    </Right>
</DflexColumn>

<Tabs 
defaultActiveKey="orders"
id=""
className="mb-3"
fill
>
<Tab eventKey="orders" title="수주관리">
<Table responsive>
    <thead>
        <tr>
<th>#</th> 
{Array.from({length:14}).map((_, index) => (
    <th key={index}>Table heading</th>
))}           
        </tr>
    </thead>
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
*/