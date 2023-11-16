package sv.edu.universidad.moneyminds;

import android.app.Activity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MyListView extends ArrayAdapter<Integer> {

    private final Activity context;
    private final List<String> maintitle;
    private final List<String> subtitle;
    private final Integer imgid;
    private final List<Integer> idD;

    public MyListView(Activity context, List<Integer> idD,List<String> maintitle,List<String> subtitle, Integer imgid) {
        super(context, R.layout.mylist, idD);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.idD = idD;
        this.maintitle=maintitle;
        this.subtitle=subtitle;
        this.imgid=imgid;

    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.mylist, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView subtitleText = (TextView) rowView.findViewById(R.id.subtitle);
        TextView idView = (TextView) rowView.findViewById(R.id.idDato);

        titleText.setText(maintitle.get(position));
        imageView.setImageResource(imgid);
        subtitleText.setText(subtitle.get(position));
        idView.setText(idD.get(position).toString());

        return rowView;

    };
}
