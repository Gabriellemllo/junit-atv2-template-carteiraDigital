import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.DigitalWallet;


class Deposito {

    @ParameterizedTest
    @DisplayName("Testa depositos validos")
    @ValueSource(doubles = {5.0, 15.50, 100.0, 750.0})
    void deveDepositarValoresValidos(double valor) {
        DigitalWallet carteira = new DigitalWallet("Gabrielle", 0.0);
        
        carteira.deposit(valor);

        assertEquals(valor, carteira.getBalance(), 0.0001, "O saldo deve ser igual ao valor depositado.");
    }

    @ParameterizedTest
    @DisplayName("Testa depositos invalidos")
    @ValueSource(doubles = {-0.01, 0.0, -1.0, -50.0})
    void deveLancarExcecaoParaDepositoInvalido(double valor) {
        DigitalWallet carteira = new DigitalWallet("Gabrielle", 10.0);
        double saldoInicial = carteira.getBalance();
        
        assertThrows(IllegalArgumentException.class, () -> carteira.deposit(valor), "Deve lancar excecao para depositos menores ou iguais a zero.");

        assertEquals(saldoInicial, carteira.getBalance(), "O saldo nao deve ser alterado apos um deposito invalido.");
    }
}