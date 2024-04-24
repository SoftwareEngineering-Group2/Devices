import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AllTest {

    @Test
    public void testAddition() {
        int result = 3 + 5;
        assertEquals(8, result);
    }

    @Test
    public void testConvertByte() {
        SerialConnection serial = new SerialConnection();
        Integer[] testArr = {0, 1, 0, 0, 0, 0, 0, 0};
        byte result = serial.convertToByte(testArr);
        assertEquals(64, result);
    }

    @Test
    public void testConvertArray() {
        SerialConnection serial = new SerialConnection();
        byte testByte = (byte) 192;
        int[] result = serial.byteToArray(testByte);
        assertEquals(0, result[0]);
        assertEquals(0, result[1]);
        assertEquals(0, result[2]);
        assertEquals(0, result[3]);
        assertEquals(0, result[4]);
        assertEquals(0, result[5]);
        assertEquals(1, result[6]);
        assertEquals(1, result[7]);
    }

}
