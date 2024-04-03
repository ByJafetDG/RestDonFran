package com.example.donfranrestaurant;

import android.content.Intent;

import java.util.UUID;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * La actividad ProductoActivity muestra los detalles de un producto específico y permite al usuario realizar un pedido.
 * También gestiona la autenticación del usuario y el envío del pedido a Firestore.
 */
public class ProductoActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    /**
     * Método llamado al crear la actividad. Configura la interfaz de usuario y define los listeners de eventos.
     * @param savedInstanceState El estado anterior de la actividad, si lo hay.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

        // Inicializar FirebaseFirestore
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Obtención de referencias a elementos de la interfaz de usuario
        Button btnOrdenar = findViewById(R.id.btnOrdenar);
        EditText etNumMesa = findViewById(R.id.etNumMesa);
        ImageView ivStar1Fill = findViewById(R.id.ivStarFill1);
        ImageView ivStar2Fill = findViewById(R.id.ivStarFill2);
        ImageView ivStar3Fill = findViewById(R.id.ivStarFill3);
        ImageView ivStar4Fill = findViewById(R.id.ivStarFill4);
        ImageView ivStar5Fill = findViewById(R.id.ivStarFill5);

        ivStar1Fill.setVisibility(View.INVISIBLE);
        ivStar2Fill.setVisibility(View.INVISIBLE);
        ivStar3Fill.setVisibility(View.INVISIBLE);
        ivStar4Fill.setVisibility(View.INVISIBLE);
        ivStar5Fill.setVisibility(View.INVISIBLE);

        ImageView ivImagenProducto = findViewById(R.id.ivImagenProducto);
        ImageView ivBack = findViewById(R.id.ivBack);
        ImageView ivHome = findViewById(R.id.ivHome);
        TextView tvNombreProducto = findViewById(R.id.tvNombreProducto);
        TextView tvDescripcion = findViewById(R.id.tvDescripcion);
        TextView tvPrecio = findViewById(R.id.tvPrecio);

        // Obtener datos pasados desde MenuActivity
        String nombre = getIntent().getStringExtra("nombre");
        String descripcion = getIntent().getStringExtra("descripcion");
        String precio = getIntent().getStringExtra("precio");
        String stars = getIntent().getStringExtra("stars");
        String url = getIntent().getStringExtra("url");

        // Configuración de OnClickListener para el botón "Ordenar"
        btnOrdenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String nombreProducto = tvNombreProducto.getText().toString();
                final String precioProducto = tvPrecio.getText().toString();
                final String numMesa = etNumMesa.getText().toString();

                // Verificar si el campo "Número de Mesa" no está vacío
                if (numMesa.isEmpty()) {
                    // Mostrar un mensaje de error si el campo está vacío
                    Toast.makeText(ProductoActivity.this, "Por favor ingrese el número de mesa", Toast.LENGTH_SHORT).show();
                    return; // Salir del método onClick sin realizar más acciones
                }

                btnOrdenar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String nombreProducto = tvNombreProducto.getText().toString();
                        final String precioProducto = tvPrecio.getText().toString();
                        final String numMesa = etNumMesa.getText().toString();

                        // Obtener el usuario actualmente autenticado
                        final FirebaseUser currentUser = mAuth.getCurrentUser();

                        // Verificar si hay un usuario autenticado
                        if (currentUser != null) {
                            Log.d("ProductoActivity", "Usuario actualmente autenticado: " + currentUser.getEmail());
                            // Obtener el nombre de usuario asociado al correo electrónico
                            String email = currentUser.getEmail();
                            if (email != null && !email.isEmpty()) {
                                // Buscar el nombre de usuario en Firestore
                                db.collection("users")
                                        .whereEqualTo("email", email)
                                        .get()
                                        .addOnSuccessListener(queryDocumentSnapshots -> {
                                            if (!queryDocumentSnapshots.isEmpty()) {
                                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                                    String username = document.getString("username");
                                                    if (username != null && !username.isEmpty()) {
                                                        // Utilizar el nombre de usuario obtenido de Firestore
                                                        sendOrder(nombreProducto, precioProducto, numMesa, username);
                                                        return; // Salir del bucle después de encontrar el nombre de usuario
                                                    }
                                                }
                                            } else {
                                                Toast.makeText(ProductoActivity.this, "No se encontró el nombre de usuario asociado al correo electrónico", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(ProductoActivity.this, "Error al obtener el nombre de usuario", Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        });
                            }
                        } else {
                            // Si no hay usuario autenticado, mostrar un mensaje de error o redirigir al usuario a la pantalla de inicio de sesión
                            Log.d("ProductoActivity", "Usuario no autenticado");
                            Toast.makeText(ProductoActivity.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
                            // Puedes agregar aquí la lógica para redirigir al usuario según tu aplicación
                        }
                    }
                });

                // Obtener el usuario actualmente autenticado
                final FirebaseUser currentUser = mAuth.getCurrentUser();

                // Verificar si hay un usuario autenticado
                if (currentUser != null) {
                    Log.d("ProductoActivity", "Usuario actualmente autenticado: " + currentUser.getEmail());
                    // Obtener el nombre de usuario asociado al correo electrónico
                    String email = currentUser.getEmail();
                    if (email != null && !email.isEmpty()) {
                        // Buscar el nombre de usuario en Firestore
                        db.collection("users")
                                .whereEqualTo("email", email)
                                .get()
                                .addOnSuccessListener(queryDocumentSnapshots -> {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                            String username = document.getString("username");
                                            if (username != null && !username.isEmpty()) {
                                                // Utilizar el nombre de usuario obtenido de Firestore
                                                sendOrder(nombreProducto, precioProducto, numMesa, username);
                                                return; // Salir del bucle después de encontrar el nombre de usuario
                                            }
                                        }
                                    } else {
                                        Toast.makeText(ProductoActivity.this, "No se encontró el nombre de usuario asociado al correo electrónico", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(ProductoActivity.this, "Error al obtener el nombre de usuario", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                });
                    }
                } else {
                    // Si no hay usuario autenticado, mostrar un mensaje de error o redirigir al usuario a la pantalla de inicio de sesión
                    Log.d("ProductoActivity", "Usuario no autenticado");
                    Toast.makeText(ProductoActivity.this, "Usuario no autenticado", Toast.LENGTH_SHORT).show();
                    // Puedes agregar aquí la lógica para redirigir al usuario según tu aplicación
                }
            }
        });

        // Configuración de visibilidad de las estrellas según la puntuación del producto
        if (stars.equals("1")){
            ivStar1Fill.setVisibility(View.VISIBLE);
        } else if (stars.equals("2")) {
            ivStar1Fill.setVisibility(View.VISIBLE);
            ivStar2Fill.setVisibility(View.VISIBLE);
        } else if (stars.equals("3")) {
            ivStar1Fill.setVisibility(View.VISIBLE);
            ivStar2Fill.setVisibility(View.VISIBLE);
            ivStar3Fill.setVisibility(View.VISIBLE);
        } else if (stars.equals("4")) {
            ivStar1Fill.setVisibility(View.VISIBLE);
            ivStar2Fill.setVisibility(View.VISIBLE);
            ivStar3Fill.setVisibility(View.VISIBLE);
            ivStar4Fill.setVisibility(View.VISIBLE);
        } else if (stars.equals("5")){
            ivStar1Fill.setVisibility(View.VISIBLE);
            ivStar2Fill.setVisibility(View.VISIBLE);
            ivStar3Fill.setVisibility(View.VISIBLE);
            ivStar4Fill.setVisibility(View.VISIBLE);
            ivStar5Fill.setVisibility(View.VISIBLE);
        }

        // Cargar la información en los TextViews
        tvNombreProducto.setText(nombre);
        tvDescripcion.setText(descripcion);
        tvPrecio.setText(precio);

        // Cargar la imagen utilizando Picasso
        Picasso.get().load(url)
                .into(ivImagenProducto, new Callback() {
                    @Override
                    public void onSuccess() {
                        // La imagen se cargó correctamente
                    }
                    @Override
                    public void onError(Exception e) {
                        // Manejar errores de carga de imagen
                        Toast.makeText(ProductoActivity.this, "La carga de la imagen ha fallado", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });

        // Configuración de OnClickListener para el botón de retroceso
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductoActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // Configuración de OnClickListener para el botón de inicio
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductoActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Método para enviar un pedido al servidor Firestore.
     * @param nombre El nombre del producto.
     * @param precio El precio del producto.
     * @param numMesa El número de mesa.
     * @param usuario El nombre de usuario.
     */
    private void sendOrder(String nombre, String precio, String numMesa, String usuario) {
        Log.d("ProductoActivity", "sendOrder: Enviando pedido...");

        // Generar un UUID único para la orden
        String orderId = UUID.randomUUID().toString();

        // Crear un mapa con los datos del pedido
        Map<String, Object> order = new HashMap<>();
        order.put("idpedido", orderId); // Agregar el ID único al mapa
        order.put("nombre", nombre);
        order.put("precio", precio);
        order.put("numMesa", numMesa);
        order.put("usuario", usuario);

        Log.d("ProductoActivity", "sendOrder: Datos del pedido: " + order.toString());

        // Añadir los datos a la colección "orders" en Firestore
        db.collection("orders").add(order)
                .addOnSuccessListener(documentReference -> {
                    Log.d("ProductoActivity", "sendOrder: Pedido enviado correctamente");
                    Toast.makeText(ProductoActivity.this, "Pedido enviado correctamente", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("ProductoActivity", "sendOrder: Error al enviar pedido", e);
                    Toast.makeText(ProductoActivity.this, "Error al enviar pedido", Toast.LENGTH_SHORT).show();
                });
    }
}