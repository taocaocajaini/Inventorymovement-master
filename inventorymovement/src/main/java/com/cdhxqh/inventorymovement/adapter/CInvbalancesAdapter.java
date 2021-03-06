package com.cdhxqh.inventorymovement.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdhxqh.inventorymovement.R;
import com.cdhxqh.inventorymovement.model.Invbalances;
import com.cdhxqh.inventorymovement.utils.MessageUtils;

import java.util.ArrayList;

/**
 * Created by apple on 15/12/12.
 * 库存余量
 */
public class CInvbalancesAdapter extends RecyclerView.Adapter<CInvbalancesAdapter.ViewHolder> {

    private static final String TAG = "CInvbalancesAdapter";
    Context mContext;
    ArrayList<Invbalances> mItems = new ArrayList<Invbalances>();
    private String location; //位置

    public cOnClickListener cOnClickListener;

    public CInvbalancesAdapter(Context context, String location) {
        this.mContext = context;
        this.location = location;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pinvbalances_list_item, viewGroup, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        final Invbalances item = mItems.get(position);
        viewHolder.binnumText.setText(item.binnum);
        viewHolder.lotnumText.setText(item.lotnum);
        if (!item.curbal.equals("0")) {
            viewHolder.curbalText.setText(item.curbal);
        }

        viewHolder.invtypeText.setText(item.kctypedesc);

        viewHolder.qrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (viewHolder.curbalText.getText().toString().equals("")) {
                    MessageUtils.showMiddleToast(mContext, "请输入数量");
                } else {
                    item.setCurbal(viewHolder.curbalText.getText().toString());
                    cOnClickListener.cOnClickListener(item);
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void update(ArrayList<Invbalances> data, boolean merge) {
        if (merge && mItems.size() > 0) {
            for (int i = 0; i < mItems.size(); i++) {
                Invbalances obj = mItems.get(i);
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
        mItems = data;

        notifyDataSetChanged();
    }

    public void adddate(ArrayList<Invbalances> data) {
        if (data.size() > 0) {
            for (int i = 0; i < data.size(); i++) {
                if (!mItems.contains(data.get(i))) {
                    mItems.add(data.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    public void removeAllData() {
        if (mItems.size() > 0) {
            mItems.removeAll(mItems);
        }
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * CardView*
         */
        public RelativeLayout cardView;

        public TextView binnumText;//货柜
        public TextView lotnumText;//批次
        public TextView curbalText;//盘点数量
        public TextView invtypeText;//库存类别
        public Button qrButton; //确认按钮


        public ViewHolder(View view) {
            super(view);
            cardView = (RelativeLayout) view.findViewById(R.id.card_container);
            binnumText = (TextView) view.findViewById(R.id.invbalances_binnum_text);
            lotnumText = (TextView) view.findViewById(R.id.invbalances_lotnum_text);
            curbalText = (TextView) view.findViewById(R.id.invbalances_curbal_text);
            invtypeText = (TextView) view.findViewById(R.id.invbalances_invtype_text);
            qrButton = (Button) view.findViewById(R.id.submit_button_id);

        }
    }


    public interface cOnClickListener {
        public void cOnClickListener(Invbalances invbalances);
    }

    public CInvbalancesAdapter.cOnClickListener getcOnClickListener() {
        return cOnClickListener;
    }

    public void setcOnClickListener(CInvbalancesAdapter.cOnClickListener cOnClickListener) {
        this.cOnClickListener = cOnClickListener;
    }
}
