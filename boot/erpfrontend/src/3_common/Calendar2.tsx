import { useMemo, useState, useEffect, useRef } from "react";
import { holidays as holidayData } from "@kyungseopk1m/holidays-kr";
import {
  Wrapper,
  Header,
  Grid,
  DayName,
  CalTopMargin,
  PrevBtn,
  NextBtn,
  TodayBtn,
} from "../stylesjs/Content.styles";

interface RawHoliday {
  date: number; // YYYYMMDD
  name: string;
}

//상수들 (고정값)
const ANIMATION_TIME = 300;//애니메이션 시간
const FIXED_HEIGHT = 360; // 달력 높이

const Calendar2 = () => {
  const [currentDate, setCurrentDate] = useState(new Date());
  //애니메이션 중인지 (연타방지)
  const [isAnimating, setIsAnimating] = useState(false);
  //왼쪽으로 가는지 / 오른쪽으로 가는지
  const [slideDir, setSlideDir] = useState<"prev" | "next">("next");
  //모바일 인지 아닌지
  const [isMobile, setIsMobile] = useState(false);

  const startX = useRef<number | null>(null);
//dom직접 접근 
const todayRef = useRef<HTMLDivElement | null>(null);
//스와이프 시작 위치 저장 오늘 날짜 칸으로 스크롤 하기위해 사용
const year = currentDate.getFullYear();
  const month = currentDate.getMonth();
//오늘 날짜 미리 저장
const today = new Date();
const todayYear = today.getFullYear();
const todayMonth = today.getMonth();
const todayDate = today.getDate();

  /* 모바일 감지 480이하이면 모바일로 판단*/
  useEffect(() => {
    const mq = window.matchMedia("(max-width:480px)");
    const handler = () => setIsMobile(mq.matches);
    handler();
    mq.addEventListener("change", handler);
    return () => mq.removeEventListener("change", handler);
  }, []);

  /* 오늘 날짜 자동 스크롤 */
  //이번달이 오늘이 속한 달이면 오늘 날짜칸이 자동으로 보이게 스크롤
  useEffect(() => {
    if (year === todayYear && month === todayMonth && todayRef.current) {
      todayRef.current.scrollIntoView({
        behavior: "smooth",
        block: "center",
      });
    }
  }, [year, month]);

  /* 월 변경 함수 (연타 방지) */
  const changeMonth = (dir: "prev" | "next") => {
    //버튼 연타 방지 에니에이션 실행 300ms뒤에 달 변경
    if (isAnimating) return;
    setIsAnimating(true);
    setSlideDir(dir);

    setTimeout(() => {
      setCurrentDate((prev) =>
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

  /* 스와이프 */
  const onTouchStart = (e: React.TouchEvent) => {
    startX.current = e.touches[0].clientX;
  };

  const onTouchEnd = (e: React.TouchEvent) => {
    if (startX.current === null) return;
    const diff = e.changedTouches[0].clientX - startX.current;
    if (Math.abs(diff) > 50) {
      diff > 0 ? goPrevMonth() : goNextMonth();
    }
    startX.current = null;
  };

  /* 연도별 공휴일 + 성탄절 */
  const rawHolidays = useMemo<RawHoliday[]>(() => {
    //현재 연도 공휴일만 필터 성탄절 직접추가 불필요한 재계산 방지
    let holidays: RawHoliday[] = [];
    if (Array.isArray(holidayData)) {
      holidays = holidayData
        .filter((h: any) => String(h.date).startsWith(String(year)))
        .map((h: any) => ({
          date: Number(h.date),
          name: String(h.name),
        }));
    }

    if (month === 11 && !holidays.some((h) => String(h.date) === `${year}1225`)) {
      holidays.push({ date: Number(`${year}1225`), name: "성탄절" });
    }

    return holidays;
  }, [year, month]);

  const weekNames = isMobile
    ? ["일", "월", "화", "수", "목", "금", "토"]
    : ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];

  /* 달력 렌더 함수 */
  const renderCalendar = (baseDate: Date) => {
    const y = baseDate.getFullYear();
    const m = baseDate.getMonth();

    const firstDay = new Date(y, m, 1).getDay();
    const lastDate = new Date(y, m + 1, 0).getDate();

    const holidays = rawHolidays.filter(
      (h) => Number(String(h.date).slice(4, 6)) === m + 1
    );

    return (
      <Grid style={{ width: "95%" }}>
        {weekNames.map((day) => (
          <DayName key={day}>{day}</DayName>
        ))}

        {Array.from({ length: firstDay }).map((_, idx) => (
          <div key={`e-${idx}`} />
        ))}

        {Array.from({ length: lastDate }, (_, idx) => {
          const day = idx + 1;
          const weekday = (firstDay + idx) % 7;

          const holiday = holidays.find(
            (h) => Number(String(h.date).slice(6, 8)) === day
          );

          const isToday =
            y === todayYear && m === todayMonth && day === todayDate;

          return (
            <div
              key={day}
              ref={isToday ? todayRef : null}
              style={{
                height: 50,
                margin: 2,
                borderRadius: 8,
                background: holiday ? "#ffefc3" : "#f4f4f4",
                color:
                  weekday === 0
                    ? "red"
                    : weekday === 6
                    ? "blue"
                    : "#333",
                border: isToday ? "2px solid #1976d2" : "none",
                fontWeight: isToday ? "bold" : "normal",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
              }}
              title={holiday?.name}
            >
              {day}
            </div>
          );
        })}
      </Grid>
    );
  };

  const currentMonthDate = new Date(year, month, 1);
  const slideMonthDate =
    slideDir === "next"
      ? new Date(year, month + 1, 1)
      : new Date(year, month - 1, 1);

  //여기 추가 안함 몰래 개발
  const renderMonths = () => {
  const isNext = slideDir === "next";

  const months = isNext
    ? [currentMonthDate, slideMonthDate] // next
    : [slideMonthDate, currentMonthDate]; // prev

  return (
    <div
      style={{
        display: "flex",
        width: "200%",
        // ⭐ 핵심: 애니메이션 여부와 방향에 따라 기준 위치 분리
        transform: isAnimating
          ? isNext
            ? "translateX(-50%)" // next 이동
            : "translateX(0%)"   // prev 이동
          : isNext
          ? "translateX(0%)"    // next 기본
          : "translateX(-50%)", // ⭐ prev 기본 위치
        transition: isAnimating
          ? `transform ${ANIMATION_TIME}ms ease`
          : "none",
      }}
    >
      {months.map((date, idx) => (
        <div key={idx} style={{ width: "50%" }}>
          {renderCalendar(date)}
        </div>
      ))}
    </div>
  );
};
  //여기 까지 몰래 개발안함

  return (
    <CalTopMargin>
      <Wrapper style={{ width: "100%" }}>
        {/* HEADER */}
        <Header>
          <PrevBtn onClick={goPrevMonth} disabled={isAnimating}>
            ◀
          </PrevBtn>

          <h3 style={{ display: "flex", gap: 8, alignItems: "center" }}>
            {year}년 {month + 1}월
            <TodayBtn
              onClick={goToday}
              disabled={year === todayYear && month === todayMonth}
            >
              오늘
            </TodayBtn>
          </h3>

          <NextBtn onClick={goNextMonth} disabled={isAnimating}>
            ▶
          </NextBtn>
        </Header>

        {/* 슬라이드 뷰포트 */}
        <div
          style={{
            position: "relative",
            overflow: "hidden",
            minHeight: FIXED_HEIGHT,
            width: "100%", // 가로 꽉차도록
          }}
          onTouchStart={onTouchStart}
          onTouchEnd={onTouchEnd}
        >
          {renderMonths()}
          {/*<div
            style={{
              display: "flex",
              width: "200%",
              transform: isAnimating
                ? slideDir === "next"
                  ? "translateX(-50%)"
                  : "translateX(50%)"
                : "translateX(0)",
              transition: `transform ${ANIMATION_TIME}ms ease`,
            }}
          >
            <div style={{ width: "50%" }}>{renderCalendar(currentMonthDate)}</div>
            <div style={{ width: "50%" }}>{renderCalendar(slideMonthDate)}</div>
          </div>*/}
        </div>
      </Wrapper>
    </CalTopMargin>
  );
};

export default Calendar2;
