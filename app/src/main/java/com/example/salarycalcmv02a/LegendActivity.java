package com.example.salarycalcmv02a;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.RemoteViews;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LegendActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_legend);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void btnLegendOnClickEvent(View view) {
        this.finish();
        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
    }

    public void btnInstructOnClickEvent(View view) {
        TextView txtLegend = findViewById(R.id.txtLegend);
        String legend = getResources().getString(R.string.textLegend);
        String currentText = txtLegend.getText().toString();
        if(currentText.equals(legend))
            txtLegend.setText(R.string.Instruction);
        else
            txtLegend.setText(R.string.textLegend);
    }
}