package com.example.demo1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class HelloApplication extends Application {
    ArrayList<SaleProduct> saleProductList = new ArrayList<>();
    HashMap<Integer, HashMap<Integer, Integer>> yearlySales = new HashMap<>();

    @Override
    public void start(Stage stage) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Month");
        yAxis.setLabel("Sales");
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setVisible(false);

        Button btn = new Button("Выберите файл");
        VBox root = new VBox();
        VBox box1 = new VBox();
        VBox box2 = new VBox(lineChart);
        VBox box3 = new VBox(btn);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        stage.setTitle("Product Sales");

        root.getChildren().add(box1);
        root.getChildren().add(box2);
        root.getChildren().add(box3);
        root.getChildren().add(btn);

        btn.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                try {
                    saleProductList.clear();
                    yearlySales.clear();
                    ExcelRead(file, saleProductList);
                    lineChart.setVisible(true);
                    updateGraph(series, saleProductList, yearlySales);
                } catch (IOException e) {
                    lineChart.setVisible(false);
                    throw new RuntimeException(e);
                }
            }
        });

        Scene scene = new Scene(root, 640, 360);
        stage.setScene(scene);
        stage.show();
    }

    private void ExcelRead(File file, ArrayList<SaleProduct> saleProductList) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            Workbook workbook = new HSSFWorkbook(fis);  // Для старых .xls файлов
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    int id = (int) row.getCell(0).getNumericCellValue();
                    String name = row.getCell(1).getStringCellValue();
                    double price = row.getCell(2).getNumericCellValue();
                    int quantity = (int) row.getCell(3).getNumericCellValue();
                    int finalPrice = (int) row.getCell(4).getNumericCellValue();
                    String date = row.getCell(5).getStringCellValue();

                    saleProductList.add(new SaleProduct(id, name, price, quantity, finalPrice, date));
                }
            }
        }
    }

    private void updateGraph(XYChart.Series<String, Number> series, ArrayList<SaleProduct> productSaleList, HashMap<Integer, HashMap<Integer, Integer>> yearlySales) {
        for (SaleProduct sale : productSaleList) {
            int year = getYearFromDate(sale.getDate());
            int month = getMonthFromDate(sale.getDate());
            yearlySales.putIfAbsent(year, new HashMap<>());
            HashMap<Integer, Integer> monthlySales = yearlySales.get(year);
            monthlySales.put(month, monthlySales.getOrDefault(month, 0) + sale.getFinalPrice());
        }

        series.getData().clear();
        for (int year : yearlySales.keySet()) {
            HashMap<Integer, Integer> monthlySales = yearlySales.get(year);
            for (int month = 1; month <= 12; month++) {
                int sales = monthlySales.getOrDefault(month, 0);
                if (sales > 0) {
                    series.getData().add(new XYChart.Data<>(getMonthName(month) + " " + year, sales));
                }
            }
        }
    }

    private int getYearFromDate(String dateStr) {
        return Integer.parseInt(dateStr.split("\\.")[2]);  // Полагаемся, что дата в формате "dd.MM.yyyy"
    }
    private int getMonthFromDate(String dateStr) {
        return Integer.parseInt(dateStr.split("\\.")[1]);  // Полагаемся, что дата в формате "dd.MM.yyyy"
    }

    private String getMonthName(int month) {
        String[] months = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь",
                "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
        return months[month - 1];
    }

    public static void main(String[] args) {
        launch();
    }
}
