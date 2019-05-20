package br.edu.faculdadedelta.produto.dao;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import br.edu.faculdadedelta.produto.modelo.Produto;

public class ProdutoDao {
    private static List<Produto> listaProduto = new ArrayList<>();
    private static Long idGerador = 1L;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;





    public void incluir(Produto produto){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        produto.setId(UUID.randomUUID().toString());
        listaProduto.add(produto);
        databaseReference.child("Produto").child(produto.getId()).setValue(listaProduto);
    }

    public void excluir(Produto seriado){
        listaProduto.remove(seriado);
    }
    public List<Produto> listar(){
        return listaProduto;
    }

    }

