package com.elixar.Adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.elixar.Method.Doctortype_method;
import com.elixar.R;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import java.util.ArrayList;

public class Doctortype_Adapter extends ArrayAdapter<Doctortype_method> {
    ArrayList<Doctortype_method> actorList;
    LayoutInflater vi;
    Context context;

    public Doctortype_Adapter(Context context, ArrayList<Doctortype_method> objects) {
        super(context, R.layout.list_doctortype, objects);
        this.context = context;
        this.vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.actorList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convert view = design
        //View v = convertView;
        View rowView;
        ViewHolder vh;
        if (convertView == null) {

            rowView = vi.inflate(R.layout.list_doctortype, null);
            setViewHolder(rowView);
        } else {
            rowView = convertView;

        }
        vh = (ViewHolder) rowView.getTag();

        vh.title.setText(Html.fromHtml(actorList.get(position).gettitle()));
        vh.subtitle.setText(Html.fromHtml(actorList.get(position).getsubtitle()));
        vh.date.setText(actorList.get(position).getdate());

        String image=actorList.get(position).getid();
        UrlImageViewHelper.setUrlDrawable(vh.dimage, image.toString(), R.drawable.no_img);

        return rowView;

    }

    static class ViewHolder {

        public TextView title, subtitle, date;
        ImageView dimage;
    }

    private void setViewHolder(View rowView) {
        ViewHolder vh = new ViewHolder();

        vh.title = (TextView) rowView.findViewById(R.id.tvProfileName);
        vh.subtitle = (TextView) rowView.findViewById(R.id.tvdesc);
        vh.date = (TextView) rowView.findViewById(R.id.tvdatetime);
        vh.dimage = (ImageView) rowView.findViewById(R.id.ivProfilePic);
        rowView.setTag(vh);

    }


}