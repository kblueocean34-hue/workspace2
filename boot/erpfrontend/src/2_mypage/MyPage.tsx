import Top from "../include/Top";
import Header from "../include/Header";
import SideBar from "../include/SideBar";
import {Container, Row, Col} from "react-bootstrap";
import {Left, Right, Flex} from "../stylesjs/Content.styles";
import Calendar from "../3_common/Calendar";
import Calendar2 from "../3_common/Calendar2";
import SmallCalendar from "../3_common/SmallCalendar";

const MyPage = () => {
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
            <Left>
<SmallCalendar/>
            </Left>
            <Right>
<Calendar2/>             
            </Right>
        </Flex>
        </Col>
    </Row>
</Container>
        </>
    )
}

export default MyPage;