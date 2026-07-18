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
 * Use the {@link Editora#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Editora extends Fragment {

    private EditText etenome, etecidade, etewebsite;
    private Button btneadd, btnelistar;
    private ListView lve;
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

    public Editora() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Editora.
     */
    // TODO: Rename and change types and number of parameters
    public static Editora newInstance(String param1, String param2) {
        Editora fragment = new Editora();
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
        View view = inflater.inflate(R.layout.fragment_editora, container, false);
        etenome = view.findViewById(R.id.etenome);
        etecidade = view.findViewById(R.id.etecidade);
        etewebsite = view.findViewById(R.id.etewebsite);
        btneadd = view.findViewById(R.id.btneadd);
        btnelistar = view.findViewById(R.id.btnelistar);
        lve = view.findViewById(R.id.lve);

        bd = new bd_livro(getActivity(), "bdlivro.db", null, 1);

        lista = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, lista);
        lve.setAdapter(adapter);
        imgsair = view.findViewById(R.id.imgsair2);
        CarregarLista();

        imgsair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sair = new Intent(getActivity(), MainActivity.class);
                startActivity(sair);
            }
        });

        lve.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int valorid;
                String row = lista.get(position);
                valorid = Integer.parseInt(row.split("\\|")[0]);
                abrirmenu(valorid);
            }
        });

        btnelistar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CarregarLista();
            }
        });

        btneadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = etenome.getText().toString();
                String cidade = etecidade.getText().toString();
                String website = etewebsite.getText().toString();
                if(nome.isEmpty() || cidade.isEmpty() || website.isEmpty())
                    Toast.makeText(getActivity(), "Um dos campos vazio.", Toast.LENGTH_LONG).show();
                else
                {
                    boolean verifica = bd.addEditora(nome, cidade, website);
                    if(verifica) {
                        Toast.makeText(getActivity(), "Adicionado com sucesso.", Toast.LENGTH_LONG).show();
                        CarregarLista();
                    }
                    else
                        Toast.makeText(getActivity(), "Erro verifique...", Toast.LENGTH_LONG).show();

                    etenome.setText("");
                    etecidade.setText("");
                    etewebsite.setText("");

                }

            }
        });




        return view;
    }

    public void CarregarLista() {
        lista.clear();
        Cursor c = bd.listarEditora();
        if(c.moveToFirst())
        {
            do{
                int id = c.getInt(0);
                String nome = c.getString(1);
                String cidade = c.getString(2);
                String website = c.getString(3);
                lista.add(id + "|" + nome + "|" + cidade + "|" + website);

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
                    atualizarE(id);
                    break;
                case 1:
                    eliminarE(id);
                    break;
                case 2:
                    dialog.dismiss();
                    break;
            }


        });
        a.show();
    }

    public void atualizarE(int id)
    {
        Cursor c = bd.listareditoraId(id);
        if(c.moveToFirst())
        {
            String nome = c.getString(c.getColumnIndexOrThrow("nome"));
            String cidade = c.getString(c.getColumnIndexOrThrow("cidade"));
            String website = c.getString(c.getColumnIndexOrThrow("website"));
            EditText enome = new EditText(getActivity());
            EditText ecidade = new EditText(getActivity());
            EditText ewebsite = new EditText(getActivity());
            enome.setText(nome);
            ecidade.setText(cidade);
            ewebsite.setText(website);
            LinearLayout layout = new LinearLayout(getActivity());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(enome);
            layout.addView(ecidade);
            layout.addView(ewebsite);
            AlertDialog.Builder a = new AlertDialog.Builder(getActivity());
            a.setMessage("Actualizar com Id=" + id);
            a.setView(layout);
            a.setPositiveButton("Confirmar", (dialog, which)->{
                String enome2 = enome.getText().toString();
                String ecidade2 = ecidade.getText().toString();
                String ewebsite2 = ewebsite.getText().toString();
                boolean verifica = bd.atulizarEditora(id, enome2, ecidade2, ewebsite2);
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

    public void eliminarE(int id)
    {
        AlertDialog.Builder a = new AlertDialog.Builder(getActivity());
        a.setTitle("Eliminar registo");
        a.setMessage("Tem a certeza que deseja eliminar?");
        a.setPositiveButton("Confirmar", ((dialog, which) -> {
            boolean verifica = bd.deleteEditora(id);
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