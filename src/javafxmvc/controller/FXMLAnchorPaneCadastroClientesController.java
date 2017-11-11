package javafxmvc.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Cliente;
import javafxmvc.model.dao.ClienteDAO;
public class FXMLAnchorPaneCadastroClientesController implements Initializable {

    @FXML
    private TableView<Cliente> tableViewClientes;
    @FXML
    private TableColumn<Cliente, String> tableColumnNome;
    @FXML
    private TableColumn<Cliente, String> tableColumnCPF;
    @FXML
    private Button buttonInserir;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Button buttonRemover;
     @FXML
    private Label labelClienteCodigo;
    @FXML
    private Label labelClienteNome;
    @FXML
    private Label labelClienteCPF;
    @FXML
    private Label labelClienteTelefone;
    
    private List<Cliente> listClientes;
    private ObservableList<Cliente> observableListClientes;
    
    //Conexao com o banco
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final ClienteDAO clienteDao = new ClienteDAO();
            
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clienteDao.setConnection(connection);
        carregarTableViewClientes();
        
        tableViewClientes.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewClientes(newValue)
        );
    }    
    
    public void carregarTableViewClientes(){
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnCPF.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        
        listClientes = clienteDao.listar();
        
        observableListClientes = FXCollections.observableArrayList(listClientes);
        tableViewClientes.setItems(observableListClientes);
    }
    
    public void selecionarItemTableViewClientes(Cliente cliente){
        if(cliente != null){
            labelClienteCodigo.setText(String.valueOf(cliente.getCdCliente()));
            labelClienteNome.setText(cliente.getNome());
            labelClienteCPF.setText(cliente.getCpf());
            labelClienteTelefone.setText(cliente.getTelefone());
        }else{
            labelClienteCodigo.setText("");
            labelClienteNome.setText("");
            labelClienteCPF.setText("");
            labelClienteTelefone.setText("");
        }
       
    }
    
    public void HandleButtonInserir() throws IOException{
        Cliente cliente = new Cliente();
        boolean buttonConfirmedCliked = showFMXLAnchorPaneCadastrosClientesDialog(cliente);
        if(buttonConfirmedCliked){
            clienteDao.inserir(cliente);
            carregarTableViewClientes();
        }
    }
    
     public void HandleButtonAlterar() throws IOException{
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();
        if(cliente != null){
            boolean buttonConfirmedCliked = showFMXLAnchorPaneCadastrosClientesDialog(cliente);
            if(buttonConfirmedCliked){
                clienteDao.alterar(cliente);
                carregarTableViewClientes();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um cliente na tabela!");
            alert.show();
        }
    }
     
    public void HandleButtonRemover() throws IOException{
        Cliente cliente = tableViewClientes.getSelectionModel().getSelectedItem();
        if(cliente != null){
                clienteDao.remover(cliente);
                carregarTableViewClientes();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um cliente na tabela!");
            alert.show();
        }
    }
    
    public boolean showFMXLAnchorPaneCadastrosClientesDialog(Cliente cliente) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroClientesDialogController.class.getResource("/javafxmvc/view/FXMLAnchorPaneCadastroClientesDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Clientes");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        FXMLAnchorPaneCadastroClientesDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCliente(cliente);
        
        dialogStage.showAndWait();
        
        return controller.isButtonConfirmedCliked();
        
    }
}
