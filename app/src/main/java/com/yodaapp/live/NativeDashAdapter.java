package com.yodaapp.live;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yodaapp.live.model.MenuEntry;

import java.util.List;


public class NativeDashAdapter  extends ArrayAdapter<MenuEntry> implements CollectionViewCallbacks {

    protected final Context mContext;
    private Toast mCurrentToast;
    int TOKEN = 0x1;
    int TOKEN2 = 0x2;
    int TOKEN3 = 0x2;
    //private Communication_Staff_Home.Callbacks mCallbacks;

    public NativeDashAdapter(Context context, List<MenuEntry> objects) {
        super(context, 0, objects);
        mContext = context;
    }

    public CollectionView.Inventory getInventory(int grade_size,int branch_size ,int section_size, int set_size) {
        CollectionView.Inventory inventory = new CollectionView.Inventory();


        inventory.addGroup(new CollectionView.InventoryGroup(TOKEN)
                .setDisplayCols(set_size)
                .setItemCount(branch_size)
                .setHeaderLabel("School and branch")
                .setShowHeader(true));

        if(grade_size != 0) {
            inventory.addGroup(new CollectionView.InventoryGroup(TOKEN2)
                    .setDisplayCols(set_size)
                    .setItemCount(grade_size)
                    .setDataIndexStart(branch_size + 1)
                    .setHeaderLabel("Grade under you")
                    .setShowHeader(true));
        }

        if(section_size != 0) {
            inventory.addGroup(new CollectionView.InventoryGroup(TOKEN3)
                    .setDisplayCols(set_size)
                    .setItemCount(grade_size)
                    .setDataIndexStart(branch_size + grade_size + 1)
                    .setHeaderLabel("Section Under You")
                    .setShowHeader(true));
        }
        return inventory;
    }

    @Override
    public View newCollectionHeaderView(Context context, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_dashmenu, parent, false);
    }

    @Override
    public void bindCollectionHeaderView(Context context, View view, int groupId, String headerLabel) {
        ((TextView) view.findViewById(R.id.name)).setText(headerLabel);
    }

    @Override
    public View newCollectionItemView(Context context, int groupId, ViewGroup parent) {
        return newView(context, parent);
    }

    @Override
    public void bindCollectionItemView(Context context, View view, int groupId, int indexInGroup, int dataIndex, Object tag) {
        bindView(view, context, dataIndex);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }


    public View newView(Context context, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_dashmenu, parent, false);
        ViewHolder holder = new ViewHolder();
        assert view != null;
        holder.name = (TextView) view.findViewById(R.id.name);
        holder.description = (ImageButton) view.findViewById(R.id.description);
        view.setTag(holder);
        return view;
    }

    public View bindView(View view, final Context context, int position) {
        ViewHolder holder = (ViewHolder) view.getTag();
        final MenuEntry menuEntry = getItem(position);

        final String hashtag = menuEntry.titleId;
        holder.name.setText(hashtag);

        view.setBackgroundColor(mContext.getResources().getColor(menuEntry.colorId));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //MenyEntryUtils.openFragment(mContext, menuEntry);
                //mCallbacks.onTopicSelected(menuEntry, view);
            }
        });

        final String desc = menuEntry.descriptionId;

        if (!TextUtils.isEmpty(desc)) {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    displayDescription(view, desc);
                }
            });
        } else {
            holder.description.setVisibility(View.GONE);
        }

        return view;
    }

    private void displayDescription(View view, String desc) {
        hideDescriptionToast();
        mCurrentToast = Toast.makeText(mContext.getApplicationContext(), desc, Toast.LENGTH_LONG);
        mCurrentToast.show();
        if (Build.VERSION.SDK_INT >= 16) {
            view.announceForAccessibility(desc);
        }
    }

    public void hideDescriptionToast() {
        if (mCurrentToast != null) {
            mCurrentToast.cancel();
            mCurrentToast = null;
        }
    }

//    public void setCallbacks(Communication_Staff_Home.Callbacks callbacks) {
//        mCallbacks = callbacks;
//    }

    private static final class ViewHolder {
        TextView name;
        ImageButton description;
    }
}
