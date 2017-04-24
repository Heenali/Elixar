package com.elixar.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elixar.Method.Doctortype_method;
import com.elixar.R;

import java.util.ArrayList;

public class searchtype_Adapter extends ArrayAdapter<Doctortype_method> {
    ArrayList<Doctortype_method> actorList;
    LayoutInflater vi;
    Context context;

    public searchtype_Adapter(Context context, ArrayList<Doctortype_method> objects) {
        super(context, R.layout.list_doctordetail, objects);
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

            rowView = vi.inflate(R.layout.list_doctordetail, null);
            setViewHolder(rowView);
        } else {
            rowView = convertView;

        }

        vh = (ViewHolder) rowView.getTag();
        vh.lessmove_layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"moving less",Toast.LENGTH_LONG).show();
            }
        });
        vh.moremove_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Toast.makeText(getContext(),"moving more",Toast.LENGTH_LONG).show();
            }
        });
        // vh.title.setText(Html.fromHtml(actorList.get(position).gettitle()));
        // vh.subtitle.setText(Html.fromHtml(actorList.get(position).getsubtitle()));
        //  vh.desc.setText(Html.fromHtml(actorList.get(position).getdate()));

        // String image=actorList.get(position).getid();
        // UrlImageViewHelper.setUrlDrawable(vh.dimage, image.toString(), R.drawable.no_img);

        return rowView;

    }

    static class ViewHolder
    {

        public TextView title, subtitle, desc;
        ImageView dimage;
        LinearLayout lessmove_layout,moremove_layout;
    }

    private void setViewHolder(View rowView)
    {
        ViewHolder vh = new ViewHolder();

        vh.title = (TextView) rowView.findViewById(R.id.desc1);
        vh.subtitle = (TextView) rowView.findViewById(R.id.desc2);
        vh.desc = (TextView) rowView.findViewById(R.id.desc3);
        vh.dimage = (ImageView) rowView.findViewById(R.id.imag);
        vh.lessmove_layout = (LinearLayout) rowView.findViewById(R.id.lessmove_layout);
        vh.moremove_layout = (LinearLayout) rowView.findViewById(R.id.moremove_layout);


        rowView.setTag(vh);

    }


}