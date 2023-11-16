package sv.edu.universidad.moneyminds.ui.Categorias;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import sv.edu.universidad.moneyminds.AdminDB;
import sv.edu.universidad.moneyminds.R;


public class catIngresos extends Fragment {

    private ListView lvLista;

    private LinearLayout binding;

    public catIngresos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = (LinearLayout) inflater.inflate(R.layout.fragment_cat_ingresos, null);
        View root = binding.getRootView();


        lvLista = binding.findViewById(R.id.lvGCat);
        genList();
        lvLista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                String t_name = (String) lvLista.getAdapter().getItem(pos);
                String t_Cat = getString(R.string.categoria_elim_set);
                String d_Cat = "";
                AdminDB admin = new AdminDB(getActivity(), "DBmoney", null, 1);
                SQLiteDatabase bd = admin.getWritableDatabase();
                Cursor filaPerG = bd.rawQuery("SELECT nombre " +
                        "FROM CatIngresos " +
                        "WHERE nombre = '" + t_name + "'",null);
                if(filaPerG.moveToFirst())
                //while(fila.moveToNext())
                {
                    d_Cat = filaPerG.getString(0);
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                    // Get the layout inflater
                    LayoutInflater inflater = requireActivity().getLayoutInflater();
                    final View  customV = inflater.inflate(R.layout.dialog_elim, null);
                    TextView v_cat = customV.findViewById(R.id.catElim);

                    v_cat.setText(t_Cat+d_Cat);

                    builder.setTitle(R.string.eliminar_gasto);
                    builder.setView(customV)
                            .setPositiveButton(R.string.eliminar , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    AdminDB admin1 = new AdminDB(getActivity(), "DBmoney", null, 1);
                                    SQLiteDatabase bde = admin1.getWritableDatabase();
                                    int cant = bde.delete("CatIngresos", "nombre = '" + t_name + "'", null);
                                    bde.close();
                                    genList();
                                    if(cant==1)Toast.makeText(getActivity() , R.string.cat_elim_succes  ,
                                            Toast.LENGTH_SHORT).show();
                                    else Toast.makeText(getActivity() , R.string.cat_elim_failed ,
                                            Toast.LENGTH_SHORT).show();

                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    bd.close();
                    builder.setCancelable(false);
                    builder.create();
                    builder.show();
                }
                else {};
                bd.close();
            }
        });

        return root;
    }

    public void genList(){
        AdminDB admin = new AdminDB(getActivity(), "DBmoney", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery("SELECT nombre FROM CatIngresos",null);
        List<String> datos = new ArrayList<String>();
        datos.add(getString(R.string.data_cat_ingreso));
        datos.add(getString(R.string.data_cat_ingreso2));
        while (fila.moveToNext())
        {
            datos.add(fila.getString(0));
        }
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, datos);

        int itemcount = adaptador.getCount();
        ViewGroup.LayoutParams params = lvLista.getLayoutParams();
        int dps = ((itemcount*70)+390);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        params.height = pixels;
        lvLista.setLayoutParams(params);
        lvLista.setAdapter(adaptador);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding =null;
    }
}