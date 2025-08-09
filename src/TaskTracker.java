import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class TaskTracker extends JFrame {
    private DefaultListModel<Task> taskListModel;
    private static final String FILE_NAME = "tasks.txt";

    public TaskTracker() {
        super("Task Tracker");

        setSize(600, 300);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Input area
        JPanel inputPanel = new JPanel();
        JTextField taskInput = new JTextField(20);
        JButton addButton = new JButton("Add Task");
        JButton removeButton = new JButton("Remove Task");
        JButton saveButton = new JButton("Save");
        inputPanel.add(taskInput);
        inputPanel.add(addButton);
        inputPanel.add(removeButton);
        inputPanel.add(saveButton);
        add(inputPanel, BorderLayout.NORTH);

        // Task list area
        taskListModel = new DefaultListModel<>();
        JList<Task> taskList = new JList<>(taskListModel);
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Custom renderer: show each task as a checkbox
        taskList.setCellRenderer((list, task, index, isSelected, cellHasFocus) -> {
            JCheckBox check = new JCheckBox(task.text, task.completed);
            check.setOpaque(true);
            if (isSelected) {
                check.setBackground(list.getSelectionBackground());
                check.setForeground(list.getSelectionForeground());
            } else {
                check.setBackground(list.getBackground());
                check.setForeground(list.getForeground());
            }
            check.setFocusable(false);
            return check;
        });

        // Toggle completion on click
        taskList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = taskList.locationToIndex(e.getPoint());
                if (index != -1) {
                    Task t = taskListModel.get(index);
                    t.completed = !t.completed;
                    taskListModel.set(index, t); // Refresh display
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(taskList);
        add(scrollPane, BorderLayout.CENTER);

        // Load tasks from file
        loadTasksFromFile();

        // Add button logic
        addButton.addActionListener(e -> {
            String text = taskInput.getText().trim();
            if (!text.isEmpty()) {
                taskListModel.addElement(new Task(text));
                taskInput.setText("");
            }
        });

        // Remove button logic
        removeButton.addActionListener(e -> {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                taskListModel.remove(selectedIndex);
            }
        });

        // Save tasks when window closes
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                saveTasksToFile();
            }
        });

        setVisible(true);

        // Save tasks button logic
        saveButton.addActionListener(e -> {
            saveTasksToFile();
            JOptionPane.showMessageDialog(inputPanel,
                    "Data has been saved.");
        });
    }

    private void loadTasksFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                Task t = new Task(parts[0]);
                if (parts.length > 1) {
                    t.completed = Boolean.parseBoolean(parts[1]);
                }
                taskListModel.addElement(t);
            }
        } catch (IOException e) {
            System.out.println("No saved task file found — starting fresh.");
        }
    }

    private void saveTasksToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 0; i < taskListModel.getSize(); i++) {
                Task t = taskListModel.get(i);
                writer.println(t.text + "|" + t.completed);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TaskTracker();
    }
}
