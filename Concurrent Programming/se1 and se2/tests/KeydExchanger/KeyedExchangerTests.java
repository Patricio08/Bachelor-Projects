package KeydExchanger;

import KeydExchanger.KeyedExchanger;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class KeyedExchangerTests {

    @Test
    public void testExchange() throws InterruptedException {

        String oldData = "Old";
        String newData = "New";

        KeyedExchanger exchanger = new KeyedExchanger();
        int timeout = 10000;

        AtomicReference<Optional<String>> opt1 = new AtomicReference<>();
        AtomicReference<Optional<String>> opt2 = new AtomicReference<>();

        Thread t1 = new Thread( () -> {
            try {
                opt1.set(exchanger.exchange(1, oldData, timeout));
            } catch (InterruptedException e) {
                System.out.println("Interrupt Exception");
            }
        });

        Thread t2 = new Thread( () -> {
            try {
                opt2.set(exchanger.exchange(1, newData, timeout));
            } catch (InterruptedException e) {
                System.out.println("Interrupt Exception");
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        //Optional<String> result = Optional.of(oldData);
        assertEquals(opt1.get().get(), newData);
        assertEquals(opt2.get().get(), oldData);
    }
}
