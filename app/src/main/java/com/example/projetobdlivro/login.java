package com.example.projetobdlivro;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class login extends Fragment {
    private EditText etluser, etlpass;
    private Button btnlogin;
    private bd_livro bd;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment login.
     */
    // TODO: Rename and change types and number of parameters
    public static login newInstance(String param1, String param2) {
        login fragment = new login();
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        etluser = view.findViewById(R.id.etluser);
        etlpass = view.findViewById(R.id.etlpass);
        btnlogin = view.findViewById(R.id.btnlogin);
        bd = new bd_livro(getActivity(), "bdlivro.db", null, 1);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = etluser.getText().toString();
                String pass = etlpass.getText().toString();
                Cursor c = bd.listarutiporUsername(user);
                if (user.isEmpty() || pass.isEmpty())
                    Toast.makeText(getActivity(), "Um dos campos vazios...", Toast.LENGTH_LONG).show();
                else {
                    if (c.moveToFirst()) {
                        String auser = c.getString(c.getColumnIndexOrThrow("username"));
                        //só o admin pode gerir utilizadores
                        if ("admin".equals(auser)) {
                            Intent intent = new Intent(getActivity(), MainActivity4.class);
                            startActivity(intent);

                        } else {
                            //utilizadores comuns
                            Toast.makeText(getActivity(), "Login efetuado com sucesso", Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(getActivity(), MainActivity2.class);
                            startActivity(intent);
                        }
                    } else
                        Toast.makeText(getActivity(), "Erro ao fazer login", Toast.LENGTH_LONG).show();
                    etluser.setText("");
                    etlpass.setText("");
                }
            }
        });




        return view;
    }
}