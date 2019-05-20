package br.edu.faculdadedelta.produto;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

import br.edu.faculdadedelta.produto.dao.ProdutoDao;
import br.edu.faculdadedelta.produto.modelo.Produto;

public class MainActivity extends AppCompatActivity {
    private EditText etnome, etvalor;
    private Button btnSalvar;
    private Produto produto = new Produto();
    private ProdutoDao dao = new ProdutoDao();

    private String nome;
    private String valor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etnome = (EditText) findViewById(R.id.etNome);
        etvalor = (EditText) findViewById(R.id.etValor);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);






        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Botão", "enviado");


                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("Estoque");
                    produto.setId(UUID.randomUUID().toString());
                    produto.setNome(etnome.getText().toString().trim());
                    produto.setValor(etvalor.getText().toString().trim());
                    myRef.child(produto.getId()).setValue(produto);
                    limparCampos(null);

                }
        });

    }


    public void listar(View view) {
        startActivity(new Intent(getBaseContext(),ListaActivity.class));
    }

    public void limparCampos(View view){
        etnome.setText("");
        etvalor.setText("");
        produto = new Produto();

    }
    private String validarFormulario() {
        String msg = "";
        if (etnome.equals("")) {
            msg = "nome esta faltando";
        }
        if (valor.equals("")) {
            msg += "\nvalor esta faltando";
        }
        return msg;
    }
}
