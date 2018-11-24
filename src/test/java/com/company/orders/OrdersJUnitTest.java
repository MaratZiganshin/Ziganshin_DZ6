package com.company.orders;

import org.assertj.swing.core.matcher.DialogMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.data.TableCell;
import org.assertj.swing.edt.FailOnThreadViolationRepaintManager;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.fixture.DialogFixture;
import org.assertj.swing.fixture.FrameFixture;

public class OrdersJUnitTest {
    private FrameFixture window;

    @BeforeClass
    public static void setUpOnce() {
        FailOnThreadViolationRepaintManager.install();
    }

    @Before
    public void setUp() {
        final JfrmMain frame = GuiActionRunner.execute(new GuiQuery<JfrmMain>(){
            @Override
            protected JfrmMain executeInEDT() throws Throwable {
                return new JfrmMain();
            }
        });

        window = new FrameFixture(frame);
        window.show(); // shows the frame to test
    }

    class Customer {
        Customer (String name, String street, String city, String state, String zip, String cardNo, String expDate) {
            this.name = name;
            this.street = street;
            this.city = city;
            this.state = state;
            this.zip = zip;
            this.cardNo = cardNo;
            this.expDate = expDate;
        }
        String name;
        String street;
        String city;
        String state;
        String zip;
        String cardNo;
        String expDate;
    }

    @Test
    public void testOrders() throws InterruptedException {
        // Menu Item Selection
        window.menuItemWithPath("File", "New").click();

        Customer[] customers = new Customer[]{
                new Customer("Kenyon Stuart", "73668 East Daniels Way", "Thousand Oaks", "NE", "06842", "1330019087", "01/01/21"),
                new Customer("Ezra Sparks", "64574 Mcclain Way", "Florence", "MD", "18769", "1978201255", "01/10/22"),
                new Customer("Mia Pratt", "27891 Saint Lucia Blvd.", "Kona",  "FL", "42423", "2062480260", "01/11/22" ),
                new Customer("Gavin Roberson", "19804 Uzbekistan Ct.", "Atlantic City",  "NY", "06595", "1528583210", "01/03/25"),
                new Customer("Uma Donovan", "64440 England Ln.", "Bakersfield",  "NJ", "18396", "1621081159", "01/01/20"),
                new Customer("Igor Newman", "65172 Bolivia Way", "Homer",  "NE", "90889" , "1516240863", "01/05/22"),
                new Customer("Amery Freeman", "50923 Garner Ct.", "Santa Barbara",  "CT", "37177", "2052255336", "01/08/24"),
                new Customer("Rana Rodriquez", "74465 North Barbados Ct.", "Biddeford",  "MA", "94671","1669447557", "01/01/23"),
                new Customer("Chelsea Sullivan", "82225 East Boyer St.", "Pittsburgh",  "MA", "20188", "2146313492", "01/04/24"),
                new Customer("Norman Ward", "26083 Hartman Ln.", "Weirton", "WA", "38447", "1478127250" , "01/02/25")
        };

        for (int i = 0; i < customers.length; ++i) {
            window.menuItemWithPath("Orders", "New order...").click();

            // Filling an order
            final DialogFixture dialog = window.dialog();

            dialog.comboBox("jcbProduct").selectItem("FamilyAlbum");
            dialog.spinner("jspQuantity").enterText("10");
            dialog.textBox("jtfName").enterText(customers[i].name);
            dialog.textBox("jtfStreet").enterText(customers[i].street);
            dialog.textBox("jtfState").enterText(customers[i].state);
            dialog.textBox("jtfCity").enterText(customers[i].city);
            dialog.textBox("jtfZip").enterText(customers[i].zip);
            dialog.textBox("jtfCardNumber").enterText(customers[i].cardNo);
            dialog.textBox("jftfExpiration").setText("").enterText(customers[i].expDate);

            dialog.button(JButtonMatcher.withText("Ok")).click();
        }

        // Checking table contents
        for (int i = 0; i < customers.length; ++i) {
            window.table().requireCellValue(TableCell.row(i).column(0), customers[i].name);
            window.table().requireCellValue(TableCell.row(i).column(4), customers[i].street);
            window.table().requireCellValue(TableCell.row(i).column(6), customers[i].state);
            window.table().requireCellValue(TableCell.row(i).column(5), customers[i].city);
            window.table().requireCellValue(TableCell.row(i).column(7), customers[i].zip);
            window.table().requireCellValue(TableCell.row(i).column(9), customers[i].cardNo);
        }

        // Closing the window
        /*window.close();
        final DialogFixture confirmation =
            window.dialog(DialogMatcher.withTitle("Confirmation"));
        confirmation.button(JButtonMatcher.withText("No")).click();*/
    }

    @After
    public void tearDown() {
        window.cleanUp();
    }
}