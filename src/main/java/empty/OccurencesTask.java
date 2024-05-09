package empty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class OccurencesTask {
//    public static ArrayList<String> getOccurences(int []value){
//        ArrayList<String> output= new ArrayList<>();
//        for(int values : value){
//            if(isInside(output, ((values+ ":" +countOccurences(values,value))))) continue;
//            output.add(values+ ":" +countOccurences(values,value));
//        }
//        return output;
//    }
//    private static boolean isInside(ArrayList<String> output, String values){
//        AtomicBoolean returnValue = new AtomicBoolean(false);
//        output.forEach(items-> {if(items.equals(values)){
//            boolean returnValue1 = true;
//            returnValue.set(returnValue1);
//        }});
//        return returnValue.get();
//    }
//    private static int countOccurences(int number, int[] values){
//        int counter = 0;
//        for(int nums: values){
//            if(number== nums)
//                counter++;
//        }
//        return counter;
//    }
//    private static HashMap<String, Integer> cast(int []value){
//        HashMap<String, Integer> output = new HashMap<>();
//        for(int num : value){
//            output.put(String.valueOf(num),countOccurences(num,value));
//        }
//        return output;
//    }
private static int countOccurences(int number, int[] values){
    int counter = 0;
    for(int nums: values){
        if(number== nums)
            counter++;
    }
    return counter;
}
    public static HashMap<String, Integer> getOccurences(int []value){
        HashMap<String, Integer> output = new HashMap<>();
        for(int num : value){
            output.put(String.valueOf(num),countOccurences(num,value));
        }
        return output;
    }
}
