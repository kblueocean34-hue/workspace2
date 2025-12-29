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
width:10%;
height:100vh;
background-color:#fff;
`;
export const Right = styled.div`
width:90%;
height:100vh;
background-color:#fff;
`;

//캘린더
export const CalendarWrapper = styled.div`
width:100%;
margin:20px auto; 
font-family:sans-serif;
`;
export const CalHeader = styled.h2`
text-align:center; margin-bottom:10px;
`;
export const Grid =styled.div`
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
