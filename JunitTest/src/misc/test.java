package misc;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

public class test {
	KnightDistance pd = new KnightDistance();
	
    @Test
    public void Test1(){
        assertEquals(pd.dist(5,6,5,4), 2);
    }

    @Test
    public void Test2(){
        assertEquals(pd.dist(2,3,3,3), 3);
    }

    @Test
    public void Test3(){ 
        assertEquals(pd.dist(2,5,2,6), 3);
    }
    
    @Test
    public void Test4(){
        assertEquals(pd.dist(1,3,3,5), 4);
    }
}
