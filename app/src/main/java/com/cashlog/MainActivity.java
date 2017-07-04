package com.cashlog;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.cashlog.CustomAdapters.BudgetBaseAdapter;
import com.cashlog.CustomAdapters.ExpenseBaseAdapter;
import com.cashlog.DbHelper.BudgetsDbHelper;
import com.cashlog.Entities.Budgets;
import com.cashlog.Entities.Expenses;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.cashlog.R.id.container;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;
    TextView expense_budget;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final FloatingActionButton fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddTransaction.class);
                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BudgetsDbHelper db = new BudgetsDbHelper(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View alertLayout = inflater.inflate(R.layout.addbudget_dialog, null);
                final EditText income_amount = (EditText) alertLayout.findViewById(R.id.income_amount);
                final EditText income_name = (EditText) alertLayout.findViewById(R.id.income_name);
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("New Budget");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                });
                final AlertDialog.Builder builder = alert.setPositiveButton("Add Budget", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if (!income_amount.getText().toString().equals("") && !income_name.getText().toString().equals("")) {
                            try {
                                Toast.makeText(MainActivity.this, "You have " +  income_amount.getText().toString() + "azn.", Toast.LENGTH_SHORT).show();
                                Budgets b = new Budgets(income_name.getText().toString(), Double.parseDouble(income_amount.getText().toString()), 0);
                                db.addBudgets(b);

                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("budget","budgetChanged");
                                intent.putExtras(bundle);
                                startActivity(intent);
                            } catch (Exception e) {

                            }
                        } else {
                            try {
                                Toast.makeText(MainActivity.this, "No enough data", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                            }
                        }
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();


            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        fab.show();
                        fab1.hide();
                        break;
                    case 1:
                        fab.hide();
                        fab1.show();
                        break;
                    case 2:
                        fab.hide();
                        fab1.hide();
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            mViewPager.setCurrentItem(1);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(id ==R.id.delete_budget) {
            BudgetsDbHelper dbHelper = new BudgetsDbHelper(this);
            try {
                Budgets budgets = dbHelper.getBudgetByStatus();

                Toast.makeText(MainActivity.this, "You removed the budget", Toast.LENGTH_SHORT).show();
                List<Expenses> expensesList = dbHelper.getAllExpenseByBudget(budgets.getBudget_id());
                for(Expenses e :expensesList){
                    dbHelper.deleteExpense(e);
                }
                dbHelper.deleteBudget(budgets);
                dbHelper.close();
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("budget","budgetChanged");
                intent.putExtras(bundle);
                startActivity(intent);
            } catch (Exception e) {
            }
        }
        return super.onOptionsItemSelected(item);
    }
    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        PieChart mChart;
        BudgetsDbHelper db;

        public PlaceholderFragment() {
        }
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            db= new BudgetsDbHelper(getActivity());

          /*


*/


            if(getArguments().getInt(ARG_SECTION_NUMBER)==1) {
                final View rootView = inflater.inflate(R.layout.fragment_transactions, container, false);
                final BudgetsDbHelper DbHelper = new BudgetsDbHelper(rootView.getContext());
                //initialize search
                SearchView searchView = (SearchView) rootView.findViewById(R.id.search_expense);
                searchView.setVisibility(View.INVISIBLE);
                try {
                    Budgets budgets = DbHelper.getBudgetByStatus();
                    final List<Expenses> aa = (ArrayList) DbHelper.getAllExpenseByBudget(budgets.getBudget_id());

                final ListView listview  = (ListView) rootView.findViewById(R.id.listview);
                final ExpenseBaseAdapter myAdapter =new ExpenseBaseAdapter(rootView.getContext(),(ArrayList<Expenses>) aa);
                listview.setAdapter(myAdapter);
                    if(aa.isEmpty()){
                    }else {
                        searchView.setVisibility(View.VISIBLE);
                        //search starts from here
                        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                            @Override
                            public boolean onQueryTextSubmit(String query) {
                                return false;
                            }

                            @Override
                            public boolean onQueryTextChange(String newText) {
                                if (newText != null && !newText.isEmpty()) {
                                    List<Expenses> lstFound = new ArrayList<Expenses>();
                                    for (Expenses item : aa) {
                                        //it will search for category,where u paid,for date and note
                                        if (item.getCategory().contains(newText) || item.getPayee().contains(newText) || item.getDate().contains(newText) || item.getNote().contains(newText))
                                            lstFound.add(item);
                                    }
                                    ExpenseBaseAdapter myAdapter = new ExpenseBaseAdapter(rootView.getContext(), (ArrayList<Expenses>) lstFound);
                                    listview.setAdapter(myAdapter);
                                } else {
                                    //if search text is null
                                    //return default
                                    ExpenseBaseAdapter myAdapter = new ExpenseBaseAdapter(rootView.getContext(), (ArrayList<Expenses>) aa);
                                    listview.setAdapter(myAdapter);
                                }
                                return true;
                            }
                        });
                    }
                    //search ends
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(getActivity(),AddTransaction.class);
                        intent.putExtra("id",(int)myAdapter.getItemId(i));
                        startActivity(intent);
                    }
                });
                //setting multi choise delete
                listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                listview.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                    public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                        actionMode.getMenu().findItem(R.id.delete).setVisible(true);
                        actionMode.getMenu().findItem(R.id.delete_budget).setVisible(false);
                        actionMode.getMenu().findItem(R.id.action_settings).setVisible(false);
                        final int checkedCount = listview.getCheckedItemCount();
                        actionMode.setTitle(checkedCount + " Selected");
                        myAdapter.toggleSelection((int) myAdapter.getItemId(i));
                    }
                    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                        actionMode.getMenuInflater().inflate(R.menu.menu_main2,menu);
                        return true;
                    }
                    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                        return false;
                    }
                    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.delete:
                                SparseBooleanArray selected = myAdapter.getSelectedIds();
                                for(int i = (selected.size() - 1); i >= 0; i--) {
                                    if(selected.valueAt(i)) {
                                       Expenses selectedItem = DbHelper.getExpenseById(selected.keyAt(i));
                                        myAdapter.remove(selectedItem,rootView.getContext());
                                    }
                                }
                                actionMode.finish();
                                startActivity(new Intent(getActivity(),MainActivity.class));
                                return true;
                            default:
                                return false;
                        }
                    }
                    public void onDestroyActionMode(ActionMode actionMode) {
                        myAdapter.removeSelection();
                    }
                });
                }catch (Exception e){

                }
                return rootView;
            }
            else if(getArguments().getInt(ARG_SECTION_NUMBER)==2){
                final View rootView = inflater.inflate(R.layout.fragment_budgets, container, false);
                final BudgetsDbHelper budgetsDbHelper = new BudgetsDbHelper(rootView.getContext());
                final List<Budgets> bb = (ArrayList) budgetsDbHelper.getAllBudgets();
                final ListView listview2  = (ListView) rootView.findViewById(R.id.budgets_listview);
                final BudgetBaseAdapter budgetBaseAdapter =new BudgetBaseAdapter(rootView.getContext(),(ArrayList<Budgets>) bb);
                listview2.setAdapter(budgetBaseAdapter);
               return rootView;
            }else{
                View rootView = inflater.inflate(R.layout.fragment_statistics, container, false);
                mChart = (PieChart) rootView.findViewById(R.id.idPieChart);

                mChart.setUsePercentValues(false);
                mChart.getDescription().setEnabled(false);
                mChart.setExtraOffsets(5, 10, 5, 5);

                mChart.setDragDecelerationFrictionCoef(0.95f);

                mChart.setExtraOffsets(20.f, 0.f, 20.f, 0.f);

                mChart.setDrawHoleEnabled(true);
                mChart.setHoleColor(Color.WHITE);

                mChart.setTransparentCircleColor(Color.WHITE);
                mChart.setTransparentCircleAlpha(110);

                mChart.setHoleRadius(40f);
                mChart.setTransparentCircleRadius(43f);

                mChart.setDrawCenterText(true);

                mChart.setRotationAngle(0);
                mChart.setRotationEnabled(true);
                mChart.setHighlightPerTapEnabled(true);
                mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry e, Highlight h) {
                        if (e == null)
                            return;
                        Log.i("VAL SELECTED",
                                "Value: " + e.getY() + ", xIndex: " + e.getX()
                                        + ", DataSet index: " + h.getDataSetIndex());
                    }

                    @Override
                    public void onNothingSelected() {
                        Log.i("PieChart", "nothing selected");
                    }
                });
                setData();
                mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
                Legend l = mChart.getLegend();
                l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
                l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
                l.setOrientation(Legend.LegendOrientation.VERTICAL);
                l.setDrawInside(false);
                l.setEnabled(true);


                return rootView;

            }

        }
        private void setData() {
            ArrayList<String> name = new ArrayList<>();
            ArrayList<Float> sumOfAmount = new ArrayList<>();
            ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
            try {
                sumOfAmount.add(db.getAmountByCategory("Food"));
                name.add("Food");
                sumOfAmount.add(db.getAmountByCategory("Leisure"));
                name.add("Leisure");
                sumOfAmount.add(db.getAmountByCategory("Transport"));
                name.add("Transport");
                sumOfAmount.add(db.getAmountByCategory("Clothes"));
                name.add("Clothes");
                sumOfAmount.add(db.getAmountByCategory("Travel"));
                name.add("Travel");
                sumOfAmount.add(db.getAmountByCategory("Health"));
                name.add("Health");
                sumOfAmount.add(db.getAmountByCategory("Bills"));
                name.add("Bills");
                sumOfAmount.add(db.getAmountByCategory("Hobbies"));
                name.add("Hobbies");
                sumOfAmount.add(db.getAmountByCategory("Gifts"));
                name.add("Gifts");
                sumOfAmount.add(db.getAmountByCategory("Groceries"));
                name.add("Groceries");
                sumOfAmount.add(db.getAmountByCategory("Kids"));
                name.add("Kids");
                sumOfAmount.add(db.getAmountByCategory("Education"));
                name.add("Education");
                sumOfAmount.add(db.getAmountByCategory("Beauty"));
                name.add("Beauty");
                sumOfAmount.add(db.getAmountByCategory("Sports"));
                name.add("Sports");
                sumOfAmount.add(db.getAmountByCategory("Other"));
                name.add("Other");
            }catch(Exception e){

            }

            // NOTE: The order of the entries when being added to the entries array determines their position around the center of
            // the chart.
            for (int i = 0;i<sumOfAmount.size();i++){
                if(sumOfAmount.get(i)>0){
                entries.add(new PieEntry(sumOfAmount.get(i),name.get(i) ));
            }}

            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);

            // add a lot of colors

            ArrayList<Integer> colors = new ArrayList<Integer>();

            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.LIBERTY_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.PASTEL_COLORS)
                colors.add(c);

            colors.add(ColorTemplate.getHoloBlue());

            dataSet.setColors(colors);
            //dataSet.setSelectionShift(0f);


            dataSet.setValueLinePart1OffsetPercentage(80.f);
            dataSet.setValueLinePart1Length(0.3f);
            dataSet.setValueLinePart2Length(0.5f);
            //dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new LargeValueFormatter(" AZN"));
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.BLACK);
            mChart.setData(data);

            // undo all highlights
            mChart.highlightValues(null);

            mChart.invalidate();
        }

    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Expenses";
                case 1:
                    return "Budgets";
                case 2:
                    return "Statistic";
            }
            return null;
        }
    }
}
