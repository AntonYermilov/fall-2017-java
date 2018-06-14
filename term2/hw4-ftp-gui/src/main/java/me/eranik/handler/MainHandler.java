package me.eranik.handler;

import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import me.eranik.ftp.Client;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;

public class MainHandler {
    @FXML private TableView<Row> tableView;
    @FXML private Button button;
    @FXML private TextField textField;
    @FXML private Text text;
    private String currentPath = ".";

    /**
     * Forms the scene and creates table of files and folders from the root directory on server.
     */
    @FXML public void initialize() {
        formRowsList();

        button.disableProperty().bind(new BooleanBinding() {
            @Override
            protected boolean computeValue() {
                return true;
            }
        });
    }

    /**
     * Changes the current directory according to the clicked row.
     * @param mouseEvent event that occurs when mouse is clicking on table's row
     */
    @FXML public void handleMouseClick(MouseEvent mouseEvent) {
        Row selectedRow = tableView.getSelectionModel().getSelectedItem();

        button.disableProperty().bind(new BooleanBinding() {
            @Override
            protected boolean computeValue() {
                return !selectedRow.getType().equals("File");
            }
        });

        if (selectedRow.getType().equals("File")) {
            String currentDirectory = Paths.get(".").toAbsolutePath().getParent().toString();
            textField.setText(Paths.get(currentDirectory, selectedRow.getName()).toString());
            return;
        }

        textField.setText("");
        if (mouseEvent.getClickCount() == 2) {
            if (selectedRow.getName().equals("..")) {
                currentPath = Paths.get(currentPath).getParent().toString();
            } else {
                currentPath = Paths.get(currentPath, selectedRow.getName()).toString();
            }
            formRowsList();
        }
    }

    /**
     * Sends query to the server and lists files from the {@code currentDirectory}.
     */
    private void formRowsList() {
        String[] response = Client.processListQuery(currentPath);

        if (response == null) {
            System.err.println("No response from server");
            System.exit(1);
        }

        ObservableList<Row> data = tableView.getItems();
        data.clear();

        ArrayList<Row> folders = new ArrayList<>();
        ArrayList<Row> files = new ArrayList<>();

        for (String responseRow : response) {
            String name = responseRow.split(" ")[0];
            String type = responseRow.split(" ")[1];

            File file = new File(name);

            if (type.equals("true")) {
                folders.add(new Row(file.getName(), "Folder"));
            } else {
                files.add(new Row(file.getName(), "File"));
            }
        }

        if (!currentPath.equals(".")) {
            data.add(new Row("..", ""));
        }
        data.addAll(folders);
        data.addAll(files);
    }

    /**
     * Handles button click. Reads path from text field and saves file from the table
     * locally according to the specified path.
     * @param mouseEvent event that occurs when button is clicked
     */
    public void handleButtonClick(MouseEvent mouseEvent) {
        String localPath = textField.getText();
        if (localPath.isEmpty()) {
            text.setFill(Color.RED);
            text.setText("No file chosen");
            return;
        }

        String name =  tableView.getSelectionModel().getSelectedItem().getName();
        if (Client.processGetQuery(Paths.get(currentPath, name).toString(), localPath)) {
            text.setFill(Color.BLUE);
            text.setText("Success!");
        } else {
            text.setFill(Color.RED);
            text.setText("Error occurred");
        }

        tableView.getSelectionModel().clearSelection();
        textField.clear();
    }

    /**
     * Describes rows in table view. Each row consists of two fields: name of file and it's type,
     * which is equal to "Folder" of "File".
     */
    public static class Row {
        public final SimpleStringProperty name;
        public final SimpleStringProperty type;

        /**
         * Creates row with the specified name and type.
         * @param name name of file
         * @param type type of file (folder of file)
         */
        public Row(String name, String type) {
            this.name = new SimpleStringProperty(name);
            this.type = new SimpleStringProperty(type);
        }

        /**
         * Returns content of name field.
         * @return content of name field
         */
        public String getName() {
            return name.get();
        }

        /**
         * Returns content of type field.
         * @return content of type field
         */
        public String getType() {
            return type.get();
        }
    }
}
