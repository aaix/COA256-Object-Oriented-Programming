package com.aidan.coursework.bookshop.gui;

import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JList;
import javax.swing.JOptionPane;

import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.ListSelectionModel;
import javax.swing.AbstractListModel;
import javax.swing.JLabel;
import java.awt.Insets;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.awt.Font;
import javax.swing.JButton;
import java.awt.CardLayout;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.aidan.coursework.bookshop.database.Deserialiser;
import com.aidan.coursework.bookshop.database.FileHandler;
import com.aidan.coursework.bookshop.database.Serialiser;
import com.aidan.coursework.bookshop.enums.AccountType;
import com.aidan.coursework.bookshop.enums.BookFormat;
import com.aidan.coursework.bookshop.enums.BookGenre;
import com.aidan.coursework.bookshop.enums.BookLanguage;
import com.aidan.coursework.bookshop.enums.BookType;
import com.aidan.coursework.bookshop.errors.*;
import com.aidan.coursework.bookshop.records.BasketEntry;
import com.aidan.coursework.bookshop.account.*;
import com.aidan.coursework.bookshop.book.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JSlider;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class GUI {
	
	FileHandler<BaseBook> books;
	FileHandler<BaseAccount> accounts;
	BaseAccount current_account;
	
	private static final String default_field_content = "Type to filter";

	private JFrame frame;
	private JTable ebook_table;
	private JTable audiobook_table;
	private JTable paperbook_table;
	private JList<String> user_selectbook_list;
	private JTable user_basket_content_table;
	private JLabel user_basket_cost;
	private JSlider audiobook_duration_filter;
	private double current_basket_total;
	private JTextField view_filter_field;
	
	private boolean initialised;
	private JTextField barcode_input;
	private EnumComboBox<BookType> type_combo;
	private JTextField title_input;
	private JTextField quantity_input;
	private JTextField price_input;
	private JTextField additional1_input;
	private JLabel additional1_label;
	private JTextField date_input;
	private EnumComboBox<BookGenre> genre_combo;
	private EnumComboBox<BookLanguage> language_combo;
	private EnumComboBox<BookFormat> additional2_combo;
	private JLabel additional2_label;


	/**
	 * Create the application.
	 */
	public GUI() {
		try {
			books = new FileHandler<BaseBook>("Stock.txt");
			accounts = new FileHandler<BaseAccount>("UserAccounts.txt");
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(frame, "ABORTING - UNABLE TO OPEN CRITICAL FILE : >" +e.getMessage());
			return;
		}
		initialize();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("F223129 - aidan's book store :D");
		frame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 14));
		frame.setBounds(100, 100, 1391, 807);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
		JPanel login_panel = new JPanel();
		frame.getContentPane().add(login_panel, "login_card");
		GridBagLayout gbl_login_panel = new GridBagLayout();
		gbl_login_panel.rowHeights = new int[]{0, 0, 0};
		gbl_login_panel.columnWidths = new int[]{0, 0};
		gbl_login_panel.columnWeights = new double[]{1.0, 0.0};
		gbl_login_panel.rowWeights = new double[]{0.0, 1.0, 0.0};
		login_panel.setLayout(gbl_login_panel);
		
		JLabel account_label = new JLabel("Select Account");
		account_label.setHorizontalAlignment(SwingConstants.CENTER);
		account_label.setFont(new Font("Tahoma", Font.PLAIN, 34));
		GridBagConstraints gbc_account_label = new GridBagConstraints();
		gbc_account_label.gridwidth = 2;
		gbc_account_label.fill = GridBagConstraints.BOTH;
		gbc_account_label.insets = new Insets(0, 0, 5, 0);
		gbc_account_label.gridx = 0;
		gbc_account_label.gridy = 0;
		login_panel.add(account_label, gbc_account_label);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		login_panel.add(scrollPane, gbc_scrollPane);
		
		JList<String> user_list = new JList<String>();
		scrollPane.setViewportView(user_list);
		user_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		user_list.setModel(new AbstractListModel<String>() {
			public int getSize() {
				return accounts.iterate().size();
			}
			public String getElementAt(int index) {
				return accounts.iterate().get(index).display();
			}
		});
		user_list.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		JButton login_button = new JButton("Log In");
		login_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = user_list.getSelectedIndex();
				if (index != -1) {
					current_account = accounts.iterate().get(index);
					switch (current_account.getType()) {
						case AdminAccountType -> switch_card("admin_card");
						case UserAccountType -> switch_card("user_card");
					}
				}
			}
		});
		login_button.setFont(new Font("Tahoma", Font.PLAIN, 24));
		GridBagConstraints gbc_login_button = new GridBagConstraints();
		gbc_login_button.fill = GridBagConstraints.BOTH;
		gbc_login_button.gridx = 1;
		gbc_login_button.gridy = 2;
		login_panel.add(login_button, gbc_login_button);
		
		JPanel user_panel = new JPanel();
		frame.getContentPane().add(user_panel, "user_card");
		GridBagLayout gbl_user_panel = new GridBagLayout();
		gbl_user_panel.columnWidths = new int[] {0, 0};
		gbl_user_panel.rowHeights = new int[] {0, 0};
		gbl_user_panel.columnWeights = new double[]{1.0, 0.0};
		gbl_user_panel.rowWeights = new double[]{1.0, 0.0};
		user_panel.setLayout(gbl_user_panel);
		
		JPanel user_shopping_wrapper = new JPanel();
		GridBagConstraints gbc_user_shopping_wrapper = new GridBagConstraints();
		gbc_user_shopping_wrapper.gridwidth = 2;
		gbc_user_shopping_wrapper.insets = new Insets(0, 0, 5, 0);
		gbc_user_shopping_wrapper.fill = GridBagConstraints.BOTH;
		gbc_user_shopping_wrapper.gridx = 0;
		gbc_user_shopping_wrapper.gridy = 0;
		user_panel.add(user_shopping_wrapper, gbc_user_shopping_wrapper);
		user_shopping_wrapper.setLayout(new GridLayout(1, 0, 0, 0));
		
		JPanel user_shopping_panel = new JPanel();
		user_shopping_wrapper.add(user_shopping_panel);
		GridBagLayout gbl_user_shopping_panel = new GridBagLayout();
		gbl_user_shopping_panel.columnWidths = new int[] {0};
		gbl_user_shopping_panel.rowHeights = new int[] {0, 0, 0};
		gbl_user_shopping_panel.columnWeights = new double[]{1.0};
		gbl_user_shopping_panel.rowWeights = new double[]{0.0, 1.0, 0.0};
		user_shopping_panel.setLayout(gbl_user_shopping_panel);
		
		JLabel user_selectbook_label = new JLabel("Select Books (Ctrl+Click to select multiple)");
		user_selectbook_label.setHorizontalAlignment(SwingConstants.CENTER);
		user_selectbook_label.setFont(new Font("Tahoma", Font.PLAIN, 20));
		GridBagConstraints gbc_user_selectbook_label = new GridBagConstraints();
		gbc_user_selectbook_label.insets = new Insets(0, 0, 5, 0);
		gbc_user_selectbook_label.fill = GridBagConstraints.BOTH;
		gbc_user_selectbook_label.gridx = 0;
		gbc_user_selectbook_label.gridy = 0;
		user_shopping_panel.add(user_selectbook_label, gbc_user_selectbook_label);
		
		user_selectbook_list = new JList<String>();
		user_selectbook_list.setModel(new AbstractListModel<String>() {
			public int getSize() {
				return books.iterate().size();
			}
			public String getElementAt(int index) {
				return books.iterate().get(index).display();
			}
		});
		user_selectbook_list.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_user_selectbook_list = new GridBagConstraints();
		gbc_user_selectbook_list.weighty = 1.0;
		gbc_user_selectbook_list.insets = new Insets(0, 0, 5, 0);
		gbc_user_selectbook_list.fill = GridBagConstraints.BOTH;
		gbc_user_selectbook_list.gridx = 0;
		gbc_user_selectbook_list.gridy = 1;
		user_shopping_panel.add(user_selectbook_list, gbc_user_selectbook_list);
		
		JButton user_selectbook_add = new JButton("Add to basket");
		user_selectbook_add.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update_shopping_basket();
				user_selectbook_list.clearSelection();
			}
		});
		user_selectbook_add.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_user_selectbook_add = new GridBagConstraints();
		gbc_user_selectbook_add.fill = GridBagConstraints.BOTH;
		gbc_user_selectbook_add.gridx = 0;
		gbc_user_selectbook_add.gridy = 2;
		user_shopping_panel.add(user_selectbook_add, gbc_user_selectbook_add);
		
		JPanel user_basket_panel = new JPanel();
		user_shopping_wrapper.add(user_basket_panel);
		GridBagLayout gbl_user_basket_panel = new GridBagLayout();
		gbl_user_basket_panel.columnWidths = new int[] {0, 0, 0};
		gbl_user_basket_panel.rowHeights = new int[] {0, 0, 0};
		gbl_user_basket_panel.columnWeights = new double[]{0.0, 0.0, 0.0};
		gbl_user_basket_panel.rowWeights = new double[]{0.0, 0.0, 0.0};
		user_basket_panel.setLayout(gbl_user_basket_panel);
		
		JLabel user_basket_title = new JLabel("Shopping Basket");
		user_basket_title.setHorizontalAlignment(SwingConstants.CENTER);
		user_basket_title.setFont(new Font("Tahoma", Font.PLAIN, 20));
		GridBagConstraints gbc_user_basket_title = new GridBagConstraints();
		gbc_user_basket_title.fill = GridBagConstraints.HORIZONTAL;
		gbc_user_basket_title.gridwidth = 3;
		gbc_user_basket_title.insets = new Insets(0, 0, 5, 0);
		gbc_user_basket_title.gridx = 0;
		gbc_user_basket_title.gridy = 0;
		user_basket_panel.add(user_basket_title, gbc_user_basket_title);
		
		JPanel user_basket_contents = new JPanel();
		GridBagConstraints gbc_user_basket_contents = new GridBagConstraints();
		gbc_user_basket_contents.weighty = 1.0;
		gbc_user_basket_contents.fill = GridBagConstraints.BOTH;
		gbc_user_basket_contents.gridwidth = 3;
		gbc_user_basket_contents.anchor = GridBagConstraints.NORTHWEST;
		gbc_user_basket_contents.gridx = 0;
		gbc_user_basket_contents.gridy = 1;
		user_basket_panel.add(user_basket_contents, gbc_user_basket_contents);
		user_basket_contents.setLayout(new BorderLayout(0, 0));
		
		JScrollPane user_basket_content_scroller = new JScrollPane();
		user_basket_contents.add(user_basket_content_scroller, BorderLayout.CENTER);
		
		user_basket_content_table = new JTable();
		user_basket_content_scroller.setViewportView(user_basket_content_table);
		
		JButton user_basket_checkout = new JButton("Checkout");
		user_basket_checkout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int confirmed = JOptionPane.showConfirmDialog(frame, String.format(
					"Are you sure you wish to checkout? Current total: £%.2f",
						current_basket_total
				));
				if (confirmed != JOptionPane.YES_OPTION) {
					return;
				};
				try {
					float remaining = ((UserAccount) current_account).checkout(books, accounts);
					display_message(String.format(
						"Thank you for the purchase! £%.2f paid and your remaining credit balance is £%.2f. Your delivery address is %s.",
							current_basket_total,
							remaining,
							current_account.getAddress().toString()
					));
					draw_tables(); // we need to update all tables after checkout
				} catch (InsufficientFundsException err) {
					display_message(err);
				}
			}
		});
		user_basket_checkout.setHorizontalAlignment(SwingConstants.RIGHT);
		user_basket_checkout.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_user_basket_checkout = new GridBagConstraints();
		gbc_user_basket_checkout.gridx = 2;
		gbc_user_basket_checkout.gridy = 2;
		user_basket_panel.add(user_basket_checkout, gbc_user_basket_checkout);
		
		user_basket_cost = new JLabel("Account Credit £00.00 Basket Total £00.00");
		user_basket_cost.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_user_basket_cost = new GridBagConstraints();
		gbc_user_basket_cost.fill = GridBagConstraints.HORIZONTAL;
		gbc_user_basket_cost.insets = new Insets(0, 0, 5, 5);
		gbc_user_basket_cost.anchor = GridBagConstraints.WEST;
		gbc_user_basket_cost.gridx = 0;
		gbc_user_basket_cost.gridy = 2;
		user_basket_panel.add(user_basket_cost, gbc_user_basket_cost);
		
		JButton user_basket_clear = new JButton("Clear Basket");
		user_basket_clear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((UserAccount) current_account).cancel_basket();
				update_shopping_basket();
			}
		});
		user_basket_clear.setHorizontalAlignment(SwingConstants.LEFT);
		user_basket_clear.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_user_basket_clear = new GridBagConstraints();
		gbc_user_basket_clear.gridx = 1;
		gbc_user_basket_clear.gridy = 2;
		user_basket_panel.add(user_basket_clear, gbc_user_basket_clear);
		
		JButton user_logout_button = new JButton("Log Out");
		user_logout_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch_card("login_card");
			}
		});
		user_logout_button.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GridBagConstraints gbc_user_logout_button = new GridBagConstraints();
		gbc_user_logout_button.anchor = GridBagConstraints.SOUTHWEST;
		gbc_user_logout_button.insets = new Insets(0, 0, 5, 0);
		gbc_user_logout_button.gridx = 0;
		gbc_user_logout_button.gridy = 1;
		user_panel.add(user_logout_button, gbc_user_logout_button);
		
		JButton user_viewbook_button = new JButton("View Books");
		user_viewbook_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch_card("view_card");
			}
		});
		user_viewbook_button.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GridBagConstraints gbc_user_viewbook_button = new GridBagConstraints();
		gbc_user_viewbook_button.anchor = GridBagConstraints.SOUTHEAST;
		gbc_user_viewbook_button.gridx = 1;
		gbc_user_viewbook_button.gridy = 1;
		user_panel.add(user_viewbook_button, gbc_user_viewbook_button);
		
		JPanel admin_panel = new JPanel();
		frame.getContentPane().add(admin_panel, "admin_card");
		GridBagLayout gbl_admin_panel = new GridBagLayout();
		gbl_admin_panel.columnWidths = new int[] {0, 0};
		gbl_admin_panel.rowHeights = new int[] {0, 0};
		gbl_admin_panel.columnWeights = new double[]{1.0, 0.0};
		gbl_admin_panel.rowWeights = new double[]{1.0, 0.0};
		admin_panel.setLayout(gbl_admin_panel);
		
		JLabel admin_add_book_label = new JLabel("Add new book");
		admin_add_book_label.setFont(new Font("Tahoma", Font.PLAIN, 26));
		GridBagConstraints gbc_admin_add_book_label = new GridBagConstraints();
		gbc_admin_add_book_label.insets = new Insets(20, 0, 0, 0);
		gbc_admin_add_book_label.gridwidth = 2;
		gbc_admin_add_book_label.weighty = 0.1;
		gbc_admin_add_book_label.anchor = GridBagConstraints.NORTH;
		gbc_admin_add_book_label.gridx = 0;
		gbc_admin_add_book_label.gridy = 0;
		admin_panel.add(admin_add_book_label, gbc_admin_add_book_label);
		
		JPanel admin_bookmanager_wrapper = new JPanel();
		GridBagConstraints gbc_admin_bookmanager_wrapper = new GridBagConstraints();
		gbc_admin_bookmanager_wrapper.weighty = 1.0;
		gbc_admin_bookmanager_wrapper.gridwidth = 2;
		gbc_admin_bookmanager_wrapper.insets = new Insets(0, 0, 5, 0);
		gbc_admin_bookmanager_wrapper.fill = GridBagConstraints.HORIZONTAL;
		gbc_admin_bookmanager_wrapper.gridx = 0;
		gbc_admin_bookmanager_wrapper.gridy = 0;
		admin_panel.add(admin_bookmanager_wrapper, gbc_admin_bookmanager_wrapper);
		admin_bookmanager_wrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JPanel barcode_panel = new JPanel();
		admin_bookmanager_wrapper.add(barcode_panel);
		barcode_panel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JLabel barcode_label = new JLabel("Barcode");
		barcode_label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		barcode_panel.add(barcode_label);
		
		barcode_input = new JTextField();
		barcode_input.setFont(new Font("Tahoma", Font.PLAIN, 16));
		barcode_panel.add(barcode_input);
		barcode_input.setColumns(6);
		
		JPanel type_panel = new JPanel();
		admin_bookmanager_wrapper.add(type_panel);
		type_panel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JLabel type_label = new JLabel("Type");
		type_label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		type_panel.add(type_label);
		
		// separate the set values here due to a bug in the
		// windowbuilder thinking the constructor doesnt
		// exist for EnumComboBox(SerialisableEnum[]) 
		// even though it very much does exist :/
		type_combo = new EnumComboBox<BookType>();
		type_combo.update_values(BookType.values());
		type_combo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		type_combo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update_add_book_fields();
			}
		});
		type_panel.add(type_combo);
		
		JPanel title_panel = new JPanel();
		admin_bookmanager_wrapper.add(title_panel);
		title_panel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JLabel title_label = new JLabel("Title");
		title_label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		title_panel.add(title_label);
		
		title_input = new JTextField();
		title_input.setFont(new Font("Tahoma", Font.PLAIN, 16));
		title_input.setColumns(20);
		title_panel.add(title_input);
		
		JPanel language_panel = new JPanel();
		admin_bookmanager_wrapper.add(language_panel);
		language_panel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JLabel language_label = new JLabel("Language");
		language_label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		language_panel.add(language_label);
		
		language_combo = new EnumComboBox<BookLanguage>();
		language_combo.update_values(BookLanguage.values());
		language_combo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		language_panel.add(language_combo);
		
		JPanel genre_panel = new JPanel();
		admin_bookmanager_wrapper.add(genre_panel);
		genre_panel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JLabel genre_label = new JLabel("Genre");
		genre_label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		genre_panel.add(genre_label);
		
		genre_combo = new EnumComboBox<BookGenre>();
		genre_combo.update_values(BookGenre.values());
		genre_combo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		genre_panel.add(genre_combo);
		
		JPanel date_panel = new JPanel();
		admin_bookmanager_wrapper.add(date_panel);
		date_panel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JLabel date_label = new JLabel("Date (dd-MM-yyyy)");
		date_label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		date_panel.add(date_label);
		
		date_input = new JTextField();
		date_input.setFont(new Font("Tahoma", Font.PLAIN, 16));
		date_panel.add(date_input);
		date_input.setColumns(10);
		
		JPanel quantity_panel = new JPanel();
		admin_bookmanager_wrapper.add(quantity_panel);
		quantity_panel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JLabel quantity_label = new JLabel("Quantity");
		quantity_label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		quantity_panel.add(quantity_label);
		
		quantity_input = new JTextField();
		quantity_input.setFont(new Font("Tahoma", Font.PLAIN, 16));
		quantity_input.setColumns(3);
		quantity_panel.add(quantity_input);
		
		JPanel price_panel = new JPanel();
		admin_bookmanager_wrapper.add(price_panel);
		price_panel.setLayout(new GridLayout(2, 1, 0, 0));
		
		JLabel price_label = new JLabel("Price (£)");
		price_label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		price_panel.add(price_label);
		
		price_input = new JTextField();
		price_input.setFont(new Font("Tahoma", Font.PLAIN, 16));
		price_input.setColumns(5);
		price_panel.add(price_input);
		
		JPanel additional1_panel = new JPanel();
		admin_bookmanager_wrapper.add(additional1_panel);
		additional1_panel.setLayout(new GridLayout(2, 1, 0, 0));
		
		additional1_label = new JLabel("Pages");
		additional1_label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		additional1_panel.add(additional1_label);
		
		additional1_input = new JTextField();
		additional1_input.setFont(new Font("Tahoma", Font.PLAIN, 16));
		additional1_panel.add(additional1_input);
		additional1_input.setColumns(3);
		
		JPanel additional2_panel = new JPanel();
		admin_bookmanager_wrapper.add(additional2_panel);
		additional2_panel.setLayout(new GridLayout(2, 1, 0, 0));
		
		// again paperbook is the default so we use condition
		additional2_label = new JLabel("Condition");
		additional2_label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		additional2_panel.add(additional2_label);
		
		// paperback is the default so we start with paperbacks
		additional2_combo = new EnumComboBox<BookFormat>();
		additional2_combo.update_values(BookFormat.getPaperbackFormats());
		additional2_combo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		additional2_panel.add(additional2_combo);
		
		JButton admin_submit_button = new JButton("Submit");
		admin_submit_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				add_new_book();
			}
		});
		admin_submit_button.setFont(new Font("Tahoma", Font.PLAIN, 20));
		admin_bookmanager_wrapper.add(admin_submit_button);
		
		JButton admin_logout_button = new JButton("Log Out");
		admin_logout_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch_card("login_card");
			}
		});
		admin_logout_button.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GridBagConstraints gbc_admin_logout_button = new GridBagConstraints();
		gbc_admin_logout_button.gridy = 1;
		gbc_admin_logout_button.anchor = GridBagConstraints.SOUTHWEST;
		gbc_admin_logout_button.insets = new Insets(0, 0, 5, 5);
		gbc_admin_logout_button.gridx = 0;
		admin_panel.add(admin_logout_button, gbc_admin_logout_button);
		
		JButton admin_viewbook_button = new JButton("View Books");
		admin_viewbook_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch_card("view_card");
			}
		});
		admin_viewbook_button.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GridBagConstraints gbc_admin_viewbook_button = new GridBagConstraints();
		gbc_admin_viewbook_button.insets = new Insets(0, 0, 5, 0);
		gbc_admin_viewbook_button.anchor = GridBagConstraints.SOUTHEAST;
		gbc_admin_viewbook_button.gridx = 1;
		gbc_admin_viewbook_button.gridy = 1;
		admin_panel.add(admin_viewbook_button, gbc_admin_viewbook_button);
		
		JPanel view_panel = new JPanel();
		frame.getContentPane().add(view_panel, "view_card");
		GridBagLayout gbl_view_panel = new GridBagLayout();
		gbl_view_panel.columnWidths = new int[] {0, 0, 0};
		gbl_view_panel.rowHeights = new int[] {0, 0};
		gbl_view_panel.columnWeights = new double[]{0.0, 1.0, 0.0};
		gbl_view_panel.rowWeights = new double[]{0.0, 0.0};
		view_panel.setLayout(gbl_view_panel);
				
		JPanel book_table_wrapper = new JPanel();
		GridBagConstraints gbc_book_table_wrapper = new GridBagConstraints();
		gbc_book_table_wrapper.weighty = 1.0;
		gbc_book_table_wrapper.gridwidth = 3;
		gbc_book_table_wrapper.insets = new Insets(0, 0, 5, 0);
		gbc_book_table_wrapper.fill = GridBagConstraints.BOTH;
		gbc_book_table_wrapper.gridx = 0;
		gbc_book_table_wrapper.gridy = 0;
		view_panel.add(book_table_wrapper, gbc_book_table_wrapper);
		book_table_wrapper.setLayout(new BorderLayout(0, 0));
		
		JTabbedPane book_type_panes = new JTabbedPane(JTabbedPane.TOP);
		book_table_wrapper.add(book_type_panes);
		book_type_panes.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		JPanel ebook_pane = new JPanel();
		book_type_panes.addTab("E-Books", null, ebook_pane, null);
		ebook_pane.setLayout(new BorderLayout(0, 0));
		
		JScrollPane ebook_scroll = new JScrollPane();
		ebook_pane.add(ebook_scroll);
		
		ebook_table = new JTable();
		ebook_table.setFont(new Font("Tahoma", Font.PLAIN, 16));
		ebook_scroll.setViewportView(ebook_table);
		ebook_table.setRowSelectionAllowed(false);
		ebook_table.setFillsViewportHeight(true);
		ebook_table.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		JPanel paperbook_pane = new JPanel();
		book_type_panes.addTab("Paperbacks", null, paperbook_pane, null);
		paperbook_pane.setLayout(new BorderLayout(0, 0));
		
		JScrollPane paperbook_scroll = new JScrollPane();
		paperbook_pane.add(paperbook_scroll);
		
		paperbook_table = new JTable();
		paperbook_table.setFont(new Font("Tahoma", Font.PLAIN, 16));
		paperbook_scroll.setViewportView(paperbook_table);
		paperbook_table.setRowSelectionAllowed(false);
		paperbook_table.setFillsViewportHeight(true);
		paperbook_table.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		JPanel audiobook_pane = new JPanel();
		book_type_panes.addTab("Audio Books", null, audiobook_pane, null);
		GridBagLayout gbl_audiobook_pane = new GridBagLayout();
		gbl_audiobook_pane.columnWidths = new int[] {0, 0};
		gbl_audiobook_pane.rowHeights = new int[] {0, 0};
		gbl_audiobook_pane.columnWeights = new double[]{0.0, 0.0};
		gbl_audiobook_pane.rowWeights = new double[]{0.0, 0.0};
		audiobook_pane.setLayout(gbl_audiobook_pane);
			
		JScrollPane audiobook_scroll = new JScrollPane();
		GridBagConstraints gbc_audiobook_scroll = new GridBagConstraints();
		gbc_audiobook_scroll.gridwidth = 2;
		gbc_audiobook_scroll.insets = new Insets(0, 0, 5, 0);
		gbc_audiobook_scroll.weighty = 1.0;
		gbc_audiobook_scroll.weightx = 1.0;
		gbc_audiobook_scroll.fill = GridBagConstraints.BOTH;
		gbc_audiobook_scroll.gridx = 0;
		gbc_audiobook_scroll.gridy = 0;
		audiobook_pane.add(audiobook_scroll, gbc_audiobook_scroll);
		
		audiobook_table = new JTable();
		audiobook_table.setFont(new Font("Tahoma", Font.PLAIN, 16));
		audiobook_scroll.setViewportView(audiobook_table);
		audiobook_table.setRowSelectionAllowed(false);
		audiobook_table.setFillsViewportHeight(true);
		audiobook_table.setColumnSelectionAllowed(false);
		audiobook_table.setEnabled(true);
		audiobook_table.setCellSelectionEnabled(false);
		
		
		audiobook_duration_filter = new JSlider();
		audiobook_duration_filter.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				draw_tables();
			}
		});
		
		int max_duration = 300;
		
		for (BaseBook book: books.iterate()) {
			if (book.getType() == BookType.AudioBookType) {
				// we can cast here because subsecond duration is not a thing
				int duration = (int) (((AudioBook) book).getAudio_length() * 60);
				
				if (duration > max_duration) {
					max_duration = duration;
				}
			}
		}
		
		// round up to the nearest 60s
		max_duration = max_duration + (60 - (max_duration % 60));
		
		audiobook_duration_filter.setMaximum(max_duration);
		
		Hashtable<Integer, JLabel> label_table = new Hashtable<Integer, JLabel>();
		
		// add labels every minute and minute+30s
		for (int secs = 0; secs <= max_duration; secs+= 60) {
			label_table.put(secs, new JLabel(String.format(
				"%d:00", 
					secs / 60
			)));
			label_table.put(secs+30, new JLabel(String.format(
				"%d:30", 
					secs / 60
			)));
		};
		
		audiobook_duration_filter.setLabelTable(label_table);
		
		audiobook_duration_filter.setValue(0);
		audiobook_duration_filter.setSnapToTicks(true);
		audiobook_duration_filter.setPaintTicks(true);
		audiobook_duration_filter.setPaintLabels(true);
		audiobook_duration_filter.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		audiobook_duration_filter.setFont(new Font("Tahoma", Font.PLAIN, 16));
		audiobook_duration_filter.setMinorTickSpacing(15);
		audiobook_duration_filter.setMajorTickSpacing(30);
		GridBagConstraints gbc_audiobook_duration_filter = new GridBagConstraints();
		gbc_audiobook_duration_filter.fill = GridBagConstraints.BOTH;
		gbc_audiobook_duration_filter.insets = new Insets(10, 50, 10, 50);
		gbc_audiobook_duration_filter.gridx = 1;
		gbc_audiobook_duration_filter.gridy = 1;
		audiobook_pane.add(audiobook_duration_filter, gbc_audiobook_duration_filter);
		
		JLabel audiobook_duration_label = new JLabel("Slide to filter Duration");
		audiobook_duration_label.setHorizontalAlignment(SwingConstants.CENTER);
		audiobook_duration_label.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GridBagConstraints gbc_audiobook_duration_label = new GridBagConstraints();
		gbc_audiobook_duration_label.insets = new Insets(0, 10, 0, 0);
		gbc_audiobook_duration_label.anchor = GridBagConstraints.WEST;
		gbc_audiobook_duration_label.gridx = 0;
		gbc_audiobook_duration_label.gridy = 1;
		audiobook_pane.add(audiobook_duration_label, gbc_audiobook_duration_label);
		audiobook_table.getTableHeader().setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		JButton view_return_button = new JButton("Return");
		view_return_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (current_account.getType()) {
					case AdminAccountType -> switch_card("admin_card");
					case UserAccountType -> switch_card("user_card");
				}
			}
		});
		view_return_button.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GridBagConstraints gbc_view_return_button = new GridBagConstraints();
		gbc_view_return_button.insets = new Insets(0, 0, 0, 5);
		gbc_view_return_button.gridx = 0;
		gbc_view_return_button.gridy = 1;
		view_panel.add(view_return_button, gbc_view_return_button);
		
		// take away focus from the field on enter
		// otherwise pressing enter wont actually do the search
		view_filter_field = new JTextField();
		view_filter_field.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					view_filter_field.getParent().requestFocus();
				}
				
			}
		});
		
		// we check if not in focus to set to "Type to filter"
		view_filter_field.addFocusListener(new FocusListener () {

			@Override
			public void focusGained(FocusEvent e) {
				if (view_filter_field.getText().equals(default_field_content)) {
					view_filter_field.setText("");
				}
			}

			@Override
			public void focusLost(FocusEvent e) {
				if (view_filter_field.getText().equals("")) {
					view_filter_field.setText(default_field_content);
				}
				draw_tables();
				
			}
		});
		
		
		view_filter_field.setText(default_field_content);
		view_filter_field.setFont(new Font("Tahoma", Font.PLAIN, 16));
		GridBagConstraints gbc_view_filter_field = new GridBagConstraints();
		gbc_view_filter_field.insets = new Insets(0, 0, 0, 5);
		gbc_view_filter_field.fill = GridBagConstraints.HORIZONTAL;
		gbc_view_filter_field.gridx = 1;
		gbc_view_filter_field.gridy = 1;
		view_panel.add(view_filter_field, gbc_view_filter_field);
		view_filter_field.setColumns(10);
		
		JButton view_barcode_lookup = new JButton("Barcode Lookup");
		view_barcode_lookup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String input = JOptionPane.showInputDialog(frame, "Please enter the book barcode");
				
				if (input != null) {
					int barcode;
					try {
						barcode = Integer.parseInt(input);
					} catch (NumberFormatException err) {
						display_message("Unable to determine input as a barcode");
						return;
					};
					
					BaseBook book = books.get_by_id(barcode);
					
					if (book == null) {
						display_message("Unable to find a book with the barcode " + barcode);
						return;
					};
					
					display_book(book);
					
				}
			}
		});
		view_barcode_lookup.setFont(new Font("Tahoma", Font.PLAIN, 18));
		GridBagConstraints gbc_view_barcode_lookup = new GridBagConstraints();
		gbc_view_barcode_lookup.gridx = 2;
		gbc_view_barcode_lookup.gridy = 1;
		view_panel.add(view_barcode_lookup, gbc_view_barcode_lookup);


		initialised = true;
	}
	
	public void display_book(BaseBook book) {
		String[] fields = get_col_names(book.getType());
		String[] values = book_to_table(book);
		
		String[] output = new String[values.length];
		for (int i = 0; i < values.length; i++) {
			output[i] = fields[i] + ": " + values[i];
		};
		
		display_message(
			Serialiser.serialise_enum(book.getType()).toUpperCase() + " INFORMATION\n"+String.join("\n", output)
		);
		
	}

	private void update_add_book_fields() {
		
		BookFormat[] format_values;
		
		switch (type_combo.get_value()) {
			case AudioBookType -> {
				format_values = BookFormat.getAudiobookFormats();
				additional1_label.setText("Duration (fractional minutes)");
				additional2_label.setText("Format");
			}
			case EBookType -> {
				format_values = BookFormat.getEbookFormats();
				additional1_label.setText("Pages");
				additional2_label.setText("Format");

			}
			case PaperBookType -> {
				format_values = BookFormat.getPaperbackFormats();
				additional1_label.setText("Pages");
				additional2_label.setText("Condition");

			}
			// default case here to ensure the compiler that
			// format_values is in fact initialised (it is paranoid)
			default -> throw new RuntimeException("Illegal branch");
		};
		
		additional2_combo.update_values(format_values);
		
	}

	private void add_new_book() {
		// once again we can use the deserialiser to do all the validation
		BaseBook new_book;
		try {
			 new_book = Deserialiser.deserialise_book(String.join(", ", new String[] {
				barcode_input.getText(),
				type_combo.get_value().serialise(),
				title_input.getText(),
				language_combo.get_value().serialise(),
				genre_combo.get_value().serialise(),
				date_input.getText(),
				quantity_input.getText(),
				price_input.getText(),
				additional1_input.getText(),
				additional2_combo.get_value().serialise()
			}));
		} catch (InvalidDatabaseEntry e) {
			display_message(e);
			return;
		};
		
		// we still need to do some validation like checking book quantity >= 0 and no conflicting barcode
		
		if (books.get_by_id(new_book.getBarcode()) != null) {
			display_message("A book already exists with barcode " + new_book.getBarcode());
			return;
		};
		
		if (new_book.getQuantity() < 0) {
			display_message("Please do not add books with negative quantites");
			return;
		};
		books.set_value_by_id(new_book.id(), new_book);
		display_book(new_book);
		
		// reset text fields
		barcode_input.setText("");
		title_input.setText("");
		date_input.setText("");
		quantity_input.setText("");
		price_input.setText("");
		quantity_input.setText("");
	}

	private void update_shopping_basket() {
		int[] selected = user_selectbook_list.getSelectedIndices();
		if (current_account.getType() != AccountType.UserAccountType) {return;};
		
		UserAccount user = (UserAccount) current_account;
		
		ArrayList<BaseBook> book_list = books.iterate();
		
		for (int i : selected) {
			try {
				user.add_book_to_basket(book_list.get(i));
			} catch (QuantityExceedsStockException e) {
				display_message(e);
				continue;
			}
		};
		
		update_shopping_basket_table();
			
	}
	
	private void update_shopping_basket_table() {

		UserAccount user = (UserAccount) current_account;
		ArrayList<BasketEntry> basket = user.get_basket();
		
		Object[][] table = new Object[basket.size()][4];
		
		for (int i = 0; i < basket.size(); i++) {
			BasketEntry entry = basket.get(i);
			table[i][0] = entry.book().getBarcode();
			table[i][1] = entry.book().getTitle();
			table[i][2] = entry.quantity();
			table[i][3] = String.format("£%.2f", entry.book().getPrice() * entry.quantity());
		};

		current_basket_total = user.basket_cost();
		
		user_basket_content_table.setModel(new RestrictedTableModel(
			table,
			new String[] {"Barcode", "Title", "Quantity (Doubleclick to edit)", "Price"},
			new Integer[] {2}, // only quantity is editable
			(Object value, int row, int col) -> {return verify_basket_value_as_valid(value, row, col);}
		));
			
		user_basket_cost.setText(String.format("Account Credit: £%.2f Basket Total: £%.2f", user.getCredit_balance(), current_basket_total));
	}

	private boolean verify_basket_value_as_valid(Object value, int row, int col) {
		String contents = (String) value;
		int quantity;
		try {
			quantity = Integer.parseInt(contents);
		} catch (NumberFormatException e ) {
			return false;
		};
		
		// 1 is minimum quantity
		if (quantity < 1) {
			return false;
		};
		
		int barcode = (Integer) user_basket_content_table.getValueAt(row, 0);
		
		try {
			((UserAccount) current_account).update_quantity(books.get_by_id(barcode), quantity);
		} catch (QuantityExceedsStockException e) {
			display_message(e);
			return false;
		};
		
		
		update_shopping_basket_table();
		return true;
		
			
	}
	
	private void display_message(String message) {
		JOptionPane.showMessageDialog(this.frame, message);
	}
	
	private void display_message(Exception exception) {
		JOptionPane.showMessageDialog(this.frame, exception.getMessage());
	}
	
	private void draw_tables() {
		// these are also restricted but we allow no editable cols
		// hence the callback being null as it is never going to be called
		Integer[] no_cols = {,};
		
		if (!initialised) {return;};
		audiobook_table.setModel(new RestrictedTableModel(
			format_for_viewing(BookType.AudioBookType),
			get_col_names(BookType.AudioBookType),
			no_cols,
			null
		));
		paperbook_table.setModel(new RestrictedTableModel(
			format_for_viewing(BookType.PaperBookType),
			get_col_names(BookType.PaperBookType),
			no_cols,
			null
		));
		ebook_table.setModel(new RestrictedTableModel(
			format_for_viewing(BookType.EBookType),
			get_col_names(BookType.EBookType),
			no_cols,
			null
			
		));
		
		
		user_selectbook_list.clearSelection();
		user_selectbook_list.updateUI();
		update_shopping_basket();
		
	}

	// Used to generate the table for each book type
	private String[][] format_for_viewing(BookType type) {
		// filter out other types
		ArrayList<BaseBook> filtered_books = books.iterate();
		filtered_books.removeIf(b -> b.getType() != type);
		
		if (!(
			view_filter_field.getText().equals(default_field_content) ||
			view_filter_field.getText().equals("")
		)) {
			filtered_books.removeIf(
				b -> {
					return !b.getTitle().toLowerCase().contains(view_filter_field.getText().toLowerCase());
				}
			);
		};
		
		
		if (type == BookType.AudioBookType) {
			int duration_cut = audiobook_duration_filter.getValue();
			filtered_books.removeIf(b -> {
				return ((AudioBook) b).getAudio_length() * 60 < duration_cut;
			});
		}
		
		
		// create output array
		String[][] res = new String[filtered_books.size()][9];
		
		// copy over array
		for (int i = 0; i < filtered_books.size(); i++) {
			String[] row = book_to_table(filtered_books.get(i));
			for (int k = 0; k < row.length; k++) {
				res[i][k] = row[k];
			}
		}
		// sort based on current account type
		if (current_account != null) {
			Arrays.sort(res, new StringCasterComparator(current_account.getType()));
		};
		return res;
	}
	
	
	// turn each book into a row
	private String[] book_to_table(BaseBook book) {
		// little cheat using the serialiser to get
		// an almost table form
		ArrayList<String> values = new ArrayList<String>(Arrays.asList(book.serialise().split(", ")));
		
		// remove the book type from the seraliser
		values.remove(1);

		// add £ to price
		values.set(6, "£" + values.get(6));
		
		// Audio books we need to convert the duration into minutes and seconds
		if (book.getType() == BookType.AudioBookType) {
			float length = ((AudioBook) book).getAudio_length();
			int seconds = (int) ((length % 1) * 60);
			
			int mins = ((int) length) / 1;
			
			values.set(7, String.format("%d:%d", mins, seconds));
		}
		String[] res = new String[values.size()];
		res = values.toArray(res);
		return res;
	}
	
	private String[] get_col_names(BookType type) {
		return switch (type) {
			case AudioBookType -> new String[] {"Barcode", "Title", "Language", "Genre", "Released", "Stock Quantity", "Price", "Duration", "Format"};
			case PaperBookType -> new String[] {"Barcode", "Title", "Language", "Genre", "Released", "Stock Quantity", "Price", "Pages", "Condition"};
			case EBookType -> new String[] {"Barcode", "Title", "Language", "Genre", "Released", "Stock Quantity", "Price", "Pages", "Format"};
		};
	}
	
	
	private void switch_card(String card) {
		// update the tables incase a new book has been added or different sorting scheme
		draw_tables();
		((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), card);
	};

}
