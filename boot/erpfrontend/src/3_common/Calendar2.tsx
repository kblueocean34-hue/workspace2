import { useMemo, useState } from "react";
import { holidays as holidayData } from "@kyungseopk1m/holidays-kr";
import { Wrapper, Header, Grid, DayName } from "../stylesjs/Content.styles";

interface RawHoliday {
  date: number; // YYYYMMDD
  name: string;
}

const Calendar2 = () => {
  const [currentDate] = useState(new Date());

  const year = currentDate.getFullYear();
  const month = currentDate.getMonth(); // 0~11

  // 연도별 공휴일 + 성탄절 수동 추가
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

    // 12월 성탄절 강제 추가
    if (month === 11 && !holidays.some(h => String(h.date) === `${year}1225`)) {
      holidays.push({ date: Number(`${year}1225`), name: "성탄절" });
    }

    return holidays;
  }, [year, month]);

  // 현재 월 공휴일 필터
  const holidays = useMemo(() => {
    return rawHolidays.filter(
      h => Number(String(h.date).slice(4, 6)) === month + 1
    );
  }, [rawHolidays, month]);

  const firstDay = new Date(year, month, 1).getDay();
  const lastDate = new Date(year, month + 1, 0).getDate();

  return (
    <Wrapper>
      <Header>
        <h3>
          {year}년 {month + 1}월
        </h3>
      </Header>

      <Grid>
        {/* 요일 */}
        {["일", "월", "화", "수", "목", "금", "토"].map(day => (
          <DayName key={day}>{day}</DayName>
        ))}

        {/* 빈칸 */}
        {Array.from({ length: firstDay }).map((_, idx) => (
          <div key={`empty-${idx}`} />
        ))}

        {/* 날짜 셀 */}
        {Array.from({ length: lastDate }, (_, idx) => {
          const day = idx + 1;
          const weekday = (firstDay + idx) % 7;

          // day와 비교해서 공휴일 찾기
          const holiday = holidays.find(
            h => Number(String(h.date).slice(6, 8)) === day
          );

          const isHoliday = Boolean(holiday);
          const isSunday = weekday === 0; // 일요일 체크
          const isChristmas = holiday?.name === "성탄절";

          return (
            <div
              key={day}
              style={{
                height: 50,
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                background: isHoliday ? "#ffefc3" : "#f4f4f4",
                borderRadius: 8,
                color: isSunday ? "red" : "#333",
                margin: 2,
              }}
              title={holiday?.name || ""}
            >
              {day} {isChristmas && "🎄"}
            </div>
          );
        })}
      </Grid>
    </Wrapper>
  );
};

export default Calendar2;
