package net.uniloftsky.markant.bank.biz.persistence.repository;

import net.uniloftsky.markant.bank.biz.persistence.TransferTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * JPA repository for transfer transaction entities
 */
@Repository
public interface TransferTransactionRepository extends JpaRepository<TransferTransactionEntity, UUID> {

    @Query(value = "SELECT tr FROM TransferTransactionEntity tr WHERE tr.fromAccountNumber = :accountNumber OR tr.toAccountNumber = :accountNumber")
    List<TransferTransactionEntity> findAllByFromOrToAccountNumber(@Param("accountNumber") long accountNumber);

}
