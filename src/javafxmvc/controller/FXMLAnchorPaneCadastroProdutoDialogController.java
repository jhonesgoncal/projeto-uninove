
package javafxmvc.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxmvc.model.dao.CategoriaDAO;
import javafxmvc.model.dao.ClienteDAO;
import javafxmvc.model.dao.ProdutoDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Categoria;
import javafxmvc.model.domain.Cliente;
import javafxmvc.model.domain.Produto;


public class FXMLAnchorPaneCadastroProdutoDialogController implements Initializable {
    
    
    @FXML
    private TextField textFieldNome;
    
    @FXML
    private TextField textFieldPreco;
    
    @FXML
    private TextField textFieldQuantidade;
    
    @FXML
    private ComboBox comboBoxCategoria;
    
    @FXML
    private Button buttonCancelar;
    
    @FXML
    private Button buttonCadastrar;
    
    private ObservableList<Categoria> observableListCategorias;
    private List<Categoria> listCategorias;
    
    private Stage dialogStage;
    private boolean buttonConfirmedCliked = false;    
    private Produto produto;
    
     //Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    
     public Stage getDialoStage() {
        return dialogStage;
    }

    public void setDialogStage(Stage dialoStage) {
        this.dialogStage = dialoStage;
    }

    public boolean isButtonConfirmedCliked() {
        return buttonConfirmedCliked;
    }

    public void setButtonConfirmedCliked(boolean buttonConfirmedCliked) {
        this.buttonConfirmedCliked = buttonConfirmedCliked;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
        this.textFieldNome.setText(produto.getNome());
        this.textFieldPreco.setText(String.valueOf(produto.getPreco()));
        this.textFieldQuantidade.setText(String.valueOf(produto.getQuantidade()));
        this.comboBoxCategoria.setValue(produto.getCategoria());
    }

    @FXML
    public void handleCadastrar(){
        if(validarEntradaDeDados()){
            produto.setNome(textFieldNome.getText());
            produto.setPreco(Double.valueOf(textFieldPreco.getText()));
            produto.setQuantidade(Integer.valueOf(textFieldQuantidade.getText()));
            produto.setCategoria((Categoria) comboBoxCategoria.getSelectionModel().getSelectedItem());
            buttonConfirmedCliked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    public void handleCancelar(){
        dialogStage.close();
    }
    
     public void carregarComboBoxCategoria() {
        listCategorias = categoriaDAO.listar();
        observableListCategorias = FXCollections.observableArrayList(listCategorias);
        comboBoxCategoria.setItems(observableListCategorias);
    }
    
    private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (textFieldNome.getText() == null || textFieldNome.getText().length() == 0) {
            errorMessage += "Nome inválido!\n";
        }
        if (textFieldPreco.getText() == null || textFieldPreco.getText().length() == 0) {
            errorMessage += "Preco inválido!\n";
        }
        if (textFieldQuantidade.getText() == null || textFieldQuantidade.getText().length() == 0) {
            errorMessage += "Quantidade inválida!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Mostrando a mensagem de erro
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro no cadastro");
            alert.setHeaderText("Campos inválidos, por favor, corrija...");
            alert.setContentText(errorMessage);
            alert.show();
            return false;
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoriaDAO.setConnection(connection);
        produtoDAO.setConnection(connection);
        carregarComboBoxCategoria();
    }    
    
}
