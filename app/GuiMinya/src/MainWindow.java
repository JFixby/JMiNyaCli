import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.BoxLayout;
import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JSpinner;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JToggleButton;
import javax.swing.JInternalFrame;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;
import javax.swing.JCheckBox;
import net.miginfocom.swing.MigLayout;
import javax.swing.border.CompoundBorder;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import javax.swing.JTable;
import javax.swing.JList;
import java.awt.List;
import java.awt.TextArea;
import java.awt.Color;
import java.awt.Canvas;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.SystemColor;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ScrollPaneConstants;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class MainWindow
{
	private App _application;
	///////////////////////////////////////////////////////
	
	public JFrame frmMinyagui;
	public JTextField worker_name;
	public JTextField worker_password;
	public JButton start_button;
	public JComboBox url_combobox;
	public JSpinner thread_spinner;
	JTextArea log_area;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmMinyagui.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return;
	}

	/**
	 * Create the application.
	 */
	public MainWindow()
	{
		initialize();
		this._application=new App(this);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		final int number_of_processor=Runtime.getRuntime().availableProcessors();;
		final MainWindow _t=this;
		frmMinyagui = new JFrame();
		
		frmMinyagui.getContentPane().setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
		frmMinyagui.setResizable(false);
		frmMinyagui.setTitle("GUIMiNya");
		frmMinyagui.setBounds(100, 100, 450, 387);
		frmMinyagui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frmMinyagui.setJMenuBar(menuBar);
		
		JMenu mnAbout = new JMenu("About");
		menuBar.add(mnAbout);
		
		JLabel lblNewLabel = new JLabel("Stratum Server address");
		lblNewLabel.setBounds(12, 10, 153, 13);
		
		JLabel lblWorkerSetting = new JLabel("Worker Setting");
		lblWorkerSetting.setBounds(12, 57, 112, 13);
		
		JLabel lblUser = new JLabel("Name");
		lblUser.setBounds(32, 79, 70, 13);
		
		worker_name = new JTextField();
		worker_name.setBounds(114, 76, 318, 19);
		worker_name.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(32, 101, 70, 13);
		
		worker_password = new JTextField();
		worker_password.setBounds(114, 101, 318, 19);
		worker_password.setColumns(10);
		
		JLabel lblMinerSetting = new JLabel("Miner setting");
		lblMinerSetting.setBounds(12, 126, 112, 13);
		
		JLabel lblNumberOfThread = new JLabel("Number of Thread");
		lblNumberOfThread.setBounds(32, 149, 105, 13);
		
		thread_spinner = new JSpinner(new SpinnerNumberModel(new Integer(number_of_processor), new Integer(1), new Integer(number_of_processor),new Integer(1)));
		thread_spinner.setBounds(149, 146, 57, 20);
		
		url_combobox = new JComboBox();
		url_combobox.setBounds(32, 29, 400, 19);
		url_combobox.setEditable(true);
		frmMinyagui.getContentPane().setLayout(null);
		frmMinyagui.getContentPane().add(lblWorkerSetting);
		frmMinyagui.getContentPane().add(lblNewLabel);
		frmMinyagui.getContentPane().add(url_combobox);
		frmMinyagui.getContentPane().add(lblPassword);
		frmMinyagui.getContentPane().add(worker_password);
		frmMinyagui.getContentPane().add(lblUser);
		frmMinyagui.getContentPane().add(worker_name);
		frmMinyagui.getContentPane().add(lblMinerSetting);
		frmMinyagui.getContentPane().add(lblNumberOfThread);
		frmMinyagui.getContentPane().add(thread_spinner);
		
		start_button = new JButton("New button");
		start_button.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0)
			{
				_t._application.onStartClick();
			}
		});
		frmMinyagui.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				_t._application.finish();
			}
		});
		
		start_button.setBounds(218, 145, 214, 21);
		frmMinyagui.getContentPane().add(start_button);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(12, 183, 420, 144);
		frmMinyagui.getContentPane().add(scrollPane);
		
		log_area = new JTextArea();
		log_area.setFont(new Font("MS UI Gothic", Font.PLAIN, 12));
		log_area.setEditable(false);
		scrollPane.setViewportView(log_area);
		
		frmMinyagui.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{lblNewLabel, lblWorkerSetting, lblUser, worker_name, lblPassword, worker_password, lblMinerSetting, lblNumberOfThread, thread_spinner}));
		frmMinyagui.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{frmMinyagui.getContentPane(), lblNewLabel, url_combobox, lblWorkerSetting, lblUser, worker_name, lblPassword, worker_password, lblMinerSetting, lblNumberOfThread, thread_spinner, menuBar, mnAbout}));
	}

	

	public void addLog(String i_msg)
	{
		//ログの追記
	}
}
