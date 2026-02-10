import axios from "axios";
import { useEffect, useMemo, useState } from "react";
import { Container, Row, Col, Table} from "react-bootstrap";
import Top from "../include/Top";
import Header from "../include/Header";
import SideBar from "../include/SideBar";
import { Left, Right, Flex, TopWrap } from "../stylesjs/Content.styles";
import { JustifyContent, W30, W70 } from "../stylesjs/Util.styles";
import { TableTitle } from "../stylesjs/Text.styles";
import { InputGroup, Search, MidLabel } from "../stylesjs/Input.styles";
import { WhiteBtn, MainSubmitBtn, BtnRight } from "../stylesjs/Button.styles";
import Lnb from "../include/Lnb";

import StockModal,{StockForm} from "../component/stock/StockModal";
/* =========================
   타입 정의
========================= */
type StockItem = {
  id: number;
  itemCode: string;
  itemName: string;
  stockQty: number;
  unitPrice: number;
  totalAmount: number;
};

/* =========================
   API 설정
========================= */
const API_BASE = "http://localhost:8888/api/stock";
const API_ITEM = "http://localhost:8888/api/stock";

/* =========================
   컴포넌트
========================= */
const StockStatus = () => {
  const [keyword, setKeyword] = useState(""); // 검색
  const [stockList, setStockList] = useState<StockItem[]>([]);

  // ✅ 모달 상태
  const [show, setShow] = useState(false);
  //show : 모달을 보여줄지 여부를 저장하는 상태값(boolean)
  const [mode, setMode] = useState<"create" | "edit">("create");
  //현재 화면이 등록(create) 모드인지 / 수정(edit) 모드인지 저장하는 상태값
  const [selectedId, setSelectedId] = useState<number | null>(null);
  //사용자가 선택한 데이터(예: 재고 항목)의 id(PK) 를 저장 

  const emptyForm = () : StockForm => ({
    itemCode:"", itemName:"", stockQty:0, unitPrice:0,
  })

  const[form, setForm] = useState<StockForm>(emptyForm());

  /* ===== 합계 계산 ===== */
  const totals = useMemo(() => {//useMemo를 써서 stockList가 바뀔 때만 다시 계산하게 최적화함
    //(렌더링 될 때마다 매번 계산 안 하게)
    const totalQty = stockList.reduce((s, i) => s + (Number(i.stockQty) || 0), 0);
    //stockList 배열을 돌면서 재고수량(stockQty) 합계를 구함
    //(Number(i.stockQty) || 0), 0); stockQty가 문자열이거나 비어있을 수 있으니 숫자로 변환
    //변환 결과가 NaN/0같이 falsy면 0으로 처리해서 안전하게 더함
    const totalAmount = stockList.reduce((s, i) => s +(Number(i.totalAmount) || 0), 0);
    return { totalQty, totalAmount };
  }, [stockList]);//stockList가 안 바뀌면 이전 계산값을 그대로 재사용 (성능 최적화)

  /* ===== 재고 조회 ===== */
  const fetchStock = async () => { //비동기 함수 선언.
    try {
      const res = await axios.get(API_BASE, {
        params: { q: keyword || undefined },
      });
      /*
      axios.get으로 서버에 GET 요청 보냄. await → 서버 응답 올 때까지 기다림. res = 서버 응답 결과(response). 
      */
//list 변수 선언. 타입은 StockItem[] → 재고 배열 형태.
      const list: StockItem[] = res.data?.map((i: any) => ({
//서버에서 받은 res.data 배열을 map으로 가공. ? 데이터가 없으면 에러 안 나게 보호 i = 서버에서 온 한 줄 데이터.
        id: i.id,//아이디 복사.
        itemCode: i.itemCode,//품목 코드 복사.
        itemName: i.itemName,//품목 이름 복사.
        stockQty: Number(i.stockQty ?? 0),//재고 수량 숫자로 변환. ?? 0 → 값이 null/undefined면 0 처리.
        unitPrice: Number(i.unitPrice?? 0),
        totalAmount: Number(i.stockQty ?? 0) * Number(i.unitPrice ?? 0),//수량 × 단가.
      })) ?? []; //map 결과가 없으면 빈 배열([]) 반환.
      setStockList(list);//가공된 배열을 state에 저장.화면이 자동으로 재렌더링됨. 
    } catch (e) {
      console.error("재고조회 실패", e);
    }
  };

  useEffect(() => {
    fetchStock();
  }, []);

  const stockMenu = [
  { key: "status", label: "재고현황", path: "/stock" },
];

//재고등록버튼
const openCreate = () => {
  setMode("create"); setSelectedId(null); setForm(emptyForm());
  setShow(true);
}

//수정
const openEdit = (row: StockItem) => {
  setMode("edit");
  setSelectedId(row.id);
  setForm({
    id:row.id,
    itemCode:row.itemCode,
    itemName:row.itemName,
    stockQty:row.stockQty,
    unitPrice:row.unitPrice,
  });
  setShow(true);
}

const closeModal = () => {
  setShow(false); setSelectedId(null); setMode("create"); setForm(emptyForm());
}

//저장 / 수정
const saveStock = async () => {
  const payload = {//서버로 보낼 데이터(body)를 payload 객체로 만든다.
  itemCode:form.itemCode?.trim(),
  itemName:form.itemName?.trim(),
  stockQty: Number(form.stockQty ?? 0),
  unitPrice: Number(form.unitPrice ?? 0),
  };
  if(!payload.itemCode) return alert("품목코드는 필수 입니다");
  if(!payload.itemCode) return alert("품목코드는 필수 입니다");

  try{
    if(mode === "edit" && selectedId) {
      await axios.put(`${API_ITEM}/${selectedId}`, payload);
    }else{
      await axios.post(`${API_BASE}`, payload);
    }
    await fetchStock();
    closeModal();
  }catch (e) {
    console.error("재고 저장 실패", e);
    alert("저장 실패(콘솔확인)");
  }
}

//삭제
const deleteStock = async () => {
 if(!selectedId) return;
 if(!window.confirm("정말 삭제 하시겠습니까")) return;

 try{
  await axios.delete(`${API_ITEM}/${selectedId}`)
  await fetchStock();
  closeModal();
 }catch (e) {
  console.error("재고 삭제 실패", e);
  alert("삭제 실패(콘솔 확인)");
 }
}

  return (
    <>
      <div className="fixed-top">
        <Top />
        <Header />
      </div>
      <SideBar />

      <Container fluid>
        <Row>
          <Col>
            <Flex>
              <Left>
<Lnb menuList={stockMenu} title="재고현황"/>                
              </Left>
              <Right>
                <TopWrap />

                <JustifyContent>
                  <TableTitle>재고현황</TableTitle>
                  <InputGroup>
                        <Search
                        type="search"
                        placeholder="품목코드/품목명 검색"
                        value={keyword}
                        onChange={(e: React.ChangeEvent<HTMLInputElement>) => setKeyword(e.target.value)}
                        onKeyDown={(e: React.KeyboardEvent<HTMLInputElement>) => {
                            if (e.key === "Enter") fetchStock();
                        }}
                        />
                    <WhiteBtn className="mx-2" onClick={fetchStock}>
                      조회
                    </WhiteBtn>
                  </InputGroup>
                </JustifyContent>

                <Table responsive hover>
                  <thead>
                    <tr>
                      <th>품목코드</th>
                      <th>품목명</th>
                      <th className="text-end">재고수량</th>
                      <th className="text-end">단가</th>
                      <th className="text-end">재고금액</th>
                    </tr>
                  </thead>
                  <tbody>
                    {stockList.length === 0 && (
                      <tr>
                        <td colSpan={5} className="text-center">
                          조회된 재고가 없습니다.
                        </td>
                      </tr>
                    )}
                    {stockList.map((i) => (
                      <tr key={i.id}>
                        <td>{i.itemCode}</td>
                        <td>{i.itemName}</td>
                        <td className="text-end">{i.stockQty}</td>
                        <td className="text-end">{i.unitPrice.toLocaleString()}</td>
                        <td className="text-end">{i.totalAmount.toLocaleString()}</td>
                      </tr>
                    ))}
                  </tbody>
                  {stockList.length > 0 && (
                    <tfoot>
                      <tr style={{ fontWeight: 700 }}>
                        <td colSpan={2} className="text-center">
                          합계
                        </td>
                        <td className="text-end">{totals.totalQty}</td>
                        <td></td>
                        <td className="text-end">{totals.totalAmount.toLocaleString()}</td>
                      </tr>
                    </tfoot>
                  )}
                </Table>

                <BtnRight>
                  <MainSubmitBtn onClick={openCreate}>재고등록</MainSubmitBtn>
                </BtnRight>
              </Right>
            </Flex>
          </Col>
        </Row>
      </Container>

      <StockModal
      show={show} mode={mode} form={form} onClose={closeModal}
      onChange={(patch) => setForm((p) => ({...p, ...patch}))}
      onSave={saveStock}
      onDelete={mode === "edit" ? deleteStock : undefined}
      />
    </>
  );
};

export default StockStatus;
