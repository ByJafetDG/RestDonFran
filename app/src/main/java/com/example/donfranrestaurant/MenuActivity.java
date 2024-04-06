package com.example.donfranrestaurant;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;


import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/**
 * La actividad MenuActivity muestra el menú del restaurante con imágenes de los diferentes platos y bebidas.
 * Permite al usuario ver los platos disponibles y seleccionar uno para ver más detalles.
 */
public class MenuActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private FirebaseFirestore firestore;

    /**
     * Método llamado al crear la actividad. Configura la interfaz de usuario y define los listeners de eventos.
     * @param savedInstanceState El estado anterior de la actividad, si lo hay.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Verificar si el usuario ha iniciado sesión previamente
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("loggedIn", false)) {
            // Si el usuario no ha iniciado sesión, redirigirlo a LoginActivity
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Inicialización de FirebaseFirestore
        firestore = FirebaseFirestore.getInstance();

        // Obtención de referencias a elementos de la interfaz de usuario
        Button btnMenu = findViewById(R.id.btnMenu);
        Button btnBebidas = findViewById(R.id.btnBebidas);
        btnMenu.setBackgroundColor(Color.parseColor("#EEC800"));
        ScrollView svBebidas = findViewById(R.id.svBebidas);
        ScrollView svMenu = findViewById(R.id.svMenu);
        ImageView ivBack = findViewById(R.id.ivBack);
        ImageView ivHome = findViewById(R.id.ivHome);
        ImageView ivProfile = findViewById(R.id.ivProfile);


        // Aquí llamamos a un método para cargar las imágenes dinámicamente en cada contenedor
        loadImagesFromFirestore("entradas", R.id.linearlayoutEntradas);
        loadImagesFromFirestore("cortes", R.id.linearlayoutCortes);
        loadImagesFromFirestore("arroces", R.id.linearlayoutArroces);
        loadImagesFromFirestore("hamburguesas", R.id.linearlayoutHamburguesas);
        loadImagesFromFirestore("pastas", R.id.linearlayoutPastas);
        loadImagesFromFirestore("postres", R.id.linearlayoutPostres);
        loadImagesFromFirestore("gaseosas", R.id.linearlayoutGaseosas);
        loadImagesFromFirestore("cervezas", R.id.linearlayoutCervezas);
        loadImagesFromFirestore("naturales", R.id.linearlayoutNaturales);
        loadImagesFromFirestore("calientes", R.id.linearlayoutCalientes);

        // Configuración de OnClickListener para el botón de Bebidas
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

        // Configuración de OnClickListener para el botón de Menú
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

        // Agregar OnClickListener al ImageView de inicio para volver al menú principal
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                svMenu.setVisibility(View.VISIBLE);
                svBebidas.setVisibility(View.GONE);
                btnMenu.setBackgroundColor(Color.parseColor("#EEC800"));
                btnBebidas.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        // Agregar OnClickListener al ImageView de inicio para volver al menú principal
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, PerfilActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * Método para cargar las imágenes desde Firestore y mostrarlas en la interfaz de usuario.
     * @param collectionName El nombre de la colección en Firestore.
     * @param linearLayoutId El ID del LinearLayout donde se mostrarán las imágenes.
     */
    private void loadImagesFromFirestore(String collectionName, int linearLayoutId) {
        LinearLayout linearLayout = findViewById(linearLayoutId);
        firestore.collection(collectionName).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    String imageUrl = document.getString("url");
                    if (imageUrl != null) {
                        // Creamos un ImageView para mostrar la imagen
                        ImageView imageView = new ImageView(MenuActivity.this);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(410, 520);
                        layoutParams.setMargins(65, 15, 0, 0);
                        imageView.setLayoutParams(layoutParams);
                        // Cargamos la imagen utilizando Picasso
                        Picasso.get().load(imageUrl).into(imageView);
                        // Hacemos el ImageView clickeable y enviamos los datos del producto a ProductoActivity
                        imageView.setOnClickListener(view -> {
                            String productName = document.getString("nombre");
                            String productDescription = document.getString("descripcion");
                            String productPrice = document.getString("precio");
                            String productStars = document.getString("stars");
                            // Aquí enviamos los datos a ProductoActivity
                            Intent intent = new Intent(MenuActivity.this, ProductoActivity.class);
                            intent.putExtra("nombre", productName);
                            intent.putExtra("descripcion", productDescription);
                            intent.putExtra("precio", productPrice);
                            intent.putExtra("stars", productStars);
                            intent.putExtra("url", imageUrl);
                            startActivity(intent);
                        });
                        // Agregamos el ImageView al LinearLayout
                        linearLayout.addView(imageView);
                    }
                }
            } else {
                Log.e("Firestore", "Error getting documents: ", task.getException());
            }
        });
    }

    /**
     * Método para mostrar un cuadro de diálogo de confirmación antes de cerrar sesión.
     */
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

    /**
     * Método para cerrar sesión del usuario y redirigirlo a LoginActivity.
     */
    private void logout() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}