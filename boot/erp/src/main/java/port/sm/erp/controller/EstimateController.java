package port.sm.erp.controller;

import lombok.*;
import org.springframework.web.bind.annotation.*;
import port.sm.erp.dto.EstimateRequest;
import port.sm.erp.dto.EstimateResponse;
import port.sm.erp.service.EstimateService;

import java.util.List;

@RestController
@RequestMapping("/api/sales/estimates")
@RequiredArgsConstructor
public class EstimateController {

    private final EstimateService estimateService;

    //전체 견적서 목록 조회
    @GetMapping
    public List<EstimateResponse> list(){
        return estimateService.list();
    }

    //견적서 단건 조회
    @GetMapping("/{id}")
    public EstimateResponse get(@PathVariable Long id){
        return estimateService.get(id);
    }

    //견적서 등록
    @PostMapping
    public void create(@RequestBody EstimateRequest request){
        estimateService.create(request);
    }

    //견적서 수정
    @PutMapping("/{id}")
    public void update(
@PathVariable Long id, @RequestBody EstimateRequest request
    ){
        estimateService.update(id, request);
    }

    //견적서 삭제
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        estimateService.delete(id);
    }
}