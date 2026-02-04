package port.sm.erp.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import port.sm.erp.entity.Customer;
import port.sm.erp.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Service //비즈니스 로직 담당
@RequiredArgsConstructor //final이 붙은 필드를 생성자로 자동주입
public class CustomerService {//고객 관련 로직을 담당하는 서비스 클래스

    private final CustomerRepository customerRepository; //db와 직접 통신한 객체

    //전체 고객 조회
    public List<Customer> getAllCustomers(){
        //모든 고객을 가져오는 메서드
        return customerRepository.findAll();
    }

    //id로 고객 한명 조회 그래서 있을수도 있고 없을수도 잇어서 Optional
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }

    //고객 생성
    public Customer createCustomer(Customer customer) {
        //새고객을 만드는 메서드
        return customerRepository.save(customer);
    }

    //고객수정(UPDATE)
    public Customer updateCustomer(Long id, Customer customer){
        //특정 id의 고객 정보수정
        if(customerRepository.existsById(id)){//해당 id가 db에 존제하는지 확인
customer.setId(id); //수정할 객체에 id를 강제로 설정 이게 없으면 insert가 될수 있음
 return customerRepository.save(customer); //id가 있으면 update 실행
        }
        return null;//해당 id가 없으면 수정 실패 → null 반환
//.orElseThrow(() -> new EntityNotFound..
    }
    
    //고객삭제
    public boolean deleteCustomer(Long id) {
        if(customerRepository.existsById(id)){ //삭제할 고객이 존재 하는지 체크
            customerRepository.deleteById(id);
            return true;
        }
        return false;//고객이 없어서 삭제 실패
    }
}
