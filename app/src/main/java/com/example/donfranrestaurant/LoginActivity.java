package com.example.donfranrestaurant;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
 * La actividad LoginActivity permite a los usuarios iniciar sesión en la aplicación DonFranRestaurant
 * utilizando su correo electrónico y contraseña, o iniciar sesión con sus cuentas de Google.
 */
public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 9001;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private SharedPreferences sharedPreferences;

    /**
     * Método llamado al crear la actividad. Configura la interfaz de usuario y define los listeners de eventos.
     * @param savedInstanceState El estado anterior de la actividad, si lo hay.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicialización de FirebaseAuth y FirebaseFirestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Obtención de referencias a elementos de la interfaz de usuario
        Button btnLoginL = findViewById(R.id.btnLoginL);
        Button btnVolver = findViewById(R.id.btnVolver);
        EditText etUsername = findViewById(R.id.etUsername);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPass = findViewById(R.id.etPass);
        ImageView ivGoogle = findViewById(R.id.ivGoogle);
        ImageView ivFacebook = findViewById(R.id.ivFacebook);
        ImageView ivInstagram = findViewById(R.id.ivInstagram);
        ImageView ivWhatsapp = findViewById(R.id.ivWhatsapp);

        // Inicialización de SharedPreferences para almacenar el estado de inicio de sesión
        sharedPreferences = getSharedPreferences("loginPrefs", Context.MODE_PRIVATE);

        // Comprobar si el usuario ya ha iniciado sesión
        if (sharedPreferences.getBoolean("loggedIn", false)) {
            navigateToMenuActivity();
        }

        // Configuración de OnClickListener para el icono de Google
        ivGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGoogleSignIn();
            }
        });

        // Configuración de OnClickListener para otros iconos de redes sociales
        ivFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialMedia.openSocialMedia(LoginActivity.this, "com.facebook.katana", "fb://page/asadosdonfran", "https://www.facebook.com/asadosdonfran");
            }
        });
        ivInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialMedia.openSocialMedia(LoginActivity.this, "com.instagram.android", "http://instagram.com/_u/donfranrestaurante", "https://www.instagram.com/donfranrestaurante");
            }
        });
        ivWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialMedia.openWhatsAppChat(LoginActivity.this, "+50671082151");
            }
        });

        // Configuración de OnClickListener para el botón de volver
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToMainActivity();
            }
        });

        // Configuración de OnClickListener para el botón de inicio de sesión
        btnLoginL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtención de los datos ingresados por el usuario
                String email = etEmail.getText().toString().trim();
                String password = etPass.getText().toString().trim();
                String username = etUsername.getText().toString().trim();

                // Validación de los campos ingresados
                if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                    showToast("Por favor, completa todos los campos");
                    return;
                }

                if (!isValidEmail(email)) {
                    showToast("Correo electrónico no válido");
                    return;
                }

                // Inicio de sesión con correo electrónico y contraseña
                loginWithEmailPassword(email, password, username);
            }
        });
    }

    /**
     * Método para iniciar el flujo de inicio de sesión con Google.
     */
    private void startGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Método para iniciar sesión con correo electrónico y contraseña en Firebase Authentication.
     * @param email Correo electrónico del usuario.
     * @param password Contraseña del usuario.
     * @param username Nombre de usuario del usuario.
     */
    private void loginWithEmailPassword(String email, String password, String username) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveLoggedInState();
                            navigateToMenuActivity();
                        } else {
                            showToast("Credenciales incorrectas o cuenta no registrada");
                        }
                    }
                });
    }

    /**
     * Método para navegar a la actividad MainActivity.
     */
    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Método para navegar a la actividad MenuActivity.
     */
    private void navigateToMenuActivity() {
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Método para guardar el estado de inicio de sesión en SharedPreferences.
     */
    private void saveLoggedInState() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("loggedIn", true);
        editor.apply();
    }

    /**
     * Método para validar un correo electrónico utilizando una expresión regular.
     * @param email Correo electrónico a validar.
     * @return true si el correo electrónico es válido, false de lo contrario.
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Método para mostrar un mensaje Toast.
     * @param message El mensaje a mostrar.
     */
    private void showToast(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
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
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
                showToast("Google sign in failed");
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
                            saveLoggedInState();
                            FirebaseUser user = mAuth.getCurrentUser();

                            // Obtener la URL de la imagen por defecto de Firestore
                            db.collection("imgprofile")
                                    .whereEqualTo("nombre", "Vaca")
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        if (!queryDocumentSnapshots.isEmpty()) {
                                            // Obtener la URL del primer documento (suponiendo que solo hay uno con el mismo nombre)
                                            String defaultImageUrl = queryDocumentSnapshots.getDocuments().get(0).getString("url");

                                            // Guardar el nombre de usuario y la imagen por defecto en Firestore
                                            Map<String, Object> userData = new HashMap<>();
                                            userData.put("username", generateUniqueUsernameFromEmail(user.getEmail()));
                                            userData.put("email", user.getEmail());
                                            userData.put("imagen", defaultImageUrl); // Establecer la imagen por defecto
                                            db.collection("users").document(user.getUid()).set(userData)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            // El usuario se registró correctamente
                                                            Toast.makeText(LoginActivity.this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
                                                            navigateToMenuActivity();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            // Error al guardar los datos del usuario en Firestore
                                                            Toast.makeText(LoginActivity.this, "Error al registrar usuario: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        } else {
                                            // No se encontró ningún documento con el nombre seleccionado
                                            Toast.makeText(LoginActivity.this, "No se encontró la imagen por defecto en la base de datos", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(e -> {
                                        // Manejar errores de lectura de Firestore
                                        Toast.makeText(LoginActivity.this, "Error al obtener datos de la base de datos", Toast.LENGTH_SHORT).show();
                                        e.printStackTrace();
                                    });
                        } else {
                            showToast("Error al iniciar sesión con Google");
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