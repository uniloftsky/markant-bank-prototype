package net.uniloftsky.markant.bank.biz.persistence.repository;

import net.uniloftsky.markant.bank.biz.persistence.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository for Bank Account entities
 */
@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
}
