package port.sm.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import port.sm.erp.dto.TradeLineResponseDTO;
import port.sm.erp.dto.TradeRequestDTO;
import port.sm.erp.dto.TradeResponseDTO;
import port.sm.erp.entity.*;
import port.sm.erp.repository.CustomerRepository;
import port.sm.erp.repository.ItemRepository;
import port.sm.erp.repository.MemberRepository;
import port.sm.erp.repository.TradeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TradeService {

    private final TradeRepository tradeRepository;

    // ✅ 추가 의존성 (없으면 Repository 생성해야 함)
    private final CustomerRepository customerRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository; // userId 쓰면 필요(안 쓰면 제거 가능)

    @Transactional(readOnly = true)
    public List<TradeResponseDTO> list() {
        List<Trade> list = tradeRepository.findListWithCustomer();
        return list.stream()
                .map(this::toResponseWithoutLines)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TradeResponseDTO getDetail(Long id) {
        Trade t = tradeRepository.findDetail(id);
        if (t == null) throw new IllegalArgumentException("거래 없음: " + id);
        return toResponseWithLines(t);
    }

    // =========================
    // DTO 변환 (네 코드 유지)
    // =========================

    private TradeResponseDTO toResponseWithoutLines(Trade t) {
        return TradeResponseDTO.builder()
                .id(t.getId())
                .tradeNo(t.getTradeNo())
                .tradeDate(t.getTradeDate())
                .tradeType(t.getTradeType() != null ? t.getTradeType().name() : null)
                .supplyAmount(t.getSupplyAmount())
                .vatAmount(t.getVatAmount())
                .feeAmount(t.getFeeAmount())
                .totalAmount(t.getTotalAmount())
                .revenueAccountCode(t.getRevenueAccountCode())
                .expenseAccountCode(t.getExpenseAccountCode())
                .counterAccountCode(t.getCounterAccountCode())
                .deptCode(t.getDeptCode())
                .deptName(t.getDeptName())
                .projectCode(t.getProjectCode())
                .projectName(t.getProjectName())
                .remark(t.getRemark())
                .status(t.getStatus() != null ? t.getStatus().name() : null)
                .customerId(t.getCustomer() != null ? t.getCustomer().getId() : null)
                .customerName(t.getCustomer() != null ? t.getCustomer().getCustomerName() : null)
                .build();
    }

    private TradeResponseDTO toResponseWithLines(Trade t) {
        TradeResponseDTO dto = toResponseWithoutLines(t);

        List<TradeLineResponseDTO> lines = (t.getTradeLines() == null ? List.<TradeLine>of() : t.getTradeLines())
                .stream()
                .map(this::toLineResponse)
                .collect(Collectors.toList());

        dto.setTradeLines(lines);
        return dto;
    }

    private TradeLineResponseDTO toLineResponse(TradeLine l) {
        return TradeLineResponseDTO.builder()
                .id(l.getId())
                .itemId(l.getItem() != null ? l.getItem().getId() : null)
                .itemCode(l.getItem() != null ? l.getItem().getItemCode() : null)
                .itemName(l.getItem() != null ? l.getItem().getItemName() : null)
                .qty(l.getQty())
                .unitPrice(l.getUnitPrice())
                .supplyAmount(l.getSupplyAmount())
                .vatAmount(l.getVatAmount())
                .totalAmount(l.getTotalAmount())
                .remark(l.getRemark())
                .build();
    }

    // ✅ SalesController가 호출하는 메서드 이름 맞춰서 추가 (네 코드 유지)
    @Transactional(readOnly = true)
    public List<TradeResponseDTO> getAllTrades() {
        return list();
    }

    @Transactional(readOnly = true)
    public TradeResponseDTO getTradeById(Long id) {
        return getDetail(id);
    }

    // =========================================================
    // ✅✅ 저장 로직
    // =========================================================

    public TradeResponseDTO createTrade(TradeRequestDTO dto) {
        Trade t = new Trade();

        applyHeader(t, dto);
        applyLines(t, dto);

        Trade saved = tradeRepository.save(t);
        return toResponseWithLines(tradeRepository.findDetail(saved.getId()));
    }

    public TradeResponseDTO updateTrade(Long id, TradeRequestDTO dto) {
        Trade t = tradeRepository.findDetail(id);
        if (t == null) throw new IllegalArgumentException("거래 없음: " + id);

        applyHeader(t, dto);

        // ✅ orphanRemoval=true 이므로, 기존 라인을 싹 비우고 다시 넣는 방식이 제일 안전함
        if (t.getTradeLines() != null) t.getTradeLines().clear();
        applyLines(t, dto);

        Trade saved = tradeRepository.save(t);
        return toResponseWithLines(tradeRepository.findDetail(saved.getId()));
    }

    public void deleteTrade(Long id) {
        tradeRepository.deleteById(id);
    }

    // =========================
    // ✅ 내부 헬퍼들
    // =========================

    private void applyHeader(Trade t, TradeRequestDTO dto) {
        // ✅ tradeNo: 프론트에서 빈 값/undefined 올 수 있으니 서버에서 자동 생성 허용
        String tradeNo = trimToNull(dto.getTradeNo());
        if (tradeNo == null) {
            tradeNo = buildAutoTradeNo(dto.getTradeType());
        }

        String tradeDate = requireText(dto.getTradeDate(), "tradeDate");
        TradeType tradeType = parseTradeType(dto.getTradeType());

        t.setTradeNo(tradeNo);
        t.setTradeDate(tradeDate);
        t.setTradeType(tradeType);

        t.setRevenueAccountCode(trimToNull(dto.getRevenueAccountCode()));
        t.setExpenseAccountCode(trimToNull(dto.getExpenseAccountCode()));
        t.setCounterAccountCode(trimToNull(dto.getCounterAccountCode()));

        t.setDeptCode(trimToNull(dto.getDeptCode()));
        t.setDeptName(trimToNull(dto.getDeptName()));
        t.setProjectCode(trimToNull(dto.getProjectCode()));
        t.setProjectName(trimToNull(dto.getProjectName()));
        t.setRemark(trimToNull(dto.getRemark()));

        t.setSupplyAmount(dto.getSupplyAmount());
        t.setVatAmount(dto.getVatAmount());
        t.setFeeAmount(dto.getFeeAmount());
        t.setTotalAmount(dto.getTotalAmount());

        // status (없으면 DRAFT)
        TradeStatus status = parseTradeStatus(dto.getStatus());
        t.setStatus(status != null ? status : TradeStatus.DRAFT);

        // customerId 필수
        if (dto.getCustomerId() == null) throw new IllegalArgumentException("customerId 필수");
        Customer c = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("거래처 없음: " + dto.getCustomerId()));
        t.setCustomer(c);

        // userId는 선택
        if (dto.getUserId() != null) {
            Member u = memberRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("사용자 없음: " + dto.getUserId()));
            t.setUser(u);
        } else {
            t.setUser(null);
        }
    }

    private void applyLines(Trade t, TradeRequestDTO dto) {
        if (dto.getTradeLines() == null) return;

        for (var lineDto : dto.getTradeLines()) {
            if (lineDto.getItemId() == null) {
                throw new IllegalArgumentException("tradeLines.itemId 필수");
            }

            Item item = itemRepository.findById(lineDto.getItemId())
                    .orElseThrow(() -> new IllegalArgumentException("품목 없음: " + lineDto.getItemId()));

            Long qty = requirePositive(lineDto.getQty(), "qty");
            Long unitPrice = requireNonNegative(lineDto.getUnitPrice(), "unitPrice");

            // ✅ 프론트가 supply/vat/total을 안 보내거나 0으로 보내도 서버에서 fallback 계산
            long supplyAmount = (lineDto.getSupplyAmount() != null && lineDto.getSupplyAmount() >= 0)
                    ? lineDto.getSupplyAmount()
                    : qty * unitPrice;

            long vatAmount = (lineDto.getVatAmount() != null && lineDto.getVatAmount() >= 0)
                    ? lineDto.getVatAmount()
                    : Math.round(supplyAmount * 0.1);

            long totalAmount = (lineDto.getTotalAmount() != null && lineDto.getTotalAmount() >= 0)
                    ? lineDto.getTotalAmount()
                    : (supplyAmount + vatAmount);

            TradeLine tl = new TradeLine();
            tl.setItem(item);
            tl.setQty(qty);
            tl.setUnitPrice(unitPrice);
            tl.setSupplyAmount(supplyAmount);
            tl.setVatAmount(vatAmount);
            tl.setTotalAmount(totalAmount);
            tl.setRemark(trimToNull(lineDto.getRemark()));

            // ✅ 핵심: 양방향 연결
            t.addTradeLine(tl);
        }
    }

    private static String requireText(String v, String field) {
        if (v == null) throw new IllegalArgumentException(field + " 필수");
        String s = v.trim();
        if (s.isEmpty()) throw new IllegalArgumentException(field + " 필수");
        return s;
    }

    private static String trimToNull(String v) {
        if (v == null) return null;
        String s = v.trim();
        return s.isEmpty() ? null : s;
    }

    private static Long requirePositive(Long v, String field) {
        if (v == null || v <= 0) throw new IllegalArgumentException(field + "는 1 이상");
        return v;
    }

    private static Long requireNonNegative(Long v, String field) {
        if (v == null || v < 0) throw new IllegalArgumentException(field + "는 0 이상");
        return v;
    }

    private static TradeType parseTradeType(String v) {
        String s = requireText(v, "tradeType");
        return TradeType.valueOf(s); // "SALES" / "PURCHASE"
    }

    private static TradeStatus parseTradeStatus(String v) {
        if (v == null || v.trim().isEmpty()) return null;
        return TradeStatus.valueOf(v.trim()); // "DRAFT" ...
    }

    // ✅ 추가: tradeNo 자동생성
    private static String buildAutoTradeNo(String tradeType) {
        String prefix = "T";
        if (tradeType != null) {
            String t = tradeType.trim().toUpperCase();
            if ("SALES".equals(t)) prefix = "S";
            else if ("PURCHASE".equals(t)) prefix = "P";
        }
        return prefix + "-" + System.currentTimeMillis();
    }
}
