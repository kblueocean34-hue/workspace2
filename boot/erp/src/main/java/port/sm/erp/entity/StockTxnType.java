package port.sm.erp.entity;

//1)오타방지, 2)가독성, 3)DB값 표준화, 4) 비즈니스 로직분리,  5) 나중에 기능 추가 쉬움
public enum StockTxnType {
    IN, OUT, ADJUST, RESERVE, RELEASE
}
//데이터 타입을 정해진 선택지로 잠가서 실수 못하게 하는 안전장치
