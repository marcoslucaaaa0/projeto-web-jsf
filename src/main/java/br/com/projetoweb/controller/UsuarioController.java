package br.com.projetoweb.controller;

import br.com.projetoweb.enums.ModeloGenero;
import br.com.projetoweb.model.Estado;
import br.com.projetoweb.model.Usuario;
import br.com.projetoweb.dao.UsuarioDAO;
import br.com.projetoweb.util.*;

import static br.com.projetoweb.shared.Constantes.*;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@ViewScoped
@ManagedBean(name = "usuarioMB")
public class UsuarioController implements Serializable {
    private ArrayList<Usuario> listaComUsuarios;
    private UsuarioDAO userDAO;
    private Usuario usuario;
    private String condicaoTelaCadastarAtualizar;
    private List<ModeloGenero> listaComModelosDeGeneros;
    private List<Estado> listaComEstados;
    private Boolean renderizarCamposRG;
    private String emailAlteracao;
    private String senhaConfirmar;
    private String senhaAntiga;
    private String senhaNova;

    public UsuarioController() {
        userDAO = new UsuarioDAO();
        renderizarCamposRG = false;
        usuario = new Usuario();
        senhaConfirmar = null;
        senhaAntiga = null;
        senhaNova = null;
    }

    public void iniciarDadosPaginaPrincipal() {
        listaComUsuarios = userDAO.retornarListaComUsuarios();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("user_update");
    }

    public void redirecionarAtualizar() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user_update", usuario);
        PagesUtil.redirectPage("cadastrarusuario");
    }

//    public void redirecionarCadastrar() throws IOException {
//        PagesUtil.redirectPage("cadastrarusuario");
//    }
//    public void redirecionarlogin() throws IOException {
//        PagesUtilLogin.redirecionarLogin("login");
//    }

    public void definirCadastrarAtualizar() {
        if (isUserUpdate()) {
            usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_update");
            emailAlteracao = usuario.getEmail();
            condicaoTelaCadastarAtualizar = CONDICAO_ATUALIZAR;
            checarCamposRG();
        } else {
            condicaoTelaCadastarAtualizar = CONDICAO_CADASTRAR;
        }
        iniciarListas();
    }

    private String retornarCondicaoTelaCadastarAtualizar() {
        if (isUserUpdate()) {
            return CONDICAO_ATUALIZAR;
        } else {
            return CONDICAO_CADASTRAR;
        }
    }

    public boolean isUserUpdate() {
        return VerificadorUtil.naoEstaNulo(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_update"));
    }

    public void checarCamposRG() {
        renderizarCamposRG = VerificadorUtil.naoEstaNuloOuVazio(usuario.getRgNumero());
        iniciarListas();
    }

    private void iniciarListas() {
        listaComModelosDeGeneros = Arrays.asList(ModeloGenero.values());
        listaComEstados = userDAO.retornarListaComEstados();
    }

    public void validarCondicaoGravacao() throws IOException {
        if (retornarCondicaoTelaCadastarAtualizar().equals(CONDICAO_CADASTRAR)) {
            validarUsuarioCadastrado();
        } else {
            alterarUsuario();
        }
    }

    public String retornarGenero(String sigla) {
        if (VerificadorUtil.naoEstaNuloOuVazio(sigla)) {
            return (sigla.equals(ModeloGenero.MASCULINO.getSigla())) ? ModeloGenero.MASCULINO.name() : ModeloGenero.FEMININO.name() ;
        } else {
            return null;
        }
    }

    public void validarUsuarioCadastrado() throws IOException {
        if (userDAO.verificarEmail(usuario)) {
            MensagemUtil.alerta("Usuario já cadastrado nesse Email");
        } else if (!usuario.getSenha().equals(senhaConfirmar)) {
            MensagemUtil.alerta("Senhas não conferem");
        } else {
            senhaConfirmar = null;
            cadastrarUsuario();
        }
    }

    private void cadastrarUsuario() throws IOException {
        if (userDAO.gravarUsuario(usuario)) {
            if (VerificadorUtil.estaNulo(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_session"))) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user_session", usuario);
            }
            PagesUtil.redirectPage("principal");
            MensagemUtil.sucesso("Usuario cadastrado com sucesso");
            usuario = new Usuario();
        } else {
            MensagemUtil.erro("Erro ao cadastrar Usuario");
        }
    }

    private void alterarUsuario() throws IOException {
        if (userDAO.alterarUsuario(usuario, emailAlteracao)) {
            PagesUtil.redirectPage("principal");
            MensagemUtil.sucesso("Usuario alterado com sucesso");
            usuario = new Usuario();
        } else {
            MensagemUtil.erro("Erro ao alterar Usuario");
        }
    }

    public void excluirUsuario() throws IOException {
        if (userDAO.deletarUsuario(usuario.getEmail())) {
            MensagemUtil.sucesso("Usuario excluido com sucesso");
            usuario = new Usuario();
            PagesUtil.redirectPage("principal");
        } else {
            MensagemUtil.erro("Erro ao excluir Usuario!");
        }
    }

    public void alterarSenha(){
        if (!usuario.getSenha().equals(senhaAntiga)) {
            MensagemUtil.alerta("Senhas atual incorreta!");
        }else if(!senhaNova.equals(senhaConfirmar)){
            MensagemUtil.alerta("Senha nova e confirmação não conferem!");
        }else{
            if(userDAO.alterarSenha(senhaNova, emailAlteracao)){
                PagesUtil.fecharDialog("dlgAlterarSenha");
                MensagemUtil.sucesso("Senha alterada com sucesso");
            }else{
                MensagemUtil.erro("Erro ao alterar senha");
            }
        }
    }

    public void buscarCep() throws ViaCEPException {
        usuario.setEndereco(ViaCepUtil.buscarEndereco(usuario.getEndereco()));
    }

    public Date retornarDataMaxima() {
        return DataUtil.getDataMaxima();
    }

    public String cpfMascara(String cpf) {
        return (VerificadorUtil.naoEstaNuloOuVazio(cpf)) ? StringUtil.putMask(cpf, CPF_MASCARA) : null;
    }

    public String cepMascara(String cep) {
        return (VerificadorUtil.naoEstaNuloOuVazio(cep)) ? StringUtil.putMask(cep, CEP_MASCARA) : null;
    }

    public String telefoneMascara(String telefone) {
        return (VerificadorUtil.naoEstaNuloOuVazio(telefone)) ? StringUtil.putMask(telefone, TELEFONE_MASCARA) : null;
    }

    public ArrayList<Usuario> getListaComUsuarios() {
        return listaComUsuarios;
    }

    public void setListaComUsuarios(ArrayList<Usuario> listaComUsuarios) {
        this.listaComUsuarios = listaComUsuarios;
    }

    public String getCondicaoTelaCadastarAtualizar() {
        return condicaoTelaCadastarAtualizar;
    }

    public void setCondicaoTelaCadastarAtualizar(String condicaoTelaCadastarAtualizar) {
        this.condicaoTelaCadastarAtualizar = condicaoTelaCadastarAtualizar;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<ModeloGenero> getListaComModelosDeGeneros() {
        return listaComModelosDeGeneros;
    }

    public void setListaComModelosDeGeneros(List<ModeloGenero> listaComModelosDeGeneros) {
        this.listaComModelosDeGeneros = listaComModelosDeGeneros;
    }

    public List<Estado> getListaComEstados() {
        return listaComEstados;
    }

    public void setListaComEstados(List<Estado> listaComEstados) {
        this.listaComEstados = listaComEstados;
    }

    public Boolean getRenderizarCamposRG() {
        return renderizarCamposRG;
    }

    public void setRenderizarCamposRG(Boolean renderizarCamposRG) {
        this.renderizarCamposRG = renderizarCamposRG;
    }

    public String getSenhaConfirmar() {
        return senhaConfirmar;
    }

    public void setSenhaConfirmar(String senhaConfirmar) {
        this.senhaConfirmar = senhaConfirmar;
    }

    public String getSenhaNova() {
        return senhaNova;
    }

    public void setSenhaNova(String senhaNova) {
        this.senhaNova = senhaNova;
    }

    public String getSenhaAntiga() {
        return senhaAntiga;
    }

    public void setSenhaAntiga(String senhaAntiga) {
        this.senhaAntiga = senhaAntiga;
    }
}
