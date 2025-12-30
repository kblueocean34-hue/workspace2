import { useMemo, useState, useEffect, useRef } from "react";
//useState (컴포넌트 안에서 값을 저장하기 위한  react훅)
//useMemo (→ 계산이 오래 걸리는 값을 기억해두고 필요할 때만 다시 계산하도록 도와줌)
import { holidays as holidayData } from "@kyungseopk1m/holidays-kr";
//대한민국 공휴일 데이터 라이브러리
import { Wrapper, Header, Grid, DayName, CalTopMargin, PrevBtn, NextBtn, TodayBtn } from "../stylesjs/Content.styles";
//화면 디자인용 스타일 컴포넌트

interface RawHoliday {//공휴일 하나의 형태를 정의
  date: number; // YYYYMMDD
  name: string;
}

const ANIMATION_TIME = 300;
const FIXED_HEIGHT = 700; //6주기준

const Calendar2 = () => {//달력을 그려주는 React함수 컴포넌트

  const [currentDate, setCurrentDate] = useState(new Date());//현재 날짜 가져오기
  
const [isAnimating, setIsAnimating] = useState(false); //예니메이션 상태관리
//const [direction, setDirection ] = useState<"prev" | "next" | "today">("today");
const [slideDir, setSlideDir] = useState<"prev"|"next">("next");
const [isMobile, setIsMobile] = useState(false);

const startX = useRef<number | null>(null);
const todayRef = useRef<HTMLDivElement | null>(null);

  const year = currentDate.getFullYear();//현재 년도 
  const month = currentDate.getMonth(); // 0~11

    //add 20251230 오늘날짜
  const today = new Date();
  const todayYear = today.getFullYear();
  const todayMonth = today.getMonth();
  const todayDate = today.getDate();

useEffect(() => {/*모바일 감지 */
  const mq = window.matchMedia("(max-width:480px)");
  const handler = () => setIsMobile(mq.matches);
  handler();
  mq.addEventListener("change", handler);
  return () => mq.removeEventListener("change", handler);
},[]);
//오늘 날짜 자동 스크롤
useEffect(() => {
  if(year === todayYear && month === todayMonth && todayRef.current){
    todayRef.current.scrollIntoView({
      behavior:"smooth", block:"center",
    })
  }
},[year, month]);

//add 공통 월 변경 함수 (연타 방지)
const changeMonth = (dir:"prev"|"next") => {
  if(isAnimating) return;
  setIsAnimating(true);
  setSlideDir(dir);

  setTimeout(() => {
    setCurrentDate(prev =>
      new Date(
        prev.getFullYear(),
        prev.getMonth() + (dir === "next" ? 1 : -1),
        1
      )
    );
    setIsAnimating(false);
  }, ANIMATION_TIME);
};

const goPrevMonth = () => changeMonth("prev");
const goNextMonth = () => changeMonth("next");
const goToday = () => 
  setCurrentDate(new Date(todayYear, todayMonth, 1));

/* */
const onTouchStart = (e:React.TouchEvent) => {
  startX.current = e.touches[0].clientX;
}

const onTouchEnd = (e:React.TouchEvent) => {
  if(startX.current === null) return;
  const diff = e.changedTouches[0].clientX -startX.current;
  if(Math.abs(diff) > 50){
    diff > 0 ? goPrevMonth() : goNextMonth();
  }
  startX.current = null;
}
/*const changeMonth = (newDate: Date, dir:typeof direction) => {
  if (isAnimating) return;
  setIsAnimating(true);
  setDirection(dir);
  setCurrentDate(newDate);
  setTimeout(() => setIsAnimating(false), ANIMATION_TIME);
}*/



/*이전 / 다음달 함수 만들기
const goPrevMonth = () => {
  changeMonth(
    new Date(year, month - 1, 1),
"prev"
  );
}

const goNextMonth = () => {
  changeMonth(
    new Date(year, month + 1, 1),
"next"
  );
}

const goToday = () => {
  changeMonth(
    new Date(todayYear, todayMonth, 1),
"today"
  );
}*/
/*const goPrevMonth = () => { 노멀한
  setCurrentDate(prev => new Date(prev.getFullYear(), prev.getMonth() -1, 1));
}

const goNextMonth = () => {
  setCurrentDate(prev => new Date(prev.getFullYear(), prev.getMonth() + 1, 1));
}*/



  // 연도별 공휴일 + 성탄절 수동 추가
  const rawHolidays = useMemo<RawHoliday[]>(() => {
    //공휴일 데이터 중에서 현재 연도 것만 추려냄(불필요한 재계산을 방지)
    let holidays: RawHoliday[] = [];
    if (Array.isArray(holidayData)) {//공휴일 데이터가 배열인지 확인
      //혹시 데이터가 깨졌을 경우를 대비
      holidays = holidayData
        .filter((h: any) => String(h.date).startsWith(String(year)))
        //현재 년도 공휴일만 필터링
        .map((h: any) => ({//필요한 형태로 변환
          date: Number(h.date),
          name: String(h.name),
        }));
    }

// 12월 성탄절 강제 추가 기존 로직에서 성탄절을 추가하려 했으나 
//노출이 안됬을경우 강제로 추가한 경우 이유가 라이브러리마다 달라서 없는 경우에는 강제 추가해야 됨
if (month === 11 && !holidays.some(h => String(h.date) === `${year}1225`)) {
  holidays.push({ date: Number(`${year}1225`), name: "성탄절" });
}

    return holidays;
  }, [year, month]);

  // 현재 월 공휴일 필터 현재보고 잇는 달의 공휴일만 사용
  const holidays = useMemo(() => {
    return rawHolidays.filter(
      h => Number(String(h.date).slice(4, 6)) === month + 1
    );
  }, [rawHolidays, month]);

  const firstDay = new Date(year, month, 1).getDay();
  //이번달 1일의 요일을 계산
  const lastDate = new Date(year, month + 1, 0).getDate();
  //이번달의 마지막 날짜를 계산


  const weekNames = isMobile
      ? ["일", "월", "화", "수", "목", "금", "토"]
    : ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];

  return (
    <CalTopMargin>
    <Wrapper>
      <Header>
<PrevBtn onClick={goPrevMonth} disabled={isAnimating}>◀</PrevBtn>
        <h3>
          {year}년 {month + 1}월{/*0부터라서 + 1 */}
                  <TodayBtn
onClick={goToday} 
disabled={year === todayYear && month === todayMonth}
        >오늘로 돌아가기</TodayBtn>
        </h3>



<NextBtn onClick={goNextMonth}>▶</NextBtn>
      </Header>
{/*예니메이션 컨테이너 */}
<div
style={{
  position:"relative",
  overflow:"hidden",
  minHeight:FIXED_HEIGHT,
}}
onTouchStart={onTouchStart}
onTouchEnd={onTouchEnd}
>
  {/*슬라이드*/}
<div 
style={{
  display:"flex", width:"200%",
  transform: isAnimating ? slideDir === "next" ? "translateX(-50%)" :"translateX(50)":"translateX(0)",
  transition:`transform ${ANIMATION_TIME}ms ease`,
}}
>
  {[0, 1].map(i => (
    <div key={i} style={{width:"50%"}}>
      <Grid>
        {weekNames.map(day =>(
          <DayName key={day}>{day}</DayName>
        ))}

        {Array.from({ length: firstDay }).map((_, idx) => (
          <div key={`empty-${idx}`} />
        ))}
        {/*빈칸 만들기 1일이 수요일이면 일월화 3칸을 비움 */}

        {/* 날짜 셀 1일부터 마지막 날짜가지 반복*/}
        {Array.from({ length: lastDate }, (_, idx) => {
          const day = idx + 1;
          const weekday = (firstDay + idx) % 7;

          // day와 비교해서 공휴일 찾기 날짜가 같은 공휴일 찾기
          const holiday = holidays.find(
            h => Number(String(h.date).slice(6, 8)) === day
          );

          const isHoliday = Boolean(holiday);//상태값들
          const isSunday = weekday === 0; // 일요일 체크
          //토요일 추가
          const isSaturday = weekday === 6;//토요일
          const isChristmas = holiday?.name === "성탄절";
//add 20251230
const isToday = year === todayYear && month === todayMonth && day === todayDate;

//공휴일은 노란배경 일요일 빨간글씨
          return (
            <div
              key={day}
              ref={isToday ? todayRef:null}
              style={{
                height: 50,
                margin:2,
                borderRadius:8,
                background: isHoliday ? "#ffefc3" : "#f4f4f4",
color:
weekday === 0 ? "red" : weekday === 6 ? "blue" : "#333",
border:isToday ? "2px solid #1976d2" : "none",
fontWeight:isToday ? "bold" : "normal", display:"flex", alignItems:"center",
              }}
              title={holiday?.name}
            >
              {day} {isChristmas && "🎄"}
            </div>
          );
        })}
      </Grid>
      </div>
  ))}
  </div>
  </div>
    </Wrapper>
    </CalTopMargin>
  );
};

export default Calendar2;

/*
style={{
width:"100%",
transition:`all ${ANIMATION_TIME}ms ease`,
transform:direction === "next" ? "translateX(0)" 
: direction === "prev" ? "translateX(0)" 
: "translateY(0)",
opacity: isAnimating ? 0.4 : 1,
}}
*/