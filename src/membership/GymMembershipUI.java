package membership;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class GymMembershipUI extends JFrame {

    // Dark mode colors
    private static final Color DARK_BG = new Color(30, 30, 30);
    private static final Color DARK_PANEL = new Color(45, 45, 45);
    private static final Color DARK_FIELD = new Color(60, 60, 60);
    private static final Color ACCENT_COLOR = new Color(255, 87, 34);
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    private static final Color TEXT_SECONDARY = new Color(180, 180, 180);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color DANGER_COLOR = new Color(244, 67, 54);

    // Components
    private JTextField txtMemberId;
    private JTextField txtName;
    private JTextField txtPhone;
    private JTextField txtEmail;
    private JTextField txtSearch;
    private JComboBox<String> cmbMembershipType;
    private JComboBox<String> cmbStatus;
    private JTable memberTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;

    // Data storage
    private final ArrayList<Member> members;
    private int selectedRow = -1;
    private int nextId = 1;

    // Membership types
    private static final String[] MEMBERSHIP_TYPES = {"1 Day", "1 Month", "6 Months", "12 Months"};
    private static final String[] STATUS_OPTIONS = {"Active", "Expired"};

    public GymMembershipUI() {
        members = new ArrayList<>();
        initializeUI();
        loadSampleData();
    }

    private void initializeUI() {
        setTitle("MAGMAMANI, MINANI ANG PR - Gym Membership Management");
        setSize(900, 840);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Center content
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout(10, 10));
        centerPanel.setBackground(DARK_BG);

        JPanel formPanel = createFormPanel();
        JPanel tablePanel = createTablePanel();

        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(tablePanel, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(DARK_PANEL);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("MAGMAMANI, MINANI ANG PR");
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitle.setForeground(ACCENT_COLOR);

        JLabel lblSubtitle = new JLabel("Gym Membership Management System");
        lblSubtitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitle.setForeground(TEXT_SECONDARY);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridLayout(2, 1, 5, 5));
        titlePanel.setBackground(DARK_PANEL);
        titlePanel.add(lblTitle);
        titlePanel.add(lblSubtitle);

        headerPanel.add(titlePanel, BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BorderLayout(10, 10));
        formPanel.setBackground(DARK_PANEL);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DARK_FIELD, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblFormTitle = new JLabel("Member Information");
        lblFormTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblFormTitle.setForeground(TEXT_COLOR);
        formPanel.add(lblFormTitle, BorderLayout.NORTH);

        // Form fields panel
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new GridBagLayout());
        fieldsPanel.setBackground(DARK_PANEL);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1 - Member ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblMemberId = createLabel("Member ID:");
        fieldsPanel.add(lblMemberId, gbc);

        gbc.gridx = 1;
        txtMemberId = createTextField();
        txtMemberId.setEditable(false);
        txtMemberId.setText(String.valueOf(nextId));
        fieldsPanel.add(txtMemberId, gbc);

        gbc.gridx = 2;
        JLabel lblName = createLabel("Full Name:");
        fieldsPanel.add(lblName, gbc);

        gbc.gridx = 3;
        txtName = createTextField();
        fieldsPanel.add(txtName, gbc);

        // Row 2 - Phone and Email
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblPhone = createLabel("Phone:");
        fieldsPanel.add(lblPhone, gbc);

        gbc.gridx = 1;
        txtPhone = createTextField();
        fieldsPanel.add(txtPhone, gbc);

        gbc.gridx = 2;
        JLabel lblEmail = createLabel("Email:");
        fieldsPanel.add(lblEmail, gbc);

        gbc.gridx = 3;
        txtEmail = createTextField();
        fieldsPanel.add(txtEmail, gbc);

        // Row 3 - Membership Type and Status
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblMembershipType = createLabel("Membership Type:");
        fieldsPanel.add(lblMembershipType, gbc);

        gbc.gridx = 1;
        cmbMembershipType = createComboBox(MEMBERSHIP_TYPES);
        fieldsPanel.add(cmbMembershipType, gbc);

        gbc.gridx = 2;
        JLabel lblStatus = createLabel("Status:");
        fieldsPanel.add(lblStatus, gbc);

        gbc.gridx = 3;
        cmbStatus = createComboBox(STATUS_OPTIONS);
        fieldsPanel.add(cmbStatus, gbc);

        formPanel.add(fieldsPanel, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(DARK_PANEL);

        btnAdd = createButton("Add Member", SUCCESS_COLOR);
        btnUpdate = createButton("Update", ACCENT_COLOR);
        btnDelete = createButton("Delete", DANGER_COLOR);
        btnClear = createButton("Clear", DARK_FIELD);

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMember();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMember();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteMember();
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearForm();
            }
        });

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnClear);

        formPanel.add(buttonPanel, BorderLayout.SOUTH);

        return formPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(10, 10));
        tablePanel.setBackground(DARK_PANEL);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DARK_FIELD, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Header with search
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(DARK_PANEL);

        JLabel lblTableTitle = new JLabel("Members List");
        lblTableTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTableTitle.setForeground(TEXT_COLOR);

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(DARK_PANEL);

        JLabel lblSearch = createLabel("Search: ");
        txtSearch = createTextField();
        txtSearch.setPreferredSize(new Dimension(200, 30));
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                searchMembers(txtSearch.getText());
            }
        });

        searchPanel.add(lblSearch);
        searchPanel.add(txtSearch);

        headerPanel.add(lblTableTitle, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        tablePanel.add(headerPanel, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Name", "Phone", "Email", "Membership Type", "Start Date", "End Date", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        memberTable = new JTable(tableModel);
        memberTable.setBackground(DARK_FIELD);
        memberTable.setForeground(TEXT_COLOR);
        memberTable.setSelectionBackground(ACCENT_COLOR);
        memberTable.setSelectionForeground(TEXT_COLOR);
        memberTable.setGridColor(DARK_PANEL);
        memberTable.setRowHeight(30);
        memberTable.setFont(new Font("Arial", Font.PLAIN, 12));

        JTableHeader header = memberTable.getTableHeader();
        header.setBackground(DARK_BG);
        header.setForeground(TEXT_COLOR);
        header.setFont(new Font("Arial", Font.BOLD, 12));

        memberTable.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String status = value != null ? value.toString() : "";
                    if ("Active".equals(status)) {
                        setForeground(SUCCESS_COLOR);
                    } else {
                        setForeground(DANGER_COLOR);
                    }
                    setBackground(DARK_FIELD);
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });

        memberTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectMember();
            }
        });

        JScrollPane scrollPane = new JScrollPane(memberTable);
        scrollPane.setBackground(DARK_FIELD);
        scrollPane.getViewport().setBackground(DARK_FIELD);
        scrollPane.setBorder(BorderFactory.createLineBorder(DARK_FIELD));

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField(15);
        field.setBackground(DARK_FIELD);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(DARK_BG, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        return field;
    }

    private JComboBox<String> createComboBox(String[] items) {
        JComboBox<String> combo = new JComboBox<>(items);
        combo.setBackground(DARK_FIELD);
        combo.setForeground(TEXT_COLOR);
        combo.setFont(new Font("Arial", Font.PLAIN, 13));
        return combo;
    }

    private JButton createButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(TEXT_COLOR);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        return button;
    }

    private void addMember() {
        if (!validateForm()) return;

        String name = txtName.getText().trim();
        String phone = txtPhone.getText().trim();
        String email = txtEmail.getText().trim();
        String membershipType = (String) cmbMembershipType.getSelectedItem();
        String status = (String) cmbStatus.getSelectedItem();

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = calculateEndDate(startDate, membershipType);

        Member member = new Member(nextId++, name, phone, email, membershipType, startDate, endDate, status);
        members.add(member);

        addMemberToTable(member);
        clearForm();

        JOptionPane.showMessageDialog(this, "Member added successfully!", "Success",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateMember() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member to update!", "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validateForm()) return;

        int id = Integer.parseInt(txtMemberId.getText());
        Member member = findMemberById(id);

        if (member != null) {
            member.setName(txtName.getText().trim());
            member.setPhone(txtPhone.getText().trim());
            member.setEmail(txtEmail.getText().trim());
            member.setMembershipType((String) cmbMembershipType.getSelectedItem());
            member.setStatus((String) cmbStatus.getSelectedItem());
            member.setEndDate(calculateEndDate(member.getStartDate(), member.getMembershipType()));

            refreshTable();
            clearForm();

            JOptionPane.showMessageDialog(this, "Member updated successfully!", "Success",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteMember() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a member to delete!", "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete this member?", "Confirm Delete",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            int id = Integer.parseInt(txtMemberId.getText());
            for (int i = 0; i < members.size(); i++) {
                if (members.get(i).getId() == id) {
                    members.remove(i);
                    break;
                }
            }
            refreshTable();
            clearForm();

            JOptionPane.showMessageDialog(this, "Member deleted successfully!", "Success",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void clearForm() {
        txtMemberId.setText(String.valueOf(nextId));
        txtName.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        cmbMembershipType.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
        selectedRow = -1;
        memberTable.clearSelection();
    }

    private void selectMember() {
        selectedRow = memberTable.getSelectedRow();
        if (selectedRow >= 0) {
            txtMemberId.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtName.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtPhone.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtEmail.setText(tableModel.getValueAt(selectedRow, 3).toString());
            cmbMembershipType.setSelectedItem(tableModel.getValueAt(selectedRow, 4).toString());
            cmbStatus.setSelectedItem(tableModel.getValueAt(selectedRow, 7).toString());
        }
    }

    private boolean validateForm() {
        if (txtName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter member name!", "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            txtName.requestFocus();
            return false;
        }
        if (txtPhone.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter phone number!", "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            txtPhone.requestFocus();
            return false;
        }
        return true;
    }

    private LocalDate calculateEndDate(LocalDate startDate, String membershipType) {
        if (membershipType == null) return startDate.plusMonths(1);
        switch (membershipType) {
            case "1 Day":
                return startDate.plusDays(1);
            case "1 Month":
                return startDate.plusMonths(1);
            case "6 Months":
                return startDate.plusMonths(6);
            case "12 Months":
                return startDate.plusMonths(12);
            default:
                return startDate.plusMonths(1);
        }
    }

    private void addMemberToTable(Member member) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Object[] row = new Object[8];
        row[0] = member.getId();
        row[1] = member.getName();
        row[2] = member.getPhone();
        row[3] = member.getEmail();
        row[4] = member.getMembershipType();
        row[5] = member.getStartDate().format(formatter);
        row[6] = member.getEndDate().format(formatter);
        row[7] = member.getStatus();
        tableModel.addRow(row);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (int i = 0; i < members.size(); i++) {
            addMemberToTable(members.get(i));
        }
    }

    private void searchMembers(String keyword) {
        tableModel.setRowCount(0);
        String lowerKeyword = keyword.toLowerCase();

        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            if (member.getName().toLowerCase().contains(lowerKeyword) ||
                member.getPhone().contains(keyword) ||
                member.getEmail().toLowerCase().contains(lowerKeyword)) {
                addMemberToTable(member);
            }
        }
    }

    private Member findMemberById(int id) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getId() == id) {
                return members.get(i);
            }
        }
        return null;
    }

    private void loadSampleData() {
        members.add(new Member(nextId++, "Juan Dela Cruz", "09171234567", "juan@email.com",
            "1 Month", LocalDate.now().minusDays(15), LocalDate.now().plusDays(15), "Active"));
        members.add(new Member(nextId++, "Maria Santos", "09181234567", "maria@email.com",
            "6 Months", LocalDate.now().minusMonths(2), LocalDate.now().plusMonths(4), "Active"));
        members.add(new Member(nextId++, "Pedro Reyes", "09191234567", "pedro@email.com",
            "12 Months", LocalDate.now().minusMonths(13), LocalDate.now().minusMonths(1), "Expired"));
        members.add(new Member(nextId++, "Ana Garcia", "09201234567", "ana@email.com",
            "1 Day", LocalDate.now(), LocalDate.now().plusDays(1), "Active"));

        refreshTable();
    }

    // Member inner class
    private static class Member {
        private final int id;
        private String name;
        private String phone;
        private String email;
        private String membershipType;
        private final LocalDate startDate;
        private LocalDate endDate;
        private String status;

        Member(int id, String name, String phone, String email, String membershipType,
               LocalDate startDate, LocalDate endDate, String status) {
            this.id = id;
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.membershipType = membershipType;
            this.startDate = startDate;
            this.endDate = endDate;
            this.status = status;
        }

        public int getId() { return id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getMembershipType() { return membershipType; }
        public void setMembershipType(String membershipType) { this.membershipType = membershipType; }
        public LocalDate getStartDate() { return startDate; }
        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}

