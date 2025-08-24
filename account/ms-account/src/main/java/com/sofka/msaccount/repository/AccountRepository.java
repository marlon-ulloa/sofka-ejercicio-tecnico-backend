package com.sofka.msaccount.repository;

import com.sofka.msaccount.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountRepository extends JpaRepository<Cuenta, Long> {
    @Query(value = "SELECT nextval('ahorros_seq')", nativeQuery = true)
    Long getNextAhorrosSequenceValue();

    @Query(value = "SELECT nextval('corriente_seq')", nativeQuery = true)
    Long getNextCorrienteSequenceValue();
    List<Cuenta> findByClienteId(Long clienteId);
    Cuenta findByNumeroCuenta(String numeroCuenta);
}
