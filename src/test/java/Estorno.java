import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import com.example.DigitalWallet;

class Estorno {
    
    static Stream<Arguments> valoresEstorno() {
        return Stream.of(
            Arguments.of(100.0, 10.0, 110.0),
            Arguments.of(0.0, 5.0, 5.0),
            Arguments.of(50.0, 0.01, 50.01)
        );
    }

    @ParameterizedTest
    @MethodSource("valoresEstorno")
    void refundComCarteiraValida(double inicial, double valor, double saldoEsperado) {
        DigitalWallet carteira = new DigitalWallet("Gabrielle", inicial);
        carteira.verify();
        
        assumeTrue(carteira.isVerified() && !carteira.isLocked(), "Pré-condição não atendida.");
        
        carteira.refund(valor);
        
        assertEquals(saldoEsperado, carteira.getBalance(), "O saldo não foi atualizado corretamente após o estorno.");
    }

    @ParameterizedTest
    @ValueSource(doubles = {0.0, -10.0, -0.01})
    void deveLancarExcecaoParaRefundInvalido(double valor) {
        DigitalWallet carteira = new DigitalWallet("Gabrielle", 100.0);
        carteira.verify();

        assumeTrue(carteira.isVerified() && !carteira.isLocked(), "Pré-condição não atendida.");

        assertThrows(IllegalArgumentException.class, () -> {
            carteira.refund(valor);
        }, "Uma exceção deveria ter sido lançada para valores de estorno inválidos.");
    }

    @Test
    void deveLancarSeNaoVerificadaOuBloqueada() {
        DigitalWallet carteiraNaoVerificada = new DigitalWallet("semVerificacao", 100.0);
        assertThrows(IllegalStateException.class, () -> {
            carteiraNaoVerificada.refund(10.0);
        }, "A exceção deveria ser lançada para carteira não verificada.");
        
        DigitalWallet carteiraBloqueada = new DigitalWallet("bloqueado", 100.0);
        carteiraBloqueada.verify(); 
        carteiraBloqueada.lock(); 
        assertThrows(IllegalStateException.class, () -> {
            carteiraBloqueada.refund(10.0);
        }, "A exceção deveria ser lançada para carteira bloqueada.");
    }
}