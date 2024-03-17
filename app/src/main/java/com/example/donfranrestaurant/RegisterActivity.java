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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegisterR;
    private Button btnVolver;
    private EditText etEmail;
    private EditText etPass;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegisterR = findViewById(R.id.btnRegisterR);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);

        btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(register);
                finish();
            }
        });

        /*-------------------------FIREBASE FIRESTONE-------------------------*/
        // Dentro del onClickListener para el botón de registro
        btnRegisterR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén el correo electrónico y la contraseña ingresados por el usuario
                String email = etEmail.getText().toString();
                String password = etPass.getText().toString();

                // Verifica si el correo electrónico o la contraseña están vacíos
                if (email.isEmpty() || password.isEmpty()) {
                    // Si están vacíos, muestra un Toast indicando que se deben completar ambos campos
                    Toast.makeText(RegisterActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create a new user
                Map<String, Object> user = new HashMap<>();
                user.put("email", email);
                user.put("pass", password);

                // Add a new document with a generated ID
                db.collection("users")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                // Si se añade correctamente, muestra un Toast indicando el éxito
                                Toast.makeText(RegisterActivity.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Si falla, muestra un Toast indicando el error
                                Toast.makeText(RegisterActivity.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                                Log.w(TAG, "Error adding document", e);
                            }
                        });

                // Limpiar campos después del registro
                etEmail.setText("");
                etPass.setText("");
            }
        });

        /*-------------------------/FIREBASE-FIRESTONE/-------------------------*/
    }
}
