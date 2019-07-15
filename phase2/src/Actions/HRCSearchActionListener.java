package Actions;

import CompanyStuff.HRCoordinator;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HRCSearchActionListener implements ActionListener {

    private HRCoordinator HRC;
    private String searchType;

    public HRCSearchActionListener(HRCoordinator HRC, String searchType) {
        this.HRC = HRC;
        this.searchType = searchType;
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println("Yay!");
    }
}
