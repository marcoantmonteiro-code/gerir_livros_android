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
 * Use the {@link Livro#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Livro extends Fragment {
    private EditText etltitulo, etlano, etlidE, etlidA;
    private Button btnladd, btnllistar;
    private ListView lvl;
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

    public Livro() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Livro.
     */
    // TODO: Rename and change types and number of parameters
    public static Livro newInstance(String param1, String param2) {
        Livro fragment = new Livro();
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
        View view = inflater.inflate(R.layout.fragment_livro, container, false);
        etltitulo = view.findViewById(R.id.etltitulo);
        etlano = view.findViewById(R.id.etlano);
        etlidE = view.findViewById(R.id.etlidE);
        etlidA = view.findViewById(R.id.etlidA);
        btnladd = view.findViewById(R.id.btnladd);
        btnllistar = view.findViewById(R.id.btnllistar);
        lvl = view.findViewById(R.id.lvl);
        imgsair = view.findViewById(R.id.imgsair3);
        bd = new bd_livro(getActivity(), "bdlivro.db", null, 1);
        lista = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, lista);
        lvl.setAdapter(adapter);
        CarregarLista();

        imgsair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sair = new Intent(getActivity(), MainActivity.class);
                startActivity(sair);
            }
        });

        lvl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int valorid;
                String row = lista.get(position);
                valorid = Integer.parseInt(row.split("\\|")[0]);
                abrirmenu(valorid);
            }
        });

        btnladd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titulo = etltitulo.getText().toString();
                String ano = etlano.getText().toString();
                String idEditora = etlidE.getText().toString();
                String idAutor = etlidA.getText().toString();

                if(titulo.isEmpty() || ano.isEmpty() || idEditora.isEmpty() || idAutor.isEmpty())
                    Toast.makeText(getActivity(), "Um dos campos está vazio...", Toast.LENGTH_LONG).show();
                else
                {
                    int Editora = Integer.parseInt(idEditora);
                    int Autor = Integer.parseInt(idAutor);
                    boolean verifica = bd.addLivro(titulo, ano, Editora, Autor);
                    if(verifica)
                    {
                        Toast.makeText(getActivity(), "Livro adicionado com sucesso...", Toast.LENGTH_LONG).show();
                        CarregarLista();

                    }
                    else
                        Toast.makeText(getActivity(), "Erro ao adicionar...", Toast.LENGTH_LONG).show();

                    etltitulo.setText("");
                    etlano.setText("");
                    etlidE.setText("");
                    etlidA.setText("");
                }
            }
        });

        return view;
    }

    public void CarregarLista() {
        lista.clear();
        Cursor c = bd.listarLivro();
        if(c.moveToFirst())
        {
            do{
                int id = c.getInt(0);
                String titulo = c.getString(1);
                String ano = c.getString(2);
                int idEditora  = c.getInt(3);
                int idAutor = c.getInt(4);
                lista.add(id + "|" + titulo + "|" + ano + "|E:" + idEditora + "|A:" + idAutor);

            }while(c.moveToNext());
        }
        c.close();
        adapter.notifyDataSetChanged();
    }

    private void abrirmenu(int id) {
        String[] opcoes = {"Actualizar", "Eliminar", "Cancelar"};
        AlertDialog.Builder a = new AlertDialog.Builder(getActivity());
        a.setTitle("Id:" + id);
        a.setItems(opcoes, (dialog, which) -> {
            switch (which) {
                case 0:
                    atualizarL(id);
                    break;
                case 1:
                    eliminarL(id);
                    break;
                case 2:
                    dialog.dismiss();
                    break;
            }


        });
        a.show();
    }

    public void atualizarL(int id)
    {
        Cursor c = bd.listarlivroId(id);
        if(c.moveToFirst())
        {
            String titulo = c.getString(c.getColumnIndexOrThrow("titulo"));
            String ano = c.getString(c.getColumnIndexOrThrow("ano"));
            int idE = c.getInt(c.getColumnIndexOrThrow("idEditora"));
            int idA = c.getInt(c.getColumnIndexOrThrow("idAutor"));
            EditText ettitulo = new EditText(getActivity());
            EditText etano = new EditText(getActivity());
            EditText etidE = new EditText(getActivity());
            EditText etidA = new EditText(getActivity());
            ettitulo.setText(titulo);
            etano.setText(ano);
            etidE.setText(String.valueOf(idE));
            etidA.setText(String.valueOf(idA));
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(ettitulo);
            layout.addView(etano);
            layout.addView(etidE);
            layout.addView(etidA);
            AlertDialog.Builder a = new AlertDialog.Builder(getActivity());
            a.setMessage("Atualizar com idLivro=" + id);
            a.setView(layout);
            a.setPositiveButton("Confirmar", (dialog,which)->{
                String ntitulo = ettitulo.getText().toString();
                String nano = etano.getText().toString();
                String nidE = etidE.getText().toString();
                String nidA = etidA.getText().toString();
                int inidE = Integer.parseInt(nidE);
                int inidA = Integer.parseInt(nidA);
                boolean verifica = bd.atualizarLivro(id, ntitulo, nano, inidE, inidA);
                if(verifica)
                {
                    Toast.makeText(getActivity(), "Atualizado com sucesso.", Toast.LENGTH_LONG).show();
                    CarregarLista();
                }
                else
                    Toast.makeText(getActivity(), "Erro ao atualizar.", Toast.LENGTH_LONG).show();
            });
            a.setNegativeButton("Cancel", null);
            a.show();
        }


    }

    public void eliminarL(int id)
    {
        AlertDialog.Builder a = new AlertDialog.Builder(getActivity());
        a.setTitle("Eliminar registo");
        a.setMessage("Tem a certeza que deseja eliminar?");
        a.setPositiveButton("Confirmar", ((dialog, which) -> {
            boolean verifica = bd.deleteLivro(id);
            if (verifica)
            {
                Toast.makeText(getActivity(), "Eliminado com sucesso.", Toast.LENGTH_LONG).show();
                CarregarLista();
            }
            else
                Toast.makeText(getActivity(), "Erro de eliminação.", Toast.LENGTH_LONG).show();
        }));

        a.setNegativeButton("Cancel", null);
        a.show();

    }


}


