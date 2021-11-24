package org.mariangolea.fintrack.bank.parser.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.mariangolea.fintrack.bank.parser.api.ParseResponse;
import org.mariangolea.fintrack.bank.parser.persistence.repository.transactions.BankTransactionService;
import org.mariangolea.fintrack.bank.parser.ui.categorized.table.TransactionTableView;
import org.mariangolea.fintrack.bank.parser.ui.preferences.categories.CategoriesView;
import org.mariangolea.fintrack.bank.parser.ui.preferences.companynames.EditCompanyNamesPane;
import org.mariangolea.fintrack.bank.parser.ui.uncategorized.UncategorizedView;
import org.mariangolea.fintrack.bank.parser.ui.uncategorized.edit.UncategorizedTransactionApplyListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

@SpringBootApplication
@EnableAutoConfiguration
@EnableJpaRepositories("org.mariangolea.fintrack.bank.parser.persistence.repository.*")
@ComponentScan("org.mariangolea.fintrack.bank.*")
@EntityScan("org.mariangolea.fintrack.bank.*")
public class CsvParserUI extends Application implements UncategorizedTransactionApplyListener {

    protected TextFlow feedbackPane;
    private TransactionTableView tableView;
    private UncategorizedView uncategorizedView;
    private final List<ParseResponse> parsedTransactionsCopy = new ArrayList<>();
    private UserPreferencesHandlerInterface userPrefsHandler;
    private UserPreferencesInterface userPrefs;
    protected final List<File> parsedCsvFiles = new ArrayList<>();
    private Stage primaryStage;
    private BorderPane root;

    protected static final String START_PARSE_MESSAGE = "Started parsing the selected CSV files ...";
    protected static final String FINISHED_PARSING_CSV_FILES = "Finished parsing the CSV files: ";

    private ConfigurableApplicationContext context;
    
    
    @Autowired
    private BankTransactionService transactionController;
    
    @Override
    public void init() throws Exception {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(CsvParserUI.class);
        context = builder.run(getParameters().getRaw().toArray(new String[0]));
        context.getAutowireCapableBeanFactory().autowireBean(this);
        initUserPrefs();
        layoutComponents();
        updateView();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Bank Transactions Merger");
        primaryStage.setScene(new Scene(root, 1000, 600, Color.BISQUE));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        context.close();
    }
    
    @Override
    public void transactionEditApplied() {
        userPrefsHandler.storePreferences();
        updateView();
    }

    protected void loadData(final List<ParseResponse> parsedTransactions) {
        Objects.requireNonNull(parsedTransactions).forEach(csvFileResponse
                -> transactionController.saveOrUpdate(csvFileResponse.parsedTransactions)
        );
        updateView();
    }

    protected MenuBar createMenu() {
        MenuBar menu = constructSimpleMenuBar();
        Menu file = new Menu("File");
        file.setAccelerator(KeyCombination.keyCombination("Alt+f"));
        MenuItem load = new MenuItem("Load data from CSV file.");
        load.setAccelerator(KeyCombination.keyCombination("l"));
        load.setOnAction((ActionEvent e) -> popCSVFileChooser());
        file.getItems().add(load);
        menu.getMenus().add(file);

        Menu edit = new Menu("Edit");
        edit.setAccelerator(KeyCombination.keyCombination("Alt+e"));
        MenuItem editCompanies = new MenuItem("Edit company names");
        editCompanies.setAccelerator(KeyCombination.keyCombination("c"));
        editCompanies.setOnAction((ActionEvent e) -> editCompanyNames());
        MenuItem editCategories = new MenuItem("Edit categories");
        editCategories.setAccelerator(KeyCombination.keyCombination("d"));
        editCategories.setOnAction((ActionEvent e) -> editCategories());
        edit.getItems().add(editCompanies);
        edit.getItems().add(editCategories);
        menu.getMenus().add(edit);

        menu.setUseSystemMenuBar(true);

        return menu;
    }

    protected MenuBar constructSimpleMenuBar() {
        return new MenuBar();
    }

    protected void createUncategorizedView() {
        if (uncategorizedView == null) {
            uncategorizedView = new UncategorizedView(this, userPrefs);
        }
    }

    protected void popCSVFileChooser() {
        String inputFolder = userPrefs.getCSVInputFolder();
        FileChooser chooser = new FileChooser();
        File inputFolderFile = null;
        if (inputFolder != null) {
            inputFolderFile = new File(inputFolder);
            if (inputFolderFile.exists()) {
                chooser.setInitialDirectory(inputFolderFile);
            }
        }

        ExtensionFilter filter = new ExtensionFilter(
                "CSV files only", "*.csv");
        chooser.getExtensionFilters().add(filter);
        chooser.setSelectedExtensionFilter(filter);
        List<File> csvFiles = chooser.showOpenMultipleDialog(primaryStage);
        if (csvFiles != null && !csvFiles.isEmpty()) {
            parseUserSelectedCSVFiles(csvFiles);
        }
    }

    protected void editCategories() {
        final CategoriesView pane = new CategoriesView(userPrefs);
        EditDialog popup = new EditDialog<>("Edit global company names preferences", pane, CategoriesView::getResult, null);
        Optional<UserPreferencesInterface> result = popup.showAndWait();
        result.ifPresent(userData -> {
            userPrefs.applyChanges(Objects.requireNonNull(userData));
            userPrefsHandler.storePreferences();
            updateView();
        });
    }
    
    protected void editCompanyNames() {
        final EditCompanyNamesPane pane = new EditCompanyNamesPane(userPrefs);
        EditDialog popup = new EditDialog<>("Edit global company names preferences", pane, EditCompanyNamesPane::getResult, null);
        Optional<UserPreferencesInterface> result = popup.showAndWait();
        result.ifPresent(userData -> {
            userPrefs.applyChanges(Objects.requireNonNull(userData));
            userPrefsHandler.storePreferences();
            updateView();
        });
    }

    protected void parseUserSelectedCSVFiles(List<File> csvFiles) {
        userPrefs.setCSVInputFolder(csvFiles.get(0).getParent());
        userPrefsHandler.storePreferences();
        parsedCsvFiles.addAll(csvFiles);
        startParsingCsvFiles(csvFiles);
    }

    protected void startParsingCsvFiles(final List<File> csvFiles) {
        appendReportMessage(START_PARSE_MESSAGE);
        Task<List<CsvFileParseResponse>> parseResponse = new Task<List<CsvFileParseResponse>>() {
            @Override
            protected List<CsvFileParseResponse> call() throws Exception {
                BankTransactionsParser fac = new BankTransactionsParser();
                final List<CsvFileParseResponse> res = new ArrayList<>();
                csvFiles.stream().map((csvFile) -> fac.parseTransactions(csvFile)).filter((response) -> 
                        (response != null)).forEachOrdered((response) -> res.add(response));
                return res;
            }
        };

        parseResponse.setOnSucceeded(e -> {
            Platform.runLater(() -> {
                appendReportMessage(FINISHED_PARSING_CSV_FILES);
                List<CsvFileParseResponse> values = (List<CsvFileParseResponse>) e.getSource().getValue();
                loadData(values);
                values.forEach((response) -> appendReportText(response));
                parsedTransactionsCopy.clear();
                parsedTransactionsCopy.addAll(values);
            });
        });
        Thread runThread = new Thread(parseResponse);
        runThread.start();
    }

    protected ScrollPane createFeedbackView() {
        feedbackPane = constructFeedbackPane();
        ScrollPane scrollPane = new ScrollPane(feedbackPane);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    protected void createTableView() {
        if (tableView == null) {
            tableView = new TransactionTableView(userPrefs);
        }
    }

    protected TextFlow constructFeedbackPane() {
        return new TextFlow();
    }

    protected void appendReportText(CsvFileParseResponse fileResponse) {
        appendHyperlinkToFile("\n", fileResponse.csvFile, ": ");
        if (fileResponse.allCsvContentProcessed && fileResponse.expectedTransactionsNumber == fileResponse.foundTransactionsNumber) {
            appendReportMessage("ALL OK!");
        } else if (!fileResponse.allCsvContentProcessed) {
            String message = "Find below CSV content lines unprocessed.\n";
            message = fileResponse.unprocessedStrings.stream().map(line -> "\t" + line).reduce(message, String::concat);
            appendReportMessage(message);
        } else {
            if (fileResponse.expectedTransactionsNumber == 0) {
                //coould not validate because CSV content did not specify an expected amount.
                appendReportMessage("CSV content did not specify a expected amount. Parser found " + fileResponse.foundTransactionsNumber + " transactions.");
            } else {
                appendReportMessage("Mismatch between expected and found transaction numbers (expected: " + fileResponse.expectedTransactionsNumber + ", found: " + fileResponse.foundTransactionsNumber + ").");
            }
        }
    }

    protected boolean appendHyperlinkToFile(final String pre, final File sourceFile, final String post) {
        if (sourceFile == null) {
            return false;
        }

        if (pre != null && !pre.isEmpty()) {
            feedbackPane.getChildren().add(new Text(pre));
        }

        Hyperlink hyperlink = new Hyperlink(sourceFile.getName());
        hyperlink.setOnMouseClicked(event -> getHostServices().showDocument(sourceFile.toURI().toString()));
        feedbackPane.getChildren().add(hyperlink);

        if (post != null && !post.isEmpty()) {
            feedbackPane.getChildren().add(new Text(post));
        }

        return true;
    }

    protected boolean appendReportMessage(final String message) {
        if (message == null) {
            return false;
        }
        feedbackPane.getChildren().add(new Text(message));
        return true;
    }

    protected void initUserPrefs() {
        UserPreferencesAbstractFactory factory = initUserPreferencesFactory();
        userPrefsHandler = Objects.requireNonNull(Objects.requireNonNull(factory).getUserPreferencesHandler());
        userPrefs = userPrefsHandler.getPreferences();
    }

    private void updateView() {
        TransactionsCategorizedSlotter calc = new TransactionsCategorizedSlotter(transactionController, userPrefs);
        uncategorizedView.updateModel(calc.getUnmodifiableUnCategorized());
        tableView.resetView(calc.getUnmodifiableSlottedCategorized());
    }

    protected void layoutComponents() {
        MenuBar menuBar = createMenu();
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));
        ColumnConstraints col = new ColumnConstraints();
        col.setFillWidth(true);
        col.setHgrow(Priority.ALWAYS);
        grid.getColumnConstraints().add(col);

        ScrollPane feedback = createFeedbackView();

        GridPane display = new GridPane();
        display.getColumnConstraints().add(col);
        createTableView();
        createUncategorizedView();
        display.add(tableView, 0, 0, 12, 1);
        display.add(uncategorizedView, 12, 0, 3, 1);

        grid.add(display, 0, 0, 15, 12);
        grid.add(feedback, 0, 12, 15, 1);
        grid.setStyle("-fx-background-color: BEIGE;");

        root = new BorderPane();
        root.setTop(menuBar);
        root.setCenter(grid);
    }

    protected Pane getRoot() {
        return root;
    }
}
