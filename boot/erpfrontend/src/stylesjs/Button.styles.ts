import styled from "styled-components";

export const BtnGroup = styled.button`
padding:10px 20px;
border:none !important;
color:none !important;
display:flex;
justfify-content:space-between;
background:none !important;
`;
export const MainBtn = styled.button`
background-color:hotpink;
color:white;
width:100%;
display:flex; justify-content:center; align-items:center;
border:none;
height:36px;
border-radius:5px;
`;

export const GrayBtn = styled.button`
background-color:Gray;
color:white;
width:100%;
display:flex; justify-content:center; align-items:center;
border:none;
height:36px;
border-radius:5px;
font-size:14px;
letter-spacing:-2%;
`;

export const DelBtn = styled.button`
background-color:red;
color:white;
width:100%;
max-width:100%;
display:flex;
justify-content:center;
align-items:center;
font-size:14px;
letter-spacing:-2%;
border:none;
border-radius:3px;
opacity:.8;
transition:all 1s;
    &hover{
    opacity:1;
    }
`;

export const WhiteBtn = styled.button`
background-color:white;
color:#999;
width:100px;
max-width:100%;
display:flex;
justify-content:center;
align-items:center;
font-size:14px;
letter-spacing:-2%;
border:none;
border-radius:3px;
opacity:.8;
transition:all 1s;
    &hover{
    opacity:1;
    }
`;