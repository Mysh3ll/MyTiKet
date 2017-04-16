package mysh3ll.loginapi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import mysh3ll.loginapi.POJO.Order.Order;

/**
 * Created by Michel on 09/02/2017.
 */

public class MyOrderAdapter extends ArrayAdapter<Order> {

    List<Order> orderList;
    Context context;
    private LayoutInflater mInflater;
    private String BASE_URL = "http://www.mysh3ll.fr/web/uploads/event/images/";

    // Constructors
    public MyOrderAdapter(Context context, List<Order> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        orderList = objects;
    }

    @Override
    public Order getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_row_view_order, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Order item = getItem(position);

        vh.textViewTitle.setText(item.getTitre());
        vh.textViewDate.setText(item.getDate());
        Picasso.with(context).load(BASE_URL + item.getImage()).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher).into(vh.imageView);

        return vh.rootView;
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;
        public final ImageView imageView;
        public final TextView textViewTitle;
        public final TextView textViewDate;

        private ViewHolder(RelativeLayout rootView, ImageView imageView, TextView textViewTitle, TextView textViewDate) {
            this.rootView = rootView;
            this.imageView = imageView;
            this.textViewTitle = textViewTitle;
            this.textViewDate = textViewDate;
        }

        public static ViewHolder create(RelativeLayout rootView) {
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageView);
            TextView textViewTitle = (TextView) rootView.findViewById(R.id.textViewTitle);
            TextView textViewDate = (TextView) rootView.findViewById(R.id.textViewDate);
            return new ViewHolder(rootView, imageView, textViewTitle, textViewDate);
        }
    }
}
