package com.cdhxqh.inventorymovement.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdhxqh.inventorymovement.R;
import com.cdhxqh.inventorymovement.model.Inventory;
import com.cdhxqh.inventorymovement.ui.CInvbalancesActivity;
import com.cdhxqh.inventorymovement.ui.detailsUi.InvDetailsActivity;

import java.util.ArrayList;

/**
 * Created by apple on 15/6/4.
 * 库存使用情况
 */
public class InvAdapter extends RecyclerView.Adapter<InvAdapter.ViewHolder> {

    private static final String TAG = "InvAdapter";
    Context mContext;
    ArrayList<Inventory> mInventorys = new ArrayList<Inventory>();

    private int mark; //库存情况/库存盘点
    public InvAdapter(Context context,int mark) {
        mContext = context;
        this.mark=mark;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        final Inventory inv = mInventorys.get(i);

        viewHolder.itemNum.setText(inv.itemnum);
        viewHolder.itemDesc.setText(inv.itemdesc);

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                if(mark==1){
                    intent.setClass(mContext, InvDetailsActivity.class);
                }else{
                    intent.setClass(mContext, CInvbalancesActivity.class);
                }


                Bundle bundle = new Bundle();
                bundle.putSerializable("inventory", inv);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mInventorys.size();
    }

    public void update(ArrayList<Inventory> data, boolean merge) {
        if (merge && mInventorys.size() > 0) {
            for (int i = 0; i < mInventorys.size(); i++) {
                Log.i(TAG, "mItems=" + mInventorys.get(i).itemnum);
                Inventory obj = mInventorys.get(i);
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
        mInventorys = data;

        notifyDataSetChanged();
    }

    public void adddate(ArrayList<Inventory> data){
        if(data.size()>0){
            for(int i = 0;i < data.size();i++){
                if(!mInventorys.contains(data.get(i))){
                    mInventorys.add(data.get(i));
                }
            }
        }
        notifyDataSetChanged();
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

        public ViewHolder(View view) {
            super(view);
            cardView = (RelativeLayout) view.findViewById(R.id.card_container);
            itemNum = (TextView) view.findViewById(R.id.item_num_text);
            itemDesc = (TextView) view.findViewById(R.id.item_desc_text);
        }
    }
}
