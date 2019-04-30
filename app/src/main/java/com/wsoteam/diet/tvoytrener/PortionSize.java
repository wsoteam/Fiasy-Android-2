package com.wsoteam.diet.tvoytrener;

import android.util.Log;



public class PortionSize {
    private String TAG = "PortionSize";


    public PortionSize() {
        Log.d(TAG, "PortionSize: " + womenLossWeight(training[7], ActivityLevel.HIGH.index(), 170, 90, 35));
    }



    private double[] training = {0.9, 0.95, 1, 1.03, 1.06, 1.09, 1.11, 1.13};  // 0 or 7 days in week


    private double caloriesMen(Double training, Double activityLevel, int growth, int weight, int age) {
        return training * (activityLevel * (66 + (13.7 * weight) + (5 * growth) - (6.8 * age)));
    }

    private double caloriesWomen(Double training, Double activityLevel, int growth, int weight, int age) {
        return training * (activityLevel * (665.0 + (9.6 * weight) + (1.8 * growth) - (4.7 * age)));
    }

    public double menSaveWeight(Double training, Double activityLevel, int growth, int weight, int age) {
        return caloriesMen(training, activityLevel, growth, weight, age);
    }

    public double menGainWeight(Double training, Double activityLevel, int growth, int weight, int age) {
        double rashod = caloriesMen(training, activityLevel, growth, weight, age);
        return rashod + ((rashod / 100) * 20);
    }

    public double menLossWeight(Double training, Double activityLevel, int growth, int weight, int age) {
        double rashod = caloriesMen(training, activityLevel, growth, weight, age);
        return rashod - ((rashod / 100) * 20);
    }


    public double womenSaveWeight(Double training, Double activityLevel, int growth, int weight, int age) {
        return caloriesWomen(training, activityLevel, growth, weight, age);
    }

    public double womenGainWeight(Double training, Double activityLevel, int growth, int weight, int age) {
        double rashod = caloriesWomen(training, activityLevel, growth, weight, age);
        return rashod + ((rashod / 100) * 20);
    }

    public double womenLossWeight(Double training, Double activityLevel, int growth, int weight, int age) {
        double rashod = caloriesWomen(training, activityLevel, growth, weight, age);
        return rashod - ((rashod / 100) * 20);
    }
}
/*
if (pol2 == 'man'){var rashod = Math.ceil(trenirovok2 * (aktivnost2 * (66 + (13.7 * ves2) + (5 * rost2) - (6.8 * vozrast2))));}
if (pol2 == 'woman'){var rashod = Math.ceil(trenirovok2 * (aktivnost2 * (665 + (9.6 * ves2) + (1.8 * rost2) - (4.7 * vozrast2))));}
*/


/*
        if ( cel2 == 'pohydet'){var rashod2 = Math.ceil(rashod - ((rashod / 100) * 20));}
        if ( cel2 == 'ne_meniat'){var rashod2 = Math.ceil(rashod);}
        if ( cel2 == 'potolstet'){var rashod2 = Math.ceil(rashod + ((rashod / 100) * 20));}	var zavtrak = Math.ceil((rashod2 / 100) * 36 );





        if (pol2 == 'man'){var rashod = Math.ceil(trenirovok2 * (aktivnost2 * (66 + (13.7 * ves2) + (5 * rost2) - (6.8 * vozrast2))));}
if (pol2 == 'woman'){var rashod = Math.ceil(trenirovok2 * (aktivnost2 * (665 + (9.6 * ves2) + (1.8 * rost2) - (4.7 * vozrast2))));}
if ( cel2 == 'pohydet'){var rashod2 = Math.ceil(rashod - ((rashod / 100) * 20));}
if ( cel2 == 'ne_meniat'){var rashod2 = Math.ceil(rashod);}
if ( cel2 == 'potolstet'){var rashod2 = Math.ceil(rashod + ((rashod / 100) * 20));}	var zavtrak = Math.ceil((rashod2 / 100) * 25 );
document.getElementById('rez_zavtrak').innerHTML= "Всего ккал: " + zavtrak;

var perek2 = Math.ceil((rashod2 / 100) * 11 );
document.getElementById('rez_perek2').innerHTML= "Всего ккал: " + perek2;

var ujin = Math.ceil((rashod2 / 100) * 21 );
document.getElementById('rez_ujin').innerHTML= "Всего ккал: " + ujin;

var obed = Math.ceil((rashod2 / 100) * 34 );
document.getElementById('rez_obed').innerHTML= "Всего ккал: " + obed;

var perek1 = Math.ceil((rashod2 / 100) * 9 );
document.getElementById('rez_perek1').innerHTML= "Всего ккал: " + perek1;
}}*/
