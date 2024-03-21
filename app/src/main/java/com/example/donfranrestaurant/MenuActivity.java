package com.example.donfranrestaurant;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;



import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class MenuActivity extends AppCompatActivity {

    // Declaramos un SparseArray para mapear los IDs de ImageView a los títulos de productos
    private SparseArray<String> imageViewTitles = new SparseArray<>();
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        firestore = FirebaseFirestore.getInstance();

        Button btnMenu = findViewById(R.id.btnMenu);
        Button btnBebidas = findViewById(R.id.btnBebidas);

        btnMenu.setBackgroundColor(Color.parseColor("#EEC800"));

        ScrollView svBebidas = findViewById(R.id.svBebidas);
        ScrollView svMenu = findViewById(R.id.svMenu);

        ImageView ivBack = findViewById(R.id.ivBack);
        ImageView ivHome = findViewById(R.id.ivHome);

        // Llenamos el SparseArray con los IDs de ImageView y los títulos correspondientes
        imageViewTitles.put(R.id.ivCorte1, "Filet de pollo");
        imageViewTitles.put(R.id.ivCorte2, "Filete Mignon");
        imageViewTitles.put(R.id.ivCorte3, "Lomito");
        imageViewTitles.put(R.id.ivCorte4, "Filet de pollo y res");

        imageViewTitles.put(R.id.ivHamburguesa1, "Hamburguesa sencilla");
        imageViewTitles.put(R.id.ivHamburguesa2, "Hamburguesa de pollo");
        imageViewTitles.put(R.id.ivHamburguesa3, "Hamburguesa vaquera");
        imageViewTitles.put(R.id.ivHamburguesa4, "Hamburguesa vaquera");

        imageViewTitles.put(R.id.ivPasta1, "Pasta con camarones en salsa");
        imageViewTitles.put(R.id.ivPasta2, "Pasta con camarones en salsa blanca");
        imageViewTitles.put(R.id.ivPasta3, "Pasta con camarones y pollo");
        imageViewTitles.put(R.id.ivPasta4, "Pasta con camarones y pollo");

        imageViewTitles.put(R.id.ivPostre1, "Pie de manzana");
        imageViewTitles.put(R.id.ivPostre2, "Pie de manzana");
        imageViewTitles.put(R.id.ivPostre3, "Pie de manzana");
        imageViewTitles.put(R.id.ivPostre4, "Pie de manzana");

        imageViewTitles.put(R.id.ivGaseosa1, "Coca cola");
        imageViewTitles.put(R.id.ivGaseosa2, "Coca cola");
        imageViewTitles.put(R.id.ivGaseosa3, "Coca cola");
        imageViewTitles.put(R.id.ivGaseosa4, "Coca cola");

        imageViewTitles.put(R.id.ivCerveza1, "Imperial cero");
        imageViewTitles.put(R.id.ivCerveza2, "Imperial cero");
        imageViewTitles.put(R.id.ivCerveza3, "Imperial cero");
        imageViewTitles.put(R.id.ivCerveza4, "Imperial cero");

        imageViewTitles.put(R.id.ivNaturales1, "Toronja rosa");
        imageViewTitles.put(R.id.ivNaturales2, "Toronja rosa");
        imageViewTitles.put(R.id.ivNaturales3, "Toronja rosa");
        imageViewTitles.put(R.id.ivNaturales4, "Toronja rosa");

        imageViewTitles.put(R.id.ivCaliente1, "Té de limón");
        imageViewTitles.put(R.id.ivCaliente2, "Té de limón");
        imageViewTitles.put(R.id.ivCaliente3, "Té de limón");
        imageViewTitles.put(R.id.ivCaliente4, "Té de limón");

        // Set onClickListeners para productos de cada categoría
        setProductListeners("cortes", R.id.ivCorte1, R.id.ivCorte2, R.id.ivCorte3, R.id.ivCorte4);
        setProductListeners("hamburguesas", R.id.ivHamburguesa1, R.id.ivHamburguesa2, R.id.ivHamburguesa3, R.id.ivHamburguesa4);
        setProductListeners("pastas", R.id.ivPasta1, R.id.ivPasta2, R.id.ivPasta3, R.id.ivPasta4);
        setProductListeners("postres", R.id.ivPostre1, R.id.ivPostre2, R.id.ivPostre3, R.id.ivPostre4);
        setProductListeners("gaseosas", R.id.ivGaseosa1, R.id.ivGaseosa2, R.id.ivGaseosa3, R.id.ivGaseosa4);
        setProductListeners("cervezas", R.id.ivCerveza1, R.id.ivCerveza2, R.id.ivCerveza3, R.id.ivCerveza4);
        setProductListeners("naturales", R.id.ivNaturales1, R.id.ivNaturales2, R.id.ivNaturales3, R.id.ivNaturales4);
        setProductListeners("calientes", R.id.ivCaliente1, R.id.ivCaliente2, R.id.ivCaliente3, R.id.ivCaliente4);

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

    private void setProductListeners(String collectionName, int... imageViewsIds) {
        for (int imageViewId : imageViewsIds) {
            ImageView imageView = findViewById(imageViewId);
            ProductData.setOnClickListenerForProduct(this, imageView, getProductTitle(imageViewId), collectionName);
        }
    }

    private String getProductTitle(int imageViewId) {
        // Buscamos el título correspondiente al imageViewId en el SparseArray
        return imageViewTitles.get(imageViewId, "");
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
