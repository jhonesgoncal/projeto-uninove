/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafxmvc.model.dao.CategoriaDAO;
import javafxmvc.model.database.Database;
import javafxmvc.model.database.DatabaseFactory;
import javafxmvc.model.domain.Categoria;
import javafxmvc.model.domain.Cliente;

public class FXMLAnchorPaneCadastroCategoriaController implements Initializable {
    @FXML
    private TableView<Categoria> tableViewCategoria;
    @FXML
    private TableColumn<Cliente, String> tableColumnCategoriaNome;
    @FXML
    private Button buttonInserir;
    @FXML
    private Button buttonAlterar;
    @FXML
    private Button buttonRemover;
    
    private List<Categoria> listCategorias;
    private ObservableList<Categoria> observableListCategorias;
    
    //Conexao com o banco
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final CategoriaDAO categoriaDao = new CategoriaDAO();
            
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        categoriaDao.setConnection(connection);
        carregarTableViewClientes();
        
        tableViewCategoria.getSelectionModel().selectedItemProperty();
    }    
    
    public void carregarTableViewClientes(){
        tableColumnCategoriaNome.setCellValueFactory(new PropertyValueFactory<>("descricao"));
               
        listCategorias = categoriaDao.listar();
        
        observableListCategorias = FXCollections.observableArrayList(listCategorias);
        tableViewCategoria.setItems(observableListCategorias);
    }
    
    public void selecionarItemTableViewCategorias(Categoria categoria){
       
    }
    
    public void handleButtonInserir() throws IOException{
        Categoria categoria = new Categoria();
        boolean buttonConfirmedCliked = showFMXLAnchorPaneCadastrosCategoriasDialog(categoria);
        if(buttonConfirmedCliked){
            categoriaDao.inserir(categoria);
            carregarTableViewClientes();
        }
    }
    
     public void handleButtonAlterar() throws IOException{
        Categoria categoria = tableViewCategoria.getSelectionModel().getSelectedItem();
        if(categoria != null){
            boolean buttonConfirmedCliked = showFMXLAnchorPaneCadastrosCategoriasDialog(categoria);
            if(buttonConfirmedCliked){
                categoriaDao.alterar(categoria);
                carregarTableViewClientes();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha um categoria na tabela!");
            alert.show();
        }
    }
     
    public void handleButtonRemover() throws IOException{
        Categoria categoria = tableViewCategoria.getSelectionModel().getSelectedItem();
        if(categoria != null){
                categoriaDao.remover(categoria);
                carregarTableViewClientes();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Por favor, escolha uma categoria na tabela!");
            alert.show();
        }
    }
    
    public boolean showFMXLAnchorPaneCadastrosCategoriasDialog(Categoria categoria) throws IOException{
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(FXMLAnchorPaneCadastroClientesDialogController.class.getResource("/javafxmvc/view/FXMLAnchorPaneCadastroCategoriaDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();
        
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Cadastro de Categoria");
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);
        
        
        FXMLAnchorPaneCadastroCategoriaDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setCategoria(categoria);
       
        dialogStage.showAndWait();
        
        return controller.isButtonConfirmedCliked();
        
    }   
    
}
