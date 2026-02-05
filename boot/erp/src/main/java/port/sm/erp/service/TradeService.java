package port.sm.erp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import port.sm.erp.dto.TradeRequestDTO;
import port.sm.erp.dto.TradeResponseDTO;
import port.sm.erp.entity.JournalStatus;
import port.sm.erp.entity.Trade;
import port.sm.erp.entity.TradeType;
import port.sm.erp.repository.TradeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    public TradeResponseDTO createTrade(TradeRequestDTO tradeRequestDTO) {
        Trade trade = new Trade();
        trade.setTradeNo(tradeRequestDTO.getTradeNo());
        trade.setTradeDate(tradeRequestDTO.getTradeDate());
        trade.setTradeType(TradeType.valueOf(tradeRequestDTO.getTradeType()));
        trade.setRevenueAccountCode(tradeRequestDTO.getRevenueAccountCode());
        trade.setExpenseAccountCode(tradeRequestDTO.getExpenseAccountCode());
        trade.setCounterAccountCode(tradeRequestDTO.getCounterAccountCode());
        trade.setDeptCode(tradeRequestDTO.getDeptCode());
        trade.setDeptName(tradeRequestDTO.getDeptName());
        trade.setProjectCode(tradeRequestDTO.getProjectCode());
        trade.setProjectName(tradeRequestDTO.getProjectName());
        trade.setRemark(tradeRequestDTO.getRemark());
        trade.setSupplyAmount(tradeRequestDTO.getSupplyAmount());
        trade.setVatAmount(tradeRequestDTO.getVatAmount());
        trade.setFeeAmount(tradeRequestDTO.getFeeAmount());
        trade.setTotalAmount(tradeRequestDTO.getTotalAmount());

        //엔티티 저장
        Trade savedTrade = tradeRepository.save(trade);

        // 저장된 엔티티를 TradeResponseDTO로 변환하여 반환
        return  new TradeResponseDTO(savedTrade);
    }

    // Trade 엔티티의 ID를 이용해 조회 후 DTO 반환
    public TradeResponseDTO getAllTradeById(Long id) {
        Trade trade = tradeRepository.findById(id)
                .orElseThrow( () -> new IllegalArgumentException("Trade not found with id: " + id));
        return new TradeResponseDTO(trade);
    }

    //모든 거래 목록 조회
public List<TradeResponseDTO> getAllTrades() {
        List<Trade> trades = tradeRepository.findAll();
        return trades.stream().map(TradeResponseDTO::new).collect(Collectors.toList());
}

//trade엔티티로 변환후 업데이트
    public TradeResponseDTO updateTrade(Long id, TradeRequestDTO tradeRequestDTO) {
        Trade existingTrade = tradeRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Trade not found with id: " + id));
//dto를 기존 엔티티에 적용
        updateTradeFromDTO(tradeRequestDTO, existingTrade);
        Trade updateTrade = tradeRepository.save(existingTrade);
        // 업데이트된 엔티티를 TradeResponseDTO로 변환하여 반환
        return new TradeResponseDTO(updateTrade);
    }

    //Id를 이용해 Trade삭제
    public void deleteTrade(Long id) {
        if (!tradeRepository.existsById(id)) {
            throw new IllegalArgumentException("Trade not found with id: " + id);
        }
        tradeRepository.deleteById(id);
    }


    private void updateTradeFromDTO(TradeRequestDTO tradeRequestDTO, Trade trade) {
        trade.setTradeNo(tradeRequestDTO.getTradeNo());
        trade.setTradeDate(tradeRequestDTO.getTradeDate());
        trade.setTradeType(TradeType.valueOf(tradeRequestDTO.getTradeType()));  // Enum 변환
        trade.setSupplyAmount(tradeRequestDTO.getSupplyAmount());
        trade.setVatAmount(tradeRequestDTO.getVatAmount());
        trade.setFeeAmount(tradeRequestDTO.getFeeAmount());
        trade.setTotalAmount(tradeRequestDTO.getTotalAmount());
        trade.setRevenueAccountCode(tradeRequestDTO.getRevenueAccountCode());
        trade.setExpenseAccountCode(tradeRequestDTO.getExpenseAccountCode());
        trade.setCounterAccountCode(tradeRequestDTO.getCounterAccountCode());
        trade.setDeptCode(tradeRequestDTO.getDeptCode());
        trade.setDeptName(tradeRequestDTO.getDeptName());
        trade.setProjectCode(tradeRequestDTO.getProjectCode());
        trade.setProjectName(tradeRequestDTO.getProjectName());
        trade.setRemark(tradeRequestDTO.getRemark());
    }

}
