package sv.edu.universidad.moneyminds.ui.Categorias;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

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
        AdminDB admin = new AdminDB(getActivity(), "DBmoney", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery("SELECT nombre FROM CatIngresos",null);
        List<String> datos = new ArrayList<String>();
        datos.add("Salario");
        datos.add("Regalo");
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

        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding =null;
    }
}