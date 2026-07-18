package com.example.projetobdlivro;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
//responsavel por gerir area de utilizador admin
public class MainActivity4 extends AppCompatActivity {

    private ListView lvuser;
    private Button btnlistaruser, btnvoltar;
    private bd_livro bd;
    private ArrayList<String> lista;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main4);

        lvuser = findViewById(R.id.lvuser);
        btnlistaruser = findViewById(R.id.btnlistaruser);
        btnvoltar = findViewById(R.id.btnvoltar);
        bd = new bd_livro(MainActivity4.this, "bdlivro.db", null, 1);
        lista = new ArrayList<>();
        adapter = new ArrayAdapter<>(MainActivity4.this, android.R.layout.simple_list_item_1, lista);
        lvuser.setAdapter(adapter);
        CarregarLista();

        lvuser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String user;
                String row = lista.get(position);
                user = row.split("\\|")[1];
                abrirmenu(user);
            }
        });

        btnvoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity4.this, MainActivity3.class);
                startActivity(intent);
            }
        });

        btnlistaruser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarregarLista();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void CarregarLista() {
        lista.clear();
        Cursor c = bd.listarUtilizador();
        if(c.moveToFirst())
        {
            do{
                String nome = c.getString(0);
                String username = c.getString(1);
                String pass = c.getString(2);
                String email = c.getString(3);
                lista.add(nome + "|" + username + "|" + pass + "|" + email);

            }while(c.moveToNext());
        }
        c.close();
        adapter.notifyDataSetChanged();
    }

    private void abrirmenu(String user) {
        String[] opcoes = {"Actualizar", "Eliminar", "Cancelar"};
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setTitle("Lista de utilizadores");
        a.setItems(opcoes, (dialog, which) -> {
            switch (which) {
                case 0:
                    atualizarU(user);
                    break;
                case 1:
                    eliminarU(user);
                    break;
                case 2:
                    dialog.dismiss();
                    break;
            }
        });
        a.show();
    }

    public void atualizarU(String user)
    {
        Cursor c = bd.listarutiporUsername(user);
        if(c.moveToFirst()) {
            EditText etunome = new EditText(MainActivity4.this);
            EditText etupass = new EditText(MainActivity4.this);
            EditText etuemail = new EditText(MainActivity4.this);
            String unome = c.getString(c.getColumnIndexOrThrow("nome"));
            String upass = c.getString(c.getColumnIndexOrThrow("password"));
            String uemail = c.getString(c.getColumnIndexOrThrow("email"));
            etunome.setText(unome);
            etupass.setText(upass);
            etuemail.setText(uemail);
            LinearLayout layout = new LinearLayout(MainActivity4.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(etunome);
            layout.addView(etupass);
            layout.addView(etuemail);
            AlertDialog.Builder a = new AlertDialog.Builder(this);
            a.setView(layout);
            a.setMessage("Atualizar: " + user);

            a.setPositiveButton("Confirmar", (dialog, which)->{
                String unnome = etunome.getText().toString();
                String unpass = etupass.getText().toString();
                String unemail = etuemail.getText().toString();
                boolean verifica = bd.atualizarUtilizador(user, unnome, unpass, unemail);
                if(verifica) {

                Toast.makeText(MainActivity4.this, "Atualizado com sucesso...", Toast.LENGTH_LONG).show();
                CarregarLista();
                }
                else
                    Toast.makeText(MainActivity4.this, "Erro o atualizar...", Toast.LENGTH_LONG).show();

            });
            a.setNegativeButton("Cancel", null);
            a.show();
        }

    }

    public void eliminarU(String user)
    {
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setMessage("Quer mesmo eliminar?");
        a.setPositiveButton("Confirmar", (dialog,which)->{
            boolean verifica = bd.eliminarUtilizador(user);
            if(verifica)
            {
                Toast.makeText(MainActivity4.this, "Eliminado com sucesso...", Toast.LENGTH_LONG).show();
                CarregarLista();
            }
            else
                Toast.makeText(MainActivity4.this, "Erro ao eliminar...", Toast.LENGTH_LONG).show();
        });
        a.setNegativeButton("Cancel", null);
        a.show();


    }
}