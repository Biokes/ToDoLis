package empty;

import java.util.Arrays;

public class HugeInteger {
    private int[] digits;
    private boolean isNegative;
    private HugeInteger(int[] output){
        this.digits = output;
    }
    public HugeInteger(String value){
        this.isNegative = value.charAt(0)=='-';
        this.digits = parse(value).getDigits();
    }
    public static HugeInteger parse(String number){
        return new HugeInteger(wrapToArray(number));
    }
    public HugeInteger add(HugeInteger other){
        int [] temp1= this.getDigits();
        int []  temp2 = other.getDigits();
        boolean isBothNegative = this.isNegative && other.isNegative;
        boolean isBothPositive = !this.isNegative && !other.isNegative;
        if(isBothNegative || isBothPositive){
            if(isBothPositive&& temp1.length ==temp2.length){
                int [] number= new int[40];

            }
        }
        return null;
    }
    public String toString(){
        StringBuilder values = new StringBuilder();
        for(int number: this.digits)
            values.append(number);
        return values.toString();
    }
    public boolean equals(Object value){
        if(this.getClass() == value.getClass()) {
            if ((this.digits.length == new HugeInteger(String.valueOf(value)).getDigits().length) && (this.isNegative == new HugeInteger(String.valueOf(value)).isNegative())) {
                for (int count = 0; count < this.digits.length; count++)
                    if (this.getDigits()[count] != new HugeInteger(String.valueOf(value)).getDigits()[count])
                        return false;
                return true;
            }
        }
        return false;
    }
    private boolean isNegative(){
        return isNegative;
    }
    private int[] getDigits(){
        return this.digits;
    }
    private static int[] wrapToArray(String number){
        validate(number);
        if(number.startsWith("-")) {
            number = number.replace("-", " ");
            number=number.strip();
            while(number.startsWith("0")){
                number = number.replace("0", " ");
                number=number.strip();
            }
            if(number.isEmpty()) number="0";
            int [] output = new int[number.length()];
            for(int count = 0; count < number.length(); count++) output[count]=Integer.parseInt(String.valueOf(number.charAt(count)));
            output[0]*=-1;
            return output;
        }
        int [] output = new int[number.length()];
        for(int count = 0; count < number.length(); count++)
            output[count]=Integer.parseInt(String.valueOf(number.charAt(count)));
        return output;
    }
    private static void validate(String input){
        if(!(input.matches("-?[0-9]{1,40}")))
            throw new RuntimeException("Number is beyond Huge integer capacity");
        boolean isNegative = false;
        if(input.startsWith("-")){
            isNegative= true;
            input= input.replace('-', ' ');
            input = input.strip();
        }
        while(input.startsWith("0")){
            input=input.replaceFirst("\\d"," ");
            input=input.strip();
        }
        if(input.isEmpty()){input ="0";return;}
        if(isNegative)input="-"+input;
    }
}
