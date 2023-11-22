package sv.edu.universidad.moneyminds.ui.Transacciones;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import sv.edu.universidad.moneyminds.AdminDB;
import sv.edu.universidad.moneyminds.Datos;
import sv.edu.universidad.moneyminds.MyListView;
import sv.edu.universidad.moneyminds.R;
import sv.edu.universidad.moneyminds.databinding.FragmentHomeBinding;

public class TranFragment extends Fragment {

    ListView lista, lista2;
    private FragmentHomeBinding binding;
    FloatingActionButton mAddGas, mAddIng;
    ExtendedFloatingActionButton mAdd;
    LinearLayout layL;

    TextView addGasActionText, addIngActionText, txtDate, txtDate2, txtTotal, txtTotalPer, txtIngreso, txtGasto;

    Boolean isAllFabsVisible;
    String today;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TranViewModel homeViewModel =
                new ViewModelProvider(this).get(TranViewModel.class);
        setHasOptionsMenu(true);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        layL = binding.linearLHome;

        mAdd = binding.snAdd;
        // FAB button
        mAddGas = binding.btnGas;
        mAddIng = binding.btnIng;

        addIngActionText = binding.ingActionText;
        addGasActionText = binding.gasActionText;


        //----------Configurar Fecha--------------------

        txtDate = binding.txtFecha;
        txtDate2 = binding.txtFecha2;

        today = ((Datos) getActivity().getApplication()).getToday();

        if(today.isEmpty())
        {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month

            String formatDate = (((Datos) getActivity().getApplication()).getMonthForInt(mMonth+1).substring(0,3).toUpperCase() + "/" + mYear);
            ((Datos) getActivity().getApplication()).setToday(formatDate);

            today = ((Datos) getActivity().getApplication()).getToday();
        }
        int month = ((Datos) getActivity().getApplication()).getMonth();
        if (month == 0){
            final Calendar c = Calendar.getInstance();
            int dMonth = c.get(Calendar.MONTH);
            int mYear = c.get(Calendar.YEAR);// current month
            ((Datos) getActivity().getApplication()).setMonth(dMonth+1);
            ((Datos) getActivity().getApplication()).setYear(mYear);

        }


        //----------Definir variables iniciales-----------------
        txtTotal = binding.txtTotal;
        txtTotalPer = binding.txtTotalPer;
        txtIngreso = binding.txtIngreso;
        txtGasto = binding.txtGasto;
        actualizarTotal();
        generarLista();


        mAddGas.setVisibility(View.GONE);
        mAddIng.setVisibility(View.GONE);
        addGasActionText.setVisibility(View.GONE);
        addIngActionText.setVisibility(View.GONE);

        isAllFabsVisible = false;

        mAdd.shrink();

        mAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAllFabsVisible) {

                            mAddGas.show();
                            mAddIng.show();
                            addGasActionText.setVisibility(View.VISIBLE);
                            addIngActionText.setVisibility(View.VISIBLE);

                            mAdd.extend();

                            isAllFabsVisible = true;

                        } else {

                            closeFAB();
                        }
                    }
                });

//-----------------------Agregar Ingreso-------------------------------------

        mAddIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());

                LayoutInflater inflater = requireActivity().getLayoutInflater();

                //-------Spinner Categorias---------------
                AdminDB admin = new AdminDB(getActivity(), "DBmoney", null, 1);
                SQLiteDatabase bd = admin.getWritableDatabase();
                Cursor fila = bd.rawQuery("SELECT nombre FROM CatIngresos",null);
                final View  customV = inflater.inflate(R.layout.dialog_add_ingreso, null);
                Spinner dropdown = customV.findViewById(R.id.lstCat);
                EditText cantidad = customV.findViewById(R.id.edCantidad);
                EditText descripcion = customV.findViewById(R.id.edDesc);
                List<String> datos = new ArrayList<String>();
                datos.add(getString(R.string.cat_add_in));
                datos.add(getString(R.string.cat_add_in2));
                while (fila.moveToNext())
                {
                    datos.add(fila.getString(0));
                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, datos);
                dropdown.setAdapter(dataAdapter);
                builder.setTitle(R.string.agregar_ingreso);
                builder.setView(customV)
                        .setPositiveButton(R.string.agregar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                AdminDB admin = new AdminDB(getActivity(), "DBmoney", null, 1);
                                SQLiteDatabase bd = admin.getWritableDatabase();
                                String s_cant = cantidad.getText().toString();
                                String categoria = dropdown.getSelectedItem().toString();
                                String tdate = ((Datos) getActivity().getApplication()).getValDate();
                                String desc = descripcion.getText().toString();
                                if(s_cant.length()!=0){
                                    Double cant = Double.valueOf(s_cant);
                                    ContentValues registro = new ContentValues();
                                    registro.put("categoria", categoria);
                                    registro.put("cantidad", cant);
                                    registro.put("fecha", tdate);
                                    registro.put("descripcion", desc);
                                    bd.insert("Ingresos", null, registro);
                                    bd.close();
                                    actualizarTotal();
                                    Toast.makeText(getActivity(), R.string.toastIngresoText,
                                            Toast.LENGTH_SHORT).show();
                                    closeFAB();
                                }
                                else {
                                    bd.close();
                                    closeFAB();
                                    Toast.makeText(getActivity(), R.string.add_cat_error,
                                            Toast.LENGTH_SHORT).show();
                                }


                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                closeFAB();
                            }
                        });
                builder.setCancelable(false);
                builder.create();
                builder.show();
            }
        });

//------------------------Agregar Gasto----------------------------------------

        mAddGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                // Get the layout inflater
                LayoutInflater inflater = requireActivity().getLayoutInflater();

                //-------Spinner Categorias---------------
                AdminDB admin = new AdminDB(getActivity(), "DBmoney", null, 1);
                SQLiteDatabase bd = admin.getWritableDatabase();
                Cursor fila = bd.rawQuery("SELECT nombre FROM CatGastos",null);
                final View  customV = inflater.inflate(R.layout.dialog_add_gasto, null);
                Spinner dropdown = customV.findViewById(R.id.lstCat);
                List<String> datos = new ArrayList<String>();
                EditText cantidad = customV.findViewById(R.id.edCantidad);
                EditText descripcion = customV.findViewById(R.id.edDesc);
                datos.add(getString(R.string.cad_add_gas));
                datos.add(getString(R.string.cat_add_gas2));
                while (fila.moveToNext())
                {
                    datos.add(fila.getString(0));
                }

                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, datos);
                dropdown.setAdapter(dataAdapter);
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setTitle(R.string.agregar_gasto);
                builder.setView(customV)
                        // Add action buttons
                        .setPositiveButton(R.string.agregar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                AdminDB admin = new AdminDB(getActivity(), "DBmoney", null, 1);
                                SQLiteDatabase bd = admin.getWritableDatabase();
                                String s_cant = cantidad.getText().toString();
                                String categoria = dropdown.getSelectedItem().toString();
                                String desc = descripcion.getText().toString();
                                String tdate = ((Datos) getActivity().getApplication()).getValDate();
                                if(s_cant.length()!=0){
                                    Double cant = Double.valueOf(s_cant);
                                    ContentValues registro = new ContentValues();
                                    registro.put("categoria", categoria);
                                    registro.put("cantidad", cant);
                                    registro.put("fecha", tdate);
                                    registro.put("descripcion", desc);
                                    bd.insert("Gastos", null, registro);
                                    bd.close();
                                    closeFAB();
                                    actualizarTotal();
                                }
                                else {
                                    bd.close();
                                    closeFAB();
                                    Toast.makeText(getActivity(), R.string.add_cat_error,
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                closeFAB();
                            }
                        });
                builder.setCancelable(false);
                builder.create();
                builder.show();
            }
        });
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                int t_id = (int) lista.getAdapter().getItem(pos);
                String t_Cat = getString(R.string.categoria_elim_set),
                        t_Cant = getString(R.string.cantidad_elim_set),
                        t_fec = getString(R.string.fecha_elim_set),
                        t_desc = getString(R.string.descripcion);
                String d_Cat = "", d_Cant = "", d_fec, d_desc="";
                AdminDB admin = new AdminDB(getActivity(), "DBmoney", null, 1);
                SQLiteDatabase bd = admin.getWritableDatabase();
                int mMonth = ((Datos) getActivity().getApplication()).getMonth();
                int mYear = ((Datos) getActivity().getApplication()).getYear();
                Cursor filaPerG = bd.rawQuery("SELECT categoria, cantidad, fecha, descripcion " +
                        "FROM Gastos " +
                        "WHERE strftime('%m', fecha) = '"+mMonth+"'" +
                        " AND strftime('%Y', fecha) = '"+mYear+"'" +
                        " AND id = " + t_id + "",null);
                if(filaPerG.moveToFirst())
                //while(fila.moveToNext())
                {
                    d_Cat = filaPerG.getString(0);
                    d_Cant = filaPerG.getString(1);
                    d_fec = filaPerG.getString(2);
                    d_desc = filaPerG.getString(3);
                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                    // Get the layout inflater
                    LayoutInflater inflater = requireActivity().getLayoutInflater();
                    final View  customV = inflater.inflate(R.layout.dialog_elim, null);
                    TextView v_cat = customV.findViewById(R.id.catElim);
                    TextView v_cant = customV.findViewById(R.id.cantElim);
                    TextView v_fec = customV.findViewById(R.id.fecElim);
                    TextView v_desc = customV.findViewById(R.id.fecDesc);

                    v_cat.setText(t_Cat+d_Cat);
                    v_cant.setText(t_Cant+d_Cant+"$");
                    v_fec.setText(t_fec+d_fec);
                    v_desc.setText(t_desc + d_desc);

                    builder.setTitle(R.string.eliminar_gasto);
                    builder.setView(customV)
                            .setPositiveButton(R.string.eliminar , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                AdminDB admin1 = new AdminDB(getActivity(), "DBmoney", null, 1);
                                SQLiteDatabase bde = admin1.getWritableDatabase();
                                int cant = bde.delete("Gastos", "id = "+t_id+"", null);
                                bde.close();
                                actualizarTotal();
                                Toast.makeText(getActivity() , R.string.elim_gas_succes ,
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
        lista2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                int t_id = (int) lista2.getAdapter().getItem(pos);
                String t_Cat = getString(R.string.categoria_elim_set),
                        t_Cant = getString(R.string.cantidad_elim_set),
                        t_fec = getString(R.string.fecha_elim_set),
                        t_desc = getString(R.string.descripcion);
                String d_Cat = "", d_Cant = "", d_fec, d_desc="";
                AdminDB admin = new AdminDB(getActivity(), "DBmoney", null, 1);
                SQLiteDatabase bd = admin.getWritableDatabase();
                int mMonth = ((Datos) getActivity().getApplication()).getMonth();
                int mYear = ((Datos) getActivity().getApplication()).getYear();
                Cursor filaPerG = bd.rawQuery("SELECT categoria, cantidad, fecha, descripcion " +
                        "FROM Ingresos " +
                        "WHERE strftime('%m', fecha) = '"+mMonth+"'" +
                        " AND strftime('%Y', fecha) = '"+mYear+"'" +
                        " AND id = " + t_id + "",null);
                if(filaPerG.moveToFirst())
                //while(fila.moveToNext())
                {
                    d_Cat = filaPerG.getString(0);
                    d_Cant = filaPerG.getString(1);
                    d_fec = filaPerG.getString(2);
                    d_desc = filaPerG.getString(3);

                    MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                    // Get the layout inflater
                    LayoutInflater inflater = requireActivity().getLayoutInflater();
                    final View  customV = inflater.inflate(R.layout.dialog_elim, null);
                    TextView v_cat = customV.findViewById(R.id.catElim);
                    TextView v_cant = customV.findViewById(R.id.cantElim);
                    TextView v_fec = customV.findViewById(R.id.fecElim);
                    TextView v_desc = customV.findViewById(R.id.fecDesc);

                    v_cat.setText(t_Cat+d_Cat);
                    v_cant.setText(t_Cant+d_Cant+"$");
                    v_fec.setText(t_fec+d_fec);
                    v_desc.setText(t_desc + d_desc);

                    builder.setTitle(R.string.eliminar_ingreso);
                    builder.setView(customV)
                            .setPositiveButton(R.string.eliminar , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    AdminDB admin1 = new AdminDB(getActivity(), "DBmoney", null, 1);
                                    SQLiteDatabase bde = admin1.getWritableDatabase();
                                    int cant = bde.delete("Ingresos", "id = "+t_id+"", null);
                                    bde.close();
                                    actualizarTotal();
                                    Toast.makeText(getActivity() , R.string.ingreso_elim_succes ,
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

//------------------------Metodo Actualizar--------------------------

    public void actualizarTotal(){
        //-------------------Cargar Total---------------------------
        AdminDB admin = new AdminDB(getActivity(), "DBmoney", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        double ingT = 0, ingP = 0, gasT = 0, gasP = 0;
        int mMonth = ((Datos) getActivity().getApplication()).getMonth();
        int mYear = ((Datos) getActivity().getApplication()).getYear();

        Log.d("TAG69", String.valueOf(mMonth) + "----" + String.valueOf(mYear));


        //----Total General Ingreso--------------------------------------
        Cursor fila = bd.rawQuery("SELECT cantidad " +
                "FROM Ingresos",null);
        while (fila.moveToNext())
        {
            ingT += Double.parseDouble(fila.getString(0));
        }
        //----Total Periodo Ingreso--------------------------------------
        Cursor filaPer = bd.rawQuery("SELECT cantidad " +
                "FROM Ingresos " +
                "WHERE strftime('%m', fecha) = '"+mMonth+"' AND strftime('%Y', fecha) = '"+mYear+"'",null);
        while (filaPer.moveToNext())
        {
            ingP += Double.parseDouble(filaPer.getString(0));
        }

        //----Total General Gasto--------------------------------------
        Cursor filaGT = bd.rawQuery("SELECT cantidad " +
                "FROM Gastos",null);
        while (filaGT.moveToNext())
        {
            gasT += Double.parseDouble(filaGT.getString(0));
        }
        //----Total Periodo Ingreso--------------------------------------
        Cursor filaPerG = bd.rawQuery("SELECT cantidad " +
                "FROM Gastos " +
                "WHERE strftime('%m', fecha) = '"+mMonth+"' AND strftime('%Y', fecha) = '"+mYear+"'",null);
        while (filaPerG.moveToNext())
        {
            gasP += Double.parseDouble(filaPerG.getString(0));
        }

        ((Datos) getActivity().getApplication()).setTotal(ingT-gasT);
        ((Datos) getActivity().getApplication()).setIngresos(ingP);
        ((Datos) getActivity().getApplication()).setGastos(gasP);


        Double total = ((Datos) getActivity().getApplication()).getTotal();
        Double ingresos = ((Datos) getActivity().getApplication()).getIngresos();
        Double gastos = ((Datos) getActivity().getApplication()).getGastos();
        Double totalPer = ingresos - gastos;
        txtTotal.setText(String.format("%.2f", total)+getString(R.string.moneda));
        txtTotalPer.setText(String.format("%.2f", totalPer)+getString(R.string.moneda));
        txtIngreso.setText(String.format("%.2f", ingP)+getString(R.string.moneda));
        txtGasto.setText(String.format("%.2f", gasP)+getString(R.string.moneda));

        //------------------Asignar Fecha------------------
        today = ((Datos) getActivity().getApplication()).getToday();
        txtDate.setText(today);
        txtDate2.setText(today);

        generarLista();

        bd.close();
    }

//----------------------Generar Lista---------------------------------------------
    public void generarLista(){
        lista = binding.list;
        lista2 = binding.list2;
        AdminDB admin = new AdminDB(getActivity(), "DBmoney", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        int mMonth = ((Datos) getActivity().getApplication()).getMonth();
        int mYear = ((Datos) getActivity().getApplication()).getYear();

        //--------------Lista Gastos-----------------

        List<String> cantidad = new ArrayList<String>();
        List<String> faCG = new ArrayList<String>();
        List<Integer> idG = new ArrayList<Integer>();
        Cursor filaPerG = bd.rawQuery("SELECT id, cantidad, categoria, fecha " +
                "FROM Gastos " +
                "WHERE strftime('%m', fecha) = '"+mMonth+"' AND strftime('%Y', fecha) = '"+mYear+"'",null);
        while (filaPerG.moveToNext())
        {
            idG.add(Integer.parseInt(filaPerG.getString(0)));
            cantidad.add(filaPerG.getString(1)+getString(R.string.moneda));
            faCG.add(filaPerG.getString(2)+" | " + filaPerG.getString(3));

        }
        MyListView adapter=new MyListView(getActivity(), idG, cantidad, faCG, R.drawable.baseline_arrow_downward_24);

        int itemcount = adapter.getCount();
        ViewGroup.LayoutParams params = lista.getLayoutParams();
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int dps =(itemcount*70);
        int pixels = (int) (dps * scale + 0.5f);
        params.height = pixels;
        lista.setLayoutParams(params);
        lista.setAdapter(adapter);

        //--------------Lista Ingresos-----------------

        List<String> cantidadI = new ArrayList<String>();
        List<String> faCI = new ArrayList<String>();
        List<Integer> idI = new ArrayList<Integer>();
        Cursor filaPerI = bd.rawQuery("SELECT id, cantidad, categoria, fecha " +
                "FROM Ingresos " +
                "WHERE strftime('%m', fecha) = '"+mMonth+"' AND strftime('%Y', fecha) = '"+mYear+"'",null);
        while (filaPerI.moveToNext())
        {
            idI.add(Integer.parseInt(filaPerI.getString(0)));
            cantidadI.add(filaPerI.getString(1)+getString(R.string.moneda));
            faCI.add(filaPerI.getString(2)+" | " + filaPerI.getString(3));

        }
        MyListView adapter2=new MyListView(getActivity(), idI, cantidadI, faCI, R.drawable.baseline_arrow_upward_24);

        int itemcount2 = adapter2.getCount();
        ViewGroup.LayoutParams params2 = lista2.getLayoutParams();
        int dps2 =(itemcount2*70);
        int dpsL = (dps2+dps+400);
        int pixels2 = (int) (dps2 * scale + 0.5f);
        int pixelsL = (int) (dpsL * scale + 0.5f);
        ViewGroup.LayoutParams paramsL = layL.getLayoutParams();

        params2.height =pixels2;
        paramsL.height = pixelsL;
        layL.setLayoutParams(paramsL);
        lista2.setLayoutParams(params2);
        lista2.setAdapter(adapter2);
    }
    private void closeFAB(){
        mAddGas.hide();
        mAddIng.hide();
        addGasActionText.setVisibility(View.GONE);
        addIngActionText.setVisibility(View.GONE);

        // Set the FAB to shrink after user
        // closes all the sub FABs
        mAdd.shrink();

        // make the boolean variable false
        // as we have set the sub FABs
        // visibility to GONE
        isAllFabsVisible = false;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_principal, menu) ;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.nav_date)
        {
            // calender class's instance and get current date , month and year from calender
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
            // date picker dialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            String formatDate =
                                    (((Datos) getActivity().getApplication()).getMonthForInt(monthOfYear+1).substring(0, 3).toUpperCase() +
                                            "/" + year);
                            String tDate = (year + "-" +
                                    String.format("%02d", monthOfYear+1) +
                                    "-" + String.format("%02d", dayOfMonth));
                            ((Datos) getActivity().getApplication()).setValDate(tDate);
                            ((Datos) getActivity().getApplication()).setMonth(monthOfYear+1);
                            ((Datos) getActivity().getApplication()).setYear(year);
                            ((Datos) getActivity().getApplication()).setToday(formatDate);

                            actualizarTotal();
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();

        }
        else if(item.getItemId() == R.id.nav_settings)
        {
            Intent oVentana = new Intent(getActivity(), Ajustes.class);
            oVentana.putExtra("Action", "Conf");
            startActivityForResult(oVentana, 777);
        }
        else return super.onOptionsItemSelected(item);
        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        actualizarTotal();
        generarLista();

    }
}