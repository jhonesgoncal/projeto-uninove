
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
import javafxmvc.model.dao.ClienteDAO;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Cliente;
import javafxmvc.model.domain.Produto;

public class FXMLAnchorPaneCadastroProdutoController implements Initializable {

    @FXML
    private TableView<Produto> tableViewCadastroProduto;
    @FXML
    private TableColumn<Produto, String> tableColumnNome;
    @FXML
    private TableColumn<Produto, String> tableColumnPreco;
    @FXML
    private Button buttonInserirProduto;
    @FXML
    private Button buttonAlterarProduto;
    @FXML
    private Button buttonRemoverProduto;
    
    @FXML
    private Label labelNomeProduto;
    @FXML
    private Label labelPrecoProduto;
    @FXML
    private Label labelQuantidadeProduto;
    @FXML
    private Label labelCategoriaProduto;
    
    private List<Produto> listProdutos;
    private ObservableList<Produto> observableListProdutos;
    
    //Conexao com o banco
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final ProdutoDAO produtoDao = new ProdutoDAO();
            
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        produtoDao.setConnection(connection);
        carregarTableViewProdutos();
        
        tableViewCadastroProduto.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> selecionarItemTableViewProdutos(newValue)
        );
    }    
    
    public void carregarTableViewProdutos(){
        tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        tableColumnPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));
        
        listProdutos = produtoDao.listar();
        
        observableListProdutos = FXCollections.observableArrayList(listProdutos);
        tableViewCadastroProduto.setItems(observableListProdutos);
    }
    
    public void selecionarItemTableViewProdutos(Produto produto){
        if(produto != null){
            labelNomeProduto.setText(String.valueOf(produto.getNome()));
            labelPrecoProduto.setText(String.valueOf(produto.getPreco()));
            labelQuantidadeProduto.setText(String.valueOf(produto.getQuantidade()));
            labelCategoriaProduto.setText(produto.getCategoria().toString());
        }else{
            labelNomeProduto.setText("");
            labelPrecoProduto.setText("");
            labelQuantidadeProduto.setText("");
            labelCategoriaProduto.setText("");
        }
       
    }
    
    public void handleInserirProduto() throws IOException{
        Produto produto = new Produto();
        boolean buttonConfirmedCliked = showFMXLAnchorPaneCadastrosProdutosDialog(produto);
        if(buttonConfirmedCliked){
            produtoDao.inserir(produto);
            carregarTableViewProdutos();
        }
    }
    
     public void buttonAlterarProduto() throws IOException{
        Produto produto = tableViewCadastroProduto.getSelectionModel().getSelectedItem();
        if(produto != null){
            boolean buttonConfirmedCliked = showFMXLAnchorPaneCadastrosProdutosDialog(produto);
            if(buttonConfirmedCliked){
                produtoDao.alterar(produto);
                carregarTableViewProdutos();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um produto na tabela!");
            alert.show();
        }
    }
     
    public void handleRemoverProduto() throws IOException{
        Produto produto = tableViewCadastroProduto.getSelectionModel().getSelectedItem();
        if(produto != null){
                produtoDao.remover(produto);
                carregarTableViewProdutos();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um produto na tabela!");
            alert.show();
        }
    }
    
    public boolean showFMXLAnchorPaneCadastrosProdutosDialog(Produto produto) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroClientesDialogController.class.getResource("/javafxmvc/view/FXMLAnchorPaneCadastroProdutoDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Produto");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        
        FXMLAnchorPaneCadastroProdutoDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setProduto(produto);
        
        
        dialogStage.showAndWait();
        
        return controller.isButtonConfirmedCliked();
        
    }
    
}
