import styled from "styled-components";

interface DayCellProps{
    isToday? : boolean;
    isEmpty? : boolean;
    isHoliday? : boolean;
}

export const Flex = styled.div`
display:flex;
`;

export const Left = styled.div`
width:20%;
height:100vh;
background-color:#fff;
`;
export const Right = styled.div`
width:80%;
height:100vh;
background-color:#fff;
`;

//캘린더
export const CalendarWrapper = styled.div`
width:100%;
display:flex; align-items:center; justify-content:space-between;
font-family:sans-serif;
padding:10px 0px;
`;
export const CalHeader = styled.h2`
text-align:center; margin-bottom:10px;
`;
export const Grid =styled.div`
width:90%;
display:grid; grid-template-columns:repeat(7, 1fr);
gap:5px;
`;
export const DayName = styled.div`
font-weight:bold; text-align:center;
`;
export const DayCell = styled.div<DayCellProps>`
height:50px; border-radius:8px; 
background:${({isToday}) => (isToday ? "#ffefc3" : "#f4f4f4")};
display:flex; align-items:center; justify-content:center;
color:${({isEmpty, isHoliday}) => (isEmpty ? "transparent": isHoliday ? "#d32f2f":"#333")};
font-weight:${({isHoliday}) => (isHoliday ? "Bold" : "normal")};
cursor:${({isEmpty}) => (isEmpty ? "default" : "pointer")};

&:hover span{
opacity:1; transform:translateY(-4px);
}
`;

export const CalTopMargin = styled.div`
margin-top:120px;
`;

export const Tooltip = styled.span`
position: absolute; background:#333; color:#fff; font-size:12px; 
padding:4px 8px; border-radius:4px; white-space:nowrap;
opacity:0; transition:0.2s; pointer-events:none;
`;

//api용
export const Wrapper = styled.div`
max-width:100%;
display:flex; align-items:center; justify-content:space-between;
flex-direction:column;
font-family:sans-serif;
padding:10px 0px;
`;
export const Header = styled.div`
width:90%;
display:flex; justify-content:space-between;
align-items:center; margin-bottom:10px;
`;

export const PrevBtn = styled.button`
cursor:pointer; font-size:18px;
background:transparent;
border:none;
&:hover{
font-size:19px;
}
`;
export const NextBtn = styled.button`
cursor:pointer; font-size:18px;
background:transparent;
border:none;
&:hover{
font-size:19px;
}
`;
export const TodayBtn = styled.button`
cursor:pointer; font-size:18px;
background:transparent;
border:none;
&:hover{
font-size:19px;
}
`;
export const CalendarRow = styled.div`
display:flex; align-items:stretch; font-family:sans-serif;
`;