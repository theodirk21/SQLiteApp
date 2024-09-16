package com.uva.dev.mobile.sqliteapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.uva.dev.mobile.sqliteapp.model.BookList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase database;
    private ListView listViewBooks;
    public Button button;
    public Integer idSelected;
    public ArrayList<Integer> arrayIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        listViewBooks = (ListView) findViewById(R.id.bookInfo);
        button  = (Button) findViewById(R.id.buttonCreate);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateView();
            }
        });

        createDatabase();
        listData();
    }

    @Override
    protected void onResume(){
        super.onResume();
        listData();
    }

    public void exclude() {
        AlertDialog.Builder msgBox = new     AlertDialog.Builder(MainActivity.this);
        msgBox.setTitle("Excluir");
        msgBox.setIcon(android.R.drawable.ic_menu_delete);
        msgBox.setMessage("Você realmente deseja excluir esse registro?");
        msgBox.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                delete();
                listData();
            }
        });
        msgBox.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        msgBox.show();
    }

    public void delete(){
        try{
            database = openOrCreateDatabase("sqlapp", MODE_PRIVATE, null);
            String sql = "DELETE FROM store WHERE id =?";
            SQLiteStatement stmt = database.compileStatement(sql);
            stmt.bindLong(1, idSelected);
            stmt.executeUpdateDelete();
            listData();
            database.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    private void openCreateView() {
        Intent intent = new Intent(this, CreateService.class);
        startActivity(intent);
    }

    private void listData() {
        try {
            arrayIds = new ArrayList<>();
            dataConnection();
            Cursor myCursor = database.rawQuery("SELECT id, nomeLivro, preco, disponivel FROM store", null);
            ArrayList<String> linhas = new ArrayList<String>();
            ArrayList<Map<String, Object>> bookList = new ArrayList<>();
//            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
//                    this,
//                    android.R.layout.simple_list_item_1,
//                    android.R.id.text1,
//                    linhas
//            );
//            listViewBooks.setAdapter(arrayAdapter);

            myCursor.moveToFirst();
            while (myCursor != null) {
                String nomeLivro = myCursor.getString(1);
                Double preco = myCursor.getDouble(2);
                Boolean disponivel = myCursor.getInt(3) == 1;

                Map<String, Object> book = new HashMap<>();
                book.put("nomeLivro", nomeLivro);
                book.put("preco", preco);
                book.put("disponivel", disponivel ? "Sim" : "Não");


                bookList.add(book);
                arrayIds.add(myCursor.getInt(0));
                myCursor.moveToNext();
            }
            myCursor.close();

            String[] from = {"nomeLivro", "preco", "disponivel"};
            int[] to = {R.id.bookInfo};

            // Configure o SimpleAdapter
            SimpleAdapter adapter = new SimpleAdapter(
                    this,
                    bookList,
                    android.R.layout.simple_list_item_1,
                    from,
                    to
            ) {
                @Override
                public View getView(final int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);

                    // Configure o comportamento do ícone de lixeira
                    ImageView deleteIcon = view.findViewById(R.id.deleteIcon);
                    deleteIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            idSelected = arrayIds.get(position);  // Obtenha o ID do item
                            exclude();  // Chame a função de exclusão existente
                        }
                    });

                    return view;
                }
            };
            listViewBooks.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void createDatabase() {

        try{
            dataConnection();
            database.execSQL("CREATE TABLE IF NOT EXISTS store(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nomeLivro TEXT, " +
                    "autor TEXT," +
                    "anoPublicacao INTEGER," +
                    "preco REAL, " +
                    "disponivel INTEGER)"
            );
            database.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void dataConnection() {
        database = openOrCreateDatabase("sqlapp", MODE_PRIVATE, null);
    }

}