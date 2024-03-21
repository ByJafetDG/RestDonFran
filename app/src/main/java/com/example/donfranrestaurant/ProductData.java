package com.example.donfranrestaurant;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProductData {

    public static void setOnClickListenerForProduct(final Context context, ImageView imageView, final String productName, final String collectionName) {
        imageView.setOnClickListener(v -> {
            FirebaseFirestore.getInstance().collection(collectionName)
                    .whereEqualTo("nombre", productName)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                            Intent intent = new Intent(context, ProductoActivity.class);
                            intent.putExtra("nombre", documentSnapshot.getString("nombre"));
                            intent.putExtra("descripcion", documentSnapshot.getString("descripcion"));
                            intent.putExtra("precio", documentSnapshot.getString("precio"));
                            intent.putExtra("stars", documentSnapshot.getString("stars"));
                            intent.putExtra("url", documentSnapshot.getString("url"));
                            context.startActivity(intent);
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Manejar errores al obtener información del producto
                    });
        });
    }

}
