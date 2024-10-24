package net.uniloftsky.markant.bank.biz.persistence.repository;

import net.uniloftsky.markant.bank.biz.persistence.WithdrawTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WithdrawTransactionRepository extends JpaRepository<WithdrawTransactionEntity, UUID> {
}
