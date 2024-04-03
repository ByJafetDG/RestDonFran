package com.example.donfranrestaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.donfranrestaurant.socialmedia.SocialMedia;

/**
 * MainActivity es la actividad principal de la aplicación DonFranRestaurant.
 * Esta actividad muestra la pantalla de inicio con opciones para iniciar sesión,
 * registrarse y acceder a las redes sociales del restaurante.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Método que se llama al crear la actividad. Configura la interfaz de usuario
     * y define los listeners de eventos para los botones e imágenes.
     * @param savedInstanceState El estado anterior de la actividad, si lo hay.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(700); // Hace que el hilo principal espere 700 milisegundos antes de continuar.
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        setTheme(R.style.Theme_DonFranRestaurant); // Establece el tema de la actividad.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtiene referencias a los botones e imágenes en la interfaz de usuario.
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);
        ImageView ivFacebook = findViewById(R.id.ivFacebook);
        ImageView ivInstagram = findViewById(R.id.ivInstagram);
        ImageView ivWhatsapp = findViewById(R.id.ivWhatsapp);

        // Define los listeners de eventos para las imágenes de las redes sociales.
        ivFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialMedia.openSocialMedia(MainActivity.this, "com.facebook.katana", "fb://page/asadosdonfran", "https://www.facebook.com/asadosdonfran");
            }
        });
        ivInstagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialMedia.openSocialMedia(MainActivity.this, "com.instagram.android", "http://instagram.com/_u/donfranrestaurante", "https://www.instagram.com/donfranrestaurante");
            }
        });
        ivWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SocialMedia.openWhatsAppChat(MainActivity.this, "+50671082151");
            }
        });

        // Define los listeners de eventos para los botones de registro e inicio de sesión.
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(register);
                finish();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(register);
                finish();
            }
        });

    }
}
