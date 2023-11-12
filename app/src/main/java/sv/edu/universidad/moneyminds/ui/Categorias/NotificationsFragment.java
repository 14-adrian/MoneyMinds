package sv.edu.universidad.moneyminds.ui.Categorias;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import sv.edu.universidad.moneyminds.AdminDB;
import sv.edu.universidad.moneyminds.Datos;
import sv.edu.universidad.moneyminds.R;
import sv.edu.universidad.moneyminds.databinding.FragmentNotificationsBinding;
import sv.edu.universidad.moneyminds.ui.ViewPagerAdapter;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    FloatingActionButton mAddGas, mAddIng;
    ExtendedFloatingActionButton mAdd;
    TextView addGasActionText, addIngActionText;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    Boolean isAllFabsVisible;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel dashboardViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);


        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        mAdd = binding.snAdd1;
        // FAB button
        mAddGas = binding.btnGas1;
        mAddIng = binding.btnIng1;
        addIngActionText = binding.ingActionText1;
        addGasActionText = binding.gasActionText1;

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

                            // Now extend the parent FAB, as
                            // user clicks on the shrinked
                            // parent FAB
                            mAdd.extend();

                            // make the boolean variable true as
                            // we have set the sub FABs
                            // visibility to GONE
                            isAllFabsVisible = true;

                        } else {

                            // when isAllFabsVisible becomes
                            // true make all the action name
                            // texts and FABs GONE.
                            mAddGas.hide();
                            mAddIng.hide();
                            addGasActionText.setVisibility(View.GONE);
                            addIngActionText.setVisibility(View.GONE);


                            mAdd.shrink();
                            isAllFabsVisible = false;
                        }
                    }
                });
        mAddIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                // Get the layout inflater
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                final View customV = inflater.inflate(R.layout.dialog_add_cat, null);
                EditText txtNombre = customV.findViewById(R.id.edNombre);
                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                builder.setTitle(R.string.agregar_ingreso);
                builder.setView(customV)
                        // Add action buttons
                        .setPositiveButton(R.string.agregar, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                AdminDB admin = new AdminDB(getActivity(), "DBmoney", null, 1);
                                SQLiteDatabase bd = admin.getWritableDatabase();
                                String nombre = txtNombre.getText().toString();
                                ContentValues registro = new ContentValues();
                                registro.put("nombre", nombre);
                                bd.insert("CatIngresos", null, registro);
                                bd.close();
                                defSize();
                                refrescar();
                                Toast.makeText(getActivity(), "Categoria Agregada con exito",
                                        Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                builder.create();
                builder.show();
            }
        });
        mAddGas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
                // Get the layout inflater
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                final View customV = inflater.inflate(R.layout.dialog_add_cat, null);
                EditText txtNombre = customV.findViewById(R.id.edNombre);
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
                                String nombre = txtNombre.getText().toString();
                                ContentValues registro = new ContentValues();
                                registro.put("nombre", nombre);
                                bd.insert("CatGastos", null, registro);
                                bd.close();
                                defSize();
                                refrescar();

                                Toast.makeText(getActivity(), "Categoria Agregada con exito",
                                        Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                builder.create();
                builder.show();
            }
        });
        setUpView();
        setUpViewPagerAdapter();

        return root;
    }

    private void setUpView(){
        tabLayout = binding.tabLayout;
        viewPager = binding.viewPager;
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager() );
    }
    private void defSize(){
        AdminDB admin = new AdminDB(getActivity(), "DBmoney", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        int mMonth = ((Datos) getActivity().getApplication()).getMonth();
        int mYear = ((Datos) getActivity().getApplication()).getYear();
        List<String> catG = new ArrayList<String>();
        Cursor filaPerG = bd.rawQuery("SELECT * " +
                "FROM CatIngresos ",null);
        while (filaPerG.moveToNext())
        {
            catG.add(filaPerG.getString(1));
        }
        Cursor filaPerI = bd.rawQuery("SELECT * " +
                "FROM CatGastos ",null);
        while (filaPerI.moveToNext())
        {
            catG.add(filaPerI.getString(1));
        }
        ViewGroup.LayoutParams paramsM = viewPager.getLayoutParams();
        int dps= ((catG.size()*50)+350);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        paramsM.height = pixels;
        viewPager.setLayoutParams(paramsM);
    }
    private void setUpViewPagerAdapter(){
        viewPagerAdapter.addFragment(new catGastos(), "Gastos");
        viewPagerAdapter.addFragment(new catIngresos(), "Ingresos");
        viewPager.setAdapter(viewPagerAdapter);
        defSize();


        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        Log.d("TAG1", "Posicion: " + tabLayout.getSelectedTabPosition() + " Titulo: " + viewPagerAdapter.getPageTitle(tabLayout.getSelectedTabPosition()));
                        break;
                    case 1:
                        Log.d("TAG1", "Posicion: " + tabLayout.getSelectedTabPosition() + " Titulo: " + viewPagerAdapter.getPageTitle(tabLayout.getSelectedTabPosition()));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        tabLayout = null;
        viewPager = null;
        viewPagerAdapter = null;
    }
    public void refrescar(){
        viewPager = null;
        viewPagerAdapter = null;
        mAddGas.hide();
        mAddIng.hide();
        addGasActionText.setVisibility(View.GONE);
        addIngActionText.setVisibility(View.GONE);
        mAdd.shrink();
        isAllFabsVisible = false;

        setUpView();
        setUpViewPagerAdapter();
    }
}