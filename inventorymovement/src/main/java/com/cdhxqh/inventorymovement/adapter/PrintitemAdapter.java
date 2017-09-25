package com.cdhxqh.inventorymovement.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdhxqh.inventorymovement.R;
import com.cdhxqh.inventorymovement.model.Printitem;
import com.cdhxqh.inventorymovement.ui.PrintPolineDetailActivity;

import java.util.ArrayList;

/**
 * Created by think on 15/12/16.
 */
public class PrintitemAdapter extends RecyclerView.Adapter<PrintitemAdapter.ViewHolder> {

    private static final String TAG = "PolineAdapter";
    Context mContext;
    String ponum;
    ArrayList<Printitem> mPos = new ArrayList<Printitem>();
//    V2EXDataSource mDataSource = Application.getDataSource();

    public PrintitemAdapter(Context context, String ponum) {
        mContext = context;
        this.ponum = ponum;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_check, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Printitem poline = mPos.get(i);


        viewHolder.itemNum.setText(poline.itemnum);
        viewHolder.itemDesc.setText(poline.description);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PrintPolineDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("printitem", poline);
//                bundle.putString("ponum", ponum);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                poline.ischeck = isChecked;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mPos.size();
    }

    public void update(ArrayList<Printitem> data, boolean merge) {
        if (merge && mPos.size() > 0) {
            for (int i = 0; i < mPos.size(); i++) {
                Printitem obj = mPos.get(i);
                boolean exist = false;
                for (int j = 0; j < data.size(); j++) {
                    if (data.get(j).itemnum == obj.itemnum) {
                        exist = true;
                        break;
                    }
                }
                if (exist) continue;
                data.add(obj);
            }
        }
        mPos = data;

        notifyDataSetChanged();
    }

    public void adddate(ArrayList<Printitem> data) {
        if (data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                if (!mPos.contains(data.get(i))) {
                    mPos.add(data.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    public void update(Printitem po) {
        for (int i = 0; i < mPos.size(); i++) {
            if (mPos.get(i).itemnum.equals(po.itemnum)) {
                mPos.set(i, po);
            }
        }
        notifyDataSetChanged();
    }

    public ArrayList<Printitem> getChecked(){
        ArrayList<Printitem> printitems = new ArrayList<>();
        for (Printitem poline : mPos){
            if (poline.ischeck){
                printitems.add(poline);
            }
        }
        return printitems;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * CardView*
         */
        public RelativeLayout cardView;

        /**
         * 编号*
         */
        public TextView itemNum;
        /**
         * 描述*
         */
        public TextView itemDesc;

        /**
         * 勾选框
         */
        public CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            cardView = (RelativeLayout) view.findViewById(R.id.card_container);
            itemNum = (TextView) view.findViewById(R.id.item_num_text);
            itemDesc = (TextView) view.findViewById(R.id.item_desc_text);
            checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        }
    }
}
