import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.example.DigitalWallet;

class SaldoInicial {
        @Test
        void deveConfigurarSaldoInicialCorreto() {
           DigitalWallet carteira = new DigitalWallet("Gabrielle", 100.0); 
           assertEquals(100.0, carteira.getBalance());
        }
        @Test
        void deveLancarExcecaoParaSaldoInicialNegativo() {
              assertThrows(IllegalArgumentException.class, () -> {
                new DigitalWallet("Matheus", -50.0);
              });
            
        }
    }