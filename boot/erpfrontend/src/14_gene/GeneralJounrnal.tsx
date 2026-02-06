import axios from "axios";
import { useEffect, useMemo, useState } from "react";
import { Container, Row, Col, Table, Button } from "react-bootstrap";
import Top from "../include/Top";
import Header from "../include/Header";
import SideBar from "../include/SideBar";
import { Left, Right, Flex, TopWrap } from "../stylesjs/Content.styles";
import { JustifyContent } from "../stylesjs/Util.styles";
import { TableTitle } from "../stylesjs/Text.styles";
import { InputGroup, Search } from "../stylesjs/Input.styles";
import { WhiteBtn, MainSubmitBtn, BtnRight } from "../stylesjs/Button.styles";
import Lnb from "../include/Lnb";

import GeneralJournalModal, {
  Journal,
  JournalLine,
} from "../component/journal/GeneralJournalModal"; // ✅ 경로 맞추기

type ColumnDef = { key: string; label: string };

const API_BASE = "http://localhost:8888/api/acc/journals";

//👉 새 전표를 만들 때 사용할 “빈 전표 기본값 생성기”
const emptyJournal = (): Journal => ({
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
  const [selectedId, setSelectedId] = useState<number | null>(null);

  const [keyword, setKeyword] = useState("");
  const [journalList, setJournalList] = useState<any[]>([]);
  const [journal, setJournal] = useState<Journal>(emptyJournal());

  const columns: ColumnDef[] = [
    { key: "journalNo", label: "전표번호" },
    { key: "journalDate", label: "전표일자" },
    { key: "customerName", label: "거래처" },
    { key: "remark", label: "적요" },
    { key: "debitTotal", label: "차변합" },
    { key: "creditTotal", label: "대변합" },
    { key: "status", label: "상태" },
  ];

  const totals = useMemo(() => {
    const debitTotal = (journal.lines || [])
      .filter((l) => l.dcType === "DEBIT")
      .reduce((sum, l) => sum + (Number(l.amount) || 0), 0);

    const creditTotal = (journal.lines || [])
      .filter((l) => l.dcType === "CREDIT")
      .reduce((sum, l) => sum + (Number(l.amount) || 0), 0);

    return { debitTotal, creditTotal };
  }, [journal.lines]);

  const fetchJournals = async () => {
    try {
      const res = await axios.get(API_BASE, {
        params: {
          page: 0,
          size: 20,
          q: keyword || undefined,
        },
      });

      const list = res.data?.content ?? res.data ?? [];

      const normalized = list.map((j: any) => {
        const lines: JournalLine[] = j.lines ?? [];
        const debitTotal = lines
          .filter((l) => l.dcType === "DEBIT")
          .reduce((s, l) => s + (Number(l.amount) || 0), 0);
        const creditTotal = lines
          .filter((l) => l.dcType === "CREDIT")
          .reduce((s, l) => s + (Number(l.amount) || 0), 0);

        return { ...j, debitTotal, creditTotal };
      });

      setJournalList(normalized);
    } catch (e) {
      console.error("전표 조회 실패", e);
    }
  };

  useEffect(() => {
    fetchJournals();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const handleClose = () => {
    setShow(false);
    setSelectedId(null);
    setJournal(emptyJournal());
  };

  const openDetail = async (id: number) => {
    try {
      const res = await axios.get(`${API_BASE}/${id}`);
      setSelectedId(id);
      setJournal(res.data);
      setShow(true);
    } catch (e) {
      console.error("전표 상세 조회 실패", e);
    }
  };

  //라인 추가/삭제/수정
  const addLine = () => {
    setJournal((prev) => ({
      ...prev,
      lines: [...(prev.lines || []), { accountCode: "", dcType: "DEBIT", amount: 0, lineRemark: "" }],
    }));
  };

  const removeLine = (idx: number) => {
    setJournal((prev) => ({
      ...prev,
      lines: prev.lines.filter((_, i) => i !== idx),
    }));
  };

  const updateLine = (idx: number, patch: Partial<JournalLine>) => {
    setJournal((prev) => ({
      ...prev,
      lines: prev.lines.map((l, i) => (i === idx ? { ...l, ...patch } : l)),
    }));
  };

  const saveJournal = async () => {
    try {
      if (!journal.journalDate) {
        alert("전표일자를 입력하세요");
        return;
      }
      if (!journal.lines || journal.lines.length === 0) {
        alert("전표 라인을 1개 이상 입력하세요");
        return;
      }

      for (const [i, l] of journal.lines.entries()) {
        if (!l.accountCode?.trim()) {
          alert(`라인 ${i + 1}: 계정코드를 입력하세요`);
          return;
        }
        if (!(Number(l.amount) > 0)) {
          alert(`라인 ${i + 1}: 금액은 0보다 커야 합니다.`);
          return;
        }
      }

      if (totals.debitTotal !== totals.creditTotal) {
        alert(`차변합(${totals.debitTotal})과 대변합(${totals.creditTotal})이 일치해야 저장됩니다.`);
        return;
      }

      if (selectedId) await axios.put(`${API_BASE}/${selectedId}`, journal);
      else await axios.post(API_BASE, journal);

      await fetchJournals();
      handleClose();
    } catch (e) {
      console.error("저장 실패", e);
    }
  };

  const deleteJournal = async () => {
    if (!selectedId) return;
    if (!window.confirm("정말 삭제하시겠습니까?")) return;

    try {
      await axios.delete(`${API_BASE}/${selectedId}`);
      await fetchJournals();
      handleClose();
    } catch (e) {
      console.error("전표 삭제 실패", e);
    }
  };

  const openNew = () => {
    setSelectedId(null);
    setJournal(emptyJournal());
    setShow(true);
  };

  const stockMenu = [{ key: "status", label: "일반전표", path: "/general" }];

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
                <Lnb menuList={stockMenu} title="일반전표" />
              </Left>

              <Right>
                <TopWrap />
                <JustifyContent>
                  <TableTitle>일반전표</TableTitle>

                  <InputGroup>
                    <WhiteBtn className="mx-2" onClick={fetchJournals}>
                      새로고침
                    </WhiteBtn>

                    <Search
                      type="search"
                      placeholder="전표번호/거래처/적요 검색"
                      value={keyword}
                      onChange={(e: React.ChangeEvent<HTMLInputElement>) => setKeyword(e.target.value)}
                      onKeyDown={(e: React.KeyboardEvent<HTMLInputElement>) => {
                        if (e.key === "Enter") fetchJournals();
                      }}
                    />

                    <MainSubmitBtn className="mx-2" onClick={fetchJournals}>
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
                    {journalList.length === 0 && (
                      <tr>
                        <td colSpan={columns.length} className="text-center">
                          등록된 전표가 없습니다
                        </td>
                      </tr>
                    )}

                    {journalList.map((j, idx) => (
                      <tr
                        key={j.id ?? idx}
                        onClick={() => (j.id ? openDetail(j.id) : null)}
                        style={{ cursor: "pointer" }}
                      >
                        {columns.map((col) => (
                          <td key={col.key}>{j[col.key] ?? "-"}</td>
                        ))}
                      </tr>
                    ))}
                  </tbody>
                </Table>

                <BtnRight>
                  <MainSubmitBtn onClick={openNew}>신규(F2)</MainSubmitBtn>
                </BtnRight>
              </Right>
            </Flex>
          </Col>
        </Row>
      </Container>

      {/* ✅ 모달 분리 적용 */}
      <GeneralJournalModal
        show={show}
        selectedId={selectedId}
        journal={journal}
        totals={totals}
        onClose={handleClose}
        onSetJournal={setJournal}
        addLine={addLine}
        removeLine={removeLine}
        updateLine={updateLine}
        onSave={saveJournal}
        onDelete={deleteJournal}
      />
    </>
  );
};

export default GeneralJournal;
