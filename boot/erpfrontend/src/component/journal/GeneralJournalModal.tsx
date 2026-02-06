import { Button, Modal, Form, Table } from "react-bootstrap";
import { Flex, RoundRect } from "../../stylesjs/Content.styles";
import { JustifyContent, W70, W30 } from "../../stylesjs/Util.styles";
import { InputGroup, Radio, Label, MidLabel } from "../../stylesjs/Input.styles";

export type JournalStatus = "DRAFT" | "POSTED";

export type JournalLine = {
  id?: number;
  accountCode: string;
  accountName?: string;
  dcType: "DEBIT" | "CREDIT";
  amount: number;
  lineRemark?: string;
};

export type Journal = {
  id?: number;
  journalNo: string;
  journalDate: string; // YYYY-MM-DD
  customerId?: number | null;
  customerName?: string;
  remark?: string;
  status: JournalStatus;
  lines: JournalLine[];
};

type Totals = { debitTotal: number; creditTotal: number };

type Props = {
  show: boolean;
  selectedId: number | null;
  journal: Journal;
  totals: Totals;

  onClose: () => void;
  onSetJournal: React.Dispatch<React.SetStateAction<Journal>>;

  addLine: () => void;
  removeLine: (idx: number) => void;
  updateLine: (idx: number, patch: Partial<JournalLine>) => void;

  onSave: () => void;
  onDelete: () => void;
};

export default function GeneralJournalModal({
  show,
  selectedId,
  journal,
  totals,
  onClose,
  onSetJournal,
  addLine,
  removeLine,
  updateLine,
  onSave,
  onDelete,
}: Props) {
  return (
    <Modal show={show} onHide={onClose} size="xl" centered>
      <Modal.Header closeButton>
        <Modal.Title>일반전표 {selectedId ? "수정" : "등록"}</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <RoundRect>
          {/* 헤더 영역 */}
          <InputGroup>
            <W30>
              <MidLabel>전표번호</MidLabel>
            </W30>
            <W70>
              <Form.Control
                value={journal.journalNo}
                onChange={(e) => onSetJournal((p) => ({ ...p, journalNo: e.target.value }))}
                placeholder="(자동채번이면 비워도 됨)"
              />
            </W70>
          </InputGroup>

          <InputGroup className="my-3">
            <W30>
              <MidLabel>전표일자</MidLabel>
            </W30>
            <W70>
              <Form.Control
                type="date"
                value={journal.journalDate}
                onChange={(e) => onSetJournal((p) => ({ ...p, journalDate: e.target.value }))}
              />
            </W70>
          </InputGroup>

          <InputGroup className="my-3">
            <W30>
              <MidLabel>거래처</MidLabel>
            </W30>
            <W70>
              <Form.Control
                value={journal.customerName || ""}
                onChange={(e) => onSetJournal((p) => ({ ...p, customerName: e.target.value }))}
                placeholder="거래처명(선택/검색 컴포넌트로 교체 가능)"
              />
            </W70>
          </InputGroup>

          <InputGroup className="my-3">
            <W30>
              <MidLabel>전표 적요</MidLabel>
            </W30>
            <W70>
              <Form.Control
                as="textarea"
                rows={2}
                value={journal.remark || ""}
                onChange={(e) => onSetJournal((p) => ({ ...p, remark: e.target.value }))}
              />
            </W70>
          </InputGroup>

          <Flex className="my-3">
            <W30>
              <MidLabel>상태</MidLabel>
            </W30>
            <W70>
              {[
                ["DRAFT", "작성중"],
                ["POSTED", "확정"],
              ].map(([v, l]) => (
                <span key={v}>
                  <Radio
                    checked={journal.status === (v as JournalStatus)}
                    onChange={() => onSetJournal((p) => ({ ...p, status: v as JournalStatus }))}
                  />
                  <Label className="mx-2">{l}</Label>
                </span>
              ))}
            </W70>
          </Flex>

          <hr />

          {/* 라인 영역 */}
          <JustifyContent>
            <div style={{ fontWeight: 700 }}>전표 라인</div>
            <div style={{ display: "flex", gap: 12, alignItems: "center" }}>
              <div>차변합: {totals.debitTotal.toLocaleString()}</div>
              <div>대변합: {totals.creditTotal.toLocaleString()}</div>
              <Button size="sm" variant="outline-primary" onClick={addLine}>
                라인 추가
              </Button>
            </div>
          </JustifyContent>

          <Table responsive className="mt-2">
            <thead>
              <tr>
                <th style={{ width: 110 }}>차/대</th>
                <th style={{ width: 160 }}>계정코드</th>
                <th>계정명</th>
                <th style={{ width: 180 }}>금액</th>
                <th>라인 적요</th>
                <th style={{ width: 80 }}></th>
              </tr>
            </thead>
            <tbody>
              {journal.lines.map((l, idx) => (
                <tr key={idx}>
                  <td>
                    <Form.Select
                      value={l.dcType}
                      onChange={(e) =>
                        updateLine(idx, { dcType: e.target.value as "DEBIT" | "CREDIT" })
                      }
                    >
                      <option value="DEBIT">차변</option>
                      <option value="CREDIT">대변</option>
                    </Form.Select>
                  </td>

                  <td>
                    <Form.Control
                      value={l.accountCode}
                      onChange={(e) => updateLine(idx, { accountCode: e.target.value })}
                      placeholder="예: 1110"
                    />
                  </td>

                  <td>
                    <Form.Control
                      value={l.accountName || ""}
                      onChange={(e) => updateLine(idx, { accountName: e.target.value })}
                      placeholder="(선택) 계정명"
                      disabled
                    />
                  </td>

                  <td>
                    <Form.Control
                      type="number"
                      value={l.amount}
                      onChange={(e) => updateLine(idx, { amount: Number(e.target.value) || 0 })}
                      min={0}
                    />
                  </td>

                  <td>
                    <Form.Control
                      value={l.lineRemark || ""}
                      onChange={(e) => updateLine(idx, { lineRemark: e.target.value })}
                      placeholder="라인 적요"
                    />
                  </td>

                  <td className="text-end">
                    <Button
                      size="sm"
                      variant="outline-danger"
                      onClick={() => removeLine(idx)}
                      disabled={journal.lines.length <= 2}
                      title="최소 2라인(차/대) 유지"
                    >
                      삭제
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>

          {totals.debitTotal !== totals.creditTotal && (
            <div style={{ color: "crimson", fontWeight: 700 }}>
              ⚠ 차변합과 대변합이 일치하지 않습니다. (저장 불가)
            </div>
          )}
        </RoundRect>
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={onClose}>
          close
        </Button>

        {selectedId && (
          <Button variant="danger" onClick={onDelete}>
            Delete
          </Button>
        )}

        <Button variant="primary" onClick={onSave}>
          {selectedId ? "Update" : "Save"}
        </Button>
      </Modal.Footer>
    </Modal>
  );
}
