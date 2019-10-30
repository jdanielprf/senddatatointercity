package br.ufma.lsdi.simulator.senddata.windows;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import br.ufma.lsdi.interscity.beans.Capability;
import br.ufma.lsdi.interscity.manager.SingletonManager;

public class CapabilityTablePanel extends JPanel {


	private static final long serialVersionUID = 1L;
	private Capability[] list;
	public CapabilityTablePanel(Capability[] listCapabilities) {
		this.list=listCapabilities;
		init();
	}

	public Object[][] getData() {
		String[][] dados = new String[list.length][6];
		for (int i = 0; i < list.length; i++) {
			dados[i][0]= list[i].getId();
			dados[i][1]= list[i].getName();
			dados[i][2]= ""+list[i].getDescription();
	
		}
		return dados;
	}

	public void removeCapability() {
		if(tabela.getSelectedRow()>=0) {
			String name=tabela.getValueAt(tabela.getSelectedRow(), 1).toString();
			SingletonManager.get().capabilities().dateleCapability(name);
			JOptionPane.showMessageDialog(this, "Recurso removido!");
			list=SingletonManager.get().capabilities().getAll();
			init();
		}else {
			JOptionPane.showMessageDialog(this, "Selecione uma linha");
		}
	}
	

	private JTable tabela;
	public void init() {
		removeAll();
		setVisible(false);
		setLayout(new BorderLayout());
		String[] colunas = { "ID", "Name", "Description" };
		Object dados[][]=getData();
		tabela = new JTable(dados, colunas);

		JScrollPane barraRolagem = new JScrollPane(tabela);
		JButton btnRemove=new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				removeCapability();
			}
		});
		add(btnRemove,BorderLayout.NORTH);
		add(barraRolagem,BorderLayout.CENTER);
		


		
		setVisible(true);
		
	}
}
