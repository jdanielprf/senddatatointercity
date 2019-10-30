package br.ufma.lsdi.simulator.senddata.windows;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import br.ufma.lsdi.interscity.beans.Resource;
import br.ufma.lsdi.interscity.manager.SingletonManager;

public class ResourceTablePanel extends JPanel {


	private static final long serialVersionUID = 1L;
	private Resource[] list;
	public ResourceTablePanel(Resource[] list) {
		this.list=list;
		init();
	}

	public Object[][] getData() {
		String[][] dados = new String[list.length][6];
		for (int i = 0; i < list.length; i++) {
			dados[i][0]= list[i].getUuid();
			dados[i][1]= list[i].getDescription();
			dados[i][2]= ""+list[i].getStatus();
			dados[i][3]= ""+list[i].getLat();
			dados[i][4]= ""+list[i].getLon();
			dados[i][5]= ""+Arrays.toString(list[i].getCapabilities());
		}
		return dados;
	}

	private ResourceJFrame frame;
	private long time=0;
	public void init() {
		setLayout(new BorderLayout());
		String[] colunas = { "UUID", "Description","Status", "Lat","Log", "Capabality" };
		Object dados[][]=getData();
		final JTable tabela = new JTable(dados, colunas);

		JScrollPane barraRolagem = new JScrollPane(tabela);
		
		JButton btn=new JButton("Add resource");
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				new ResourceJFrame().start();
			}
		});
		add(btn,BorderLayout.NORTH);
		add(barraRolagem,BorderLayout.CENTER);
		
		tabela.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			public void valueChanged(ListSelectionEvent e) {
				String uuid=tabela.getValueAt(tabela.getSelectedRow(), 0).toString();
				if(time+1000>System.currentTimeMillis()) {
					return;
				}
				time=System.currentTimeMillis();
				if(frame==null||!frame.isVisible()) {
					
					Resource resource = SingletonManager.get().resources().getResource(uuid);
					frame=new ResourceJFrame();
					frame.load(resource);
					frame.start();
				}
			}
		});


	}
}
