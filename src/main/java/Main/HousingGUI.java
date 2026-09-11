package Main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Entity.Housing;

import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class HousingGUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private JTextField idField = new JTextField();
    private JTextField nameField = new JTextField();
    private JTextField unitField = new JTextField();
    private JTextField districtField = new JTextField();
    private JTextField areaField = new JTextField();
    private JTextField ownerField = new JTextField();

    private JTextField keywordField = new JTextField(18);

    private JTable table;
    private DefaultTableModel model;

    public HousingGUI() {

        setTitle("社會住宅管理系統（JTable版）");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initForm();
        initTable();
        initButtons();
        initTableClickEvent();

        loadTable();
    }

    private void initForm() {

        JPanel form = new JPanel(new GridLayout(6, 2, 5, 5));

        idField.setEditable(false);
        nameField.setEditable(false);
        unitField.setEditable(false);
        districtField.setEditable(false);
        areaField.setEditable(false);
        ownerField.setEditable(false);

        form.add(new JLabel("ID"));
        form.add(idField);

        form.add(new JLabel("社宅名稱"));
        form.add(nameField);

        form.add(new JLabel("戶數"));
        form.add(unitField);

        form.add(new JLabel("行政區"));
        form.add(districtField);

        form.add(new JLabel("面積(平方公尺)"));
        form.add(areaField);

        form.add(new JLabel("主辦單位"));
        form.add(ownerField);

        add(form, BorderLayout.NORTH);
    }

    private void initTable() {

        model = new DefaultTableModel(
                new String[]{"ID", "社宅名稱", "戶數", "行政區", "面積(平方公尺)", "主辦單位"}, 0
        ) {
            /**
			 * 
			 */
			private static final long serialVersionUID = 7478581950581195701L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void initButtons() {

        JPanel btnPanel = new JPanel();
        JButton searchBtn = new JButton("查詢");
        JButton addBtn = new JButton("新增");
        JButton updateBtn = new JButton("修改");
        JButton deleteBtn = new JButton("刪除");
        JButton cancelBtn = new JButton("重新整理");
        

        btnPanel.add(new JLabel("查詢關鍵字"));
        btnPanel.add(keywordField);

        btnPanel.add(searchBtn);
        btnPanel.add(cancelBtn);
        btnPanel.add(addBtn);
        btnPanel.add(updateBtn);
        btnPanel.add(deleteBtn);
        
        

        add(btnPanel, BorderLayout.SOUTH);
        
        searchBtn.addActionListener(e -> searchByKeyword());
        cancelBtn.addActionListener(e -> cancelSearch());
        addBtn.addActionListener(e -> openAddDialog());
        updateBtn.addActionListener(e -> openUpdateDialog());
        deleteBtn.addActionListener(e -> deleteByInputId());
        
        
    }

    private void initTableClickEvent() {

        table.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int row = table.getSelectedRow();

                if (row >= 0) {
                    idField.setText(model.getValueAt(row, 0).toString());
                    nameField.setText(model.getValueAt(row, 1).toString());
                    unitField.setText(model.getValueAt(row, 2).toString());
                    districtField.setText(model.getValueAt(row, 3).toString());
                    areaField.setText(model.getValueAt(row, 4).toString());
                    ownerField.setText(model.getValueAt(row, 5).toString());
                }
            }
        });
    }

    private void openAddDialog() {

        HousingFormPanel formPanel = new HousingFormPanel();

        int result = JOptionPane.showConfirmDialog(
                this,
                formPanel,
                "新增社會住宅資料",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            formPanel.validateInput();

            HousingService.create(
                    formPanel.getHousingName(),
                    formPanel.getHouseholdCount(),
                    formPanel.getDistrict(),
                    formPanel.getAreaSquareMeter(),
                    formPanel.getOrganizer()
            );

            JOptionPane.showMessageDialog(this, "新增成功");

            loadTable();
            clearTopForm();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "戶數請輸入整數，面積請輸入數字");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "新增失敗：" + ex.getMessage());
        }
    }

    private void openUpdateDialog() {

        String idText = idField.getText().trim();

        if (idText.isEmpty()) {
            idText = JOptionPane.showInputDialog(this, "請輸入要修改的 ID");

            if (idText == null) {
                return;
            }

            idText = idText.trim();
        }

        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "請先選擇資料或輸入 ID");
            return;
        }

        try {
            int id = Integer.parseInt(idText);

            Housing selectedHousing = findHousingById(id);

            if (selectedHousing == null) {
                JOptionPane.showMessageDialog(this, "查無此 ID：" + id);
                return;
            }

            HousingFormPanel formPanel = new HousingFormPanel(selectedHousing);

            int result = JOptionPane.showConfirmDialog(
                    this,
                    formPanel,
                    "修改社會住宅資料",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE
            );

            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            formPanel.validateInput();

            HousingService.update(
                    id,
                    formPanel.getHousingName(),
                    formPanel.getHouseholdCount(),
                    formPanel.getDistrict(),
                    formPanel.getAreaSquareMeter(),
                    formPanel.getOrganizer()
            );

            JOptionPane.showMessageDialog(this, "修改成功");

            loadTable();
            showHousingOnTopForm(id);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID、戶數請輸入整數，面積請輸入數字");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "修改失敗：" + ex.getMessage());
        }
    }
    private void deleteByInputId() {

        String idText = JOptionPane.showInputDialog(this, "請輸入要刪除的 ID");

        if (idText == null) {
            return;
        }

        idText = idText.trim();

        if (idText.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID 不可空白");
            return;
        }

        try {
            int id = Integer.parseInt(idText);

            int result = JOptionPane.showConfirmDialog(
                    this,
                    "確定要刪除 ID = " + id + " 的整筆資料嗎？",
                    "刪除確認",
                    JOptionPane.YES_NO_OPTION
            );

            if (result != JOptionPane.YES_OPTION) {
                return;
            }

            boolean deleted = HousingService.delete(id);

            if (deleted) {
                JOptionPane.showMessageDialog(this, "刪除成功");
            } else {
                JOptionPane.showMessageDialog(this, "刪除失敗：資料庫找不到 ID = " + id);
            }

            loadTable();
            clearTopForm();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID 請輸入整數");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "刪除失敗：" + ex.getMessage());
        }
    }
    private void cancelSearch() {
        keywordField.setText("");
        table.clearSelection();
        clearTopForm();
        loadTable();
    }

    private void searchByKeyword() {

        String keyword = keywordField.getText().trim();

        if (keyword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "請輸入查詢關鍵字");
            return;
        }

        try {
            model.setRowCount(0);
            clearTopForm();

            List<Housing> list = HousingService.searchByKeyword(keyword);

            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(this, "查無相關資料");
                return;
            }

            addHousingRows(list);

            if (list.size() == 1) {
                showHousingOnTopForm(list.get(0));
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "查詢失敗：" + ex.getMessage());
        }
    }

    public void loadTable() {

        try {
            model.setRowCount(0);

            List<Housing> list = HousingService.findAll();

            addHousingRows(list);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "查詢全部失敗：" + ex.getMessage());
        }
    }

    private void addHousingRows(List<Housing> list) {

        for (Housing h : list) {
            model.addRow(new Object[]{
                    h.getId(),
                    h.getHousingName(),
                    h.getHouseholdCount(),
                    h.getDistrict(),
                    h.getAreaSquareMeter(),
                    h.getOrganizer()
            });
        }
    }

    private Housing findHousingById(int id) {

        List<Housing> list = HousingService.findAll();

        for (Housing h : list) {
            if (h.getId() == id) {
                return h;
            }
        }

        return null;
    }

    private void showHousingOnTopForm(int id) {

        Housing housing = findHousingById(id);

        if (housing != null) {
            showHousingOnTopForm(housing);
        }
    }

    private void showHousingOnTopForm(Housing housing) {

        idField.setText(String.valueOf(housing.getId()));
        nameField.setText(housing.getHousingName());
        unitField.setText(String.valueOf(housing.getHouseholdCount()));
        districtField.setText(housing.getDistrict());
        areaField.setText(String.valueOf(housing.getAreaSquareMeter()));
        ownerField.setText(housing.getOrganizer());
    }

    private void clearTopForm() {

        idField.setText("");
        nameField.setText("");
        unitField.setText("");
        districtField.setText("");
        areaField.setText("");
        ownerField.setText("");
    }

    private static class HousingFormPanel extends JPanel {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
	
		private JTextField nameField = new JTextField(20);
        private JTextField unitField = new JTextField(20);
        private JTextField districtField = new JTextField(20);
        private JTextField areaField = new JTextField(20);
        private JTextField ownerField = new JTextField(20);

        public HousingFormPanel() {
            init();
        }

        public HousingFormPanel(Housing housing) {
            init();

            nameField.setText(housing.getHousingName());
            unitField.setText(String.valueOf(housing.getHouseholdCount()));
            districtField.setText(housing.getDistrict());
            areaField.setText(String.valueOf(housing.getAreaSquareMeter()));
            ownerField.setText(housing.getOrganizer());
        }

        private void init() {

            setLayout(new GridLayout(5, 2, 8, 8));

            add(new JLabel("社宅名稱"));
            add(nameField);

            add(new JLabel("戶數"));
            add(unitField);

            add(new JLabel("行政區"));
            add(districtField);

            add(new JLabel("面積(平方公尺)"));
            add(areaField);

            add(new JLabel("主辦單位"));
            add(ownerField);
        }

        public void validateInput() {

            if (getHousingName().isEmpty()) {
                throw new IllegalArgumentException("社宅名稱不可空白");
            }

            if (unitField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("戶數不可空白");
            }

            if (getDistrict().isEmpty()) {
                throw new IllegalArgumentException("行政區不可空白");
            }

            if (areaField.getText().trim().isEmpty()) {
                throw new IllegalArgumentException("面積不可空白");
            }

            if (getOrganizer().isEmpty()) {
                throw new IllegalArgumentException("主辦單位不可空白");
            }

            Integer.parseInt(unitField.getText().trim());
            Double.parseDouble(areaField.getText().trim());
        }

        public String getHousingName() {
            return nameField.getText().trim();
        }

        public int getHouseholdCount() {
            return Integer.parseInt(unitField.getText().trim());
        }

        public String getDistrict() {
            return districtField.getText().trim();
        }

        public double getAreaSquareMeter() {
            return Double.parseDouble(areaField.getText().trim());
        }

        public String getOrganizer() {
            return ownerField.getText().trim();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HousingGUI gui = new HousingGUI();
            gui.setVisible(true);
        });
    }
}
