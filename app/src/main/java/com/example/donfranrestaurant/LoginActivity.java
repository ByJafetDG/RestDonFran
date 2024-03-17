package com.example.donfranrestaurant;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    private Button btnVolver;
    private Button btnLoginL;
    private EditText etEmail;
    private EditText etPass;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnVolver = findViewById(R.id.btnVolver);

        etEmail = findViewById(R.id.etEmail);

        etPass = findViewById(R.id.etPass);

        btnLoginL = findViewById(R.id.btnLoginL);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(register);
                finish();
            }
        });

        // Dentro del onClickListener para el botón de inicio de sesión
        btnLoginL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén el correo electrónico y la contraseña ingresados por el usuario
                String email = etEmail.getText().toString();
                String password = etPass.getText().toString();

                // Verifica si el correo electrónico o la contraseña están vacíos
                if (email.isEmpty() || password.isEmpty()) {
                    // Si están vacíos, muestra un Toast indicando que se deben completar ambos campos
                    Toast.makeText(LoginActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Consulta la base de datos Firestore para encontrar un usuario con el correo electrónico proporcionado
                db.collection("users")
                        .whereEqualTo("email", email)
                        .whereEqualTo("pass", password)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    // Si se encuentra un usuario con el correo electrónico y contraseña proporcionados
                                    if (!task.getResult().isEmpty()) {
                                        // Iniciar sesión exitosamente
                                        Log.d(TAG, "Inicio de sesión exitoso");

                                        // Muestra un Toast indicando que las credenciales son correctas
                                        Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                                        // Abre la actividad MenuActivity
                                        Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                                        startActivity(intent);

                                        // Finaliza la actividad actual para evitar volver atrás con el botón de retroceso
                                        finish();
                                    } else {
                                        // No se encontró ningún usuario con el correo electrónico y contraseña proporcionados
                                        Log.w(TAG, "Credenciales inválidas");

                                        // Muestra un Toast indicando que las credenciales son incorrectas
                                        Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Error al buscar en la base de datos Firestore
                                    Log.w(TAG, "Error al buscar usuario", task.getException());
                                    // Maneja el error según sea necesario
                                }
                            }
                        });
            }
        });


    }

}
