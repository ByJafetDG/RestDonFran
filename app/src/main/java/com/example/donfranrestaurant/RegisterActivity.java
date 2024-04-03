package com.example.donfranrestaurant;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.donfranrestaurant.socialmedia.SocialMedia;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * La actividad RegisterActivity permite a los usuarios registrarse en la aplicación DonFranRestaurant
 * utilizando su correo electrónico y contraseña, o iniciar sesión con sus cuentas de Google.
 */
public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final int RC_SIGN_IN = 9001;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    /**
     * Método llamado al crear la actividad. Configura la interfaz de usuario y define los listeners de eventos.
     * @param savedInstanceState El estado anterior de la actividad, si lo hay.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Obtención de referencias a elementos de la interfaz de usuario
        Button btnVolver = findViewById(R.id.btnVolver);
        Button btnRegisterR = findViewById(R.id.btnRegisterR);
        EditText etUsername = findViewById(R.id.etUsername);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPass = findViewById(R.id.etPass);
        ImageView ivGoogle = findViewById(R.id.ivGoogle);
        ImageView ivFacebook = findViewById(R.id.ivFacebook);
        ImageView ivInstagram = findViewById(R.id.ivInstagram);
        ImageView ivWhatsapp = findViewById(R.id.ivWhatsapp);

        // Inicialización de FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Configuración de OnClickListener para los iconos de redes sociales
        ivGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Configuración de inicio de sesión con Google
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(RegisterActivity.this, gso);

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
        ivFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialMedia.openSocialMedia(RegisterActivity.this, "com.facebook.katana", "fb://page/asadosdonfran", "https://www.facebook.com/asadosdonfran");
            }
        });
        ivInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialMedia.openSocialMedia(RegisterActivity.this, "com.instagram.android", "http://instagram.com/_u/donfranrestaurante", "https://www.instagram.com/donfranrestaurante");
            }
        });
        ivWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialMedia.openWhatsAppChat(RegisterActivity.this, "+50671082151");
            }
        });

        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(register);
                finish();
            }
        });

        // Configuración de OnClickListener para el botón de registro
        /*-------------------------FIREBASE FIRESTONE-------------------------*/
        btnRegisterR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtén el correo electrónico, contraseña y nombre de usuario ingresados por el usuario
                String email = etEmail.getText().toString().trim();
                String password = etPass.getText().toString().trim();
                String username = etUsername.getText().toString().trim();

                // Verifica si el correo electrónico, contraseña o nombre de usuario están vacíos
                if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                    // Si están vacíos, muestra un Toast indicando que se deben completar todos los campos
                    Toast.makeText(RegisterActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Verificar si el correo electrónico tiene un formato válido
                if (!isValidEmail(email)) {
                    // Si el correo electrónico no es válido, muestra un mensaje de error
                    Toast.makeText(RegisterActivity.this, "Correo electrónico no válido", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Registrar al usuario con correo electrónico y contraseña en Firebase Authentication
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // El registro fue exitoso
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    // Guardar el nombre de usuario en Firestore
                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("username", username);
                                    userData.put("email", email);
                                    db.collection("users").document(user.getUid()).set(userData)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    // El nombre de usuario se guardó correctamente en Firestore
                                                    Toast.makeText(RegisterActivity.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    // Error al guardar el nombre de usuario en Firestore
                                                    Toast.makeText(RegisterActivity.this, "Error al registrar usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    // El registro falló, muestra un mensaje de error
                                    Toast.makeText(RegisterActivity.this, "Error al registrar usuario: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                // Limpiar campos después del registro
                etEmail.setText("");
                etPass.setText("");
                etUsername.setText("");
            }
        });
        /*-------------------------/FIREBASE-FIRESTONE/-------------------------*/
    }

    /**
     * Método para verificar si un correo electrónico tiene un formato válido.
     * @param email El correo electrónico a verificar.
     * @return true si el correo electrónico tiene un formato válido, false de lo contrario.
     */
    private boolean isValidEmail(String email) {
        // Expresión regular para validar un correo electrónico
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Método llamado al recibir un resultado de una actividad iniciada para obtener un resultado.
     * @param requestCode El código de solicitud original enviado a startActivityForResult().
     * @param resultCode El código de resultado devuelto por la actividad.
     * @param data Un Intent que lleva los datos resultantes.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verifica si el resultado corresponde al inicio de sesión con Google
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // El inicio de sesión con Google fue exitoso, autentica con Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // El inicio de sesión con Google falló, muestra un mensaje de error
                Log.w(TAG, "Google sign in failed", e);
                Toast.makeText(RegisterActivity.this, "Google sign in failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Método para autenticar con Firebase utilizando las credenciales de Google.
     * @param acct La cuenta de Google con la que se inició sesión.
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Autenticación con Firebase exitosa, el usuario se registró correctamente
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Generar un nombre de usuario único utilizando el nombre de correo electrónico
                            String username = generateUniqueUsernameFromEmail(user.getEmail());

                            // Guardar el nombre de usuario en Firestore
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("username", username);
                            userData.put("email", user.getEmail());
                            db.collection("users").document(user.getUid()).set(userData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // El nombre de usuario se guardó correctamente en Firestore
                                            Toast.makeText(RegisterActivity.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Error al guardar el nombre de usuario en Firestore
                                            Toast.makeText(RegisterActivity.this, "Error al registrar usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            // La autenticación con Firebase falló
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * Método para generar un nombre de usuario único utilizando el nombre de correo electrónico.
     * @param email El correo electrónico del usuario.
     * @return Un nombre de usuario único generado a partir del correo electrónico.
     */
    private String generateUniqueUsernameFromEmail(String email) {
        // Generar un nombre de usuario único utilizando el nombre de correo electrónico
        String username = email.split("@")[0]; // Obtener la parte del correo electrónico antes del símbolo @
        return username;
    }
}