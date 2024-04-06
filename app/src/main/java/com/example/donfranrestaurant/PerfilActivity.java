package com.example.donfranrestaurant;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.donfranrestaurant.socialmedia.SocialMedia;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import io.reactivex.rxjava3.annotations.NonNull;

public class PerfilActivity extends AppCompatActivity {

    /**
     * Método llamado al crear la actividad. Configura la interfaz de usuario y define los listeners de eventos.
     * @param savedInstanceState El estado anterior de la actividad, si lo hay.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        ImageView ivPicture = findViewById(R.id.ivPicture);
        TextView tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        TextView tvEditPerfil = findViewById(R.id.tvEditPerfil);
        TextView tvSoporte = findViewById(R.id.tvSoporte);
        TextView tvEditUsername = findViewById(R.id.tvEdittUserName);
        EditText etUsername = findViewById(R.id.etUserName);
        TextView tvEditPicture = findViewById(R.id.tvEditPicture);
        RadioGroup rgGroup = findViewById(R.id.rgGroup);
        RadioButton rdImagen1 = findViewById(R.id.rdImage1);
        RadioButton rdImagen2 = findViewById(R.id.rdImage2);
        RadioButton rdImagen3 = findViewById(R.id.rdImage3);
        Button btnCambiar = findViewById(R.id.btnCambiar);
        ImageView ivBack = findViewById(R.id.ivBack);
        ImageView ivHome = findViewById(R.id.ivHome);
        ImageView ivFlecha = findViewById(R.id.ivFlecha);
        ImageView ivFlecha2 = findViewById(R.id.ivFlecha2);

        // Dentro del método onCreate de PerfilActivity
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

// Obtener el usuario actualmente autenticado
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Obtener el UID del usuario
            String uid = currentUser.getUid();
            // Buscar los datos del usuario en Firestore usando el UID
            db.collection("users")
                    .document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            // Obtener el nombre de usuario y la URL de la imagen del documento del usuario
                            String username = documentSnapshot.getString("username");
                            String imageUrl = documentSnapshot.getString("imagen");

                            // Mostrar el nombre de usuario en tvNombreUsuario
                            tvNombreUsuario.setText(username);
                            etUsername.setText((username));

                            // Cargar la imagen en ivPicture utilizando Picasso
                            Picasso.get().load(imageUrl)
                                    .into(ivPicture, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            // La imagen se cargó correctamente
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            // Manejar errores de carga de imagen
                                            Toast.makeText(PerfilActivity.this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                                            e.printStackTrace();
                                        }
                                    });
                        } else {
                            // El documento del usuario no existe en Firestore
                            // Manejar este caso según sea necesario
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Manejar errores de lectura de Firestore
                        Toast.makeText(PerfilActivity.this, "Error al obtener datos del usuario", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    });
        }

        // Agregar OnClickListener al ImageView de regreso y cierra sesión
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        // Agregar OnClickListener al ImageView de inicio para volver al menú principal
        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PerfilActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });

        tvSoporte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialMedia.openWhatsAppChat(PerfilActivity.this, "+50671082151");
            }
        });

        // Agregar OnClickListener al TextView tvEditPerfil
        tvEditPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar si los elementos de edición de perfil están visibles
                if (tvEditUsername.getVisibility() == View.VISIBLE) {
                    // Si los elementos de edición están visibles, hacerlos invisibles
                    tvEditUsername.setVisibility(View.INVISIBLE);
                    tvEditPicture.setVisibility(View.INVISIBLE);
                    etUsername.setVisibility(View.INVISIBLE);
                    rgGroup.setVisibility(View.INVISIBLE);
                    btnCambiar.setVisibility(View.INVISIBLE);
                    // Hacer visible el TextView tvSoporte
                    tvSoporte.setVisibility(View.VISIBLE);
                    ivFlecha2.setVisibility(View.VISIBLE);
                } else {
                    // Si los elementos de edición no están visibles, hacerlos visibles
                    tvEditUsername.setVisibility(View.VISIBLE);
                    tvEditPicture.setVisibility(View.VISIBLE);
                    etUsername.setVisibility(View.VISIBLE);
                    rgGroup.setVisibility(View.VISIBLE);
                    btnCambiar.setVisibility(View.VISIBLE);
                    // Hacer invisible el TextView tvSoporte
                    tvSoporte.setVisibility(View.INVISIBLE);
                    ivFlecha2.setVisibility(View.INVISIBLE);
                }
            }
        });

        // Agregar un OnCheckedChangeListener al RadioGroup
        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Verificar qué RadioButton se seleccionó
                String selectedImageName = "";
                if (rdImagen1.isChecked()) {
                    selectedImageName = "Gallina";
                } else if (rdImagen2.isChecked()) {
                    selectedImageName = "Vaca"; // Cambia este nombre por el adecuado
                } else if (rdImagen3.isChecked()) {
                    selectedImageName = "Cerdo"; // Cambia este nombre por el adecuado
                }

                // Obtener la URL de la imagen del documento en la colección "imgperfil" según el nombre seleccionado
                db.collection("imgprofile")
                        .whereEqualTo("nombre", selectedImageName)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                // Obtener la URL del primer documento (suponiendo que solo hay uno con el mismo nombre)
                                String imageUrl = queryDocumentSnapshots.getDocuments().get(0).getString("url");

                                // Cargar la imagen en el ImageView utilizando Picasso
                                Picasso.get().load(imageUrl)
                                        .into(ivPicture, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                // La imagen se cargó correctamente
                                            }

                                            @Override
                                            public void onError(Exception e) {
                                                // Manejar errores de carga de imagen
                                                Toast.makeText(PerfilActivity.this, "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
                                                e.printStackTrace();
                                            }
                                        });
                            } else {
                                // No se encontró ningún documento con el nombre seleccionado
                                Toast.makeText(PerfilActivity.this, "No se encontró la imagen seleccionada en la base de datos", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(e -> {
                            // Manejar errores de lectura de Firestore
                            Toast.makeText(PerfilActivity.this, "Error al obtener datos de la base de datos", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        });
            }
        });

        // Agregar OnClickListener al botón "Cambiar"
        btnCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el usuario actualmente autenticado
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser != null) {
                    // Obtener el UID del usuario
                    String uid = currentUser.getUid();

                    // Verificar qué RadioButton está seleccionado
                    String selectedImageName = "";
                    if (rdImagen1.isChecked()) {
                        selectedImageName = "Gallina";
                    } else if (rdImagen2.isChecked()) {
                        selectedImageName = "Vaca"; // Cambia este nombre por el adecuado
                    } else if (rdImagen3.isChecked()) {
                        selectedImageName = "Cerdo"; // Cambia este nombre por el adecuado
                    }

                    // Obtener la URL de la imagen del documento en la colección "imgperfil" según el nombre seleccionado
                    db.collection("imgprofile")
                            .whereEqualTo("nombre", selectedImageName)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots -> {
                                if (!queryDocumentSnapshots.isEmpty()) {
                                    // Obtener la URL del primer documento (suponiendo que solo hay uno con el mismo nombre)
                                    String imageUrl = queryDocumentSnapshots.getDocuments().get(0).getString("url");

                                    // Obtener el nuevo valor del nombre de usuario desde el EditText
                                    String newUsername = etUsername.getText().toString();

                                    // Actualizar el valor del campo "imagen" y "username" en Firestore
                                    db.collection("users")
                                            .document(uid)
                                            .update("imagen", imageUrl, "username", newUsername)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // Actualizar el TextView con el nuevo nombre de usuario
                                                    tvNombreUsuario.setText(newUsername);
                                                    Toast.makeText(PerfilActivity.this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(PerfilActivity.this, "Error al actualizar el perfil", Toast.LENGTH_SHORT).show();
                                                    e.printStackTrace();
                                                }
                                            });
                                } else {
                                    // No se encontró ningún documento con el nombre seleccionado
                                    Toast.makeText(PerfilActivity.this, "No se encontró la imagen seleccionada en la base de datos", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(e -> {
                                // Manejar errores de lectura de Firestore
                                Toast.makeText(PerfilActivity.this, "Error al obtener datos de la base de datos", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            });
                }
            }
        });
    }
}
