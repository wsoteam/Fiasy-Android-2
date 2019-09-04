package com.wsoteam.diet;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import android.view.View;
import android.widget.Button;

public class AddMeasurementsActivity extends AppCompatActivity {

    private Button backButton;
    private CardView weightCardView;
    private CardView waistCardView;
    private CardView chestCardView;
    private CardView hipsCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_measurements);

        backButton = findViewById(R.id.btnBackAddMeasur);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
