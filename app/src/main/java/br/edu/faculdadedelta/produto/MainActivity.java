package br.edu.faculdadedelta.produto;

import android.content.Intent;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.edu.faculdadedelta.produto.dao.ProdutoDao;
import br.edu.faculdadedelta.produto.modelo.Produto;

public class MainActivity extends AppCompatActivity {
    private EditText etnome, etvalor;
    private ListView lvListar;
    private Button btnSalvar;
    private Produto produto = new Produto();
    private ProdutoDao dao = new ProdutoDao();
    private Produto produtoSelecionada;

    private String nome;
    private String valor;


    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private List<Produto> listaProduto = new ArrayList<Produto>();
    private ArrayAdapter<Produto> arrayAdapterProduto;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etnome = (EditText) findViewById(R.id.etNome);
        etvalor = (EditText) findViewById(R.id.etValor);
        lvListar = (ListView) findViewById(R.id.lvLista);

        lvListar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                produtoSelecionada = (Produto) parent.getItemAtPosition(position);
                etnome.setText(produtoSelecionada.getNome());
                etvalor.setText(produtoSelecionada.getValor());
            }
        });

    }
    private void inicializarFireBase() {
        FirebaseApp.initializeApp(MainActivity.this);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        databaseReference = firebaseDatabase.getReference();
    }


    public void listar(View view) {
        startActivity(new Intent(getBaseContext(),ListaActivity.class));
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

    @Override
    protected void onStart() {
        super.onStart();
        inicializarFireBase();
        EventoDatabase();
    }

    private void EventoDatabase() {
        databaseReference.child("Estoque").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaProduto.clear();
                for(DataSnapshot objSnapshot:dataSnapshot.getChildren()){
                    Produto p = objSnapshot.getValue(Produto.class);
                    listaProduto.add(p);
                }
                arrayAdapterProduto = new ArrayAdapter<Produto>(MainActivity.this,
                        android.R.layout.simple_list_item_1,listaProduto);
                lvListar.setAdapter(arrayAdapterProduto);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.menu_novo){
            Produto p = new Produto();
            p.setId(UUID.randomUUID().toString());
            p.setNome(etnome.getText().toString());
            p.setValor(etvalor.getText().toString());
            databaseReference.child("Estoque").child(p.getId()).setValue(p);
            limparCampos();
        }else if(id==R.id.menu_atualizar){
           Produto p = new Produto();
           p.setId(produtoSelecionada.getId());
           p.setNome(etnome.getText().toString().trim());
           p.setValor(etvalor.getText().toString().trim());
           databaseReference.child("Estoque").child(p.getId()).setValue(p);
            limparCampos();
        }else if(id == R.id.menu_deletar){
            Produto p = new Produto();
            p.setId(produtoSelecionada.getId());
            databaseReference.child("Estoque").child(p.getId()).removeValue();
            limparCampos();
        }
        return super.onOptionsItemSelected(item);
    }

    private void limparCampos() {
        etnome.setText("");
        etvalor.setText("");
    }
}
