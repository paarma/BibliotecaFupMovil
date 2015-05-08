package com.webileapps.navdrawer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by pablo on 29/04/15.
 */
public class FmLogin extends Activity {

    private EditText tbxUsuario, tbxClave;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fm_login);

        inicializarComponentes();
    }

    private void inicializarComponentes() {
        tbxUsuario = (EditText) findViewById(R.id.tbxUsuario);
        tbxClave = (EditText) findViewById(R.id.tbxClave);
    }

    public void login(View view){
        Intent goInicial = null;
        goInicial = new Intent(FmLogin.this, MainActivity.class);
        startActivity(goInicial);

    }
}
