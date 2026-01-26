import {useEffect, useState} from "react";
import Lnb from "../include/Lnb";
import Top from "../include/Top";

import { Wrapper, DflexColumn, Content, Ctap } from "../styled/Sales.styles";
import { SpaceBetween, Center, Dflex, PageTotal } from "../styled/Component.styles";

import { Container, Row, Col, Table, Button, Modal, Form, Pagination } from "react-bootstrap";

import * as XLSX from "xlsx";
import {saveAs} from "file-saver";

const API_BASE = "http://localhost:9500";

type KpiItem = {
id:number;
kpiName: string;
kpiGroup?: string;        // KPI그룹(영업/마케팅/운영 등)
owner?: string;           // 담당자
periodType: "MONTH" | "QUARTER" | "YEAR";  // 집계 기준
periodValue: string;      // 예: "2026-01" / "2026-Q1" / "2026"
targetValue: number;  // ✅ 이게 있어야 함
actualValue: number;  // ✅ 이것도
unit?:"string";// 단위(%, 건, 원 등)
status?: "ON_TRACK" | "RISK" | "OFF_TRACK";  // 상태(선택)
useYn: "Y" | "N";          // 사용여부
remark?: string;           // 비고
updatedAt?: string;
}

//데이터 + 페이지 정보를 한 번에 받기 위해서
type PageResponse<T> = {
content:T[]; // 실제 데이터 목록 T는 뭐든 가능 (KPI, 주문, 회원 등) PageResponse<KpiItem> PageResponse<Order>
totalElements:number; // 전체 데이터 개수 “총 124건” 표시할 때 씀
totalPages:number; // 전체 페이지 수 페이지 버튼 몇 개 만들지 결정 << 1 2 3 4 >>
number:number; // 현재 페이지 번호 (0부터 시작)
size:number; // 한 페이지당 개수 몇 개씩 보여주는지 “10개씩 보기 / 20개씩 보기” 같은 옵션에 사용
}

//테이블 상단에 들어가는 헤더
//KpiItem에 들어있는 속성 중 하나를 key로 쓰고,화면에 보여줄 이름(label)을 같이 묶은 목록이다
//key: keyof KpiItem KpiItem 안에 실제로 존재하는 필드 이름만 key로 쓸 수 있다
const TABLE_HEADERS: {key:keyof KpiItem; label:string}[] = [
  { key: "kpiName", label: "KPI명" },
  { key: "kpiGroup", label: "그룹" },
  { key: "owner", label: "담당자" },
  { key: "periodType", label: "기간유형" },
  { key: "periodValue", label: "기간" },
  { key: "targetValue", label: "목표" },
  { key: "actualValue", label: "실적" },
  { key: "unit", label: "단위" },
  { key: "status", label: "상태" },
  { key: "useYn", label: "사용여부" },
  { key: "remark", label: "비고" },
];

const KpiManagement = () => {
    const [rows, setRows] = useState<KpiItem[]>([]);
    //ows 안에는 KpiItem 객체들이 여러 개 들어있는 배열만 들어갈 거야
    //([]) 이건 초기값 : 처음 화면이 뜰 때는 KPI 데이터가 아직 없으니까
    const [page, setPage] = useState(0);
    /*
    page: 현재 보고 있는 페이지 번호
    setPage: 페이지를 바꾸는 함수
    */
   const [size, setSize] = useState(10);//size: 한 페이지에 몇 개 보여줄지(페이지 크기) 10개씩 보기
   const [totalPages, setTotalPages] = useState(0);
/*
totalPages: 전체 페이지가 몇 개인지
페이지 버튼(1,2,3...)을 몇 개 만들지 결정할 때 필요
setTotalPages: 서버 응답으로 업데이트   
*/
const [totalElements, setTotalElements] = useState(0);
/*
totalElements: 전체 데이터가 총 몇 개인지 setTotalElements: 서버 응답으로 업데이트
*/
//등록 모달
const [showCreate, setShowCreate] = useState(false);
const [createForm, setCreateForm] = useState({
    kpiName: "",
    kpiGroup: "",
    owner: "",
    periodType: "MONTH" as "MONTH" | "QUARTER" | "YEAR",
    periodValue: "",
    targetValue: "",
    actualValue: "",
    unit: "",
    status: "ON_TRACK" as "ON_TRACK" | "RISK" | "OFF_TRACK",
    useYn: "Y" as "Y" | "N",
    remark: "",
})

//상세 (수정/삭제) 모달
const [showDetail, setShowDetail] = useState(false); //상세보기 창(모달/패널)을 지금 보여줄까?”를 저장하는 상태
//true면 보여줌 false면 숨김
const [selected, setSelected] = useState<KpiItem | null>(null);
//사용자가 클릭한 “선택된 KPI 1건”을 저장하는 상태 
//<KpiItem | null> 선택된 KPI가 있을 때는 KpiItem 아직 아무것도 선택 안 했으면 null
//처음: null (선택 없음) 클릭 후: { id: 3, kpiName: "...", ... } (선택됨)
const [editForm, setEditForm] = useState({
        kpiName: "",
    kpiGroup: "",
    owner: "",
    periodType: "MONTH" as "MONTH" | "QUARTER" | "YEAR",
    periodValue: "",
    targetValue: "",
    actualValue: "",
    unit: "",
    status: "ON_TRACK" as "ON_TRACK" | "RISK" | "OFF_TRACK",
    useYn: "Y" as "Y" | "N",
    remark: "",
})

const onCreateChange = (e:React.ChangeEvent<any>) => {//등록 폼 변경 함수
/*e 이벤트 객체(input에서 값이 바뀔 때 React가 주는 정보)
React.ChangeEvent<any>는 “change 이벤트다”라고 타입을 붙인 것
초보 단계에선 any 대신 HTMLInputElement 같은 걸 쓰기도 하지만, 
지금은 “일단 어떤 input이든 받겠다” 의미
*/
const {name, value} = e.target;
/*
name = input의 name 속성값 
*/
setCreateForm((prev) => ({...prev, [name]:value}));
/*
createForm 상태를 업데이트하는 코드
(prev)는 “업데이트 직전의 이전 상태값” { ...prev }는 이전 값들을 그대로 복사
[name]: value는 name에 해당하는 키만 value로 바꾼다
*/
}

//수정
const onEditChange = (e:React.ChangeEvent<any>) => {
    const {name, value} = e.target;
    setEditForm((prev) => ({...prev, [name]:value}));
}

//목록 조회 함수(페이징)
const fetchList = async (p:number) => {
    try{
const res = await fetch(`${API_BASE}/api/kpis?page=${p}&size=${size}`);
if(!res.ok) throw new Error("서버 오류");
const data:PageResponse<KpiItem> = await res.json();
setRows(data.content);
setTotalPages(data.totalPages);
setTotalElements(data.totalElements);
    }catch(err){
console.error("KPI 목록 조회 실패", err);
    }
};

return(
<>
</>
)
}

export default KpiManagement;