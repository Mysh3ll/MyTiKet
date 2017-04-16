package mysh3ll.loginapi;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.List;

import mysh3ll.loginapi.POJO.CodeUnique.QrCode;

/**
 * Created by Michel on 09/02/2017.
 */

public class MyCodeUniqueAdapter extends ArrayAdapter<QrCode> {

    List<QrCode> codeUniqueList;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public MyCodeUniqueAdapter(Context context, List<QrCode> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        codeUniqueList = objects;
    }

    @Override
    public QrCode getItem(int position) {
        return codeUniqueList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_row_view_code_unique, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        QrCode item = getItem(position);

        vh.textViewTitle.setText("Numéro de place :");
        vh.textViewNumPlace.setText(item.getNumPlace());
        // metrre qrcode ici
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try{
            BitMatrix bitMatrix = multiFormatWriter.encode(item.getCodeUnique(), BarcodeFormat.QR_CODE,250,250);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            vh.imageView.setImageBitmap(bitmap);
        }
        catch (WriterException e){
            e.printStackTrace();
        }

        return vh.rootView;
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;
        public final ImageView imageView;
        public final TextView textViewTitle;
        public final TextView textViewNumPlace;

        private ViewHolder(RelativeLayout rootView, ImageView imageView, TextView textViewTitle, TextView textViewNumPlace) {
            this.rootView = rootView;
            this.imageView = imageView;
            this.textViewTitle = textViewTitle;
            this.textViewNumPlace = textViewNumPlace;
        }

        public static ViewHolder create(RelativeLayout rootView) {
            ImageView imageView = (ImageView) rootView.findViewById(R.id.imageViewQrCode);
            TextView textViewTitle = (TextView) rootView.findViewById(R.id.textViewTitle);
            TextView textViewNumPlace = (TextView) rootView.findViewById(R.id.textViewNumPlace);
            return new ViewHolder(rootView, imageView, textViewTitle, textViewNumPlace);
        }
    }
}
