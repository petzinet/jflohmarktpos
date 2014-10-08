/**
 * 
 */
package de.petzi_net.jflohmarkt.pos;

import java.awt.AWTKeyStroke;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.petzi_net.jflohmarkt.pos.laf.POSLookAndFeel;
import de.petzi_net.jflohmarkt.pos.table.ReceiptModel;
import de.petzi_net.jflohmarkt.pos.table.ReceiptTable;
import de.petzi_net.jflohmarkt.pos.valuetype.ChecksumValueType;
import de.petzi_net.jflohmarkt.pos.valuetype.IntegerValueType;

/**
 * @author axel
 *
 */
@SuppressWarnings("serial")
public class JFlohmarktPOS extends JFrame {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				try {
					UIManager.setLookAndFeel(new POSLookAndFeel());
				} catch (UnsupportedLookAndFeelException e) {
					throw new RuntimeException(e);
				}
				new JFlohmarktPOS();
			}
			
		});
	}
	
	private final static int CASHIER_NUMBER_LENGTH = 3;
	
	private final ReceiptStorage receiptStorage;
	private final BasePanel modules;
	private final ButtonGroup actionButtonGroup;
	private final LoginPanel loginPanel;
	private final ActionButton logout;
	
	private JFlohmarktPOS() {
		super("JFlohmarkt POS");
		JFrame.setDefaultLookAndFeelDecorated(false);
		setUndecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1024, 768);
		
		Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
			
			@Override
			public void uncaughtException(Thread t, Throwable e) {
				e.printStackTrace();
				System.exit(-1);
			}
			
		});
		
		BasePanel mainPane = new BasePanel(new BorderLayout(0, 0));
		mainPane.setOpaque(true);
		mainPane.setBackground(Color.LIGHT_GRAY);
		mainPane.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		
		BasePanel contentPanel = new BasePanel(new BorderLayout(5, 5));
		
		ReceiptModel receiptModel = new ReceiptModel();
		
		ReceiptTable table = new ReceiptTable(receiptModel);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		contentPanel.add(scrollPane, BorderLayout.CENTER);
		
		receiptStorage = new ReceiptStorage();
		
		modules = new BasePanel(new CardLayout(0, 0));
		
		SalePanel salePanel = new SalePanel(receiptModel, receiptStorage);
		modules.add(salePanel, "sale");
		DropoffPanel dropoffPanel = new DropoffPanel(receiptModel, receiptStorage);
		modules.add(dropoffPanel, "dropoff");
		PickupPanel pickupPanel = new PickupPanel(receiptModel, receiptStorage);
		modules.add(pickupPanel, "pickup");
		InventoryPanel inventoryPanel = new InventoryPanel(receiptModel, receiptStorage);
		modules.add(inventoryPanel, "inventory");
		
		contentPanel.add(modules, BorderLayout.EAST);
		
		mainPane.add(contentPanel, BorderLayout.CENTER);
		
		BasePanel controlPanel = new BasePanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		actionButtonGroup = new ButtonGroup();
		
		ActionToggleButton sale = new ActionToggleButton("Verkauf", Color.GREEN);
		controlPanel.add(sale);
		actionButtonGroup.add(sale);
		sale.addActionListener(new ModuleSwitcher(salePanel));
		ActionToggleButton dropoff = new ActionToggleButton("Einlage", Color.YELLOW);
		controlPanel.add(dropoff);
		actionButtonGroup.add(dropoff);
		dropoff.addActionListener(new ModuleSwitcher(dropoffPanel));
		ActionToggleButton pickup = new ActionToggleButton("Leerung", Color.CYAN);
		controlPanel.add(pickup);
		actionButtonGroup.add(pickup);
		pickup.addActionListener(new ModuleSwitcher(pickupPanel));
		ActionToggleButton inventory = new ActionToggleButton("Bestand", Color.ORANGE);
		controlPanel.add(inventory);
		actionButtonGroup.add(inventory);
		inventory.addActionListener(new ModuleSwitcher(inventoryPanel));
		
		loginPanel = new LoginPanel(sale);
		modules.add(loginPanel, "login");
		SettingsPanel settingsPanel = new SettingsPanel();
		modules.add(settingsPanel, "settings");
		
		logout = new ActionButton("Abmelden", Color.RED);
		controlPanel.add(logout);
		logout.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				receiptStorage.setCashierNumber(0);
				for (Component comp : modules.getComponents()) {
					if (comp instanceof JComponent) {
						((JComponent) comp).putClientProperty(PROPERTY_LAST_FOCUS_OWNER, null);
					}
					if (comp instanceof ModulePanel<?>) {
						((ModulePanel<?>) comp).cancel();
					}
				}
				actionButtonGroup.clearSelection();
				logout.setEnabled(false);
				loginPanel.showIt();
			}
			
		});
		logout.setEnabled(false);
		BasePanel infoPanel = new BasePanel(new FlowLayout());
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
		infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));
		JLabel nameLabel = new JLabel("JFlohmarkt POS");
		nameLabel.setFont(new Font("Dialog", Font.BOLD, 32));
		nameLabel.setForeground(Color.BLACK);
		nameLabel.setAlignmentX(1.0f);
		infoPanel.add(nameLabel);
		JLabel versionLabel = new JLabel("Version 0.1");
		versionLabel.setFont(new Font("Dialog", Font.PLAIN, 10));
		versionLabel.setForeground(Color.BLACK);
		versionLabel.setAlignmentX(1.0f);
		infoPanel.add(versionLabel);
		controlPanel.add(infoPanel);
		
		mainPane.add(controlPanel, BorderLayout.NORTH);
		
		setContentPane(mainPane);
		
		KeyboardFocusManager.setCurrentKeyboardFocusManager(new ExtendedKeyboardFocusManager());
		Set<AWTKeyStroke> keyStrokes = new HashSet<AWTKeyStroke>(KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
		keyStrokes.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_ENTER, 0));
		KeyboardFocusManager.getCurrentKeyboardFocusManager().setDefaultFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, keyStrokes);
		
		if (receiptStorage.getPosNumber() > 0) {
			loginPanel.showIt();
		} else {
			settingsPanel.showIt();
		}
		
		setVisible(true);
	}
	
	private static final String PROPERTY_LAST_FOCUS_OWNER = ModuleSwitcher.class.getName() + ".lastFocusOwner";
	
	private class ModuleSwitcher implements ActionListener {

		private final ModulePanel<?> modulePanel;
		
		public ModuleSwitcher(ModulePanel<?> modulePanel) {
			this.modulePanel = modulePanel;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
			for (Component comp : modulePanel.getParent().getComponents()) {
				if (comp.isVisible()) {
					if (comp instanceof JComponent) {
						((JComponent) comp).putClientProperty(PROPERTY_LAST_FOCUS_OWNER, focusOwner);
					}
				}
				if (comp == modulePanel) {
					comp.setVisible(true);
				} else {
					comp.setVisible(false);
					if (comp instanceof ModulePanel<?>) {
						((ModulePanel<?>) comp).cancel();
					}
				}
			}
			focusOwner = (Component) modulePanel.getClientProperty(PROPERTY_LAST_FOCUS_OWNER);
			if (focusOwner != null) {
				focusOwner.requestFocusInWindow();
			} else {
				KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
			}
		}
		
	}
	
	public static void flashFrame(Component component) {
		Frame frame = JOptionPane.getFrameForComponent(component);
		if (frame instanceof JFrame) {
			JRootPane rootPane = ((JFrame) frame).getRootPane();
			final Component glassPane = rootPane.getGlassPane();
			glassPane.setBackground(new Color(255, 255, 255, 255));
			if (glassPane instanceof JComponent)
				((JComponent) glassPane).setOpaque(true);
			SwingUtilities.invokeLater(new FrameFlashHelper(glassPane, 6));
		}
	}
	
	private static class FrameFlashHelper implements Runnable {
		
		private final Component glassPane;
		private final int outstandingSteps;
		
		public FrameFlashHelper(Component glassPane, int outstandingSteps) {
			this.glassPane = glassPane;
			this.outstandingSteps = outstandingSteps;
		}
		
		@Override
		public void run() {
			Toolkit.getDefaultToolkit().beep();
			glassPane.setVisible(!glassPane.isVisible());
			if (outstandingSteps - 1 > 0) {
				SwingUtilities.invokeLater(new FrameFlashHelper(glassPane, outstandingSteps - 1));
			}
		}
		
	}
	
	public class SettingsPanel extends BasePanel {

		private final ControlTextField<Integer> posNumber;
		private final ControlTextField<Integer> lastReceiptNumber;
		
		public SettingsPanel() {
			super(new BorderLayout(0, 0));
			
			BasePanel controlPanel = new BasePanel(new FlowLayout());
			controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
			add(controlPanel, BorderLayout.CENTER);
			add(new NumPadPanel(), BorderLayout.SOUTH);
			
			controlPanel.add(new ControlLabel("Kassennummer"));
			posNumber = new ControlTextField<Integer>(new IntegerValueType(8));
			controlPanel.add(posNumber);
			controlPanel.add(new ControlLabel("letzte Bonnummer"));
			lastReceiptNumber = new ControlTextField<Integer>(new IntegerValueType(8), this, "next");
			controlPanel.add(lastReceiptNumber);
		}
		
		public void showIt() {
			for (Enumeration<AbstractButton> e = actionButtonGroup.getElements(); e.hasMoreElements();) {
				e.nextElement().setEnabled(false);
			}
			for (Component comp : getParent().getComponents()) {
				comp.setVisible(false);
			}
			posNumber.setValue(receiptStorage.getPosNumber());
			lastReceiptNumber.setValue(receiptStorage.getLastReceiptNumber());
			setVisible(true);
			posNumber.requestFocusInWindow();
		}
		
		public void next() {
			Integer pos = (Integer) posNumber.getValue();
			Integer lastReceipt = (Integer) lastReceiptNumber.getValue();
			if (pos != null && pos.intValue() > 0 && lastReceipt != null && lastReceipt.intValue() >= 0) {
				receiptStorage.setup(pos, lastReceipt);
				for (Enumeration<AbstractButton> e = actionButtonGroup.getElements(); e.hasMoreElements();) {
					e.nextElement().setEnabled(true);
				}
				loginPanel.showIt();
			}
		}

	}
	
	public class LoginPanel extends BasePanel {

		private final AbstractButton startAction;
		private final ControlTextField<Integer> cashierNumber;
		
		public LoginPanel(AbstractButton startAction) {
			super(new BorderLayout(0, 0));
			this.startAction = startAction;
			
			BasePanel actions = new BasePanel(BasePanel.Layout.HORIZONTAL);
			ActionButton quit = new ActionButton("Beenden", Color.RED, this, "quit");
			actions.add(quit);
			add(actions, BorderLayout.NORTH);
			
			BasePanel controlPanel = new BasePanel(new FlowLayout());
			controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
			add(controlPanel, BorderLayout.CENTER);
			add(new NumPadPanel(), BorderLayout.SOUTH);
			
			controlPanel.add(new ControlLabel("Kassierer"));
			cashierNumber = new ControlTextField<Integer>(new ChecksumValueType(CASHIER_NUMBER_LENGTH), this, "next");
			controlPanel.add(cashierNumber);
			controlPanel.add(new DummyComponent());
		}
		
		public void showIt() {
			for (Enumeration<AbstractButton> e = actionButtonGroup.getElements(); e.hasMoreElements();) {
				e.nextElement().setEnabled(false);
			}
			for (Component comp : getParent().getComponents()) {
				comp.setVisible(false);
			}
			cashierNumber.setValue(receiptStorage.getCashierNumber() <= 0 ? null : receiptStorage.getCashierNumber());
			setVisible(true);
			cashierNumber.requestFocusInWindow();
		}
		
		public void next() {
			Integer cashier = (Integer) cashierNumber.getValue();
			if (cashier != null && cashier.intValue() > 0) {
				receiptStorage.setCashierNumber(cashier);
				for (Enumeration<AbstractButton> e = actionButtonGroup.getElements(); e.hasMoreElements();) {
					e.nextElement().setEnabled(true);
				}
				logout.setEnabled(true);
				startAction.doClick();
			}
		}
		
		public void quit() {
			System.exit(0);
		}

	}

}
