package com.example.salarycalcmv02a;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;   //  Delete
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import android.content.SharedPreferences;

import org.w3c.dom.Text;

import java.lang.ref.Cleaner;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    SharedPreferences settings;         //  Для сохранения данных при выходе из приложения
    ArrayList<String> overWorkArray;   //  Массив с переработками
    ListView overWorkList;  //  Отображение массива с переработками на экране
    ArrayAdapter<String> overWorkAdapter;   //  Адаптер для связи массива с переработками
                                            // и списка на экране
    Map<String, Integer> overWorkMap = new HashMap<String, Integer>();  //  Для рассчёта переработок
    TextView base;
    TextView regionalCoeff;
    TextView harm;
    TextView substitutePerc;
    TextView substituteHours;
    TextView workHours;
    TextView bonus;
    TextView singlePay;
    TextView valOverWorkHours;
    //  Output data areas;
    TextView salary;
    TextView clearSalary;
    TextView inHourSalary;
    TextView overWorkH;
    TextView overWork;
    TextView overWorkClear;

    Boolean bEasterEgg = false;
    Boolean bEasterEggShowOnce = false;
    int EASTEREGG_SHOW = 100000;

    public void btnLegendOnClickEvent(View view) {
        Intent intent = new Intent(this, LegendActivity.class);
        startActivity(intent);
    }

    static class SalaryType {
        public

        float getSalaryInHour() {
            float temp;
            if(tax) {
                //temp = base * ((1.00F + (bonus / 100.00F)) * (1.00F + regK / 100.00F));
                temp = base;
            }
            else {
                //temp = base * ((1.00F + (bonus / 100.00F)) * (1.00F + regK / 100.00F)) / workHours;
                temp = base / workHours;
            }
            return temp;
        }
        //void init(){
        //    tax = false;
        //    base = 0.0F;         //  Оклад/Тариф, руб
        //    regK = 0.0F;         //  Региональный коэффициент, %
        //    harm = 0.0F;         //  Вредность, %
        //    substituteP = 0.0F;  //  % за замещение
        //    substituteH = 0.0F;  //  Часов замещение
        //    bonus = 0.0F;        //  Премия, %
        //    workHours = 0.0F;    //  Рабочих часов в месяце
        //    singlePay = 0.0F;    //  Единовременная выплата, руб
        //    totalSalary = 0.0F;  //  Всего заработано, до вычета налога
        //    clearSalary = 0.0F;  //  Чистая зарплата, после вычета налога
        //    salaryInHour = 0.0F; //  Зарплата в час, руб
        //    overWorks = 0.0F;    //  Количество часов переработки
        //    totalSalaryWithoutOverwork = 0.0F;
        //}
        boolean tax = false;
        float base = 0.0F;         //  Оклад/Тариф, руб
        float regK = 0.0F;         //  Региональный коэффициент, %
        float harm = 0.0F;         //  Вредность, %
        float substituteP = 0.0F;  //  % за замещение
        float substituteH = 0.0F;  //  Часов замещение
        float bonus = 0.0F;        //  Премия, %
        float workHours = 0.0F;    //  Рабочих часов в месяце
        float singlePay = 0.0F;    //  Единовременная выплата, руб
        float totalSalary = 0.0F;  //  Всего заработано, до вычета налога
        float clearSalary = 0.0F;  //  Чистая зарплата, после вычета налога
        float salaryInHour = 0.0F; //  Зарплата в час, руб
        float overWorks = 0.0F;    //  Количество часов переработки
        float totalSalaryWithoutOverwork = 0.0F;  //  Для рассчёта чистой переработки
    }
    SalaryType salaryData = new SalaryType(); //  Создание ссылки на структуру данных зарплаты
    CalendarView calendar;
    String strForCheckDublicat;     //  Строка дляпроверки на дубликат строки при добавлении записи о переработке
    int dayOfWeek;                  //  День недели
    boolean tax = false;            //  Значение чекбокса Тариф
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        overWorkArray = new ArrayList<>();
        overWorkList = findViewById(R.id.countriesList);
        overWorkAdapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, overWorkArray);

        // устанавливаем для списка адаптер
        overWorkList.setAdapter(overWorkAdapter);

        settings = getSharedPreferences("SalaryCalcData", MODE_PRIVATE);

        CheckBox _tax = findViewById(R.id.chbTax);
        EditText _base = findViewById(R.id.valBase);
        EditText _rk = findViewById(R.id.valReginalCoeff);
        EditText _bonus = findViewById(R.id.valBonus);
        EditText _wh = findViewById(R.id.valWorkHours);
        EditText _harm = findViewById(R.id.valHarm);
        EditText _substitP = findViewById(R.id.valSubstitPerc);
        EditText _substitH = findViewById(R.id.valSubstitHour);
        TextView _salary = findViewById(R.id.salary);
        TextView _clearSalary = findViewById(R.id.clearSalary);
        TextView _salaryInHour = findViewById(R.id.inHourSalary);
        TextView _overWorkH = findViewById(R.id.overWorkH);
        TextView _overWork = findViewById(R.id.overWork);
        TextView _overWorkClear = findViewById(R.id.overWorkClear);
        tax = settings.getBoolean("tax", Boolean.parseBoolean(("false")));
        _tax.setChecked(tax);
        //_tax.setActivated(settings.getBoolean("tax", Boolean.parseBoolean(("false"))));
        _base.setText(settings.getString("base", "73000"));
        //_base.setText(new StringBuilder().append(settings.getBoolean("tax", Boolean.parseBoolean(("false")))));
        _rk.setText(settings.getString("rk", "25"));
        _bonus.setText(settings.getString("bonus", "30"));
        _wh.setText(settings.getString("wh", "184"));
        _harm.setText(settings.getString("harm", "5"));
        _salary.setText(settings.getString("salary", "0"));
        _clearSalary.setText(settings.getString("clearSalary", "0"));
        _salaryInHour.setText(settings.getString("salaryInHour", "0"));
        _overWorkH.setText(settings.getString("overWorkH", "0"));
        _overWork.setText(settings.getString("overWork", "0"));
        _overWorkClear.setText(settings.getString("overWorkClear", "0"));
        _substitP.setText(settings.getString("substitP", "0"));
        _substitH.setText(settings.getString("substitH", "0"));

        int _overWorkArraySize = settings.getInt("overWorkArraySize", (int)0);
        overWorkArray.clear();
        for(int i = 0; i != _overWorkArraySize; ++i) {
            overWorkArray.add(settings.getString("overWorkArrayEl" + i, "null"));
        }
        overWorkAdapter.notifyDataSetChanged();

        overWorkMap.clear();
        int _overWorkMapSize = settings.getInt("overWorkMapSize", (int)0);
        String _key;
        int _value;
        for(int i = 0; i != _overWorkMapSize; ++i){
            _key = settings.getString("overWorkMapElK" + i, "null");
            _value = settings.getInt("overWorkMapElV" + i, (int)0);
            overWorkMap.put(_key, _value);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        strForCheckDublicat = "null";

        TextView oklad = findViewById(R.id.oklad);
        if(tax)
            oklad.setText("Ставка:");
        else
            oklad.setText("Оклад:");

        //  Установка текущей даты в календаре при начальной загрузке приложения.
        //CalendarView c = (CalendarView)findViewById(R.id.datePicker);
        //c.setDate(System.currentTimeMillis());
        //Calendar today = Calendar.getInstance();
        //int year = today.get(Calendar.YEAR);
        //int month = today.get(Calendar.MONTH);
        //int day = today.get(Calendar.DAY_OF_MONTH);
        //DatePicker dp = findViewById(R.id.datePicker);
        //dp.init(year, month, day, null);




        // получаем элемент TextView
        //TextView selection = findViewById(R.id.inputDate);

        overWorkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = overWorkArray.get(position);
                int index = selectedItem.indexOf(":");
                String selectedDate;
                if(index > -1 && index < selectedItem.length() - 1) {
                    selectedDate = selectedItem.substring(0, index + 2);
                    overWorkMap.remove(selectedDate);
                }
                overWorkArray.remove(position);
                overWorkAdapter.notifyDataSetChanged();
            }
        });
        calendar = findViewById(R.id.datePicker);
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, dayOfMonth);
                dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
                boolean holiday = false;
                char HO = 'O';
                if(dayOfWeek == 1 || dayOfWeek == 7)
                    HO = 'H';
                //  Определение дня недели
                strForCheckDublicat = new StringBuilder().append(HO).append(".").append(dayOfMonth).append(".").
                        append(month + 1).append(".").append(year).append(" :$").toString();
            }
        });
        CheckBox chBox = findViewById(R.id.chbTax);
        chBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tax = isChecked;
                TextView oklad = findViewById(R.id.oklad);
                if(tax)
                    oklad.setText("Ставка:");
                else
                    oklad.setText("Оклад:");
            }
        });

        //base.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});

    }
    public void salaryCalc(SalaryType salaryData){
        salaryData.salaryInHour = salaryData.getSalaryInHour();    //  Базовая зарплата в час
        int hours = 0;
        for(Map.Entry<String, Integer> entry : overWorkMap.entrySet()){
            hours += entry.getValue();
        }
        salaryData.overWorks = hours;
        float substituteTotal = salaryData.salaryInHour * (salaryData.substituteP / 100.00F)
                * salaryData.substituteH;
        salaryData.totalSalary = ((salaryData.base + hours * salaryData.salaryInHour) * (1.00F + salaryData.bonus / 100.00F)
                + 0) * ( 1.00F + (salaryData.harm / 100.00F)) * (1.00F + (salaryData.regK / 100.00F))
                + salaryData.singlePay + substituteTotal + overWorkCalc();

        salaryData.totalSalaryWithoutOverwork = salaryData.totalSalary - (salaryData.base * (1.00F + salaryData.bonus / 100.00F)
                + 0.0F) * ( 1.00F + (salaryData.harm / 100.00F)) * (1.00F + (salaryData.regK / 100.00F))
                + salaryData.singlePay + substituteTotal;

        salaryData.clearSalary = salaryData.totalSalary * 0.87F;
    }
    public void salaryCalcTax(SalaryType salaryData){
        salaryData.salaryInHour = salaryData.base;    //  Базовая зарплата в час
        int hours = 0;
        for(Map.Entry<String, Integer> entry : overWorkMap.entrySet()){
            hours += entry.getValue();
        }
        salaryData.overWorks = hours;
        float substituteTotal = salaryData.salaryInHour * (salaryData.substituteP / 100.00F)
                * salaryData.substituteH;
        salaryData.totalSalary = ((salaryData.base * salaryData.workHours + hours * salaryData.salaryInHour) +
                ((salaryData.substituteH * (salaryData.base * (salaryData.substituteP / 100.00F)))
                / salaryData.workHours)) * (1.00F + ((salaryData.bonus / 100.00F) + (salaryData.harm / 100.00F)))
                * (1.00F + salaryData.regK / 100.00F) + salaryData.singlePay + substituteTotal + overWorkCalc();
        salaryData.totalSalaryWithoutOverwork = salaryData.totalSalary - (salaryData.base * salaryData.workHours +
                ((salaryData.substituteH * (salaryData.base * (salaryData.substituteP / 100.00F)))
                        / salaryData.workHours)) * (1.00F + ((salaryData.bonus / 100.00F) + (salaryData.harm / 100.00F)))
                * (1.00F + salaryData.regK / 100.00F) + salaryData.singlePay + substituteTotal;

        salaryData.clearSalary = salaryData.totalSalary * 0.87F;
    }
    public float overWorkCalc(){

        boolean holiday = false;
        float cnt = 0;

        float _salaryInHour = salaryData.salaryInHour;
        for(Map.Entry<String, Integer> entry : overWorkMap.entrySet()) {

            float counter = entry.getValue();

            //if (dayOfWeek == 0 || dayOfWeek == 6)
            //    holiday = true;
            //else
            //    holiday = false;

            char tmpCh = entry.getKey().charAt(0);

            if(tmpCh == 'H'){
                while (counter > 0) {
                    cnt += _salaryInHour;   //  Надо ли умножать на 2?
                    --counter;
                }
            }
            else{
                while(counter > 0){
                    if(counter > 2)
                        cnt += _salaryInHour;
                    if(counter <= 2)
                        cnt += (_salaryInHour / 2);    //  Надо ли делить на 2? Или умножить на 2,5?
                    --counter;
                }
            }
        }
        return cnt;
    }
    public void showData(SalaryType _salaryData){
        //String tempSalary = String.format("%.2f", _salaryData.totalSalary);
        salary.setText(String.format("%.2f", _salaryData.totalSalary));
        //salary.setText(Float.toString(_salaryData.totalSalary).format("%.2f", ));
        clearSalary.setText(String.format("%.2f", _salaryData.clearSalary));
        inHourSalary.setText(String.format("%.2f", _salaryData.salaryInHour));
        overWorkH.setText(String.format("%.1f", _salaryData.overWorks));
        overWork.setText(String.format("%.2f", _salaryData.totalSalaryWithoutOverwork));
        overWorkClear.setText(String.format("%.2f", _salaryData.totalSalaryWithoutOverwork * 0.87));
    }
    public void btnAddOnClickEvent(View view) {
        EditText overWorkHours = findViewById(R.id.valOverWorkHours);
        //DatePicker inputDate = findViewById(R.id.datePicker);
        CalendarView inputDate = findViewById(R.id.datePicker);
        inputDate.getWeekDayTextAppearance();
        //String strDay   = Integer.toString(inputDate.getDayOfMonth());
        //String strMonth = Integer.toString(inputDate.getMonth() + 1);
        //String strYear  = Integer.toString(inputDate.getYear());
        String strOverWorkHours = overWorkHours.getText().toString();
        int iOverWorkHours;
        if(!strOverWorkHours.isEmpty())
            iOverWorkHours = Integer.parseInt(strOverWorkHours);
        else
            iOverWorkHours = 0;
        if(!(strForCheckDublicat.equals("null"))) {
            String strInputDate = strForCheckDublicat + strOverWorkHours;
            boolean dublicat = false;
            for (String str : overWorkArray) {    //  Нельзя записать один и тот же день двады
                if (str.contains(strForCheckDublicat)) {
                    dublicat = true;
                    break;
                }
            }
            if (!dublicat && (iOverWorkHours > 0) && (iOverWorkHours < 13)) { //  Количество часов переработки в день 1 - 12
                overWorkArray.add(strInputDate);
                overWorkAdapter.notifyDataSetChanged();
                overWorkMap.put(strForCheckDublicat, iOverWorkHours);
            }
            overWorkHours.setText("");
        }
    }
    public void btnCalcOnClickEvent(View view){
        base = findViewById(R.id.valBase);
        regionalCoeff = findViewById(R.id.valReginalCoeff);
        harm = findViewById(R.id.valHarm);
        substitutePerc = findViewById(R.id.valSubstitPerc);
        substituteHours = findViewById(R.id.valSubstitHour);
        workHours = findViewById(R.id.valWorkHours);
        bonus = findViewById(R.id.valBonus);
        singlePay = findViewById(R.id.valSinglePay);
        valOverWorkHours = findViewById(R.id.valOverWorkHours);
        //  Output data areas;
        salary = findViewById(R.id.salary);
        clearSalary = findViewById(R.id.clearSalary);
        inHourSalary = findViewById(R.id.inHourSalary);
        overWorkH = findViewById(R.id.overWorkH);
        overWork = findViewById(R.id.overWork);
        overWorkClear = findViewById(R.id.overWorkClear);
        if(base.getText().toString().isEmpty())    salaryData.base = 0.0F;
        else salaryData.base = Integer.parseInt(base.getText().toString());
        if(regionalCoeff.getText().toString().isEmpty())    salaryData.regK = 0.0F;
        else salaryData.regK = Float.parseFloat(regionalCoeff.getText().toString());
        if(bonus.getText().toString().isEmpty())    salaryData.bonus = 0.0F;
        else salaryData.bonus = Float.parseFloat(bonus.getText().toString());
        if(harm.getText().toString().isEmpty())    salaryData.harm = 0.0F;
        else salaryData.harm = Float.parseFloat(harm.getText().toString());
        if(substitutePerc.getText().toString().isEmpty())    salaryData.substituteP = 0.0F;
        else salaryData.substituteP = Float.parseFloat(substitutePerc.getText().toString());
        if(substituteHours.getText().toString().isEmpty())    salaryData.substituteH = 0.0F;
        else salaryData.substituteH = Float.parseFloat(substituteHours.getText().toString());
        if(singlePay.getText().toString().isEmpty())    salaryData.singlePay = 0.0F;
        else salaryData.singlePay = Float.parseFloat(singlePay.getText().toString());
        if(workHours.getText().toString().isEmpty())    salaryData.workHours = 0.0F;
        else salaryData.workHours = Float.parseFloat(workHours.getText().toString());

        if(!tax)
            salaryCalc(salaryData);
        else
            salaryCalcTax(salaryData);
        showData(salaryData);

        SharedPreferences.Editor prefEditor = settings.edit();
        prefEditor.putString("base", base.getText().toString());
        prefEditor.putString("rk", regionalCoeff.getText().toString());
        prefEditor.putString("bonus", bonus.getText().toString());
        prefEditor.putString("harm", harm.getText().toString());
        prefEditor.putString("wh", workHours.getText().toString());
        prefEditor.putString("salary", salary.getText().toString());
        prefEditor.putString("clearSalary", clearSalary.getText().toString());
        prefEditor.putString("salaryInHour", inHourSalary.getText().toString());
        prefEditor.putString("overWorkH", overWorkH.getText().toString());
        prefEditor.putString("overWork", overWork.getText().toString());
        prefEditor.putString("overWorkClear", overWorkClear.getText().toString());
        prefEditor.putString("substitP", substitutePerc.getText().toString());
        prefEditor.putString("substitH", substituteHours.getText().toString());
        for(int i = 0; i < overWorkArray.size(); ++i)
            prefEditor.putString("overWorkArrayEl" + i, overWorkArray.get(i));
        prefEditor.putInt("overWorkArraySize", overWorkArray.size());
        //for(int i = 0; i < overWorkMap.size(); ++i)
        //prefEditor.putString("overWorkMapEl" + i, overWorkMap.get("q"));
        int i = 0;
        for(Map.Entry<String, Integer> entry : overWorkMap.entrySet()){
            prefEditor.putString("overWorkMapElK" + i, entry.getKey());
            prefEditor.putInt("overWorkMapElV" + i, entry.getValue());
            ++i;
        }
        prefEditor.putInt("overWorkMapSize", overWorkMap.size());
        prefEditor.putBoolean("tax", tax);
        prefEditor.apply();

        //  Easter Egg
        TextView eg = findViewById(R.id.EasterEgg);

        //if(bEasterEgg)
        //    eg.setVisibility(View.VISIBLE);
        //else
        //    eg.setVisibility(View.INVISIBLE);
        //if(bEasterEggShowOnce && bEasterEgg)
        //    bEasterEgg = false;
        if(bEasterEgg)
            eg.setVisibility(View.INVISIBLE);
        if((salaryData.totalSalaryWithoutOverwork * 0.87F > EASTEREGG_SHOW) && !bEasterEgg) {
            eg.setVisibility(View.VISIBLE);
            bEasterEgg = true;
            //bEasterEggShowOnce = true;

        }

        //salaryData.init();
    }
    public void btnResetOnClickEvent(View view) {
        base = findViewById(R.id.valBase);
        regionalCoeff = findViewById(R.id.valReginalCoeff);
        harm = findViewById(R.id.valHarm);
        substitutePerc = findViewById(R.id.valSubstitPerc);
        substituteHours = findViewById(R.id.valSubstitHour);
        workHours = findViewById(R.id.valWorkHours);
        bonus = findViewById(R.id.valBonus);
        singlePay = findViewById(R.id.valSinglePay);
        valOverWorkHours = findViewById(R.id.valOverWorkHours);
        //  Output data areas;
        salary = findViewById(R.id.salary);
        clearSalary = findViewById(R.id.clearSalary);
        inHourSalary = findViewById(R.id.inHourSalary);
        overWorkH = findViewById(R.id.overWorkH);
        overWork = findViewById(R.id.overWork);
        overWorkClear = findViewById(R.id.overWorkClear);

        //base.setText("0");
        //regionalCoeff.setText("0");
        //harm.setText("0");
        //substitutePerc.setText("0");
        //substituteHours.setText("0");
        //workHours.setText("0");
        //bonus.setText("0");
        //singlePay.setText("0");
        valOverWorkHours.setText("");

        salary.setText("0");
        clearSalary.setText("0");
        inHourSalary.setText("0");
        overWorkH.setText("0");
        overWork.setText("0");
        overWorkClear.setText("0");

        overWorkArray.clear();
        overWorkMap.clear();
        overWorkAdapter.notifyDataSetChanged();

        TextView eg = findViewById(R.id.EasterEgg);
        if(bEasterEgg) {
            eg.setVisibility(View.INVISIBLE);
            bEasterEgg = false;
        }

    }
}