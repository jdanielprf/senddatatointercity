package br.ufma.lsdi.simulator.senddata.windows;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import br.ufma.lsdi.interscity.beans.CapabilityValue;
import br.ufma.lsdi.interscity.beans.Resource;
import br.ufma.lsdi.interscity.manager.SingletonManager;

public class ResourceDataTablePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceDataTablePanel() {

		init();
	}

	public Object[][] getData(Resource r) throws Exception{
		
		List<CapabilityValue> data = SingletonManager.get().data().getData(r.getUuid());
		Object values[][]=new Object[data.size()][3];
		int i=0;
		for (Iterator<CapabilityValue> iterator = data.iterator(); iterator.hasNext();) {
			CapabilityValue capabilityValue = (CapabilityValue) iterator.next();
			values[i][0]=capabilityValue.getName();
			values[i][1]=capabilityValue.getValue();
			values[i++][2]=capabilityValue.getTimeStamp();
		}
		return values;
	}

	private JComboBox<Resource> cmb;

	public void init() {
		setLayout(new BorderLayout());
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JLabel("Resource:"), BorderLayout.WEST);
		Resource[] list = SingletonManager.get().resources().getAll();
		cmb = new JComboBox<Resource>(list);
		cmb.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				Resource r=(Resource) cmb.getSelectedItem();
				loadtable(r) ;
			}
		});
		panel.add(cmb, BorderLayout.CENTER);

		add(panel, BorderLayout.NORTH);
		tablePanel = new JPanel(new GridLayout(1,1));
		add(tablePanel, BorderLayout.CENTER);
	}
	private JPanel tablePanel;

	public void loadtable(Resource r) {
		try {
		tablePanel.removeAll();
		tablePanel.setVisible(false);
		Object dados[][] = getData(r);
		
		if(dados!=null&&dados.length>0) {
			
			String[] colunas = { "Capability", "Value", "Date" };
			JTable tabela = new JTable(dados, colunas);
			JScrollPane barraRolagem = new JScrollPane(tabela);
			tablePanel.add(barraRolagem);
			tablePanel.setVisible(true);
			setVisible(true);
		}
		}catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Dados não encontrados");
		}
	}
}
