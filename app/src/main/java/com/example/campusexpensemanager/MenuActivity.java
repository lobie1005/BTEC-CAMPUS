package com.example.campusexpensemanager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.campusexpensemanager.DataBase.ExpensesData;
import com.example.campusexpensemanager.Model.Expenses;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;

    private DrawerLayout drawerLayout;

    private  NavigationView navigationView;
    FloatingActionButton fab;

     private  HomeFragment homeFragment;
     private ListCategoryActivity listCategoryActivity;
     private ExpenseFragment expenseFragment;
     private BudgetFragment budgetFragment;
     private ProfileFragment profileFragment;
    private ExpensesData expensesData;
    private DatabaseHelper databaseHelper;
    private RecyclerView rv_income, rv_expense;
    private TextView tv_income, tv_expense;
    private TextView tv_incomeAmount, tv_incomeType, tv_incomeNote;
    private EditText et_incomeAmount, et_incomeType, et_incomeNote;
    private List<Expenses> expenseList = new ArrayList<>();
    public DatabaseHandler1 databaseHandler1;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        expensesData = new ExpensesData(MenuActivity.this);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        databaseHandler1 = new DatabaseHandler1(MenuActivity.this);

        fab = findViewById(R.id.fab);

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.Home);
        }
        setFragment(new HomeFragment());

        homeFragment = new HomeFragment();
        expenseFragment = new ExpenseFragment();
        budgetFragment = new BudgetFragment();
        profileFragment = new ProfileFragment();

        bottomNavigationView.setBackground(null);

       bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
               int itemId = menuItem.getItemId();
               if (itemId == R.id.Home) {
                   openFragment(new HomeFragment());
                   return true;
               } else if (itemId == R.id.Expense) {
                   openFragment(new ExpenseFragment());
                   return true;

               } else if (itemId == R.id.Budget) {
                   openFragment(new BudgetFragment());
                   return true;
               } else if (itemId == R.id.Profile) {
                   Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                   startActivity(intent);
                   return true;
               }
               return false;
           }

       });
        fragmentManager = getSupportFragmentManager();
        openFragment(new HomeFragment());
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showBottomDialog();
                    }
                });



    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private  void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout ExpenseLayout = dialog.findViewById(R.id.layoutExpense);
        LinearLayout BudgetLayout = dialog.findViewById(R.id.layoutBudget);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        ExpenseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showExpenseDialog();

                dialog.dismiss();
            }
        });

        BudgetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBudgetDialog();
                dialog.dismiss();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.nav_Home) {
            openFragment(new HomeFragment());
        } else if (itemId == R.id.nav_Expense) {
            openFragment(new ExpenseFragment());
        } else if (itemId == R.id.nav_Budget) {
            openFragment(new BudgetFragment());
        } else if (itemId == R.id.nav_Profile) {
            startActivity(new Intent(MenuActivity.this, MainActivity.class));
        } else if (itemId == R.id.nav_logout) {
            startActivity(new Intent(MenuActivity.this, LoginActivity.class));
        } else if (itemId == R.id.nav_cateList ){
            startActivity(new Intent(MenuActivity.this, ListCategoryActivity.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    private void openFragment(Fragment fragment){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.commit();
    }

    private void showExpenseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.expense_add_item, null);
        Spinner edt_name = customLayout.findViewById(R.id.spinner_ExpensesType);
        EditText edt_amount = customLayout.findViewById(R.id.edt_ExpensesAmount);
        EditText edt_note = customLayout.findViewById(R.id.edt_ExpensesNote);

        Button btn_save = customLayout.findViewById(R.id.btn_save);
        Button btn_cancel = customLayout.findViewById(R.id.btn_cancel);

        builder.setView(customLayout);
        AlertDialog alertDialog = builder.create();

        alertDialog.show();
        expensesData = new ExpensesData(MenuActivity.this);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = edt_amount.getText().toString().trim();
                Spinner name = edt_name.toString().trim();
                String note = edt_note.getText().toString().trim();
                long date = System.currentTimeMillis();

                if (amount.isEmpty()) {
                    edt_amount.setError("Empty amount");
                    return;                                                                        0
                } else if (note.isEmpty()) {
                    edt_note.setError("Empty note");
                    return;
                } else {
                    long insert = expensesData.addNewExpenses(amount, name, note, String.valueOf(date));
                    alertDialog.dismiss();

                    if (insert == -1) {
                        Toast.makeText(MenuActivity.this, "Insert Failure", Toast.LENGTH_LONG).show();
                        // Có thể không cần Intent để khởi tạo một Activity mới
                    } else {
                        Toast.makeText(MenuActivity.this, "Insert Successfully", Toast.LENGTH_LONG).show();
                        // Nếu bạn cần cập nhật giao diện, hãy làm mới Fragment hoặc cập nhật dữ liệu
                        updateUI();

                    }

                }

            }
        });


    }

    private void updateUI() {
        // Ví dụ: Cập nhật dữ liệu cho ExpenseFragment
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if (fragment instanceof ExpenseFragment) {
            ((ExpenseFragment) fragment).refreshData(); // Giả sử có phương thức refreshData() trong ExpenseFragment để làm mới dữ liệu
        }
    }


    private void showBudgetDialog (){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View customLayout = getLayoutInflater().inflate(R.layout.add_budgets, null);
        EditText et_incomeAmount = customLayout.findViewById(R.id.et_incomeAmount);
        EditText et_incomeType = customLayout.findViewById(R.id.et_incomeType);
        EditText et_incomeNote = customLayout.findViewById(R.id.et_incomeNote);
        Button btn_save = customLayout.findViewById(R.id.btn_save);
        Button btn_cancel = customLayout.findViewById(R.id.btn_cancel);

        builder.setView(customLayout);
        AlertDialog alertDialog = builder.create();

        alertDialog.show();
        databaseHandler1 = new DatabaseHandler1(MenuActivity.this);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = et_incomeAmount.getText().toString().trim();
                String type = et_incomeType.getText().toString().trim();
                String note = et_incomeNote.getText().toString().trim();
                long date = System.currentTimeMillis();
                databaseHandler1 = new DatabaseHandler1(MenuActivity.this);
                if (amount.isEmpty()) {
                    et_incomeAmount.setError("Amount cannot be empty");
                    return;
                }
                if (type.isEmpty()) {
                    et_incomeType.setError("Type cannot be empty");
                    return;
                }
                // Add the transaction to the database
                boolean insert = databaseHandler1.addData(amount, type, note, String.valueOf(date));

                if (insert == false) {
                    Toast.makeText(MenuActivity.this, "Insert Failed", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MenuActivity.this, "Insert Successfully", Toast.LENGTH_LONG).show();

                }
                alertDialog.dismiss();
                updateUI1();
            }
        });

    }

    private void updateUI1() {
        // Ví dụ: Cập nhật dữ liệu cho ExpenseFragment
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_layout);
        if (fragment instanceof BudgetFragment) {
            ((BudgetFragment) fragment).refreshData1();
        }
    }


}
