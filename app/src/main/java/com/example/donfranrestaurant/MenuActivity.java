package com.example.donfranrestaurant;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class MenuActivity extends AppCompatActivity {

    private ScrollView svBebidas;

    private Button btnBebidas;

    private ScrollView svMenu;

    private Button btnMenu;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnBebidas = findViewById(R.id.btnBebidas);

        svBebidas = findViewById(R.id.svBebidas);

        btnMenu = findViewById(R.id.btnMenu);

        btnMenu.setBackgroundColor(Color.parseColor("#EEC800"));

        svMenu = findViewById(R.id.svMenu);

        btnBebidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hacer visible el ScrollView de Bebidas y ocultar el ScrollView de Menú
                svBebidas.setVisibility(View.VISIBLE);
                svMenu.setVisibility(View.GONE);

                // Cambiar el color de fondo
                btnBebidas.setBackgroundColor(Color.parseColor("#EEC800"));
                btnMenu.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hacer visible el ScrollView de Menú y ocultar el ScrollView de Bebidas
                svMenu.setVisibility(View.VISIBLE);
                svBebidas.setVisibility(View.GONE);

                // Cambiar el color de fondo y el color de borde del botón de Menú al color seleccionado
                btnMenu.setBackgroundColor(Color.parseColor("#EEC800"));
                btnBebidas.setBackgroundColor(Color.TRANSPARENT);

            }
        });

    }
}
