package com.cashlog.DbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cashlog.Entities.Budgets;
import com.cashlog.Entities.Expenses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BudgetsDbHelper extends SQLiteOpenHelper {
    public static final String BUDGETS_DB_NAME = "Budget.db";
    public static final String BUDGETS_TABLE_NAME = "budgets";
    public static final String BUDGET_COLUMN_ID = "budget_id";
    public static final String BUDGET_COLUMN_NAME = "budget_name";
    public static final String BUDGET_COLUMN_INCOME = "overall_income";
    public static final String BUDGET_COLUMN_Status = "status";


    public static final String EXPENSES_TABLE_NAME = "expenses";
    public static final String EXPENSES_COLUMN_ID = "id";
    public static final String EXPENSES_COLUMN_PAYEE = "payee";
    public static final String EXPENSES_COLUMN_AMOUNT = "amount";
    public static final String EXPENSES_COLUMN_CATEGORY = "category";
    public static final String EXPENSES_COLUMN_DATE = "date";
    public static final String EXPENSES_COLUMN_NOTE = "note";
    public static final String EXPENSES_COLUMN_TIME = "timer";
    public static final String EXPENSES_COLUMN_RATING = "rating";
    private HashMap hp;

    public BudgetsDbHelper(Context context) {
        super(context, BUDGETS_DB_NAME, null, 1);
    }



    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + BUDGETS_TABLE_NAME +
                        "(" + BUDGET_COLUMN_ID + " integer primary key," + BUDGET_COLUMN_NAME + " text," + BUDGET_COLUMN_INCOME + " real, " + BUDGET_COLUMN_Status + " integer )"
        );
        db.execSQL(
                "create table " + EXPENSES_TABLE_NAME +
                        "(" + EXPENSES_COLUMN_ID + " integer primary key," + EXPENSES_COLUMN_PAYEE + " text," + EXPENSES_COLUMN_AMOUNT + " real," + EXPENSES_COLUMN_CATEGORY + " text," + EXPENSES_COLUMN_DATE + " text," + EXPENSES_COLUMN_NOTE + " text," + EXPENSES_COLUMN_TIME + " text," + EXPENSES_COLUMN_RATING + " real," + BUDGET_COLUMN_ID + " integer)"

        );
    }

    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS" + BUDGETS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + EXPENSES_TABLE_NAME);
        onCreate(db);
    }

    public void addBudgets(Budgets budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BUDGET_COLUMN_NAME, budget.getBudget_name());
        contentValues.put(BUDGET_COLUMN_INCOME, budget.getOverall_income());
        contentValues.put(BUDGET_COLUMN_Status, budget.getStatus());
        db.insert(BUDGETS_TABLE_NAME, null, contentValues);
        db.close();
    }

    public void addExpense(Expenses expense) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EXPENSES_COLUMN_PAYEE, expense.getPayee());
        contentValues.put(EXPENSES_COLUMN_AMOUNT, expense.getAmount());
        contentValues.put(EXPENSES_COLUMN_CATEGORY, expense.getCategory());
        contentValues.put(EXPENSES_COLUMN_DATE, expense.getDate());
        contentValues.put(EXPENSES_COLUMN_TIME, expense.getTime());
        contentValues.put(EXPENSES_COLUMN_NOTE, expense.getNote());
        contentValues.put(EXPENSES_COLUMN_RATING, expense.getRating());
        contentValues.put(BUDGET_COLUMN_ID, expense.getBudget_id());
        db.insert(EXPENSES_TABLE_NAME, null, contentValues);
        db.close();
    }

    public List<Budgets> getAllBudgets() {
        List<Budgets> budgetsList = new ArrayList<Budgets>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + BUDGETS_TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToLast()) {
            do {
                Budgets b = new Budgets();
                b.setBudget_id(Integer.parseInt(cursor.getString(0)));
                b.setBudget_name(cursor.getString(1));
                b.setOverall_income(cursor.getDouble(2));
                b.setStatus(cursor.getInt(3));
                budgetsList.add(b);
            } while (cursor.moveToPrevious());
        }
        db.close();
        return budgetsList;
    }

    public List<Expenses> getAllExpenses() {
        List<Expenses> expensesList = new ArrayList<Expenses>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + EXPENSES_TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Expenses e = new Expenses();
                e.setId(Integer.parseInt(cursor.getString(0)));
                e.setPayee(cursor.getString(1));
                e.setAmount(cursor.getDouble(2));
                e.setCategory(cursor.getString(3));
                e.setDate(cursor.getString(4));
                e.setNote(cursor.getString(5));
                e.setTime(cursor.getString(6));
                e.setRating((cursor.getFloat(7)));
                e.setBudget_id(Integer.parseInt(cursor.getString(8)));
                expensesList.add(e);
            } while (cursor.moveToNext());
        }
        db.close();
        return expensesList;
    }

    public void deleteBudget(Budgets b) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(BUDGETS_TABLE_NAME, BUDGET_COLUMN_ID + " = ?", new String[]{String.valueOf(b.getBudget_id())});
        db.close();
    }

    public void deleteExpense(Expenses e) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(EXPENSES_TABLE_NAME, EXPENSES_COLUMN_ID + " = ?", new String[]{String.valueOf(e.getId())});
        db.close();
    }

    public int updateBudgets(Budgets b) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(BUDGET_COLUMN_NAME, b.getBudget_name());
//        values.put(BUDGET_COLUMN_INCOME, b.getOverall_income());
        values.put(BUDGET_COLUMN_Status, b.getStatus());
        // updating row
        return db.update(BUDGETS_TABLE_NAME, values, BUDGET_COLUMN_ID + " = ?",
                new String[]{String.valueOf(b.getBudget_id())});
    }

    public int updateExpense(Expenses expense) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EXPENSES_COLUMN_PAYEE, expense.getPayee());
        values.put(EXPENSES_COLUMN_AMOUNT, expense.getAmount());
        values.put(EXPENSES_COLUMN_CATEGORY, expense.getCategory());
        values.put(EXPENSES_COLUMN_DATE, expense.getDate());
        values.put(EXPENSES_COLUMN_TIME, expense.getTime());
        values.put(EXPENSES_COLUMN_NOTE, expense.getNote());
        values.put(EXPENSES_COLUMN_RATING, expense.getRating());
        values.put(BUDGET_COLUMN_ID, expense.getBudget_id());
        // updating row
        return db.update(EXPENSES_TABLE_NAME, values, EXPENSES_COLUMN_ID + " = ?",
                new String[]{String.valueOf(expense.getId())});
    }

    public Budgets getBudgetsById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(BUDGETS_TABLE_NAME, new String[]{BUDGET_COLUMN_ID,
                BUDGET_COLUMN_NAME, BUDGET_COLUMN_INCOME}, BUDGET_COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Budgets b = new Budgets(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getDouble(3));
        db.close();
        return b;
    }

    public Expenses getExpenseById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(EXPENSES_TABLE_NAME, new String[]{EXPENSES_COLUMN_ID,
                EXPENSES_COLUMN_PAYEE, EXPENSES_COLUMN_AMOUNT, EXPENSES_COLUMN_CATEGORY, EXPENSES_COLUMN_DATE, EXPENSES_COLUMN_NOTE, EXPENSES_COLUMN_TIME, EXPENSES_COLUMN_RATING, BUDGET_COLUMN_ID}, EXPENSES_COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Expenses e = new Expenses(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getDouble(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getFloat(7), cursor.getInt(8));
        db.close();
        return e;
    }

    public List<Expenses> getAllExpenseByBudget(int id) {
        List<Expenses> expensesList = new ArrayList<Expenses>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(EXPENSES_TABLE_NAME, new String[]{EXPENSES_COLUMN_ID,
                EXPENSES_COLUMN_PAYEE, EXPENSES_COLUMN_AMOUNT, EXPENSES_COLUMN_CATEGORY, EXPENSES_COLUMN_DATE, EXPENSES_COLUMN_NOTE, EXPENSES_COLUMN_TIME, EXPENSES_COLUMN_RATING, BUDGET_COLUMN_ID}, BUDGET_COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                Expenses e = new Expenses(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getDouble(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getFloat(7), cursor.getInt(8));
                expensesList.add(e);
            } while (cursor.moveToNext());
        }
        db.close();
        return expensesList;
    }

    public Budgets getBudgetByStatus() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(BUDGETS_TABLE_NAME, new String[]{BUDGET_COLUMN_ID}, BUDGET_COLUMN_Status + "=?", new String[]{String.valueOf(1)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Budgets budgets = new Budgets(cursor.getInt(0));
        db.close();
        return budgets;
    }
    public Budgets getBudgetId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(BUDGETS_TABLE_NAME, new String[]{BUDGET_COLUMN_ID}, BUDGET_COLUMN_Status + "=?", new String[]{String.valueOf(1)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Budgets budgets = new Budgets(cursor.getInt(0));
        db.close();
        return budgets;
    }

    public Double getCategoryAmount(int id,String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        Double sum = 0.0;
        Cursor cursor = db.query(EXPENSES_TABLE_NAME, new String[]{ "sum("+EXPENSES_COLUMN_AMOUNT+")"}, BUDGET_COLUMN_ID + "=? and "+EXPENSES_COLUMN_CATEGORY+"=?", new String[]{String.valueOf(id),category}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                sum = cursor.getDouble(0);
            } while (cursor.moveToNext());
        }
        db.close();
        return sum;
    }
    public Float getAmountByCategory(String category) {
        Budgets budgets = getBudgetByStatus();
        return getCategoryAmount(budgets.getBudget_id(),category).floatValue();
    }
    public Double getTotalAm(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Double sum = 0.0;
        Cursor cursor = db.query(EXPENSES_TABLE_NAME, new String[]{ "sum("+EXPENSES_COLUMN_AMOUNT+")"}, BUDGET_COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                sum = cursor.getDouble(0);
            } while (cursor.moveToNext());
        }
        db.close();
        return sum;
    }


}