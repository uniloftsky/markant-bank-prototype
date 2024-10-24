package net.uniloftsky.markant.bank.biz.persistence.repository;

import net.uniloftsky.markant.bank.biz.persistence.DepositTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DepositTransactionRepository extends JpaRepository<DepositTransactionEntity, UUID> {

    List<DepositTransactionEntity> findAllByAccountNumber(long accountNumber);

}
