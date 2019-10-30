package br.ufma.lsdi.simulator.senddata.windows;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.ufma.lsdi.interscity.beans.Capability;
import br.ufma.lsdi.interscity.beans.Resource;
import br.ufma.lsdi.interscity.manager.SingletonManager;

public class ResourceJFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceJFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		init();
	}

	JTextField txtDescription,txtLat,txtLon;
	JComboBox<String> cmbStatus;
	List<JComboBox<String>> capabilities;
	private JTextField txtUuid;
	
	public void init() {
		Capability[] listCapabilities = SingletonManager.get().capabilities().getAll();
		String listC[]=new String[listCapabilities.length+1];
		listC[0]="";
		for (int i = 0; i < listCapabilities.length; i++) {
			listC[i+1]=listCapabilities[i].getName();
		}
		
		JPanel panel = new JPanel(new GridLayout( 25,1));

		panel.add(new JLabel("UUID:"));
		txtUuid = new JTextField();
		txtUuid.setEnabled(false);
		panel.add(txtUuid);
		
		panel.add(new JLabel("Description:"));
		txtDescription = new JTextField();
		panel.add(txtDescription);

		panel.add(new JLabel("Lat:"));
		txtLat = new JTextField();
		panel.add(txtLat);

		panel.add(new JLabel("Long:"));
		txtLon = new JTextField();
		panel.add(txtLon);
		
		panel.add(new JLabel("Status:"));
		cmbStatus = new JComboBox<String>(new String[]{"active","inactive"});
		panel.add(cmbStatus);

		panel.add(new JLabel("Capabilities:"));
		capabilities=new ArrayList<JComboBox<String>>();
		for (int i = 0; i < 10; i++) {
			JComboBox<String> cmb=new JComboBox<String>(listC);
			capabilities.add(cmb);
			panel.add(cmb);
		}
		
		JButton txtSave = new JButton("Save");
		txtSave.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				save();
			}
		});
		panel.add(txtSave);

		setContentPane(panel);
	}
	
	public void load(Resource resource) {
		txtUuid.setText(resource.getUuid());
		txtDescription.setText(resource.getDescription());
		txtLat.setText(""+resource.getLat());
		txtLon.setText(""+resource.getLon());
		for (int i = 0; i < resource.getCapabilities().length; i++) {
			capabilities.get(i).setSelectedItem( resource.getCapabilities()[i]);
		}
		
	}
	
	public void save() {
		
		Resource resource=new Resource();
		resource.setDescription(txtUuid.getText());
		resource.setDescription(txtDescription.getText());
		resource.setLat(Double.parseDouble(txtLat.getText()));
		resource.setLon(Double.parseDouble(txtLon.getText()));
		resource.setStatus(cmbStatus.getSelectedItem().toString());
		
		List<String> cap=new ArrayList<String>();
		for (Iterator<JComboBox<String>> iterator = capabilities.iterator(); iterator.hasNext();) {
			JComboBox<String> jComboBox = (JComboBox<String>) iterator.next();
			if(jComboBox.getSelectedItem().toString().length()>0) {
				cap.add(jComboBox.getSelectedItem().toString());
			}
		}
		
		resource.setCapabilities(cap.toArray(new String[cap.size()]));
		
		try {
			SingletonManager.get().resources().createOrUpdateResource(resource);
			JOptionPane.showMessageDialog(this, "Recurso criado");
		}catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Não foi possivel criar recurso");
		}
	}

	public void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
