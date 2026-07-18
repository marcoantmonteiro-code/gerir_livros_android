package com.example.projetobdlivro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class bd_livro extends SQLiteOpenHelper {

    public bd_livro(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
        public void onCreate(SQLiteDatabase db) {
        //criação das tabelas para a base de dados
        db.execSQL(
                "CREATE TABLE editora (" +
                        "  idEditora INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  nome TEXT," +
                        "  cidade TEXT," +
                        "  website TEXT" +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE autor (" +
                        "  idAutor INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  nome TEXT," +
                        "  nacionalidade TEXT," +
                        "  anoNascimento TEXT" +
                        ")"
        );

        db.execSQL(
                "CREATE TABLE livro (" +
                        "  idLivro INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "  titulo TEXT," +
                        "  ano TEXT," +
                        "  idEditora INTEGER," +
                        "  idAutor INTEGER," +
                        "  FOREIGN KEY(idEditora) REFERENCES editora(idEditora) " +
                        "      ON DELETE CASCADE ON UPDATE CASCADE," +
                        "  FOREIGN KEY(idAutor) REFERENCES autor(idAutor) " +
                        "      ON DELETE CASCADE ON UPDATE CASCADE" +
                        ")"
        );
        db.execSQL(
                "CREATE TABLE utilizador (" +
                        "  nome TEXT," +
                        "  username TEXT PRIMARY KEY," +
                        "  password TEXT," +
                        "  email TEXT" +
                        ")"
        );


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor listarUtilizador()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM utilizador", null);
    }

    public boolean addUtilizador(String nome, String username, String password, String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("username", username);
        values.put("password", password);
        values.put("email", email);
        long resultado = db.insert("utilizador", null, values);
        if(resultado==-1)
            return false;
        else
            return true;

    }

    public Cursor listarutiporUsername(String username)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM utilizador WHERE username=?", new String[]{username});
    }

    public boolean atualizarUtilizador(String user, String nome, String pass, String email)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("password", pass);
        values.put("email", email);
        int rows = db.update("utilizador", values, "username=?", new String[]{user});
        if(rows>0)
            return true;
        else
            return false;
    }

    public boolean eliminarUtilizador(String user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("utilizdor", "username=?", new String[]{user});
        if(rows > 0)
            return true;
        else
            return false;

    }

    public boolean addLivro(String titulo, String ano, int idEditora, int idAutor)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("ano", ano);
        values.put("idEditora", idEditora);
        values.put("idAutor", idAutor);

        long resultado = db.insert("livro", null, values);
        Log.d("BDLIVRO", "Resultado insert livro = " + resultado);

        return resultado != -1;
    }


    public Cursor listarLivro()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM livro", null);
    }


    public boolean addAutor(String nome, String nacionalidade, String anoNascimento)
    {
        SQLiteDatabase db =  this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("nacionalidade", nacionalidade);
        values.put("anoNascimento", anoNascimento);
        long resultado = db.insert("autor", null, values);
        if(resultado==-1)
            return false;
        else
            return true;

    }

    public Cursor listarAutor()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM autor", null);
    }

    public boolean addEditora(String nome, String cidade, String website)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("cidade", cidade);
        values.put("website", website);
        long resultado = db.insert("editora", null, values);
        if(resultado==-1)
            return false;
        else
            return true;
    }

    public Cursor listarEditora()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM editora", null);

    }

    public Cursor listarautorId(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM autor WHERE idAutor=?", new String[]{String.valueOf(id)});
    }

    public boolean atualizarAutor(int id, String nome, String nacionalidade, String anoNascimento)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("nacionalidade", nacionalidade);
        values.put("anoNascimento", anoNascimento);
        int rows = db.update("autor", values, "idAutor=?", new String[]{String.valueOf(id)});
        if(rows>0)
            return true;
        else
            return false;
    }

    public boolean deleteAutor(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("autor", "idAutor=?", new String[]{String.valueOf(id)});
        if(rows>0)
            return true;
        else
            return false;
    }

    public Cursor listareditoraId(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM editora WHERE idEditora=?", new String[]{String.valueOf(id)});

    }

    public boolean atulizarEditora(int id, String nome, String cidade, String website)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("cidade", cidade);
        values.put("website", website);
        int rows = db.update("editora", values, "idEditora=?", new String[]{String.valueOf(id)});
        if(rows>0)
            return true;
        else
            return false;
    }

    public boolean deleteEditora(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("editora", "idEditora=?", new String[]{String.valueOf(id)});
        if(rows>0)
            return true;
        else
            return false;

    }

    public Cursor listarlivroId(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM livro WHERE idLivro=?", new String[]{String.valueOf(id)});
    }

    public boolean atualizarLivro(int id, String titulo, String ano, int idEditora, int idAutor)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("titulo", titulo);
        values.put("ano", ano);
        values.put("idEditora", idEditora);
        values.put("idAutor", idAutor);
        int rows = db.update("livro", values, "idLivro=?", new String[]{String.valueOf(id)});
        if(rows>0)
            return true;
        else
            return false;
    }

    public boolean deleteLivro(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("livro", "idLivro=?", new String[]{String.valueOf(id)});
        if(rows>0)
            return true;
        else return false;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true); // ativa PRAGMA foreign_keys = ON
    }


}
