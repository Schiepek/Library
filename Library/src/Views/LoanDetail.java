package Views;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import domain.Loan;

public class LoanDetail extends JFrame {

	private JPanel contentPane;
	private Loan currentLoan;


	public LoanDetail(Loan currentLoan) {
		super();
		if (currentLoan == null) { currentLoan = initEmptyLoan(); }
		this.currentLoan = currentLoan;
		
		initGUI();
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	private Loan initEmptyLoan() {
		Loan tempLoan = new Loan(null,null);
		return tempLoan;
	}
	
	private void initGUI() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
	}

}
