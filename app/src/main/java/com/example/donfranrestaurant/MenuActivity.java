package com.example.donfranrestaurant;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class MenuActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        firestore = FirebaseFirestore.getInstance();

        Button btnMenu = findViewById(R.id.btnMenu);
        Button btnBebidas = findViewById(R.id.btnBebidas);

        ScrollView svBebidas = findViewById(R.id.svBebidas);
        ScrollView svMenu = findViewById(R.id.svMenu);

        ImageView ivBack = findViewById(R.id.ivBack);
        ImageView ivHome = findViewById(R.id.ivHome);

        ImageView ivCorte1 = findViewById(R.id.ivCorte1);
        ImageView ivCorte2 = findViewById(R.id.ivCorte2);
        ImageView ivCorte3 = findViewById(R.id.ivCorte3);
        ImageView ivCorte4 = findViewById(R.id.ivCorte4);

        ImageView ivHamburguesa1 = findViewById(R.id.ivHamburguesa1);
        ImageView ivHamburguesa2 = findViewById(R.id.ivHamburguesa2);
        ImageView ivHamburguesa3 = findViewById(R.id.ivHamburguesa3);
        ImageView ivHamburguesa4 = findViewById(R.id.ivHamburguesa4);

        ImageView ivPasta1 = findViewById(R.id.ivPasta1);
        ImageView ivPasta2 = findViewById(R.id.ivPasta2);
        ImageView ivPasta3 = findViewById(R.id.ivPasta3);
        ImageView ivPasta4 = findViewById(R.id.ivPasta4);

        ImageView ivPostre1 = findViewById(R.id.ivPostre1);
        ImageView ivPostre2 = findViewById(R.id.ivPostre2);
        ImageView ivPostre3 = findViewById(R.id.ivPostre3);
        ImageView ivPostre4 = findViewById(R.id.ivPostre4);

        ProductData.setOnClickListenerForProductCortes(this, ivCorte1, "Filet de pollo");
        ProductData.setOnClickListenerForProductCortes(this, ivCorte2, "Filete Mignon");
        ProductData.setOnClickListenerForProductCortes(this, ivCorte3, "Lomito");
        ProductData.setOnClickListenerForProductCortes(this, ivCorte4, "Filet de pollo y res");

        ProductData.setOnClickListenerForProductHamburguer(this, ivHamburguesa1, "Hamburguesa sencilla");
        ProductData.setOnClickListenerForProductHamburguer(this, ivHamburguesa2, "Hamburguesa de pollo");
        ProductData.setOnClickListenerForProductHamburguer(this, ivHamburguesa3, "Hamburguesa vaquera");
        ProductData.setOnClickListenerForProductHamburguer(this, ivHamburguesa4, "Hamburguesa vaquera");

        ProductData.setOnClickListenerForProductPastas(this, ivPasta1, "Pasta con camarones en salsa");
        ProductData.setOnClickListenerForProductPastas(this, ivPasta2, "Pasta con camarones en salsa blanca");
        ProductData.setOnClickListenerForProductPastas(this, ivPasta3, "Pasta con camarones y pollo");
        ProductData.setOnClickListenerForProductPastas(this, ivPasta4, "Pasta con camarones y pollo");

        ProductData.setOnClickListenerForProductPostres(this, ivPostre1, "Pie de manzana");
        ProductData.setOnClickListenerForProductPostres(this, ivPostre2, "Pie de manzana");
        ProductData.setOnClickListenerForProductPostres(this, ivPostre3, "Pie de manzana");
        ProductData.setOnClickListenerForProductPostres(this, ivPostre4, "Pie de manzana");

        btnMenu.setBackgroundColor(Color.parseColor("#EEC800"));

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

        // Agregar OnClickListener al ImageView de regreso y cierra sesión
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });

        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                svMenu.setVisibility(View.VISIBLE);
                svBebidas.setVisibility(View.GONE);
                btnMenu.setBackgroundColor(Color.parseColor("#EEC800"));
                btnBebidas.setBackgroundColor(Color.TRANSPARENT);
            }
        });
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Deseas cerrar sesión?");
        builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // No hacer nada
            }
        });
        builder.show();
    }

    private void logout() {
        SharedPreferences sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
