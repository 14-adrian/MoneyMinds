package sv.edu.universidad.moneyminds;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminDB extends SQLiteOpenHelper {

    public AdminDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Ingresos (id integer primary key autoincrement, categoria text, cantidad real, fecha text, descripcion text)");
        db.execSQL("CREATE TABLE Gastos (id integer primary key autoincrement, categoria text, cantidad real, fecha text, descripcion text)");
        db.execSQL("CREATE TABLE CatIngresos (id integer primary key autoincrement, nombre text)");
        db.execSQL("CREATE TABLE CatGastos (id integer primary key autoincrement, nombre text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnt, int versionNue) {
        db.execSQL("DROP TABLE IF EXISTS Ingresos");
        db.execSQL("DROP TABLE IF EXISTS Gastos");
        db.execSQL("DROP TABLE IF EXISTS CatIngresos");
        db.execSQL("DROP TABLE IF EXISTS CatGastos");
        db.execSQL("CREATE TABLE Ingresos (id integer primary key autoincrement, categoria text, cantidad real, fecha text, descripcion text)");
        db.execSQL("CREATE TABLE Gastos (id integer primary key autoincrement, categoria text, cantidad real, fecha text, descripcion text)");
        db.execSQL("CREATE TABLE CatIngresos (id integer primary key autoincrement, nombre text)");
        db.execSQL("CREATE TABLE CatGastos (id integer primary key autoincrement, nombre text)");
    }
}