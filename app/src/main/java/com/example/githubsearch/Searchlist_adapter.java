package com.example.githubsearch;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class Searchlist_adapter extends BaseAdapter {
    Context context;
    List<Search_list> valueList;
    static int position = 0;
    MainActivity mainActivity;
    ViewGit_list finalViewItem1;



    public Searchlist_adapter(List<Search_list> listValue, Context context, MainActivity mainActivity) {
        this.context = context;
        this.valueList = listValue;
        this.mainActivity = mainActivity;
    }





    @Override
    public int getCount() {
        return this.valueList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.valueList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getViewTypeCount() {

        if (getCount() > 0) {
            return getCount();
        } else {
            return super.getViewTypeCount();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewGit_list viewGitList = null;
        Searchlist_adapter.position=position;

        if(convertView == null)
        {
            viewGitList = new ViewGit_list();
            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInfiater.inflate(R.layout.custom_item, null);
            viewGitList.title = convertView.findViewById(R.id.name);
            viewGitList.description = convertView.findViewById(R.id.description);
            viewGitList.language = convertView.findViewById(R.id.language);
            viewGitList.license = convertView.findViewById(R.id.license);
            viewGitList.rating = convertView.findViewById(R.id.rating);

            convertView.setTag(viewGitList);

            viewGitList.title.setText(valueList.get(position).fullname);
            viewGitList.description.setText(valueList.get(position).description);
            viewGitList.language.setText(valueList.get(position).language);
            viewGitList.license.setText(valueList.get(position).license);
            viewGitList.rating.setText(valueList.get(position).rating);

            finalViewItem1 = viewGitList;
            ViewGit_list finalViewItem = viewGitList;

        }
        else
        {
            viewGitList = (ViewGit_list) convertView.getTag();
        }
        return convertView;
    }

}

class ViewGit_list {
    TextView title;
    TextView description;
    TextView language;
    TextView license;
    TextView rating;
}

