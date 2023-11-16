package sv.edu.universidad.moneyminds.ui.Transacciones;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import sv.edu.universidad.moneyminds.AdminDB;
import sv.edu.universidad.moneyminds.R;

public class Ajustes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);
    }

    public void Volver(View view) {
        Intent resp = new Intent();
        resp.putExtra("Action", "Conf");
        setResult(10, resp);
        finish();

    }

    public void CleanDB(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.warning_text)
                .setTitle(R.string.warning);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.aceptar , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AdminDB admin1 = new AdminDB(getApplicationContext(), "DBmoney", null, 1);
                SQLiteDatabase db = admin1.getWritableDatabase();
                db.execSQL("DELETE FROM Ingresos");
                db.execSQL("DELETE FROM Gastos");
                db.close();
                Toast msn = Toast.makeText(getApplicationContext(),
                        R.string.cleardata_success, Toast.LENGTH_SHORT);
                msn.show();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setCancelable(false);
        builder.create();
        builder.show();
    }

    public void CleanCat(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.warning_text)
                .setTitle(R.string.warning);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.aceptar , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AdminDB admin1 = new AdminDB(getApplicationContext(), "DBmoney", null, 1);
                SQLiteDatabase db = admin1.getWritableDatabase();
                db.execSQL("DELETE FROM CatIngresos");
                db.execSQL("DELETE FROM CatGastos");
                db.close();
                Toast msn = Toast.makeText(getApplicationContext(),
                        R.string.cleardata_success, Toast.LENGTH_SHORT);
                msn.show();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.setCancelable(false);
        builder.create();
        builder.show();
    }
}