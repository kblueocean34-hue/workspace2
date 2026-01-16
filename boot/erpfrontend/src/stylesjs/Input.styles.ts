import styled from "styled-components";

export const InputGroup = styled.div`
max-width:100%;
display:flex;
justify-content:space-evenly;
align-items:center;
`;

export const InsertTitle = styled.input`
border:1px solid #ccc;
border-radius:5px;
width:100%;
padding:10px 16px;
margin:5px 0px 10px;
`;

export const InsertMemo = styled.textarea`
border:1px solid #ccc;
border-radius:5px;
width:100%;
padding:10px 16px;
margin:5px 0px 10px;
resize:none;
height:auto;
`;

export const TimeInput = styled.input`
border:1px solid #ccc;
border-radius:5px;
width:100%;
padding:10px 16px;
margin:5px 0px 10px;
`;

//select를 스타일드 컴포넌트로 만들때는 무조건 select
export const Select = styled.select`
border:1px solid #ccc;
border-radius:5px;
width:100%;
padding:10px 16px;
margin:5px 0px 10px;
`;

//
export const Search = styled.input`
outline:none;
border:1px solid #ccc;
border-radius:5px;
max-width:100%;
padding:10px 16px;
margin:5px 0px 10px;
  /* search 타입에서 브라우저 기본 UI 제거 (특히 Safari/Chrome) */
  -webkit-appearance: none;
  appearance: none;

  &:focus,
  &:focus-visible {
    outline: none !important;
    border-color: 1px solid #ccc !important;
    box-shadow: none !important;
  }
`;