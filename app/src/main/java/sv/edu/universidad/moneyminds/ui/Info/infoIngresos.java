package sv.edu.universidad.moneyminds.ui.Info;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sv.edu.universidad.moneyminds.AdminDB;
import sv.edu.universidad.moneyminds.Datos;
import sv.edu.universidad.moneyminds.ListViewInfo;
import sv.edu.universidad.moneyminds.R;

public class infoIngresos extends Fragment {

    private PieChart pieChart;
    private ListView lvLista;
    public TextView txtFecha;
    public MaterialCardView mcV;
    private LinearLayout  binding;

    public infoIngresos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = (LinearLayout) inflater.inflate(R.layout.fragment_info_ingresos, null);
        View root = binding.getRootView();

        txtFecha = binding.findViewById(R.id.txtFecha);
        String today = ((Datos) getActivity().getApplication()).getToday();
        txtFecha.setText(today);


        lvLista = binding.findViewById(R.id.lvGCat);
        mcV = binding.findViewById(R.id.crdIn);

        pieChart = binding.findViewById(R.id.pieCat);
        genChart();

        return root;
    }
    public void genChart(){

        //--------Gen Data---------------------
        AdminDB admin = new AdminDB(getActivity(), "DBmoney", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        int mMonth = ((Datos) getActivity().getApplication()).getMonth();
        int mYear = ((Datos) getActivity().getApplication()).getYear();
        Random rnd = new Random();

        List<Float> cantG = new ArrayList<Float>();
        List<String> cantGStr = new ArrayList<String>();
        List<String> catG = new ArrayList<String>();
        List<Integer> colores = new ArrayList<Integer>();
        Float total = (float) 0;


        Cursor filaPerG = bd.rawQuery("SELECT sum(cantidad), categoria " +
                "FROM Ingresos " +
                "WHERE strftime('%m', fecha) = '"+mMonth+"' " +
                "AND strftime('%Y', fecha) = '"+mYear+"'" +
                "GROUP BY categoria",null);
        while (filaPerG.moveToNext())
        {
            cantG.add(filaPerG.getFloat(0));
            cantGStr.add(filaPerG.getString(0));
            total += filaPerG.getFloat(0);
            catG.add(filaPerG.getString(1));
        }

        for (int i = 0; i < cantG.size(); i++) {
            int color = Color.argb(255, rnd.nextInt(230), rnd.nextInt(230), rnd.nextInt(230));
            colores.add(color);
            float por = (cantG.get(i)/total)*100;
            pieChart.addPieSlice(new PieModel(catG.get(i), por, color));
        }
        pieChart.setInnerValueString(total.toString()+"$");
        pieChart.startAnimation();

        //----------Generar Lista---------------------

        ListViewInfo adapter=new ListViewInfo(getActivity(), colores, catG, cantGStr, R.drawable.baseline_play_arrow_24);
        int itemcount = adapter.getCount();
        ViewGroup.LayoutParams params = lvLista.getLayoutParams();
        ViewGroup.LayoutParams paramsM = mcV.getLayoutParams();
        int dps = ((itemcount*70)+390);
        int dps2 =(itemcount*70);

        final float scale = getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        int pixels2 = (int) (dps2 * scale + 0.5f);

        paramsM.height = pixels;
        params.height = pixels2;
        mcV.setLayoutParams(paramsM);
        lvLista.setLayoutParams(params);
        lvLista.setAdapter(adapter);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding =null;
    }
}