package com.laila.unifacs.atividadepraticaunidade_1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.laila.unifacs.atividadepraticaunidade_1.R;
import com.laila.unifacs.atividadepraticaunidade_1.model.Usuario;

public class MainActivity extends AppCompatActivity {

    private int counter = 0;
    private TextView usuarioTextView, senhaTextView;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set default user and password
        usuario = new Usuario("admin", "admin");
        this.usuarioTextView = findViewById(R.id.usuario_login_EditText);
        this.senhaTextView = findViewById(R.id.senha_login_EditText);

    }

    public void login(View view) {

        String user = usuarioTextView.getText().toString();
        String password = senhaTextView.getText().toString();

        // Get Usuario obj from ConfigScreenActivity
        Intent getConfigScreenDataIntent = getIntent();
        Bundle bundle = getConfigScreenDataIntent.getExtras();

        // If Usuario obj exists, set this Usuario object to new Usuario object
        if (bundle != null) {

            this.usuario = (Usuario) bundle.getSerializable("newUser");

        }

        // Validates if user and password are correct
        if (user.equals(usuario.getUsuario()) && password.equals(usuario.getSenha())) {

            counter = 0;
            // Changes to main screen
            Intent intentConfigScreen = new Intent(getApplicationContext(), ConfigScreenActivity.class);
            intentConfigScreen.putExtra("userObj", this.usuario);
            
            Intent intentMainScreen = new Intent(getApplicationContext(), MainScreenActivity.class);
            startActivity(intentMainScreen);

            // If counter = 2 (3 tries), close app
        } else if (counter == 2) {

            Toast.makeText(getApplicationContext(), "Muitas tentativas incorretas", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, LockScreenActivity.class);
            startActivity(intent);
            finish();

            // If user and/or password incorrect, increase count
        } else {
            counter += 1;
            Toast.makeText(getApplicationContext(), "Usu??rio/Senha Incorreto\nVoc?? tem " + (3 - counter) + " tentativas", Toast.LENGTH_SHORT).show();
        }

    }

}                                                                                                                                            