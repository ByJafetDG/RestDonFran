package com.example.donfranrestaurant.socialmedia;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class SocialMedia {
    public static void openSocialMedia(Context context, String packageName, String appLink, String webLink) {
        // Verificar si la aplicación está instalada
        if (isAppInstalled(context, packageName)) {
            // Si está instalada, abrir la aplicación
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(appLink));
            context.startActivity(appIntent);
        } else {
            // Si no está instalada, abrir el enlace en el navegador web
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webLink));
            context.startActivity(webIntent);
        }
    }

    // Método para abrir WhatsApp y comenzar un chat con un número específico
    public static void openWhatsAppChat(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String url = "https://api.whatsapp.com/send?phone=" + phoneNumber;
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    // Método para verificar si una aplicación está instalada en el dispositivo
    private static boolean isAppInstalled(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        return intent != null;
    }
}
