package br.ufma.lsdi.simulator.senddata.windows;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DataSetFieldPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private String name;

	private int id;
	private JComboBox<String> cmb;
	private JComboBox<String> cmbType;
	
	public boolean isTimeStamp(){
		return cmb.getSelectedItem().toString().equals("timestamp");
	}
	public boolean check(){
		return cmb.getSelectedItem().toString().length()>1;
	}
	
	public String capability(){
		return cmb.getSelectedItem().toString();
	}
	
	public String type(){
		return cmbType.getSelectedItem().toString();
	}
	
	public DataSetFieldPanel(int id,String name,String list[]){
		this.setId(id);
		setLayout(new GridLayout(1,4));
		add(new JLabel("Field:"));
		add(new JLabel(name));

		String listType[]=new String[] {"string","int","double","unixdate","dateformat"};
		cmbType = new JComboBox<String>(listType);
		add(cmbType);
		cmb = new JComboBox<String>(list);
		add(cmb);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
