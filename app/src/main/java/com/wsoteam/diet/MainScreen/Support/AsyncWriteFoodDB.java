package com.wsoteam.diet.MainScreen.Support;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.wsoteam.diet.App;
import com.wsoteam.diet.BranchOfAnalyzer.POJOFoodSQL.FoodDAO;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AsyncWriteFoodDB extends AsyncTask<Context, Void, Void> {
    private static final String TAG = "AsyncWriteFoodDB";
    private String packageName = "com.wild.diet";
    private String DATABASE_NAME = "foodDB.db";
    private String FILE_NAME = "rewrite";

    @Override
    protected Void doInBackground(Context... contexts) {
        FoodDAO foodDAO = App.getInstance().getFoodDatabase().foodDAO();
//        if (isEmptyDB(foodDAO)) {
            Log.d(TAG, "Start rewrite");
            rewriteDB(contexts[0]);
//        }
        return null;
    }

    private void rewriteDB(Context context) {
        try {
            InputStream myInput = context.getAssets().open(FILE_NAME);
            String outFileName = context.getFilesDir().getParent() + "/databases/" + DATABASE_NAME;
            OutputStream outputStream = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            myInput.close();
            Log.d(TAG, "DB rewrited");
        } catch (IOException e) {
            Log.d(TAG, e.toString());
            e.printStackTrace();
        }
    }

    private boolean isEmptyDB(FoodDAO foodDAO) {
        boolean isEmpty = true;
        if (foodDAO.getById(1) != null) {
            isEmpty = false;
        }
        return isEmpty;
    }
}
