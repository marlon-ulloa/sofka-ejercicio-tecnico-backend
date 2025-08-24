package com.sofka.msaccount.service;

import com.sofka.msaccount.dto.AccountDTO;
import com.sofka.msaccount.dto.Response;
import com.sofka.msaccount.entity.Cuenta;
import com.sofka.msaccount.exception.ResourceNotFoundException;
import com.sofka.msaccount.repository.AccountRepository;
import com.sofka.msaccount.util.FormatData;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Clase que se encarga de reaizar la logica de negocio y sirve como intermediario entre el reposiroty y el controller
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Cuenta> getAllCuentas() {
        return accountRepository.findAll();
    }

    public AccountDTO getCuentaById(Long id) {
        Cuenta account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada con id " + id));
        return FormatData.getInstance().fromAccountToAccountDTO(account);
    }

    public List<AccountDTO> findByClientId(Long clientId){
        List<Cuenta> accounts= accountRepository.findByClienteId(clientId);

        return FormatData.getInstance().fromAccountListToAccountDTOList(accounts);
    }

    public Response createCuenta(AccountDTO accountDTO) {
        Cuenta newAccount = new Cuenta();
        Cuenta account = FormatData.getInstance().fromAccountDTOToAccount(newAccount, accountDTO);
        //Si no se envia el numero de cuenta entonce se genera uno manteniendo una diferenciacion entre:
        // ahorros (inicia con 4, se coloca 4 ceros y seguido del secuencial),
        // y corriente (inicia con 2, se coloca 4 ceros y seguido del secuencial)
        if (accountDTO.getNumeroCuenta() == null || accountDTO.getNumeroCuenta().isEmpty()) {
            String prefix = account.getTipoCuenta().toLowerCase().compareTo("ahorros") == 0 ? "4" : "2";
            Long nexVal = account.getTipoCuenta().toLowerCase().compareTo("ahorros") == 0 ?
                    accountRepository.getNextAhorrosSequenceValue() :
                    accountRepository.getNextCorrienteSequenceValue();
            String numero = prefix + String.format("%05d", nexVal);
            account.setNumeroCuenta(numero);
        }
        account.setSaldoActual(account.getSaldoInicial());
        Cuenta accountCreated = accountRepository.save(account);
        Response response = FormatData.getInstance().formatResponse(HttpStatus.CREATED.value(), "Cuenta creada exitosamente", accountCreated);
        return response;
    }

    public Response updateCuenta(Long id, AccountDTO accountDTO) {
        AccountDTO actualAccount = getCuentaById(id);
        Cuenta account = FormatData.getInstance().fromAccountDTOToAccount(new Cuenta(), actualAccount);
        account.setTipoCuenta(accountDTO.getTipoCuenta());
        account.setSaldoInicial(accountDTO.getSaldoInicial());
        account.setSaldoActual(accountDTO.getSaldoActual());
        account.setEstado(accountDTO.getEstado());
        Cuenta updatedAccount = accountRepository.save(account);
        Response response = FormatData.getInstance().formatResponse(HttpStatus.OK.value(), "Cuenta actualizada exitosamente", updatedAccount);
        return response;
    }
}
