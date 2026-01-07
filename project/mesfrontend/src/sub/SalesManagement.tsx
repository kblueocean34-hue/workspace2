import Lnb from "../include/Lnb";
import Top from "../include/Top";
import {Wrapper, DflexColumn, Content, Ctap}
from "../styled/Sales.styles";
import {Container, Row, Col, Tab, Tabs} from "react-bootstrap";
import { BaseTable } from "../styled/Table.styles";
import { ColGroup } from "../commons/ColGroup";




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
<Tabs 
defaultActiveKey="orders"
id=""
className="mb-3"
fill
>
<Tab eventKey="order" title="수주관리">
<BaseTable>
<ColGroup columns={[]}/>
</BaseTable>
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