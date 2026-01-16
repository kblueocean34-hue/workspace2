import {Container, Row, Col, Table, Button} from "react-bootstrap";
import Top from "../include/Top";
import Header from "../include/Header";
import SideBar from "../include/SideBar";
import {Left, Right, Flex, TopWrap} from "../stylesjs/Content.styles";
import {useMemo, useState} from "react";
import { JustifyContent } from "../stylesjs/Util.styles";
import { TableTitle } from "../stylesjs/Text.styles";
import { InputGroup, Search } from "../stylesjs/Input.styles";
import { WhiteBtn } from "../stylesjs/Button.styles";

type SortDirection = "asc" | "desc";

type SortState = {key: string | null; direction:SortDirection;}





type ColumnDef = {
    key:string; //데이터키 (unique)
    label:string; //화면에 보이는 헤더명
}

const initialColumns : ColumnDef[] = [
  { key: "itemCode", label: "품목코드" },
  { key: "itemName", label: "품목명" },
  { key: "itemGroup", label: "품목그룹" },
  { key: "spec", label: "규격" },
  { key: "barcode", label: "바코드" },
  { key: "inPrice", label: "입고단가" },
  { key: "outPrice", label: "출고단가" },
  { key: "itemType", label: "품목구분" },
  { key: "image", label: "이미지" },
];

const Inventory = () => {

    const [x, setX] = useState(0);
    //헤더 / 푸터 컬럼은 json(상태)로 관리
    const [columns, setColumns] = useState<ColumnDef[]>(initialColumns);

    const [sort, setSort] = useState<SortState>({//다른곳에 잘못선언하면 백지
key: null, direction:"asc",
});

    //사용자가 자유롭게 추가 할 컬럼을 입력
    const[newColLabel, setNewColLabel] = useState("");
    const[newColkey, setNewColKey] = useState("");

    const removeColumn = (key: string) => {

    }

    //정렬 토클함수 만들기
const toggleSort = (key: string) => {
    setSort((prev:any) => {
        //같은 컬럼 클릭 -> 방향만 토글
        if(prev.key === key) {
            return{
                key,
                direction:prev.direction === "asc" ? "desc" : "asc",
            };
        }

        //다른 컬럼 클릭 ->asc부터 시작
        return{
            key,
            direction:"asc",
        }
    })
}
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

            </Left>
            <Right>
<TopWrap/>{/*헤더를 사용하는 만큼에 높이만큼 설정을 해야 보임 */}
<JustifyContent>
    
    <TableTitle>
        품목등록리스트
    </TableTitle> 

<InputGroup>

<WhiteBtn>
사용중단포함
</WhiteBtn>

<Search type="search" placeholder="검색"/>

</InputGroup>
    

</JustifyContent>
<Table responsive>
    <thead>
        <tr>
{columns.map((c) => {
    const isActive = sort.key === c.key;
    const dir = sort.direction;
    
    return(
<th key={c.key}>
<div className="">
    <span>{c.label}</span>
    <Button
    size="sm" variant="outline-danger"
    onClick={()=> toggleSort(c.key) }>
        {!isActive && "정렬"}
        {isActive && dir === "asc" && "▲"}
        {isActive && dir === "desc" && "▼"}
    </Button>
</div>
</th>);
})}
        </tr>
    </thead>
    <tbody>
        <tr>
            <td></td>
        </tr>
    </tbody>
    <tfoot>
        <tr>
            <th></th>
        </tr>
    </tfoot>
</Table>           
            </Right>
        </Flex>
        </Col>
    </Row>
</Container>
</>
    );
}
export default Inventory;