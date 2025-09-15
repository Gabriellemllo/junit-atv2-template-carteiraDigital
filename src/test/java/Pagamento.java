import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.DigitalWallet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class Pagamento {

    @ParameterizedTest
    @CsvSource({
        "100.0, 30.0, true", 
        "50.0, 80.0, false", 
        "10.0, 10.0, true"
    })

    void pagamentoComCarteiraVerificadaENaoBloqueada(double saldoInicial, double valorPagamento, boolean resultadoEsperado) {
        DigitalWallet digitalWallet = new DigitalWallet("Gabrielle", saldoInicial);
        digitalWallet.unlock();
        digitalWallet.verify();

        assumeTrue(digitalWallet.isVerified() && !digitalWallet.isLocked());

        boolean result = digitalWallet.pay(valorPagamento);
        assertEquals(resultadoEsperado, result);

        if (result) {
            assertEquals(saldoInicial - valorPagamento, digitalWallet.getBalance(), "O saldo deve ser debitado após o pagamento.");
        } else {
            assertEquals(saldoInicial, digitalWallet.getBalance(), "O saldo não deve ser alterado se o pagamento falhar.");
        }
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -10.0, -0.01})
    void deveLancarExcecaoParaPagamentoInvalido(double valor) {
        DigitalWallet carteira = new DigitalWallet("Gabrielle", 100.0);
        carteira.verify(); 
        carteira.unlock();
        
        assumeTrue(carteira.isVerified() && !carteira.isLocked());
        
        assertThrows(IllegalArgumentException.class, () -> {
            carteira.pay(valor);
        }, "O pagamento deve ter um valor maior que zero.");
    }

    @Test
    void deveLancarSeNaoVerificada() {
        DigitalWallet carteira = new DigitalWallet("Gabrielle", 100.0);
        carteira.unlock();
        
        Assertions.assertThrows(IllegalStateException.class, () -> carteira.pay(10.0), "Deve lançar exceção se a carteira não estiver verificada.");
    }
    
    @Test
    void deveLancarSeBloqueada() {
        DigitalWallet carteira = new DigitalWallet("Gabrielle", 100.0);
        carteira.verify();
        carteira.lock();
        
        Assertions.assertThrows(IllegalStateException.class, () -> carteira.pay(10.0), "Deve lançar exceção se a carteira estiver bloqueada.");
    }
}