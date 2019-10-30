package br.ufma.lsdi.simulator.senddata.windows;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

import br.ufma.lsdi.interscity.beans.CapabilityValue;
import br.ufma.lsdi.interscity.beans.Resource;
import br.ufma.lsdi.interscity.manager.ContextNetSingleton;
import br.ufma.lsdi.interscity.manager.DateUtil;
import br.ufma.lsdi.interscity.manager.SingletonManager;

public class DataSetPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<DataSetFieldPanel> listPanels = new ArrayList<DataSetFieldPanel>();

	private Resource listResource[];
	private JComboBox<Resource> txtUuid;
	private JTextField txtDate, txtHour;

	private boolean sendToContextNet=false;
	
	public String[] listCmb() {
		Resource r = (Resource) txtUuid.getSelectedItem();
		String[] list = Arrays.copyOf(r.getCapabilities(), r.getCapabilities().length + 2);
		list[list.length - 2] = "timestamp";
		list[list.length - 1] = "";
		Arrays.sort(list);
		return list;

	}

	public DataSetPanel(Resource list[]) {
		listResource = list;
		init();
	}

	private JTextField txtFile;
	private JTextField txtSeparator;
	private JTextField txtDateStart;
	private JTextField txtHourStart;
	private JTextField txtInterval;
	private JCheckBox chkStream;

	public void init() {
		setLayout(new GridLayout(20, 1));

		JPanel panelFile = new JPanel(new BorderLayout());
		panelFile.add(new JLabel("File"), BorderLayout.WEST);
		txtFile = new JTextField("                  ");
		panelFile.add(txtFile, BorderLayout.CENTER);
		JButton btnChoice = new JButton("Open");
		btnChoice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				open();
			}
		});

		panelFile.add(btnChoice, BorderLayout.EAST);
		// ////////////////////////////
		JPanel uuidPanel = new JPanel(new BorderLayout());
		uuidPanel.add(new JLabel("UUID:"), BorderLayout.WEST);
		txtUuid = new JComboBox<Resource>(listResource);
		txtUuid.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				struture(txtFile.getText());
			}
		});
		uuidPanel.add(txtUuid, BorderLayout.CENTER);
		// ////////////////////////////
		JPanel datePanel = new JPanel(new BorderLayout());

		datePanel.add(new JLabel("Data:"), BorderLayout.WEST);
		txtDate = new JTextField("yyyy-MM-dd");
		datePanel.add(txtDate, BorderLayout.CENTER);
		txtHour = new JTextField("hh:mm:ss.SSS");
		datePanel.add(txtHour, BorderLayout.EAST);
		// ////////////////////////////
		JPanel separatorPanel = new JPanel(new BorderLayout());
		separatorPanel.add(new JLabel("Separador:"), BorderLayout.WEST);
		txtSeparator = new JTextField(",");
		separatorPanel.add(txtSeparator, BorderLayout.CENTER);
		// ////////////////////////////
		JPanel timePanel = new JPanel(new GridLayout(1,9));
		timePanel.add(new JLabel("Start date "));
		timePanel.add(new JLabel("Date:"));
		
		txtDateStart = new JTextField("2010-10-10");
		timePanel.add(txtDateStart);
		
		timePanel.add(new JLabel("Time:"));
		
		txtHourStart = new JTextField("10:10:10.101");
		timePanel.add(txtHourStart);
		
		timePanel.add(new JLabel("Interval:"));
		
		txtInterval = new JTextField("1001");
		timePanel.add(txtInterval);
		
		timePanel.add(new JLabel("Stream:"));
		chkStream = new JCheckBox();
		timePanel.add(chkStream);
		// ////////////////////////////
		JButton enviar = new JButton("Send to InterSCity");
		enviar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					enviar();
			}
		});
		
		add(enviar);
		
		JButton enviarContextNet = new JButton("Send to ContextNet");
		enviarContextNet.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				enviar();
			}
		});
		add(enviarContextNet);
		
		add(panelFile);
		add(uuidPanel);
		add(datePanel);
		add(separatorPanel);
		add(timePanel);
		setVisible(true);
	}
	


	public void stream() {
		long interval=Integer.parseInt(txtInterval.getText());
		
		ArrayList<CapabilityValue> values = getData();
		if(values==null) {
			return ;
		}
		final String uuid = ((Resource) txtUuid.getSelectedItem()).getUuid();
		final Iterator<CapabilityValue> iterator=values.iterator();
		final Timer t=new Timer();
		t.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				if(iterator.hasNext()) {
					CapabilityValue val = iterator.next();
					List<CapabilityValue> list=new ArrayList<CapabilityValue>();
					list.add(val);
					SingletonManager.get().data().sendDataResource(uuid, list);
					System.out.println(val);
				}else {
					t.cancel();
				}
				
			}
		}, 0,interval );
	}
	
	public void send(String uuid,ArrayList<CapabilityValue> values) {
		if(sendToContextNet) {
			if(ContextNetSingleton.check()) {
				try {
					ContextNetSingleton.get().sendCapabilityValues(uuid, values);
				}catch (Exception e) {
					JOptionPane.showMessageDialog(this, "Erro conection ContextNet");
				}
			}else {
				JOptionPane.showMessageDialog(this, "Erro conection ContextNet");
			}
		}else {
			System.out.println(Arrays.toString(values.toArray()));
			SingletonManager.get().data().sendDataResource(uuid, values);
		}
	}
	
	public void batch() {
		ArrayList<CapabilityValue> values = getData();
//		if(values.size()>500) {
//			JOptionPane.showMessageDialog(this, "Utilize a opção stream para enviar uma quantidade massiva de dados!");
//			return;
//		}
		if(values!=null) {
			Resource r = (Resource) txtUuid.getSelectedItem();
			send(r.getUuid(), values);
		}
	}
	public void enviar() {
		if(chkStream.isSelected()) {
			stream();
		}else {
			batch();
		}
	}

	public Object convertType(String type,String value) throws Exception {
		if (type.equals("int")) {
			return Integer.parseInt(value);
		} else if (type.equals("double")) {
			return Double.parseDouble(value);
		} else if (type.equals("dateformat")) {
			 return DateUtil.convertStr(txtDate.getText(),txtHour.getText(),value);
		} else if (type.equals("unixdate")) {
			return DateUtil.unixDate(value);
		} else {
			return value;
		}
	}

	public ArrayList<CapabilityValue> getData() {
		
		
		File f = new File(txtFile.getText());
		try {
			Date startDate=DateUtil.strToDate(txtDate.getText(), txtHour.getText(), txtDateStart.getText(), txtHourStart.getText());
			int millisec=Integer.parseInt(txtInterval.getText());
			
			ArrayList<CapabilityValue> values = new ArrayList<CapabilityValue>();
			Scanner scan = new Scanner(f);
			scan.nextLine();
			int timeStampCollum=-1;
			for (Iterator<DataSetFieldPanel> iterator = listPanels.iterator(); iterator.hasNext();) {
				DataSetFieldPanel capabilityValue = (DataSetFieldPanel) iterator.next();
				if(capabilityValue.isTimeStamp()) {
					timeStampCollum=listPanels.indexOf(capabilityValue);
					break;
				}
			}
			
			while (scan.hasNextLine()) {
				String line[] = scan.nextLine().split(txtSeparator.getText());
				
				for (int i = 0; i < line.length; i++) {
					if (listPanels.get(i).check()&&!listPanels.get(i).isTimeStamp()) {
						CapabilityValue value = new CapabilityValue();
						value.setName(listPanels.get(i).capability());
						
						if(timeStampCollum>=0) {
							Date d=(Date) convertType(listPanels.get(timeStampCollum).type(), line[timeStampCollum]);
							value.setTimeStamp(DateUtil.convertDate(d));
						}else {
							startDate=DateUtil.addHoursToJavaUtilDate(startDate, millisec);
							value.setTimeStamp(DateUtil.convertDate(startDate));
						}
						Object v=convertType(listPanels.get(i).type(), line[i]);
						value.setValue(v);						

						values.add(value);
						System.out.println(value);
					}
				}
			}
			
			scan.close();
			return values;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void removeAll() {

		while (listPanels.size() > 0) {
			remove(listPanels.get(0));
			listPanels.remove(0);
		}

	}

	public void struture(String strFile) {
		File file = new File(strFile);
		try {
			removeAll();
			Scanner scan = new Scanner(file);
			if (scan.hasNext()) {
				String line = scan.nextLine();
				String fileds[] = line.split(txtSeparator.getText());
				for (int i = 0; i < fileds.length; i++) {
					DataSetFieldPanel data = new DataSetFieldPanel(i, fileds[i], listCmb());
					add(data);
					listPanels.add(data);
				}
				setVisible(false);
				setVisible(true);

			}
			scan.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void open() {
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setDialogTitle("Choose a directory to save your file: ");
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

		int returnValue = jfc.showSaveDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			if (jfc.getSelectedFile().isFile()) {
				txtFile.setText(jfc.getSelectedFile().getAbsolutePath());
				struture(txtFile.getText());
			}
		}

	}
}
