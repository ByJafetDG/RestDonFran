package com.example.donfranrestaurant;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ProductoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto);

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

                    }

                    @Override
                    public void onError(Exception e) {
                        // Manejar errores de carga de imagen
                        Toast.makeText(ProductoActivity.this, "La carga de la imagen ha fallado", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductoActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ivHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductoActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

}
