package com.musasyihab.easycontact.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.musasyihab.easycontact.R;
import com.musasyihab.easycontact.data.model.ContactModel;
import com.musasyihab.easycontact.util.Constants;
import com.musasyihab.easycontact.util.ContactSortComparator;

import java.util.Collections;
import java.util.List;

/**
 * Created by musasyihab on 9/20/17.
 */

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ContactItemViewHolder> {

    private List<ContactModel> list;
    private Context context;
    private ContactListListener mItemListener;

    public ContactListAdapter(List<ContactModel> list, ContactListListener mItemListener) {
        this.list = list;
        this.mItemListener = mItemListener;
    }

    @Override
    public ContactItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        context = parent.getContext();
        return new ContactItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactItemViewHolder holder, int i) {

        final ContactModel contact = list.get(i);

        holder.contactName.setText(contact.getFullname());

        if(i==0){
            holder.contactLabelLayout.setVisibility(View.VISIBLE);
            if(contact.isFavorite()) {
                holder.contactLabelStar.setVisibility(View.VISIBLE);
                holder.contactLabelTxt.setVisibility(View.INVISIBLE);
            } else {
                holder.contactLabelStar.setVisibility(View.INVISIBLE);
                holder.contactLabelTxt.setVisibility(View.VISIBLE);
                holder.contactLabelTxt.setText(contact.getFirstName().charAt(0)+"");
            }

        } else {
            char label = getLabel(i);
            if(label!=0 && !contact.isFavorite()){
                holder.contactLabelLayout.setVisibility(View.VISIBLE);
                holder.contactLabelStar.setVisibility(View.INVISIBLE);
                holder.contactLabelTxt.setVisibility(View.VISIBLE);
                holder.contactLabelTxt.setText(label+"");
            } else {
                holder.contactLabelLayout.setVisibility(View.INVISIBLE);
            }
        }

        String avatar = contact.getProfilePic();

        if(avatar!=null && !avatar.isEmpty() && !avatar.equals(Constants.EMPTY_AVATAR_URL)){
            holder.contactAvaImg.setVisibility(View.VISIBLE);
            holder.contactAvaTxt.setVisibility(View.GONE);

            Glide.with(context)
                    .load(avatar)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(holder.contactAvaImg);
        } else {
            holder.contactAvaImg.setVisibility(View.GONE);
            holder.contactAvaTxt.setVisibility(View.VISIBLE);

            holder.contactAvaTxt.setText(contact.getFirstName().charAt(0)+"");
        }

        holder.contactLayout.setOnClickListener(__ -> mItemListener.onContactClick(contact));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private char getLabel(int pos){
        if(pos>0){
            char currentFirstChar = list.get(pos).getFirstName().toUpperCase().charAt(0);
            char previousFirstChar = list.get(pos-1).getFirstName().toUpperCase().charAt(0);
            if(currentFirstChar!=previousFirstChar){
                return currentFirstChar;
            }
        }
        return 0;
    }

    public static class ContactItemViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout contactLayout;
        public FrameLayout contactLabelLayout;
        public ImageView contactLabelStar;
        public TextView contactLabelTxt;
        public TextView contactName;
        public TextView contactAvaTxt;
        public ImageView contactAvaImg;

        public ContactItemViewHolder(View rowView) {
            super(rowView);
            contactLayout = (LinearLayout) rowView.findViewById(R.id.contact_item_layout);
            contactLabelLayout = (FrameLayout) rowView.findViewById(R.id.contact_item_label_layout);
            contactLabelStar = (ImageView) rowView.findViewById(R.id.contact_item_label_star);
            contactLabelTxt = (TextView) rowView.findViewById(R.id.contact_item_label_initial);
            contactName = (TextView) rowView.findViewById(R.id.contact_item_name);
            contactAvaTxt = (TextView) rowView.findViewById(R.id.contact_item_ava_text);
            contactAvaImg = (ImageView) rowView.findViewById(R.id.contact_item_ava_img);
        }
    }

    public interface ContactListListener {
        void onContactClick(ContactModel clickedItem);
    }
}