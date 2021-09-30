package com.example.p.helpers;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.p.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ListViewHelper extends ArrayAdapter<String> {

    private ArrayList<String> Name;
    private ArrayList<String> Description;

    private Activity context;
    public ListViewHelper(Activity context, ArrayList<String> Name,ArrayList<String> description)
    {
        super(context, R.layout.listdata,Name);
        this.context = context;
        this.Name = Name;
        this.Description = description;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;
        if(r==null)
        {

            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.listdata,null,true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) r.getTag();
        }
        viewHolder.name.setText(Name.get(position));
        viewHolder.desc.setText(Description.get(position));

        return r;
    }

    class ViewHolder
    {
        TextView name;
        TextView desc;
        ViewHolder(View v)
        {
            name = v.findViewById(R.id.captionDesc);
            desc = v.findViewById(R.id.descriptionId);
        }
    }
}
