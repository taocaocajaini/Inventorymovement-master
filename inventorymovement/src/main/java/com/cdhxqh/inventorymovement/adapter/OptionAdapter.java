package com.cdhxqh.inventorymovement.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdhxqh.inventorymovement.R;
import com.cdhxqh.inventorymovement.model.Invtype;
import com.cdhxqh.inventorymovement.model.Option;
import com.cdhxqh.inventorymovement.ui.OptionActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by think on 2015/8/17.
 */
public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder> {
    OptionActivity activity;
    List<Option> optionList = new ArrayList<>();
    boolean isNoDesc = false;
    public OptionAdapter(OptionActivity activity) {
        this.activity = activity;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Option option = optionList.get(position);

        if (isNoDesc){
            holder.itemDescTitle.setVisibility(View.GONE);
            holder.itemDesc.setVisibility(View.GONE);
        }
        holder.itemNum.setText(option.getName());
        holder.itemDesc.setText(option.getDesc());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.responseData(optionList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public RelativeLayout relativeLayout;
        /**
         * CardView*
         */
        public RelativeLayout cardView;
        /**
         * 编号名称*
         */
        public TextView itemNumTitle;
        /**
         * 描述名称*
         */
        public TextView itemDescTitle;
        /**
         * 编号*
         */
        public TextView itemNum;
        /**
         * 描述*
         */
        public TextView itemDesc;

        public ViewHolder(View view) {
            super(view);
            cardView = (RelativeLayout) view.findViewById(R.id.card_container);

            itemNumTitle=(TextView) view.findViewById(R.id.item_num_title);
            itemDescTitle=(TextView) view.findViewById(R.id.item_desc_title);


            itemNum = (TextView) view.findViewById(R.id.item_num_text);
            itemDesc = (TextView) view.findViewById(R.id.item_desc_text);
        }
    }


    public void update(ArrayList<Option> data, boolean merge) {
        if (merge && optionList.size() > 0) {
            for (int i = 0; i < optionList.size(); i++) {
                Option workOrder = optionList.get(i);
                boolean exist = false;
                for (int j = 0; j < data.size(); j++) {
                    if (data.get(j) == workOrder) {
                        exist = true;
                        break;
                    }
                }
                if (exist) continue;
                data.add(workOrder);
            }
        }
        optionList = data;
        notifyDataSetChanged();
    }
//
    public void addPersonDate(ArrayList<Invtype> data){
        if(data.size()>0){
            Option option;
            for(int i = 0;i < data.size();i++){
                option = new Option();
                option.setName(data.get(i).getVALUE());
                option.setDesc(data.get(i).getDESCRIPTION());
                optionList.add(option);
            }
        }
        notifyDataSetChanged();
    }

}
