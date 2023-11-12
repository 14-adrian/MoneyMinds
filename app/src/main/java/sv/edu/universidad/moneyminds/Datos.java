package sv.edu.universidad.moneyminds;

import android.app.Application;

import java.text.DateFormatSymbols;

public class Datos extends Application {
    private String today = "";
    private String valDate = "";
    private int month = 0;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    private int year = 0;
    private double ingresos = 0;
    private double gastos = 0;
    private double total = 0;

    public void setValDate(String val){
        this.valDate = val;
    }
    public String getValDate(){
        return valDate;
    }
    public String getToday(){
        return today;
    }
    public void setToday(String val){
        this.today = val;
    }

    public String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num > 0 && num <= 12) {
            month = months[num-1];
        }
        return month;
    }

    public double getIngresos() {
        return ingresos;
    }

    public void setIngresos(double ingresos) {
        this.ingresos = ingresos;
    }

    public double getGastos() {
        return gastos;
    }

    public void setGastos(double gastos) {
        this.gastos = gastos;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }
}
