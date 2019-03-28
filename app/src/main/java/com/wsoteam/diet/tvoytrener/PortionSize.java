package com.wsoteam.diet.tvoytrener;

import android.util.Log;
import org.apache.commons.math3.util.Precision;


public class PortionSize {

    private String TAG = "PortionSize";

    public PortionSize(){
        Log.d(TAG, "PortionSize: " + rashod2("ne_meniat",rashod("woman", training[3], ActivityLevel.HIGH.index(), 200, 70, 22)));
    }

   /* if(trenirovok2 == 0){trenirovok2 = 0.9}
if(trenirovok2 == 1){trenirovok2 = 0.95}
if(trenirovok2 == 2){trenirovok2 = 1}
if(trenirovok2 == 3){trenirovok2 = 1.03}
if(trenirovok2 == 4){trenirovok2 = 1.06}
if(trenirovok2 == 5){trenirovok2 = 1.09}
if(trenirovok2 == 6){trenirovok2 = 1.11}
if(trenirovok2 == 7){trenirovok2 = 1.13}
*/
private double[] training = {0.9, 0.95, 1, 1.03, 1.06, 1.09, 1.11, 1.13};


private double rashod(String gender, Double training, Double activityLevel, int growth, int weight, int age){

    double result;

    switch (gender){
        case "man":
            result = training * (activityLevel * (66 + (13.7 * weight) + (5 * growth) - (6.8 * age)));
            break;

        case "woman":
            result = training * (activityLevel * (665.0 + (9.6 + weight) + (1.8 * growth) - (4.7 * age)));
            break;

        default:
                result = 0;
                break;
    }
    return Precision.round(result, 0);
}

private double rashod2(String cel, Double rashod){
    Double result;

    switch (cel){
        case "pohydet":
            result = rashod - ((rashod / 100) * 20);
            break;
        case "ne_meniat":
            result = rashod;
            break;
        case "potolstet":
            result = rashod + ((rashod / 100) * 20);
            break;
            default:
                result = 0.0;
                break;
    }

    return result;
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
