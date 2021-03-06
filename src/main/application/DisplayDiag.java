package application;

import engine.EIon;
import engine.MSystem;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class DisplayDiag extends JFrame {

	private JTable tablewaterout;
	private JTable tablesystemout;
	private JTable tableworking;
	private JTable tablecalc;
	java.text.DecimalFormat df2 = new java.text.DecimalFormat("0.00");
	MSystem msystem;
	private JTable table_para;

	public DisplayDiag(MSystem msystem) {
		try {
			this.msystem = msystem;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(getContentPane(), e.getMessage(), "错误！", JOptionPane.ERROR_MESSAGE);
		}
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setTitle("输出界面");
		setMinimumSize(new Dimension(650, 500));
		int w = (Toolkit.getDefaultToolkit().getScreenSize().width - getWidth()) / 2;
		int h = (Toolkit.getDefaultToolkit().getScreenSize().height - getHeight()) / 2;
		setLocation(w, h);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		jtabbedPaneFocusEvent(tabbedPane);
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(tabbedPane,
				GroupLayout.DEFAULT_SIZE, 644, Short.MAX_VALUE));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(tabbedPane,
				GroupLayout.DEFAULT_SIZE, 493, Short.MAX_VALUE));

		JPanel panel = new JPanel();
		tabbedPane.addTab("系统报告输出", null, panel, null);

		String[] name_1 = { "排列", "压力容器数", "膜元件数", "进水压力(MPa)", "产水压力(MPa)", "浓水压力(MPa)", "进水流量(m3/h)", "产水流量(m3/h)",
				"浓水流量(m3/h)", "通量(LMH)", "最大水通量(LMH)" };
		Object[][] data_1 = new Object[msystem.section() + 1][11];
		TableModel tablemodel_1 = new DefaultTableModel(data_1, name_1);
		tablesystemout = new JTable(tablemodel_1) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		for (int i = 1; i < tablesystemout.getRowCount(); i++) {
			tablemodel_1.setValueAt("1-" + (i), i, 0);
			tablemodel_1.setValueAt(msystem.sections()[i - 1].parNVi, i, 1);
			tablemodel_1.setValueAt(msystem.sections()[i - 1].parEi, i, 2);
			tablemodel_1.setValueAt(df2.format(msystem.sections()[i - 1].streamf.parP), i, 3);
			tablemodel_1.setValueAt(df2.format(msystem.sections()[i - 1].streamp.parP), i, 4);
			tablemodel_1.setValueAt(df2.format(msystem.sections()[i - 1].streamc.parP), i, 5);
			tablemodel_1.setValueAt(df2.format(msystem.sections()[i - 1].streamf.parQ), i, 6);
			tablemodel_1.setValueAt(df2.format(msystem.sections()[i - 1].streamp.parQ), i, 7);
			tablemodel_1.setValueAt(df2.format(msystem.sections()[i - 1].streamc.parQ), i, 8);
			try {
				tablemodel_1.setValueAt(df2.format(msystem.sections()[i - 1].parFi()), i, 9);
				tablemodel_1.setValueAt(df2.format(msystem.sections()[i - 1].parFimax()), i, 10);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(getContentPane(), e.getMessage(), "错误！", JOptionPane.ERROR_MESSAGE);
			}
		}
		for (int i = 0; i < tablesystemout.getColumnCount(); i++) {
			tablemodel_1.setValueAt(name_1[i], 0, i);
		}
		tableFocusEvent(tablesystemout);
		JScrollPane scrollPane_1 = new JScrollPane(tablesystemout);
		JTableHeader heade_1 = tablesystemout.getTableHeader();
		heade_1.setPreferredSize(new Dimension(heade_1.getWidth(), 0));// 表头高度设置
		tablesystemout.getTableHeader().setReorderingAllowed(false);
		tablesystemout.getTableHeader().setVisible(false);
		tablesystemout.setRowHeight(0, 30);
		TableModel tablemodel_para = new DefaultTableModel(4, 4);
		table_para = new JTable(tablemodel_para) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		String[] colname_0 = { "产水流量", "产水回收率", "循环流量", "进水压力" };
		String[] colname_2 = { "平均水通量", "温度", "总膜元件数", "总膜面积" };
		for (int i = 0; i < table_para.getRowCount(); i++) {
			table_para.setRowHeight(20);
			tablemodel_para.setValueAt(colname_0[i], i, 0);
			tablemodel_para.setValueAt(colname_2[i], i, 2);
		}
		tablemodel_para.setValueAt(String.format("%.1f", msystem.pariQp) + " m3/h", 0, 1);
		tablemodel_para.setValueAt(String.format("%.1f", msystem.pariY) + " %", 1, 1);
		tablemodel_para.setValueAt(String.format("%.1f", msystem.pariQr) + " m3/h", 2, 1);
		tablemodel_para.setValueAt(String.format("%.3f", msystem.streamf.parP) + " MPa", 3, 1);
		try {
			tablemodel_para.setValueAt(String.format("%.2f", msystem.pariJ()) + " LMH", 0, 3);
		} catch (Exception e) {
		}
		tablemodel_para.setValueAt(String.format("%.1f", msystem.streams.parT()) + " ℃", 1, 3);
		int tempmonum = 0;
		int temparea = 0;
		for (int i = 0; i < msystem.sections().length; i++) {
			tempmonum += msystem.sections()[i].parEi * msystem.sections()[i].parNVi;
			temparea += msystem.sections()[i].branes()[0].parSE * msystem.sections()[i].parEi
					* msystem.sections()[i].parNVi;
		}
		tablemodel_para.setValueAt(tempmonum + " 支", 2, 3);
		tablemodel_para.setValueAt(temparea + " m2", 3, 3);
		JScrollPane scrollPane_para = new JScrollPane(table_para);
		JTableHeader heade_para = table_para.getTableHeader();
		heade_para.setPreferredSize(new Dimension(heade_1.getWidth(), 0));// 表头高度设置
		table_para.getTableHeader().setReorderingAllowed(false);
		table_para.getTableHeader().setVisible(false);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane_para, GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
								.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE))
				.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addGap(23)
						.addComponent(scrollPane_para, GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE).addGap(33)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
						.addContainerGap(64, Short.MAX_VALUE)));
		panel.setLayout(gl_panel);
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("水质报告输出", null, panel_1, null);
		tablewaterout = new JTable(EIon.values().length + 3 + msystem.streams.cods().length,
				2 * msystem.section() + 2) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		DefaultTableModel tablemodel = (DefaultTableModel) tablewaterout.getModel();
		tablemodel.setValueAt("指标", 0, 0);
		for (int n2 = 0; n2 < msystem.section(); n2++) {
			tablemodel.setValueAt((n2 + 1) + "段 产水(mg/L)", 0, 2 * n2 + 1);
			tablemodel.setValueAt((n2 + 1) + "段 浓水(mg/L)", 0, 2 * n2 + 2);
		}
		tablemodel.setValueAt("总产水(mg/L)", 0, tablewaterout.getColumnCount() - 1);
		for (int m = 0; m < EIon.values().length; m++) {
			tablemodel.setValueAt(msystem.sections()[0].streamp.ion(EIon.values()[m]).name, m + 1, 0);
			for (int n = 0; n < msystem.section(); n++) {
				tablemodel.setValueAt(df2.format(msystem.sections()[n].streamp.ion(EIon.values()[m]).parcj()), m + 1,
						2 * n + 1);
				tablemodel.setValueAt(df2.format(msystem.sections()[n].streamc.ion(EIon.values()[m]).parcj()), m + 1,
						2 * n + 2);
			}
			tablemodel.setValueAt(df2.format(msystem.streamp.ion(EIon.values()[m]).parcj()), m + 1,
					2 * msystem.section() + 1);
		}
		tablemodel.setValueAt("TDS", EIon.values().length + 1, 0);
		tablemodel.setValueAt("pH", EIon.values().length + 2, 0);
		for (int t = 0; t < msystem.section(); t++) {
			tablemodel.setValueAt(df2.format(msystem.sections()[t].streamp.tds()), EIon.values().length + 1, 2 * t + 1);
			tablemodel.setValueAt(df2.format(msystem.sections()[t].streamc.tds()), EIon.values().length + 1, 2 * t + 2);

			tablemodel.setValueAt(df2.format(msystem.sections()[t].streamp.parpH()), EIon.values().length + 2,
					2 * t + 1);
			tablemodel.setValueAt(df2.format(msystem.sections()[t].streamc.parpH()), EIon.values().length + 2,
					2 * t + 2);
			for (int j = 0; j < msystem.streams.cods().length; j++) {
				tablemodel.setValueAt(msystem.sections()[t].streamp.cods()[j].name, EIon.values().length + 3 + j, 0);
				tablemodel.setValueAt(df2.format(msystem.sections()[t].streamp.cods()[j].parcj),
						EIon.values().length + 3 + j, 2 * t + 1);
				tablemodel.setValueAt(df2.format(msystem.sections()[t].streamc.cods()[j].parcj),
						EIon.values().length + 3 + j, 2 * t + 2);
				tablemodel.setValueAt(df2.format(msystem.streamp.cods()[j].parcj), EIon.values().length + 3 + j,
						2 * msystem.section() + 1);
			}
		}
		tablemodel.setValueAt(df2.format(msystem.streamp.tds()), EIon.values().length + 1, 2 * msystem.section() + 1);
		tablemodel.setValueAt(df2.format(msystem.streamp.parpH()), EIon.values().length + 2, 2 * msystem.section() + 1);
		for (int i = 0; i < tablewaterout.getColumnCount(); i++) {
			tablewaterout.getColumnModel().getColumn(i).setPreferredWidth(102);
		}
		JTableHeader heade = tablewaterout.getTableHeader();
		heade.setPreferredSize(new Dimension(heade.getWidth(), 0));// 表头高度设置
		tablewaterout.getTableHeader().setReorderingAllowed(false);
		tablewaterout.getTableHeader().setVisible(false);
		tablewaterout.setRowHeight(0, 30);// 指定2行的高度30
		tableFocusEvent(tablewaterout);
		JScrollPane scrollPane = new JScrollPane(tablewaterout);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1
				.setHorizontalGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING,
						gl_panel_1.createSequentialGroup().addContainerGap()
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
								.addContainerGap()));
		gl_panel_1
				.setVerticalGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_1.createSequentialGroup().addContainerGap()
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
								.addContainerGap()));
		panel_1.setLayout(gl_panel_1);

		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("元件工作条件输出", null, panel_2, null);
		int containnum = msystem.sections()[0].parEi;
		for (int i = 1; i < msystem.section(); i++) {
			containnum += msystem.sections()[i].parEi;
		}
		String[] name_2 = { "元件位置", "回收率", "膜通量(LMH)", "进水流量(m3/h)", "产水流量(m3/h)", "浓水流量(m3/h)" };
		Object[][] data_2 = new Object[containnum + 1][6];
		TableModel tablemodel_2 = new DefaultTableModel(data_2, name_2);
		tableworking = new JTable(tablemodel_2) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		ArrayList<Integer> rows1 = new ArrayList<>();
		ArrayList<Integer> cols1 = new ArrayList<>();
		int num = 0;
		for (int i = 0; i < msystem.section(); i++) {
			if (i > 0) {
				num += msystem.sections()[i - 1].parEi;
			}
			for (int j = 0; j < msystem.sections()[i].parEi; j++) {
				tablemodel_2.setValueAt("第" + (i + 1) + "段  " + "第" + (j + 1) + "支", num + j + 1, 0);
				tablemodel_2.setValueAt(df2.format(msystem.sections()[i].branes()[j].parYt()), num + j + 1, 1);
				tablemodel_2.setValueAt(df2.format(msystem.sections()[i].branes()[j].parFi()), num + j + 1, 2);
				tablemodel_2.setValueAt(df2.format(msystem.sections()[i].branes()[j].streamf.parQ), num + j + 1, 3);
				tablemodel_2.setValueAt(df2.format(msystem.sections()[i].branes()[j].streamp.parQ), num + j + 1, 4);
				tablemodel_2.setValueAt(df2.format(msystem.sections()[i].branes()[j].streamc.parQ), num + j + 1, 5);
				if (msystem.sections()[i].branes()[j].parFi() > 30) {
					rows1.add(num + j + 1);
					cols1.add(2);
				}
				if (msystem.sections()[i].branes()[j].streamf.parQ > 15) {
					rows1.add(num + j + 1);
					cols1.add(3);
				}
				if (msystem.sections()[i].branes()[j].streamc.parQ < 3) {
					rows1.add(num + j + 1);
					cols1.add(5);
				}
			}
		}
		tableworking.setDefaultRenderer(Object.class, new myTableCellRenderer(rows1, cols1));
		tableFocusEvent(tableworking);
		for (int i = 0; i < tableworking.getColumnCount(); i++) {
			tablemodel_2.setValueAt(name_2[i], 0, i);
		}
		JTableHeader heade_2 = tableworking.getTableHeader();
		heade_2.setPreferredSize(new Dimension(heade_2.getWidth(), 0));// 表头高度设置
		tableworking.getTableHeader().setReorderingAllowed(false);
		tableworking.getTableHeader().setVisible(false);
		tableworking.setRowHeight(0, 30);// 指定2行的高度30
		JScrollPane scrollPane_2 = new JScrollPane(tableworking);
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2
				.setHorizontalGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING,
						gl_panel_2.createSequentialGroup().addContainerGap()
								.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
								.addContainerGap()));
		gl_panel_2
				.setVerticalGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_2.createSequentialGroup().addContainerGap()
								.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
								.addContainerGap()));
		panel_2.setLayout(gl_panel_2);

		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("元件结垢预测", null, panel_3, null);
		String[] name_3 = { "元件位置", "CaCO3(L.I.)", "CaSO4", "Ca3(PO4)2", "BaSO4", "SrSO4", "CaF2" };
		Object[][] data_3 = new Object[containnum + 1][7];
		TableModel tablemodel_3 = new DefaultTableModel(data_3, name_3);
		tablecalc = new JTable(tablemodel_3) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		ArrayList<Integer> rows2 = new ArrayList<>();
		ArrayList<Integer> cols2 = new ArrayList<>();
		int ct = 0;
		for (int i = 0; i < msystem.section(); i++) {
			if (i > 0) {
				ct += msystem.sections()[i - 1].parEi;
			}
			for (int j = 0; j < msystem.sections()[i].parEi; j++) {
				tablemodel_3.setValueAt("第" + (i + 1) + "段  " + "第" + (j + 1) + "支", ct + j + 1, 0);
				tablemodel_3.setValueAt(df2.format(msystem.sections()[i].branes()[j].streamc.parSCaCO3()), ct + j + 1,
						1);
				tablemodel_3.setValueAt(df2.format(msystem.sections()[i].branes()[j].streamc.parSCaSO4() * 100) + " %",
						ct + j + 1, 2);
				tablemodel_3.setValueAt(
						df2.format(msystem.sections()[i].branes()[j].streamc.parSCa3PO42() * 100) + " %", ct + j + 1,
						3);
				tablemodel_3.setValueAt(df2.format(msystem.sections()[i].branes()[j].streamc.parSBaSO4() * 100) + " %",
						ct + j + 1, 4);
				tablemodel_3.setValueAt(df2.format(msystem.sections()[i].branes()[j].streamc.parSSrSO4() * 100) + " %",
						ct + j + 1, 5);
				tablemodel_3.setValueAt(df2.format(msystem.sections()[i].branes()[j].streamc.parSCaF2() * 100) + " %",
						ct + j + 1, 6);
				if (msystem.sections()[i].branes()[j].streamc.parSCaCO3() > 0) {
					rows2.add(ct + j + 1);
					cols2.add(1);
				}
				if (msystem.sections()[i].branes()[j].streamc.parSCaSO4() > 1) {
					rows2.add(ct + j + 1);
					cols2.add(2);
				}
				if (msystem.sections()[i].branes()[j].streamc.parSCa3PO42() > 1) {
					rows2.add(ct + j + 1);
					cols2.add(3);
				}
				if (msystem.sections()[i].branes()[j].streamc.parSBaSO4() > 1) {
					rows2.add(ct + j + 1);
					cols2.add(4);
				}
				if (msystem.sections()[i].branes()[j].streamc.parSSrSO4() > 1) {
					rows2.add(ct + j + 1);
					cols2.add(5);
				}
				if (msystem.sections()[i].branes()[j].streamc.parSCaF2() > 1) {
					rows2.add(ct + j + 1);
					cols2.add(6);
				}
			}
		}
		tableFocusEvent(tablecalc);
		tablecalc.setDefaultRenderer(Object.class, new myTableCellRenderer(rows2, cols2));
		for (int i = 0; i < tablecalc.getColumnCount(); i++) {
			tablemodel_3.setValueAt(name_3[i], 0, i);
		}
		JTableHeader heade_3 = tablecalc.getTableHeader();
		heade_3.setPreferredSize(new Dimension(heade_3.getWidth(), 0));// 表头高度设置
		tablecalc.getTableHeader().setReorderingAllowed(false);
		tablecalc.getTableHeader().setVisible(false);
		tablecalc.setRowHeight(0, 30);// 指定2行的高度30
		tablecalc.getColumnModel().getColumn(0).setPreferredWidth(100);
		JScrollPane scrollPane_3 = new JScrollPane(tablecalc);
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3
				.setHorizontalGroup(gl_panel_3.createParallelGroup(Alignment.TRAILING).addGroup(Alignment.LEADING,
						gl_panel_3.createSequentialGroup().addContainerGap()
								.addComponent(scrollPane_3, GroupLayout.DEFAULT_SIZE, 611, Short.MAX_VALUE)
								.addContainerGap()));
		gl_panel_3
				.setVerticalGroup(gl_panel_3.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_3.createSequentialGroup().addContainerGap()
								.addComponent(scrollPane_3, GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
								.addContainerGap()));
		panel_3.setLayout(gl_panel_3);
		getContentPane().setLayout(groupLayout);
	}

	public void tableFocusEvent(JTable jt) {
		jt.setFocusable(true);
		jt.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					dispose();
				}
			}
		});
	}

	public void jtabbedPaneFocusEvent(JTabbedPane jtp) {
		jtp.setFocusable(true);
		jtp.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					dispose();
				}
			}
		});
	}
}

class myTableCellRenderer implements TableCellRenderer {
	DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
	ArrayList<Integer> rows, columns;

	public myTableCellRenderer(ArrayList<Integer> rows, ArrayList<Integer> columns) {
		this.rows = rows;
		this.columns = columns;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component renderer = dtcr.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (!isSelected) {
			renderer.setBackground(Color.WHITE);
		}
		for (int i = 0; i < rows.size(); i++) {
			if (rows.get(i) == row && columns.get(i) == column) {
				renderer.setBackground(new Color(176, 23, 31));
			}
		}
		return renderer;
	}
}