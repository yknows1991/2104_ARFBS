package Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class AppointmentForm implements ActionListener 
{
    JFrame AppointmentForm = new JFrame("DCF Dental Clinic");
    JButton scheduleBtn = new JButton("SCHEDULE APPOINTMENT");
    JPanel content = new BackgroundPanel("Background (2).png");  // Use custom panel with background
    
    // Header components
    JButton homeBtn = new JButton("HOME");
    JButton aboutUsBtn = new JButton("ABOUT US");
    JButton servicesBtn = new JButton("SERVICES");
    JButton appointmentBtn = new JButton("APPOINTMENT");
    JButton productsBtn = new JButton("PRODUCTS");
    JButton contactUsBtn = new JButton("CONTACT US");
    JPanel header = new JPanel();

    // Form input fields
    JTextField nameField = new JTextField(20);
    JTextField emailField = new JTextField(20);
    JTextField phoneNumberField = new JTextField(20);
    JTextField dateField = new JTextField(20);
    JTextField timeField = new JTextField(20);
    JTextField reasonField = new JTextField(20);

    // Message label for feedback
    JLabel messageLabel = new JLabel("");

    AppointmentForm() 
    {
        // Set window icon
        ImageIcon image = new ImageIcon(getClass().getResource("DCFlogo.png"));
        AppointmentForm.setIconImage(image.getImage());
        AppointmentForm.setLayout(new BorderLayout());
        AppointmentForm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        AppointmentForm.setSize(1440, 1024);

        // Header setup
        header.setLayout(new BorderLayout());
        header.setPreferredSize(new Dimension(100, 100));
        header.setBackground(new Color(5, 59, 67));

        // Header logo
        ImageIcon headerLogo = new ImageIcon(getClass().getResource("haederLogo.png"));
        Image scaledHeader = headerLogo.getImage().getScaledInstance(250, 150, Image.SCALE_SMOOTH);
        JLabel Header = new JLabel(new ImageIcon(scaledHeader));

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        logoPanel.setBackground(new Color(5, 59, 67));
        logoPanel.add(Header);
        header.add(logoPanel, BorderLayout.WEST);

        // Navigation buttons
        JPanel navPanel = new JPanel(new GridLayout(1, 6, 10, 0)); 
        navPanel.setBackground(new Color(5, 59, 67));

        // Set styles for buttons
        setButtonStyles(homeBtn);
        setButtonStyles(aboutUsBtn);
        setButtonStyles(servicesBtn);
        setButtonStyles(appointmentBtn);
        setButtonStyles(productsBtn);
        setButtonStyles(contactUsBtn);
        
        navPanel.add(homeBtn);
        navPanel.add(aboutUsBtn);
        navPanel.add(servicesBtn);
        navPanel.add(appointmentBtn);
        navPanel.add(productsBtn);
        navPanel.add(contactUsBtn);

        header.add(navPanel, BorderLayout.CENTER);
        AppointmentForm.add(header, BorderLayout.NORTH);  // Add header to the top

        // Content area with background
        content.setLayout(new GridBagLayout());  // Center-align content
        content.setBackground(new Color(5, 59, 67)); // Dark teal background

        // Appointment Form Panel setup
        JPanel formPanel = new JPanel();
        formPanel.setPreferredSize(new Dimension(500, 600));
        formPanel.setBackground(Color.WHITE);  // White background for form area
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));  // Padding for form content
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        // Form Title
        JLabel titleLabel = new JLabel("APPOINTMENT REQUEST FORM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        formPanel.add(titleLabel);

        // Form Fields
        formPanel.add(createFormField("NAME", nameField));
        formPanel.add(createFormField("EMAIL", emailField));
        formPanel.add(createFormField("PHONE NUMBER", phoneNumberField));
        formPanel.add(createFormField("DATE (MONTH / DAY / YEAR)", dateField));
        formPanel.add(createFormField("TIME", timeField));
        formPanel.add(createFormField("PLEASE LET US KNOW THE REASON OF YOUR BOOKING", reasonField));

        // Schedule Appointment Button
        scheduleBtn.setFont(new Font("Arial", Font.BOLD, 18));
        scheduleBtn.setForeground(Color.WHITE);
        scheduleBtn.setBackground(new Color(5, 59, 67));  // Match background color
        scheduleBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        scheduleBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        scheduleBtn.addActionListener(this);
        formPanel.add(Box.createVerticalStrut(20));  // Add spacing before button
        formPanel.add(scheduleBtn);

        // Add message label
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(messageLabel);

        // Add form panel to content area
        content.add(formPanel);
        AppointmentForm.add(content, BorderLayout.CENTER);

        // Make frame visible
        AppointmentForm.setVisible(true);
    }

    // Method to set button styles
    private void setButtonStyles(JButton button) 
    {
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(5, 59, 67));
        button.setFocusable(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.addActionListener(this);
    }

    // Helper method to create labeled form fields
    private JPanel createFormField(String labelText, JTextField textField) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BorderLayout());
        fieldPanel.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.BLACK);

        textField.setPreferredSize(new Dimension(300, 30));
        textField.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        fieldPanel.add(label, BorderLayout.NORTH);
        fieldPanel.add(textField, BorderLayout.CENTER);
        fieldPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));  // Spacing between fields

        return fieldPanel;
    }

    @Override
    public void actionPerformed(ActionEvent e) 
    {
        if (e.getSource() == scheduleBtn) 
        {
            // Get input values from fields
            String name = nameField.getText();
            String email = emailField.getText();
            String phoneNumber = phoneNumberField.getText();
            String date = dateField.getText();
            String time = timeField.getText();
            String reason = reasonField.getText();

            try 
            {               	
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection
                (
                    "jdbc:mysql://localhost:3306/dcfdentalclinicdb", 
                    "admin", 
                    "admin"
                );
                
                PreparedStatement ps = con.prepareStatement("INSERT INTO appointments (Name, Email, PhoneNumber, Date, Time, Reason) VALUES (?, ?, ?, ?, ?, ?)");
                ps.setString(1, name);
                ps.setString(2, email);
                ps.setString(3, phoneNumber);
                ps.setString(4, date);
                ps.setString(5, time);
                ps.setString(6, reason);
                int rowsAffected = ps.executeUpdate();

                if (rowsAffected > 0) 
                {
                	messageLabel.setText("Appointment scheduled successfully!");
                	AppointmentForm.dispose();
                    
                } 
                else 
                {
                    messageLabel.setText("Failed to schedule appointment. Please try again.");
                }

            } 
            catch (Exception ex) 
            {
                ex.printStackTrace(); // Prints detailed error in the console
                messageLabel.setText("Error: " + ex.getMessage());
            }
        } 
        else if (e.getSource() == homeBtn) 
        {
            AppointmentForm.dispose();
            new homePage();
        } 
        else if (e.getSource() == aboutUsBtn) 
        {
            AppointmentForm.dispose();
            new aboutUs();
        }
    }

    // Custom JPanel for Background Image
    class BackgroundPanel extends JPanel 
    {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) 
        {
            backgroundImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        }

        @Override
        protected void paintComponent(Graphics g) 
        {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public static void main(String[] args) 
    {
        new AppointmentForm();
    }
}
