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
  Flex,
} from "../stylesjs/Content.styles";
import{
Fixed, Modal,  ModalTitle, ModalDate
} from "../stylesjs/Modal.styles";
import {
  BtnGroup,
  MainBtn,
  GrayBtn
} from "../stylesjs/Button.styles";
import api from "../api";
import { JustifyContent, W49 } from "../stylesjs/Util.styles";
import { InsertTitle, InsertMemo, TimeInput } from "../stylesjs/Input.styles";

interface RawHoliday {
  date: number; // YYYYMMDD
  name: string;
}

type CalEvent = {
  id: number;
  date: string; // "YYYY-MM-DD"
  title: string;
  memo?: string;
  startTime?: string; // "HH:mm"
  endTime?: string;   // "HH:mm"
};

// 상수들 (고정값)
const ANIMATION_TIME = 300; // 애니메이션 시간
const FIXED_HEIGHT = 360; // 달력 높이

const pad2 = (n: number) => String(n).padStart(2, "0");
const toISODate = (y: number, m1to12: number, d: number) =>
  `${y}-${pad2(m1to12)}-${pad2(d)}`;

const Calendar2 = () => {
  const [currentDate, setCurrentDate] = useState(new Date());
  // 애니메이션 중인지 (연타방지)
  const [isAnimating, setIsAnimating] = useState(false);
  // 왼쪽으로 가는지 / 오른쪽으로 가는지
  const [slideDir, setSlideDir] = useState<"prev" | "next">("next");
  // 모바일 인지 아닌지
  const [isMobile, setIsMobile] = useState(false);

  // ✅ 일정 상태
  const [events, setEvents] = useState<CalEvent[]>([]);
  const [selectedDate, setSelectedDate] = useState<string | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);

  const [form, setForm] = useState({
    title: "",
    memo: "",
    startTime: "",
    endTime: "",
  });

  const startX = useRef<number | null>(null);
  // dom 직접 접근 (오늘 칸 자동 스크롤)
  const todayRef = useRef<HTMLDivElement | null>(null);

  const year = currentDate.getFullYear();
  const month = currentDate.getMonth(); // 0~11

  // 오늘 날짜 미리 저장
  const today = new Date();
  const todayYear = today.getFullYear();
  const todayMonth = today.getMonth();
  const todayDate = today.getDate();

  /* 모바일 감지 480이하이면 모바일로 판단 */
  useEffect(() => {
    const mq = window.matchMedia("(max-width:480px)");
    const handler = () => setIsMobile(mq.matches);
    handler();
    mq.addEventListener("change", handler);
    return () => mq.removeEventListener("change", handler);
  }, []);

  /* 오늘 날짜 자동 스크롤 */
  useEffect(() => {
    if (year === todayYear && month === todayMonth && todayRef.current) {
      todayRef.current.scrollIntoView({
        behavior: "smooth",
        block: "center",
      });
    }
  }, [year, month, todayYear, todayMonth]);

  /* 월 변경 함수 (연타 방지) */
  const changeMonth = (dir: "prev" | "next") => {
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
  const goToday = () => setCurrentDate(new Date(todayYear, todayMonth, 1));

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
    let holidays: RawHoliday[] = [];
    if (Array.isArray(holidayData)) {
      holidays = holidayData
        .filter((h: any) => String(h.date).startsWith(String(year)))
        .map((h: any) => ({
          date: Number(h.date),
          name: String(h.name),
        }));
    }

    if (
      month === 11 &&
      !holidays.some((h) => String(h.date) === `${year}1225`)
    ) {
      holidays.push({ date: Number(`${year}1225`), name: "성탄절" });
    }

    return holidays;
  }, [year, month]);

  const weekNames = isMobile
    ? ["일", "월", "화", "수", "목", "금", "토"]
    : ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];

  // ✅ 이번 달 일정 로딩
  const reloadMonthEvents = async () => {
    const from = `${year}-${pad2(month + 1)}-01`;
    const last = new Date(year, month + 1, 0).getDate();
    const to = `${year}-${pad2(month + 1)}-${pad2(last)}`;
    try{
    const res = await api.get("/events", { params: { from, to } });
    setEvents(res.data);
    } catch (err:any) {
      if(err.response?.status === 401) {
        alert("로그인이 필요합니다");
      } else if(err.response?.status === 403){
        alert("권한이 없습니다");
      } else{
        console.error(err);
      }
    }

  };

  useEffect(() => {
    reloadMonthEvents().catch((e) => {
      // 토큰이 없거나 만료면 여기서 401 날 수 있음(ProtectedRoute가 보통 막아줌)
      console.error(e);
    });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [year, month]);

  // ✅ 일정 저장
  const saveEvent = async () => {
    if (!selectedDate) return;
    if (!form.title.trim()) {
      alert("제목을 입력하세요");
      return;
    }

   try{
    await api.post("/events", {
      date: selectedDate,
      title: form.title,
      memo: form.memo,
      startTime: form.startTime || null,
      endTime: form.endTime || null,
    });
    await reloadMonthEvents();
    setIsModalOpen(false);
  } catch (err:any) {
    if(err.response?.status === 401) alert ("로그인이 필요합니다");
    else if(err.response?.status === 403 ) alert ("관한이 없습니다");
  }
  };

  // ✅ 일정 삭제
  const deleteEvent = async (id: number) => {
    if (!confirm("이 일정을 삭제할까요?")) return;
    try{
        await api.delete(`/events/${id}`);
        await reloadMonthEvents();
    } catch (err:any){
      if(err.response?.status === 401 ) alert ("로그인이 필요합니다");
      else if(err.response?.status === 403) alert("권한이 없습니다");
      else console.error(err);
    }


  };

  const selectedDayEvents = selectedDate
    ? events.filter((e) => e.date === selectedDate)
    : [];

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

          const isToday = y === todayYear && m === todayMonth && day === todayDate;

          // ✅ 이 날짜(칸)의 ISO 날짜
          const iso = toISODate(y, m + 1, day);
          const dayEvents = events.filter((e) => e.date === iso);

          return (
            <div
              key={day}
              ref={isToday ? todayRef : null}
              onClick={() => {
                setSelectedDate(iso); // ✅ “아무 날짜나 클릭” 핵심
                setForm({ title: "", memo: "", startTime: "", endTime: "" });
                setIsModalOpen(true);
              }}
              style={{
                height: 50,
                margin: 2,
                borderRadius: 8,
                background: holiday ? "#ffefc3" : "#f4f4f4",
                color:
                  weekday === 0 ? "red" : weekday === 6 ? "blue" : "#333",
                border: isToday ? "2px solid #1976d2" : "none",
                fontWeight: isToday ? "bold" : "normal",
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
                cursor: "pointer",
                gap: 4,
                paddingTop: 4,
              }}
              title={holiday?.name}
            >
              <div>{day}</div>

              {/* ✅ 일정 1개 미리보기(있으면 보여주기) */}
              {dayEvents.length > 0 && (
                <div
                  style={{
                    fontSize: 10,
                    padding: "2px 6px",
                    borderRadius: 6,
                    background: "#ddd",
                    maxWidth: "90%",
                    overflow: "hidden",
                    textOverflow: "ellipsis",
                    whiteSpace: "nowrap",
                  }}
                >
                  {dayEvents[0].title}
                </div>
              )}
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

  const renderMonths = () => {
    const isNext = slideDir === "next";

    const monthsArr = isNext
      ? [currentMonthDate, slideMonthDate] // next
      : [slideMonthDate, currentMonthDate]; // prev

    return (
      <div
        style={{
          display: "flex",
          width: "200%",
          transform: isAnimating
            ? isNext
              ? "translateX(-50%)"
              : "translateX(0%)"
            : isNext
            ? "translateX(0%)"
            : "translateX(-50%)",
          transition: isAnimating ? `transform ${ANIMATION_TIME}ms ease` : "none",
        }}
      >
        {monthsArr.map((date, idx) => (
          <div key={idx} style={{ width: "50%" }}>
            {renderCalendar(date)}
          </div>
        ))}
      </div>
    );
  };

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
            width: "100%",
          }}
          onTouchStart={onTouchStart}
          onTouchEnd={onTouchEnd}
        >
          {renderMonths()}
        </div>
      </Wrapper>

      {/* ✅ 일정 입력/조회 모달 */}
      {isModalOpen && (
        <Fixed
          onClick={() => setIsModalOpen(false)}
        >
          <Modal
            onClick={(e:any) => e.stopPropagation()}
          >
            <ModalTitle>일정</ModalTitle>
            <ModalDate>{selectedDate}</ModalDate>

            {/* ✅ 선택한 날짜의 기존 일정 목록 */}
            {selectedDayEvents.length > 0 && (
              <div style={{ marginBottom: 12 }}>
                {selectedDayEvents.map((ev) => (
                  <div
                    key={ev.id}
                  >
                    <div style={{ display: "flex", justifyContent: "space-between", gap: 8 }}>
                      <b style={{ flex: 1, overflow: "hidden", textOverflow: "ellipsis", whiteSpace: "nowrap" }}>
                        {ev.title}
                      </b>
                      <button onClick={() => deleteEvent(ev.id)}>삭제</button>
                    </div>
                    {ev.memo && (
                      <div style={{ fontSize: 12, marginTop: 6, whiteSpace: "pre-wrap" }}>
                        {ev.memo}
                      </div>
                    )}
                    {(ev.startTime || ev.endTime) && (
                      <div style={{ fontSize: 12, marginTop: 6 }}>
                        {ev.startTime ?? ""} {ev.endTime ? `~ ${ev.endTime}` : ""}
                      </div>
                    )}
                  </div>
                ))}
              </div>
            )}

            {/* ✅ 새 일정 추가 */}
            <InsertTitle
              placeholder="제목"
              value={form.title}
              onChange={(e:any) => setForm((p) => ({ ...p, title: e.target.value }))}
              
            />
            <InsertMemo
              placeholder="메모"
              value={form.memo}
              onChange={(e:any) => setForm((p) => ({ ...p, memo: e.target.value }))}
              
            />
            <JustifyContent>
              <W49>
              <TimeInput
                type="time"
                value={form.startTime}
                onChange={(e:any) => setForm((p) => ({ ...p, startTime: e.target.value }))}
                style={{ flex: 1, padding: 10 }}
              />
              </W49>
              <W49>
              <TimeInput
                type="time"
                value={form.endTime}
                onChange={(e:any) => setForm((p) => ({ ...p, endTime: e.target.value }))}
                style={{ flex: 1, padding: 10 }}
              />
              </W49>
            </JustifyContent>

          
              <JustifyContent>
               <W49><GrayBtn onClick={() => setIsModalOpen(false)}>닫기</GrayBtn></W49> 
               <W49><MainBtn onClick={saveEvent}>저장</MainBtn></W49>
              </JustifyContent>
              
              
          
          </Modal>
        </Fixed>
      )}
    </CalTopMargin>
  );
};

export default Calendar2;
