package com.wsoteam.diet.MainScreen.Support;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
            Toast.makeText(contexts[0], "Start rewrite", Toast.LENGTH_SHORT).show();
            rewriteDB(contexts[0]);
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
            Toast.makeText(context, "DB rewrited", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private boolean isEmptyDB() {
        boolean isEmpty = true;
        try {
            CFood cFood = CFood.first(CFood.class);
            if (cFood.getName() != null) {
                isEmpty = false;
            }
        } catch (Exception e) {
            isEmpty = true;
        }
        return isEmpty;
    }
}
