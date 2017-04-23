package com.example.eider.saripaar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyMICCallback;
import com.kinvey.android.callback.KinveyPingCallback;
import com.kinvey.android.callback.KinveyUserCallback;
import com.kinvey.java.User;
import com.mobsandgeeks.saripaar.ValidationError;
import  com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

public class MainActivity extends AppCompatActivity  implements Validator.ValidationListener{
Validator validador;
    @Email (message = "Debe introducir un correo valido, Ejemplo:fulanito@coolmail.com")
    private EditText etCorreo;

    @Password(message = "la contraseña debe de ser minimo de 6 caracteres",min = 6, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS)
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        validador = new Validator(this);
        validador.setValidationListener(this);


        etCorreo = (EditText) findViewById(R.id.etCorreo);
        passwordEditText = (EditText) findViewById(R.id.etContraseña);


        final Client mKinveyClient = new Client.Builder("kid_rk-N3muug", "730ae7f10b514c90bb0e85a841e5472d"
                , this.getApplicationContext()).build();

            mKinveyClient.ping(new KinveyPingCallback() {
         @Override
         public void onSuccess(Boolean aBoolean) {
             Toast.makeText(MainActivity.this, "Conectado al server", Toast.LENGTH_SHORT).show();
         }

         @Override
         public void onFailure(Throwable throwable) {
             Toast.makeText(MainActivity.this, "Error al conectarse al Server", Toast.LENGTH_SHORT).show();
         }
     });
    }
public void Loggin(View view){
    validador.validate();
}

    public void  RegistrarUsuario(){
        Client client = new Client.Builder("kid_rk-N3muug"
                ,"730ae7f10b514c90bb0e85a841e5472d"
                ,getApplicationContext()).build();

        Toast.makeText(this, "creando usuario...", Toast.LENGTH_SHORT).show();

                client.user().create(etCorreo.getText().toString().trim(),
                        passwordEditText.getText().toString().trim(), new KinveyUserCallback() {
                            @Override
                            public void onSuccess(User user) {
                                CharSequence text = "Bienvenido, "+ user.get("username");
                                user.put("email", user.get("username"));
                                Toast.makeText(MainActivity.this,text, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                CharSequence text = "no se pudo crear tu cuneta "+ throwable.getMessage();
                                Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();

                            }

    });
    }
    @Override
    public void onValidationSucceeded() {
        Toast.makeText(this, "todo bien", Toast.LENGTH_SHORT).show();
        RegistrarUsuario();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // Display error messages ;)
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }
}
