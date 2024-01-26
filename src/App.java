public class App {
    public static void main(String[] args) throws Exception {
        BlackJack bj = new BlackJack();
        GUI gui = new GUI(bj);
        bj.setGUI(gui);

        MainMenu mainMenu = new MainMenu(bj);
        mainMenu.showMainMenu();
    }
}