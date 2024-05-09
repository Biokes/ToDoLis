package empty;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountOccurenceTest {
    @Test
    public void testCounter(){
        HashMap<String,Integer> output = new HashMap<>();
        output.put("1", 2);output.put("2",2);
        output.put("3",1);
        assertEquals(output,OccurencesTask.getOccurences(new int[]{1,2,2,1,3}));
    }
}
