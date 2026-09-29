package HospitalManagementSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class HospitalManagementSystemUI extends JFrame {

    private static final String url = "jdbc:mysql://127.0.0.1:3306/hospital";
    private static final String username = "root";
    private static final String password = "Himanshu1234@";

    private Connection connection;
    private Patient patient;
    private Doctor doctor;

    public HospitalManagementSystemUI() {

        // Connect to database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            connection = DriverManager.getConnection(
                    url,
                    username,
                    password
            );

            // Objects for existing backend classes
            patient = new Patient(connection);
            doctor = new Doctor(connection);

        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Database connection failed:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Window settings
        setTitle("Hospital Management System");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        createUI();
    }

    private void createUI() {

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        );

        // Heading
        JLabel title = new JLabel(
                "HOSPITAL MANAGEMENT SYSTEM",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 26));

        mainPanel.add(title, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonPanel = new JPanel(
                new GridLayout(3, 2, 20, 20)
        );

        JButton addPatientButton = new JButton("Add Patient");
        JButton viewPatientsButton = new JButton("View Patients");
        JButton viewDoctorsButton = new JButton("View Doctors");
        JButton bookAppointmentButton = new JButton("Book Appointment");
        JButton exitButton = new JButton("Exit");

        buttonPanel.add(addPatientButton);
        buttonPanel.add(viewPatientsButton);
        buttonPanel.add(viewDoctorsButton);
        buttonPanel.add(bookAppointmentButton);
        buttonPanel.add(exitButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // Button actions
        addPatientButton.addActionListener(e -> showAddPatientForm());

        viewPatientsButton.addActionListener(e -> viewPatients());

        viewDoctorsButton.addActionListener(e -> viewDoctors());

        bookAppointmentButton.addActionListener(e -> showAppointmentForm());

        exitButton.addActionListener(e -> {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            System.exit(0);
        });

        add(mainPanel);
    }

    // --------------------------------------------------
    // ADD PATIENT
    // --------------------------------------------------

    private void showAddPatientForm() {

        JTextField nameField = new JTextField();
        JTextField ageField = new JTextField();
        JTextField genderField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));

        panel.add(new JLabel("Patient Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Age:"));
        panel.add(ageField);

        panel.add(new JLabel("Gender:"));
        panel.add(genderField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Add Patient",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            String name = nameField.getText();
            String gender = genderField.getText();

            try {

                int age = Integer.parseInt(ageField.getText());

                if (name.isEmpty() || gender.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Please fill all fields."
                    );
                    return;
                }

                boolean added = patient.addPatient(
                        name,
                        age,
                        gender
                );

                if (added) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Patient added successfully!"
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Patient could not be added."
                    );
                }

            } catch (NumberFormatException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Age must be a number.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    // --------------------------------------------------
    // VIEW PATIENTS
    // --------------------------------------------------

    private void viewPatients() {

        String[] columns = {
                "Patient ID",
                "Name",
                "Age",
                "Gender"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0);

        try {

            String query = "SELECT * FROM patients";

            PreparedStatement preparedStatement =
                    connection.prepareStatement(query);

            ResultSet resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");

                model.addRow(new Object[]{
                        id,
                        name,
                        age,
                        gender
                });
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        JFrame frame = new JFrame("Patients");

        frame.add(scrollPane);

        frame.setSize(600, 400);
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
    }

    // --------------------------------------------------
    // VIEW DOCTORS
    // --------------------------------------------------

    private void viewDoctors() {

        String[] columns = {
                "Doctor ID",
                "Name",
                "Specialization"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0);

        try {

            String query = "SELECT * FROM doctors";

            PreparedStatement preparedStatement =
                    connection.prepareStatement(query);

            ResultSet resultSet =
                    preparedStatement.executeQuery();

            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization =
                        resultSet.getString("specialization");

                model.addRow(new Object[]{
                        id,
                        name,
                        specialization
                });
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        JTable table = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(table);

        JFrame frame = new JFrame("Doctors");

        frame.add(scrollPane);

        frame.setSize(650, 400);
        frame.setLocationRelativeTo(this);
        frame.setVisible(true);
    }

    // --------------------------------------------------
    // BOOK APPOINTMENT
    // --------------------------------------------------

    private void showAppointmentForm() {

        JTextField patientIdField = new JTextField();
        JTextField doctorIdField = new JTextField();
        JTextField dateField = new JTextField();

        JPanel panel = new JPanel(
                new GridLayout(3, 2, 10, 10)
        );

        panel.add(new JLabel("Patient ID:"));
        panel.add(patientIdField);

        panel.add(new JLabel("Doctor ID:"));
        panel.add(doctorIdField);

        panel.add(new JLabel("Appointment Date (YYYY-MM-DD):"));
        panel.add(dateField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Book Appointment",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {

            try {

                int patientId =
                        Integer.parseInt(patientIdField.getText());

                int doctorId =
                        Integer.parseInt(doctorIdField.getText());

                String appointmentDate =
                        dateField.getText();

                if (!patient.getPatientById(patientId)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Patient does not exist."
                    );

                    return;
                }

                if (!doctor.getDoctorById(doctorId)) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Doctor does not exist."
                    );

                    return;
                }

                if (!HospitalManagementSystem.checkDoctorAvailability(
                        doctorId,
                        appointmentDate,
                        connection
                )) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Doctor is not available on this date."
                    );

                    return;
                }

                String query =
                        "INSERT INTO appointments " +
                        "(patient_id, doctor_id, appointment_date) " +
                        "VALUES (?, ?, ?)";

                PreparedStatement preparedStatement =
                        connection.prepareStatement(query);

                preparedStatement.setInt(1, patientId);
                preparedStatement.setInt(2, doctorId);
                preparedStatement.setString(3, appointmentDate);

                int rowsAffected =
                        preparedStatement.executeUpdate();

                if (rowsAffected > 0) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Appointment booked successfully!"
                    );

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Failed to book appointment."
                    );
                }

            } catch (NumberFormatException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Patient ID and Doctor ID must be numbers.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );

            } catch (SQLException e) {

                JOptionPane.showMessageDialog(
                        this,
                        e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    // --------------------------------------------------
    // MAIN
    // --------------------------------------------------

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            HospitalManagementSystemUI ui =
                    new HospitalManagementSystemUI();

            ui.setVisible(true);
        });
    }
}