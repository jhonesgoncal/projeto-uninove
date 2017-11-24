/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmvc.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxmvc.model.domain.Categoria;
import javafxmvc.model.domain.Cliente;


public class FXMLAnchorPaneCadastroCategoriaDialogController implements Initializable {

   
    @FXML
    private TextField textFieldCadastroCategoria;
    
    @FXML
    private Button buttonCancelar;
    
    @FXML
    private Button buttonConfirmar;
    
    private Stage dialogStage;
    private boolean buttonConfirmedCliked = false;    
    private Categoria categoria;
    
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

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
        this.textFieldCadastroCategoria.setText(categoria.getDescricao());
    }

    @FXML
    public void handleButtonCadastrar(){
        if(validarEntradaDeDados()){
            categoria.setDescricao(textFieldCadastroCategoria.getText());

            buttonConfirmedCliked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    public void handleButtonCancelar(){
        dialogStage.close();
    }
    
        private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (textFieldCadastroCategoria.getText() == null || textFieldCadastroCategoria.getText().length() == 0) {
            errorMessage += "Nome inválido!\n";
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
        // TODO
    }    
    
}
