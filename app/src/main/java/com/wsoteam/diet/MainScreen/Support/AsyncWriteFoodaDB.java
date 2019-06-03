package com.wsoteam.diet.MainScreen.Support;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.orm.SugarContext;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.CFood;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AsyncWriteFoodaDB extends AsyncTask<Context, Void, Void> {
    private String packageName = "com.wild.diet";

    @Override
    protected Void doInBackground(Context... contexts) {
        if (isEmptyDB()) {
            rewriteDB(contexts[0]);
            Log.e("LOL", "start rewrite db");
        }
        return null;
    }

    private void rewriteDB(Context context) {
        try {
            InputStream myInput = context.getAssets().open("FoodDB.db");
            String outFileName = "/data/data/" + packageName + "/databases/" + "foodBase.db";
            OutputStream outputStream = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            myInput.close();
            SugarContext.init(context);
        } catch (IOException e) {
            Log.e("LOL", e.toString());
        }
    }

    private boolean isEmptyDB() {
        boolean isEmpty = true;
        CFood cFood = CFood.first(CFood.class);
        if (cFood != null){
            isEmpty = false;
        }
        return isEmpty;
    }
}
