package com.ecjia.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.ecmoban.android.sijiqing.R;
import com.ecjia.view.activity.HelpWebActivity;
import com.ecjia.entity.responsemodel.ARTICLE;
import com.ecjia.entity.responsemodel.SHOPHELP;

import java.util.ArrayList;


public class HelpNewAdapter extends BaseAdapter implements
        MyStickyListHeadersAdapter, SectionIndexer {

    private final Context mContext;
    private ArrayList<ARTICLE> articles = new ArrayList<>();
    private int[] mSectionIndices;
    private String[] mSectionLetters;
    private LayoutInflater mInflater;

    public HelpNewAdapter(Context context, ArrayList<SHOPHELP> helpLists) {
        mContext = context;
        mInflater = LayoutInflater.from(context);

        if (helpLists != null && helpLists.size() > 0) {
            for (int i = 0; i < helpLists.size(); i++) {
                articles.addAll(helpLists.get(i).article);
            }
        }
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
    }

    private int[] getSectionIndices() {
        ArrayList<Integer> sectionIndices = new ArrayList<Integer>();
        String lastFirstChar = articles.get(0).getParentName();
        sectionIndices.add(0);
        for (int i = 1; i < articles.size(); i++) {
            if (articles.get(i).getParentName().equals(lastFirstChar)) {
                lastFirstChar = articles.get(i).getParentName();
                sectionIndices.add(i);
            }
        }
        int[] sections = new int[sectionIndices.size()];
        for (int i = 0; i < sectionIndices.size(); i++) {
            sections[i] = sectionIndices.get(i);
        }
        return sections;
    }

    private String[] getSectionLetters() {
        String[] letters = new String[mSectionIndices.length];
        for (int i = 0; i < mSectionIndices.length; i++) {
            letters[i] = articles.get(mSectionIndices[i]).getParentName();
        }
        return letters;
    }

    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public Object getItem(int position) {
        return articles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.my_helpcell, parent, false);
            holder.shophelp_item = (LinearLayout) convertView.findViewById(R.id.shophelp_item);
            holder.shophelp_content = (TextView) convertView.findViewById(R.id.shophelp_content);
            holder.middlelinetop = convertView.findViewById(R.id.help_middle_line_top);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.shophelp_content.setText(articles.get(position).getShort_title());
        holder.shophelp_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HelpWebActivity.class);
                intent.putExtra("id", articles.get(position).getId());
                intent.putExtra("title", articles.get(position).getTitle());
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;

        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.header_new_help, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        // set header text as first char in name
        String headerChar = articles.get(position).getParentName();
        holder.text.setText(headerChar);

        return convertView;
    }

    @Override
    public String getHeaderId(int position, int a) {
        return articles.get(position).getParentName();
    }

    /**
     * Remember that these have to be static, postion=1 should always return
     * the same Id that is.
     */
    @Override
    public long getHeaderId(int position) {
        // return the first character of the country as ID because this is what
        // headers are based upon
        return articles.get(position).getParentName().subSequence(0, 1).charAt(0);
    }

    @Override
    public int getPositionForSection(int section) {
        if (mSectionIndices.length == 0) {
            return 0;
        }

        if (section >= mSectionIndices.length) {
            section = mSectionIndices.length - 1;
        } else if (section < 0) {
            section = 0;
        }
        return mSectionIndices[section];
    }

    @Override
    public int getSectionForPosition(int position) {
        for (int i = 0; i < mSectionIndices.length; i++) {
            if (position < mSectionIndices[i]) {
                return i - 1;
            }
        }
        return mSectionIndices.length - 1;
    }

    @Override
    public Object[] getSections() {
        return mSectionLetters;
    }

    public void clear() {
        articles.clear();
        mSectionIndices = new int[0];
        mSectionLetters = new String[0];
        notifyDataSetChanged();
    }

    public void restore() {
        articles.clear();
        mSectionIndices = getSectionIndices();
        mSectionLetters = getSectionLetters();
        notifyDataSetChanged();
    }

    class HeaderViewHolder {
        TextView text;
    }

    class ViewHolder {
        TextView shophelp_content;
        LinearLayout shophelp_item;
        View firstline, endline, middlelinetop;
    }

}