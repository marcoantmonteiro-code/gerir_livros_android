package com.example.projetobdlivro;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity2 extends AppCompatActivity {
    private String[] paginas = {"Autor", "Editora", "Livro"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
        TabLayout tablayout = findViewById(R.id.tablayout);
        ViewPager2 vp = findViewById(R.id.vp);
        //cria 3 fragmentos, autor, editora e livro
        FragmentStateAdapter adapter = new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if(position==0)
                    return new Autor();
                else if(position==1)
                    return new Editora();
                else
                    return new Livro();
            }

            @Override
            public int getItemCount() {
                return 3;
            }
        };
        vp.setAdapter(adapter);
        new TabLayoutMediator(tablayout, vp, (tab, position)->tab.setText(paginas[position])).attach();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}