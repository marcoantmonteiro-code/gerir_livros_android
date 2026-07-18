package com.example.projetobdlivro;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Autor#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Autor extends Fragment {

    private EditText etnome, etnac, etano;
    private Button btnadd, btnlistar;
    private ListView lv;
    private bd_livro bd;
    private ArrayList<String> lista;
    private ArrayAdapter<String> adapter;
    private ImageView imgsair;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Autor() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Autor.
     */
    // TODO: Rename and change types and number of parameters
    public static Autor newInstance(String param1, String param2) {
        Autor fragment = new Autor();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_autor, container, false);
        etnome = view.findViewById(R.id.etanome);
        etnac = view.findViewById(R.id.etanac);
        etano = view.findViewById(R.id.etaano);
        lv = view.findViewById(R.id.lva);
        bd = new bd_livro(getActivity(), "bdlivro.db", null, 1);
        btnadd= view.findViewById(R.id.btnaadd);
        btnlistar = view.findViewById(R.id.btnalistar);
        imgsair = view.findViewById(R.id.imgsair1);
        lista = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, lista);
        lv.setAdapter(adapter);
        CarregarLista();

        imgsair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sair = new Intent(getActivity(), MainActivity.class);
                startActivity(sair);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int valorid;
                String row = lista.get(position);
                valorid= Integer.parseInt(row.split("\\|")[0]);
                abrirmenu(valorid);
            }
        });

        btnlistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarregarLista();
            }
        });

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = etnome.getText().toString();
                String nac = etnac.getText().toString();
                String ano = etano.getText().toString();
                if(nome.isEmpty() || nac.isEmpty() || ano.isEmpty()) {
                    Toast.makeText(getActivity(), "Um dos campos está vazio", Toast.LENGTH_LONG).show();
                }
                else {
                    boolean verifica = bd.addAutor(nome, nac, ano);
                    if (verifica) {
                        Toast.makeText(getActivity(), "Adicionado com sucesso", Toast.LENGTH_LONG).show();
                        CarregarLista();
                    }
                    else
                        Toast.makeText(getActivity(), "Erro verifique...", Toast.LENGTH_LONG).show();

                    etnome.setText("");
                    etnac.setText("");
                    etano.setText("");
                }
            }
        });

        return view;
    }


    public void CarregarLista()
    {
        lista.clear();
        Cursor c = bd.listarAutor();
        if(c.moveToFirst())
        {
            do{
                int id = c.getInt(0);
                String nome = c.getString(1);
                String nac = c.getString(2);
                String ano = c.getString(3);
                lista.add(id + "|" + nome + "|" + nac + "|" + ano);

            }while(c.moveToNext());
        }
        c.close();
        adapter.notifyDataSetChanged();


    }

    private void abrirmenu(int id)
    {
        String[] opcoes = {"Actualizar", "Eliminar","Cancelar"};
        AlertDialog.Builder  a = new AlertDialog.Builder(getActivity());
        a.setTitle("Id:"+id);
        a.setItems(opcoes, (dialog, which)->{
            switch(which)
            {
                case 0: atualizarA(id); break;
                case 1: eliminarA(id); break;
                case 2: dialog.dismiss(); break;
            }


        });
        a.show();
    }

    public void atualizarA(int id)
    {
        Cursor c = bd.listarautorId(id);
        if(c.moveToFirst()) {
            String nome = c.getString(c.getColumnIndexOrThrow("nome"));
            String nacionalidade = c.getString(c.getColumnIndexOrThrow("nacionalidade"));
            String anoNascimento = c.getString(c.getColumnIndexOrThrow("anoNascimento"));
            EditText etanome = new EditText(getActivity());
            EditText etanacionalidade = new EditText(getActivity());
            EditText etaanoNascimento = new EditText(getActivity());
            etanome.setText(nome);
            etanacionalidade.setText(nacionalidade);
            etaanoNascimento.setText(anoNascimento);
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(etanome);
            layout.addView(etanacionalidade);
            layout.addView(etaanoNascimento);
            AlertDialog.Builder a = new AlertDialog.Builder(getActivity());
            a.setMessage("Atualizar com id=" + id);
            a.setView(layout);
            a.setPositiveButton("Confirmar", ((dialog, which) ->{
                String anome = etanome.getText().toString();
                String anacionalidade = etanacionalidade.getText().toString();
                String aanoNascimento = etaanoNascimento.getText().toString();
                boolean verifica = bd.atualizarAutor(id, anome, anacionalidade, aanoNascimento);
                if(verifica) {
                    Toast.makeText(getActivity(), "Atualizado com sucesso", Toast.LENGTH_LONG).show();
                    CarregarLista();
                }
                else
                    Toast.makeText(getActivity(), "Erro ao atualizar", Toast.LENGTH_LONG).show();

            } ));
            a.setNegativeButton("Cancelar", null);
            a.show();

        }

    }

    public void eliminarA(int id)
    {
        AlertDialog.Builder a = new AlertDialog.Builder(getActivity());
        a.setTitle("Eliminar");
        a.setMessage("Tem a certeza que quer eliminar?");
        a.setPositiveButton("Ok", ((dialog, which) -> {
            boolean verifica = bd.deleteAutor(id);
            if(verifica)
            {
                Toast.makeText(getActivity(), "Eliminado com sucesso", Toast.LENGTH_LONG).show();
                CarregarLista();
            }
            else
                Toast.makeText(getActivity(), "Erro ao eliminar", Toast.LENGTH_LONG).show();
        }));

        a.setNegativeButton("Cancel", null);
        a.show();
    }
}

