package port.sm.erp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import port.sm.erp.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {



    boolean existsByEmail(String email);

	//Object findByProviderAndProviderId(String string, String providerId);
	Optional<Member> findByProviderAndProviderId(String provider, String providerId);
	
	//일정용
	Optional<Member> findById(Long id);
    Optional<Member> findByUsername(String username);
    Optional<Member> findByEmail(String email);

}