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
import javafxmvc.model.domain.Cliente;

public class FXMLAnchorPaneCadastroClientesDialogController implements Initializable {

    @FXML
    private Label labelClienteNome;
    
    @FXML
    private Label labelClienteCPF;
    
    @FXML
    private Label labelClienteTelefone;
    
    @FXML
    private TextField textFieldClienteNome;
    
    @FXML
    private TextField textFieldClienteCPF;
    
    @FXML
    private TextField textFieldClienteTelefone;
    
    @FXML
    private Button buttonCancelar;
    
    @FXML
    private Button buttonConfirmar;
    
    private Stage dialogStage;
    private boolean buttonConfirmedCliked = false;    
    private Cliente cliente;
    
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
        this.textFieldClienteNome.setText(cliente.getNome());
        this.textFieldClienteCPF.setText(cliente.getCpf());
        this.textFieldClienteTelefone.setText(cliente.getTelefone());
    }

    @FXML
    public void HandleButtonConfirmar(){
        if(validarEntradaDeDados()){
            cliente.setNome(textFieldClienteNome.getText());
            cliente.setCpf(textFieldClienteCPF.getText());
            cliente.setTelefone(textFieldClienteTelefone.getText());

            buttonConfirmedCliked = true;
            dialogStage.close();
        }
    }
    
    @FXML
    public void HandleButtonCancelar(){
        dialogStage.close();
    }
    
        private boolean validarEntradaDeDados() {
        String errorMessage = "";

        if (textFieldClienteNome.getText() == null || textFieldClienteNome.getText().length() == 0) {
            errorMessage += "Nome inválido!\n";
        }
        if (textFieldClienteCPF.getText() == null || textFieldClienteCPF.getText().length() == 0) {
            errorMessage += "CPF inválido!\n";
        }
        if (textFieldClienteTelefone.getText() == null || textFieldClienteTelefone.getText().length() == 0) {
            errorMessage += "Telefone inválido!\n";
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