package com.wsoteam.diet.presentation.food.helper;

public class NounsDeclension {

    // str1 - "1 Машина", str2 - "2 Машины", str3 - "5 Машин"

    public static String check(int number, String str1, String str2, String str5){

        number = number % 100;

        if (number >= 11 && number <= 19){
            return str5;
        } else {
            number = number % 10;

            switch (number){
                case 1: return str1;
                case 2:
                case 3:
                case 4:return str2;
                    default: return str5;
            }
        }
    }
}
