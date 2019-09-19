package numeric;

import org.junit.Assert;
import org.junit.Test;

public class FractionTest {
    @Test
    public void test(){
        Assert.assertEquals(Fraction.gcd(-1,5), 1);
        Assert.assertEquals(Fraction.gcd(0,5), 5);
        Assert.assertEquals(Fraction.gcd(1,5), 1);
        Assert.assertEquals(Fraction.gcd(5,5), 5);
        Assert.assertEquals(Fraction.gcd(99,5), 1);
        Assert.assertEquals(Fraction.gcd(100,5), 5);
        Assert.assertEquals(Fraction.gcd(101,5), 1);
        Assert.assertEquals(Fraction.gcd(5,-1), 1);
        Assert.assertEquals(Fraction.gcd(5,0), 5);
        Assert.assertEquals(Fraction.gcd(5,1), 1);
        Assert.assertEquals(Fraction.gcd(5,99), 1);
        Assert.assertEquals(Fraction.gcd(5,100), 5);
        Assert.assertEquals(Fraction.gcd(5,101), 1);
    }
}
