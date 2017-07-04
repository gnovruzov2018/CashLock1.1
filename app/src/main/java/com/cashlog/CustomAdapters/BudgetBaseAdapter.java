package com.cashlog.CustomAdapters;
import android.content.Context;
import android.content.Intent;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import com.cashlog.DbHelper.BudgetsDbHelper;
import com.cashlog.Entities.Budgets;
import com.cashlog.MainActivity;
import com.cashlog.R;
import java.util.ArrayList;
import java.util.List;

public class BudgetBaseAdapter extends BaseAdapter {
    ArrayList<Budgets> b = new ArrayList<>();
    Context context;
    LayoutInflater layoutInflater;
    public int getCount() {
        return b.size();
    }
    private SparseBooleanArray mSelectedItemsIds;
    public BudgetBaseAdapter(Context context, ArrayList<Budgets> b) {
        this.context = context;
        this.b = b;
        layoutInflater = LayoutInflater.from(this.context);
        mSelectedItemsIds  = new SparseBooleanArray();
    }

    public Budgets getItem(int i) {
        return b.get(i);
    }

    @Override
    public long getItemId(int i) {
        if(b.get(i).getStatus()==1)
        select_pos= b.get(i).getBudget_id()-1;
        return b.get(i).getBudget_id();
    }
   int select_pos=0;
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        final BudgetBaseAdapter.ViewHolder viewHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.activity_budget_listview, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (BudgetBaseAdapter.ViewHolder) view.getTag();
        }
        final BudgetsDbHelper budgetsDbHelper = new BudgetsDbHelper(view.getContext());
        final List<Budgets> bb = budgetsDbHelper.getAllBudgets();
        if (bb.get(i).getStatus() == 1) {
            viewHolder.radioButton.setChecked(true);
        }
        viewHolder.radioButton.setText(" " + b.get(i).getBudget_name());
        viewHolder.radioButton.setClickable(true);
        viewHolder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (Budgets budgets : bb) {
                        budgets.setStatus(0);
                        budgetsDbHelper.updateBudgets(budgets);
                        budgetsDbHelper.close();
                    }
                    Budgets budgets = bb.get(i);
                    budgets.setStatus(1);
                    budgetsDbHelper.updateBudgets(budgets);
                    budgetsDbHelper.close();
                    Intent intent = new Intent(buttonView.getContext(), MainActivity.class);
                    buttonView.getContext().startActivity(intent);
                }
                notifyDataSetChanged();
            }
        });

        viewHolder.income.setText(String.valueOf(b.get(i).getOverall_income()));
        if (budgetsDbHelper.getTotalAm(b.get(i).getBudget_id()) == 0.0) {
            viewHolder.expense_budget.setText("0");
        } else{
            viewHolder.expense_budget.setText("-" + budgetsDbHelper.getTotalAm(b.get(i).getBudget_id()).toString());
    }
        return view;
    }
    private class ViewHolder{
        TextView income, expense_budget;
        RadioButton radioButton;
        public ViewHolder(View view){
//            name= (TextView) view.findViewById(R.id.text_budget_name);
            income = (TextView) view.findViewById(R.id.text_budget_income);
            radioButton = (RadioButton) view.findViewById(R.id.radioButton);
            expense_budget = (TextView) view.findViewById(R.id.text_budget_expense);
        }

    }
    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));

    }
    public void remove(Budgets b,Context context){
        BudgetsDbHelper BDbH = new BudgetsDbHelper(context);
        BDbH.deleteBudget(b);
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
