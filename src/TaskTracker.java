import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TaskTracker extends JFrame {
    private DefaultListModel<String> taskListModel;
    private static final String FILE_NAME = "tasks.txt";

    public TaskTracker() {
        super("Task Tracker");

        setSize(500, 300);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Input area
        JPanel inputPanel = new JPanel();
        JTextField taskInput = new JTextField(20);
        JButton addButton = new JButton("Add Task");
        JButton removeButton = new JButton("Remove Task");
        inputPanel.add(taskInput);
        inputPanel.add(addButton);
        inputPanel.add(removeButton);
        add(inputPanel, BorderLayout.NORTH);

        // Task list area
        taskListModel = new DefaultListModel<>();
        JList<String> taskList = new JList<>(taskListModel);
        JScrollPane scrollPane = new JScrollPane(taskList);
        add(scrollPane, BorderLayout.CENTER);

        // Load tasks from file
        loadTasksFromFile();

        // Add button logic
        addButton.addActionListener(e -> {
            String task = taskInput.getText().trim();
            if (!task.isEmpty()) {
                taskListModel.addElement(task);
                taskInput.setText(""); // Clear input field
            }
        });

        // Remove button logic
        removeButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex(); // Get the selected task index
            if (selectedIndex != -1) { // Ensure something is selected
                taskListModel.remove(selectedIndex); // Remove from model
            }
        });


        // Save tasks when window closes
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                saveTasksToFile();
            }
        });

        setVisible(true); // Show the window
    }

    private void loadTasksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                taskListModel.addElement(line);
            }
        } catch (IOException e) {
            System.out.println("No saved task file found — starting fresh.");
        }
    }

    private void saveTasksToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < taskListModel.getSize(); i++) {
                writer.println(taskListModel.getElementAt(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TaskTracker();
    }
}
