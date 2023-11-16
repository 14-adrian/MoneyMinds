package sv.edu.universidad.moneyminds.ui.Info;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import sv.edu.universidad.moneyminds.AdminDB;
import sv.edu.universidad.moneyminds.Datos;
import sv.edu.universidad.moneyminds.R;
import sv.edu.universidad.moneyminds.databinding.FragmentDashboardBinding;
import sv.edu.universidad.moneyminds.ui.ViewPagerAdapter;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);




        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        setUpView();
        setUpViewPagerAdapter();





        //final TextView textView = binding.textDashboard;
        //dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    private void setUpView(){
        tabLayout = binding.tabLayout;
        viewPager = binding.viewPager;
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager() );
    }
    private void setUpViewPagerAdapter(){
        viewPagerAdapter.addFragment(new infoGastos(), getString(R.string.gastos));
        viewPagerAdapter.addFragment(new infoIngresos(), getString(R.string.ingresos));
        viewPager.setAdapter(viewPagerAdapter);

        AdminDB admin = new AdminDB(getActivity(), "DBmoney", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        int mMonth = ((Datos) getActivity().getApplication()).getMonth();
        int mYear = ((Datos) getActivity().getApplication()).getYear();
        List<String> catG = new ArrayList<String>();
        Cursor filaPerG = bd.rawQuery("SELECT sum(cantidad), categoria " +
                "FROM Gastos " +
                "WHERE strftime('%m', fecha) = '"+mMonth+"' " +
                "AND strftime('%Y', fecha) = '"+mYear+"'" +
                "GROUP BY categoria",null);
        while (filaPerG.moveToNext())
        {
            catG.add(filaPerG.getString(1));
        }
        Cursor filaPerI = bd.rawQuery("SELECT sum(cantidad), categoria " +
                "FROM Ingresos " +
                "WHERE strftime('%m', fecha) = '"+mMonth+"' " +
                "AND strftime('%Y', fecha) = '"+mYear+"'" +
                "GROUP BY categoria",null);
        while (filaPerI.moveToNext())
        {
            catG.add(filaPerI.getString(1));
        }
        ViewGroup.LayoutParams paramsM = viewPager.getLayoutParams();
        int dps= ((catG.size()*50)+380);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        paramsM.height = pixels;
        viewPager.setLayoutParams(paramsM);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        Log.d("TAG1", "Pos: " + tabLayout.getSelectedTabPosition() + " T: " + viewPagerAdapter.getPageTitle(tabLayout.getSelectedTabPosition()));
                        break;
                    case 1:
                        Log.d("TAG1", "Pos: " + tabLayout.getSelectedTabPosition() + " T: " + viewPagerAdapter.getPageTitle(tabLayout.getSelectedTabPosition()));
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
}