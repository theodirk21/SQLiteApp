package com.uva.dev.mobile.sqliteapp;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.uva.dev.mobile.sqliteapp.model.Store;

public class CreateService extends AppCompatActivity {

    EditText editTextName;
    EditText editTextAutor;
    EditText editTextAnoPublicacao;
    EditText editTextPreco;
    CheckBox editTextDisponivel;
    Button button;
    SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextAutor = (EditText) findViewById(R.id.editTextAutor);
        editTextAnoPublicacao = (EditText) findViewById(R.id.editTextAnoPublicacao);
        editTextPreco = (EditText) findViewById(R.id.editTextPreco);
        editTextDisponivel = (CheckBox) findViewById(R.id.editTextDisponivel);
        button = (Button) findViewById(R.id.buttonCreateElement);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                create();
            }
        });
    }

    public void create(){
        if(!TextUtils.isEmpty(editTextName.getText().toString()) && !TextUtils.isEmpty(editTextAutor.getText().toString())
       ){
            try{

                String nomeLivro = editTextName.getText().toString();
                String autor = editTextAutor.getText().toString();
                Integer anoPublicacao = Integer.parseInt(editTextAnoPublicacao.getText().toString());
                Double preco = Double.parseDouble(editTextPreco.getText().toString());
                Boolean disponivel = editTextDisponivel.isChecked();

                Store store = new Store( nomeLivro, autor, anoPublicacao, preco, disponivel);

                database = openOrCreateDatabase("sqlapp", MODE_PRIVATE, null);
                System.out.println(database);
                String sql = "INSERT INTO store(nomeLivro, autor, anoPublicacao, preco, disponivel) VALUES (?, ?, ?, ?, ?)";
                SQLiteStatement stmt = database.compileStatement(sql);
                stmt.bindString(1, store.getNomeLivro());
                stmt.bindString(2, store.getAutor());
                stmt.bindLong(3, store.getAnoPublicacao());
                stmt.bindDouble(4, store.getPreco());
                stmt.bindLong(5, store.isDisponivel() ? 1 : 0);;

                stmt.executeInsert();
                database.close();
                finish();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
