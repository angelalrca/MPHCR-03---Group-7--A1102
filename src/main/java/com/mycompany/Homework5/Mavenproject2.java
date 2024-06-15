package com.mycompany.mavenproject2;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Mavenproject2 {
    private static final String filePath = "src/main/java/csvfles/Employeedetails_1.csv";
    private JTable table;
    private DefaultTableModel tableModel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Mavenproject2::new);
    }

    public Mavenproject2() {
        JFrame frame = new JFrame("MotorPH Employee App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        tableModel = new DefaultTableModel(new String[]{"Employee #", "Last Name", "First Name", "Birthday", "Address", "Phone Number", "SSS #", "Philhealth #", "TIN #", "Pag-ibig #", "Status", "Position", "Immediate Supervisor"}, 0);
        table = new JTable(tableModel);
        loadCsvData();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRecord();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String employeeNumber = promptForEmployeeNumber();
                if (employeeNumber != null) {
                    updateRecord(employeeNumber);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String employeeNumber = promptForEmployeeNumber();
                if (employeeNumber != null) {
                    deleteRecord(employeeNumber);
                }
            }
        });

        panel.add(new JScrollPane(table));
        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    private String promptForEmployeeNumber() {
        return JOptionPane.showInputDialog("Enter Employee Number:");
    }

    private void addRecord() {
        String[] newRecord = new String[table.getColumnCount()];
        for (int i = 0; i < table.getColumnCount(); i++) {
            newRecord[i] = JOptionPane.showInputDialog("Enter " + table.getColumnName(i) + ":");
        }
        tableModel.addRow(newRecord);
        saveCsvData();
    }

    private void updateRecord(String employeeNumber) {
        int selectedRow = findRowByEmployeeNumber(employeeNumber);
        if (selectedRow != -1) {
            for (int i = 0; i < table.getColumnCount(); i++) {
                String value = JOptionPane.showInputDialog("Enter new " + table.getColumnName(i) + ":", table.getValueAt(selectedRow, i));
                tableModel.setValueAt(value, selectedRow, i);
            }
            saveCsvData();
        } else {
            JOptionPane.showMessageDialog(null, "Employee with Employee Number " + employeeNumber + " not found.");
        }
    }

    private void deleteRecord(String employeeNumber) {
        int selectedRow = findRowByEmployeeNumber(employeeNumber);
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);
            saveCsvData();
        } else {
            JOptionPane.showMessageDialog(null, "Employee with Employee Number " + employeeNumber + " not found.");
        }
    }

    private int findRowByEmployeeNumber(String employeeNumber) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (employeeNumber.equals(tableModel.getValueAt(i, 0))) { // Assuming Employee # is in the first column
                return i;
            }
        }
        return -1;
    }

    private void loadCsvData() {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = reader.readAll();
            for (String[] record : records) {
                tableModel.addRow(record);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    private void saveCsvData() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                int colCount = tableModel.getColumnCount();
                String[] row = new String[colCount];
                for (int j = 0; j < colCount; j++) {
                    row[j] = (String) tableModel.getValueAt(i, j);
                }
                writer.writeNext(row);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
