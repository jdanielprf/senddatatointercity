package br.ufma.lsdi.simulator.senddata.windows;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import br.ufma.lsdi.interscity.beans.Capability;
import br.ufma.lsdi.interscity.beans.Resource;
import br.ufma.lsdi.interscity.manager.ContextNet;
import br.ufma.lsdi.interscity.manager.SingletonManager;

public class MainWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabs;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public void init() {
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

	/**
	 * Create the frame.
	 */

	private JTextField url;
	private JTextField txtIP;
	private JTextField txtPort;
	public static ContextNet contextNet;

	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout());
		// ////////////////////
		url = new JTextField("http://cidadesinteligentes.lsdi.ufma.br/eq1");
		JPanel p = new JPanel(new BorderLayout());
		p.add(new JLabel("URL:"), BorderLayout.WEST);
		p.add(url, BorderLayout.CENTER);
		JButton btnConnect = new JButton(">>");
		btnConnect.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				checkConnect();
			}
		});
		p.add(btnConnect, BorderLayout.EAST);
		contentPane.add(p, BorderLayout.NORTH);
		// /////////////////
		JPanel panelContextNet=new JPanel(new GridLayout(1,8));
		txtIP=new  JTextField();
		txtIP.setSize(200, 40);
		txtPort=new  JTextField();
		txtIP.setSize(100, 40);
		panelContextNet.add(new JLabel("ContextNet "));
		panelContextNet.add(new JLabel("IP:"));
		panelContextNet.add(txtIP);
		panelContextNet.add(new JLabel("Port:"));
		panelContextNet.add(txtPort);
		JButton btnContextNet=new JButton("Connect");
		btnContextNet.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				connectContextNet();
			}
		});

		panelContextNet.add(btnContextNet);
		


		contentPane.add(panelContextNet,BorderLayout.SOUTH);
		// /////////////////
		tabs = new JTabbedPane();
		contentPane.add(tabs, BorderLayout.CENTER);
		setContentPane(contentPane);

	}

	public void connectContextNet() {
		try {
			String ip=txtIP.getText();
			int port=Integer.parseInt(txtPort.getText());
			contextNet= new ContextNet(ip,port);
			contextNet.connect();
		}catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Não foi possivel conectar");
		}
	}
	public void checkConnect() {
		try {
			tabs.setVisible(false);	
			SingletonManager.init(url.getText());
			Resource[] listResources = SingletonManager.get().resources().getAll();
			Capability[] listCapabilities = SingletonManager.get().capabilities().getAll();
			initTabs(listResources,listCapabilities);

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Erro!!");
			e.printStackTrace();
		}
	}

	public void initTabs(Resource[] listResources,Capability[] listCapabilities) {
		tabs.removeAll();
		tabs.setVisible(false);
		tabs.add("Resouces", new ResourceTablePanel(listResources));
		tabs.add("Capabilities", new CapabilityTablePanel(listCapabilities));
		tabs.add("Data", new ResourceDataTablePanel());
		tabs.add("DataSet 1", new DataSetPanel(listResources));
		tabs.add("DataSet 2", new DataSetPanel(listResources));
		tabs.add("DataSet 3", new DataSetPanel(listResources));
		tabs.add("DataSet 4", new DataSetPanel(listResources));
		tabs.add("DataSet 5", new DataSetPanel(listResources));
		tabs.setVisible(true);
		setVisible(true);
	}
}
