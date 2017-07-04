package com.cashlog.CustomAdapters;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cashlog.DbHelper.BudgetsDbHelper;
import com.cashlog.Entities.Expenses;
import com.cashlog.R;

import java.util.ArrayList;
/**
 * Created by Nahid on 24.11.2016.
 */
public class ExpenseBaseAdapter extends BaseAdapter {
    ArrayList<Expenses> e = new ArrayList<>();
    Context context;
    LayoutInflater layoutInflater;
    public int getCount() {
        return e.size();
    }
    private SparseBooleanArray mSelectedItemsIds;
    public ExpenseBaseAdapter(Context context, ArrayList<Expenses> e) {
        this.context = context;
        this.e = e;
        layoutInflater = LayoutInflater.from(this.context);
        mSelectedItemsIds  = new SparseBooleanArray();
    }

    public Expenses getItem(int i) {
        return e.get(i);
    }

    @Override
    public long getItemId(int i) {
        return e.get(i).getId();
    }

    @Override
    public View getView(int i, View view, final ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.activity_list_view, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) view.getTag();
        }
        final Expenses e = getItem(i);
        viewHolder.showPlace.setText(" "+e.getPayee());
        viewHolder.showAmount.setText(String.valueOf(e.getAmount())+" azn");
        viewHolder.showDate.setText(String.valueOf(e.getDate())+",");
        viewHolder.showTime.setText(String.valueOf(e.getTime()));
        viewHolder.showExpense.setText(String.valueOf(e.getCategory()));
        viewHolder.showNotes.setText(String.valueOf(e.getNote()));
        viewHolder.showRating.setProgress(((int) e.getRating()));

        return view;
    }
    private class ViewHolder{
        TextView showTime, showDate, showExpense, showPlace, showNotes, showAmount;
        RatingBar showRating;
        public ViewHolder(View view){
            showPlace= (TextView) view.findViewById(R.id.showPlace);
            showAmount = (TextView) view.findViewById(R.id.showAmount);
            showDate = (TextView) view.findViewById(R.id.showDate);
            showRating = (RatingBar) view.findViewById(R.id.showRating);
            showExpense= (TextView) view.findViewById(R.id.showExpense);
            showTime= (TextView) view.findViewById(R.id.showTime);
            showNotes= (TextView) view.findViewById(R.id.showNotes);

        }
    }
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));

    }
    public void remove(Expenses e, Context context){
        BudgetsDbHelper EDbH = new BudgetsDbHelper(context);
        EDbH.deleteExpense(e);
        notifyDataSetChanged();
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if(value) {
            mSelectedItemsIds.put(position, value);
        } else {
            mSelectedItemsIds.delete(position);
        }
        notifyDataSetChanged();
    }
    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

}
