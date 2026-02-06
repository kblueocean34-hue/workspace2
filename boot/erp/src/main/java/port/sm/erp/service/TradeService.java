package port.sm.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import port.sm.erp.dto.TradeRequestDTO;
import port.sm.erp.dto.TradeResponseDTO;
import port.sm.erp.entity.Trade;
import port.sm.erp.entity.TradeStatus;
import port.sm.erp.entity.TradeType;
import port.sm.erp.repository.TradeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;

    /** ✅ 생성 */
    public TradeResponseDTO createTrade(TradeRequestDTO dto) {
        Trade trade = new Trade();

        // ===== 필수값 =====
        trade.setTradeNo(requireText(dto.getTradeNo(), "전표번호(tradeNo)"));
        trade.setTradeDate(requireText(dto.getTradeDate(), "전표일자(tradeDate)")); // 엔티티가 String 기준
        trade.setTradeType(parseTradeType(dto.getTradeType()));

        // ===== 금액 =====
        trade.setSupplyAmount(dto.getSupplyAmount());
        trade.setVatAmount(dto.getVatAmount());
        trade.setFeeAmount(dto.getFeeAmount());
        trade.setTotalAmount(dto.getTotalAmount());

        // ===== 계정 =====
        trade.setRevenueAccountCode(nullIfBlank(dto.getRevenueAccountCode()));
        trade.setExpenseAccountCode(nullIfBlank(dto.getExpenseAccountCode()));
        trade.setCounterAccountCode(requireText(dto.getCounterAccountCode(), "입금/지급계정(counterAccountCode)"));

        // ===== 조직/프로젝트 =====
        trade.setDeptCode(nullIfBlank(dto.getDeptCode()));
        trade.setDeptName(nullIfBlank(dto.getDeptName()));
        trade.setProjectCode(nullIfBlank(dto.getProjectCode()));
        trade.setProjectName(nullIfBlank(dto.getProjectName()));

        // ===== 비고 =====
        trade.setRemark(nullIfBlank(dto.getRemark()));

        // ===== 상태 (DB NOT NULL 방어) =====
        trade.setStatus(parseStatusOrDefault(dto.getStatus(), TradeStatus.DRAFT));

        Trade saved = tradeRepository.save(trade);
        return new TradeResponseDTO(saved);
    }

    /** ✅ 단건 조회 */
    @Transactional(readOnly = true)
    public TradeResponseDTO getTradeById(Long id) {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trade not found with id: " + id));
        return new TradeResponseDTO(trade);
    }

    /** ✅ 전체 조회 */
    @Transactional(readOnly = true)
    public List<TradeResponseDTO> getAllTrades() {
        return tradeRepository.findAll()
                .stream()
                .map(TradeResponseDTO::new)
                .collect(Collectors.toList());
    }

    /** ✅ 수정 */
    public TradeResponseDTO updateTrade(Long id, TradeRequestDTO dto) {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Trade not found with id: " + id));

        // 필수값은 수정에서도 방어 (원하면 requireText 대신 null이면 유지로 바꿔도 됨)
        trade.setTradeNo(requireText(dto.getTradeNo(), "전표번호(tradeNo)"));
        trade.setTradeDate(requireText(dto.getTradeDate(), "전표일자(tradeDate)"));
        trade.setTradeType(parseTradeType(dto.getTradeType()));

        trade.setSupplyAmount(dto.getSupplyAmount());
        trade.setVatAmount(dto.getVatAmount());
        trade.setFeeAmount(dto.getFeeAmount());
        trade.setTotalAmount(dto.getTotalAmount());

        trade.setRevenueAccountCode(nullIfBlank(dto.getRevenueAccountCode()));
        trade.setExpenseAccountCode(nullIfBlank(dto.getExpenseAccountCode()));
        trade.setCounterAccountCode(requireText(dto.getCounterAccountCode(), "입금/지급계정(counterAccountCode)"));

        trade.setDeptCode(nullIfBlank(dto.getDeptCode()));
        trade.setDeptName(nullIfBlank(dto.getDeptName()));
        trade.setProjectCode(nullIfBlank(dto.getProjectCode()));
        trade.setProjectName(nullIfBlank(dto.getProjectName()));

        trade.setRemark(nullIfBlank(dto.getRemark()));

        // status가 들어오면 반영, 안 들어오면 기존 유지 (단, null이면 DRAFT 보정)
        if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
            trade.setStatus(parseStatusOrDefault(dto.getStatus(), TradeStatus.DRAFT));
        } else if (trade.getStatus() == null) {
            trade.setStatus(TradeStatus.DRAFT);
        }

        Trade saved = tradeRepository.save(trade);
        return new TradeResponseDTO(saved);
    }

    /** ✅ 삭제 */
    public void deleteTrade(Long id) {
        if (!tradeRepository.existsById(id)) {
            throw new IllegalArgumentException("Trade not found with id: " + id);
        }
        tradeRepository.deleteById(id);
    }

    // =========================
    // Utils
    // =========================

    private String requireText(String v, String name) {
        if (v == null || v.isBlank()) {
            throw new IllegalArgumentException(name + " 는(은) 필수입니다.");
        }
        return v.trim();
    }

    private String nullIfBlank(String v) {
        if (v == null) return null;
        String t = v.trim();
        return t.isEmpty() ? null : t;
    }

    private TradeType parseTradeType(String v) {
        // v가 null/빈값이면 기본 SALES
        if (v == null || v.isBlank()) return TradeType.SALES;
        return TradeType.valueOf(v.trim().toUpperCase());
    }

    private TradeStatus parseStatusOrDefault(String v, TradeStatus def) {
        if (v == null || v.isBlank()) return def;
        try {
            return TradeStatus.valueOf(v.trim().toUpperCase());
        } catch (Exception e) {
            return def;
        }
    }
}
