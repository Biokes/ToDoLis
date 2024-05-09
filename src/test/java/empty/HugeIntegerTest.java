package empty;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HugeIntegerTest {
    @Test
    public void testAddHugeInteger(){
        HugeInteger number = HugeInteger.parse("123456789");
        assertEquals("123456789", number.toString());
        assertTrue(new HugeInteger("12346879"). equals(new HugeInteger("12346879")));
        assertFalse(new HugeInteger("-12346789").equals(new HugeInteger("12346879")));
        assertEquals(new HugeInteger("-1"),HugeInteger.parse("-1"));
        assertEquals(new HugeInteger("0"),HugeInteger.parse("-00000"));
        assertEquals("-1234",HugeInteger.parse("-01234").toString());
    }
    @Test
    public  void testHugeIntegerAdd(){
        assertEquals(new HugeInteger("222222"),(new HugeInteger("111111").add(new HugeInteger("111111"))));
        assertEquals(new HugeInteger("-222222"),(new HugeInteger("-0444444").add(new HugeInteger("0222222"))));
        assertEquals(new HugeInteger("222222"),(new HugeInteger("444444").add(new HugeInteger("-0222222"))));
        assertEquals(new HugeInteger("1234"), (new HugeInteger("1235").add(new HugeInteger("-1"))));
        assertEquals(new HugeInteger("45637"), (new HugeInteger("37").add(new HugeInteger("045600"))));
    }
}