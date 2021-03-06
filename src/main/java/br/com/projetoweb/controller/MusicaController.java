package br.com.projetoweb.controller;

import br.com.projetoweb.dao.MusicaDAO;
import br.com.projetoweb.enums.GenerosMusicais;
import br.com.projetoweb.model.Musica;
import br.com.projetoweb.util.MensagemUtil;
import br.com.projetoweb.util.PagesUtil;
import br.com.projetoweb.util.VerificadorUtil;
import com.sun.faces.context.SessionMap;
import org.primefaces.context.RequestContext;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpSession;
import static br.com.projetoweb.shared.Constantes.CONDICAO_ATUALIZAR;
import static br.com.projetoweb.shared.Constantes.CONDICAO_CADASTRAR;
import javax.*;
@ViewScoped
@ManagedBean(name = "musicaMB")
public class MusicaController {
    private List<GenerosMusicais> listaComGeneros;
    private MusicaDAO musicaDao;
    private Musica musica;
    private ArrayList<Musica> listaComMusicas;
    private String condicaoTelaCadastarMusicaAtualizar;
    private String filtroTitulo;

    public MusicaController() {
        musica = new Musica();
        musicaDao = new MusicaDAO();
        listaComMusicas = new ArrayList<>();
        listaComGeneros = new ArrayList<>();
        iniciarListas();
    }

    private void AdicionarMusica()  {
        if (musicaDao.AdicionarMusica(musica)) {
            if (VerificadorUtil.estaNulo(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_session"))) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user_session", musica);
            }
            iniciarDadosPaginaPlaylist();
            MensagemUtil.sucesso("Musica adicionada com sucesso");
            fecharDialogAlterarCadastrarMusica();
            musica = new Musica();
            condicaoTelaCadastarMusicaAtualizar = null;
        } else {
            MensagemUtil.erro("Erro ao adicionar Musica");
        }
    }

    public void redirecionarMusicaAtualizar(){
        condicaoTelaCadastarMusicaAtualizar = CONDICAO_ATUALIZAR;
        PagesUtil.abrirDialogAtualizado("AlterarCadastrarMusica");
    }

    public void fecharDialogAlterarCadastrarMusica(){
        musica = new Musica();
        PagesUtil.fecharDialog("dlgAlterarCadastrarMusica");
    }

    private void alterarListaDeMusica() {
        if (musicaDao.alterarMusica(musica)) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("music_update");
            condicaoTelaCadastarMusicaAtualizar = null;
            iniciarDadosPaginaPlaylist();
            MensagemUtil.sucesso("Alterada com sucesso");
            fecharDialogAlterarCadastrarMusica();
        } else {
            MensagemUtil.erro("Erro ao alterar ");
        }
    }

    public void excluirMusica() throws IOException {
        if (musicaDao.deletarMusica(musica.getId())) {
            MensagemUtil.sucesso("Excluido com sucesso");
            musica = new Musica();
            iniciarDadosPaginaPlaylist();
        } else {
            MensagemUtil.erro("Erro ao excluir !");
        }
    }

    public String retornarGeneroMusical(String sigla) {
        if (VerificadorUtil.naoEstaNuloOuVazio(sigla)) {
            return GenerosMusicais.retonarGeneroMusical(sigla);
        } else {
            return null;
        }
    }

    public void redirecionarCadastrarMusica() {
        musica = new Musica();
        condicaoTelaCadastarMusicaAtualizar = CONDICAO_CADASTRAR;
        PagesUtil.abrirDialogAtualizado("AlterarCadastrarMusica");
    }

    public void iniciarListas() {
        listaComGeneros = Arrays.asList(GenerosMusicais.values());
    }

    public void definirCadastrarMusicaAtualizar() {
        if (isMusicUpdate()) {
            musica = (Musica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("music_update");
            condicaoTelaCadastarMusicaAtualizar = CONDICAO_ATUALIZAR;
        } else {
            condicaoTelaCadastarMusicaAtualizar = CONDICAO_CADASTRAR;
        }
        iniciarListas();
    }

    private String retornarCondicaoTelaCadastarAtualizar() {
        if (isMusicUpdate()) {
            return CONDICAO_ATUALIZAR;
        } else {
            return CONDICAO_CADASTRAR;
        }
    }

    public boolean isMusicUpdate() {
        return VerificadorUtil.naoEstaNulo(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("music_update"));
    }


    public void validarCondicaoGravacaoDaMusica() throws IOException {
        if (musica.getId() == null) {
            AdicionarMusica();
        } else {
            alterarListaDeMusica();
        }
    }

    public void iniciarDadosPaginaPlaylist() {
        listaComMusicas = musicaDao.retornarPlaylist();
    }

    public void filtroDeMusicasPorTitulo(){
        listaComMusicas = musicaDao.retornarPlaylistFiltradaPorTitulo(filtroTitulo);
    }

    public List<GenerosMusicais> getListaComGeneros() {
        return listaComGeneros;
    }

    public void setListaComGeneros(List<GenerosMusicais> listaComGeneros) {
        this.listaComGeneros = listaComGeneros;
    }

    public Musica getMusica() {
        return musica;
    }

    public void setMusica(Musica musica) {
        this.musica = musica;
    }

    public ArrayList<Musica> getListaComMusicas() {
        return listaComMusicas;
    }

    public void setListaComMusicas(ArrayList<Musica> listaComMusicas) {
        this.listaComMusicas = listaComMusicas;
    }

    public String getCondicaoTelaCadastarMusicaAtualizar() {
        return condicaoTelaCadastarMusicaAtualizar;
    }

    public void setCondicaoTelaCadastarMusicaAtualizar(String condicaoTelaCadastarMusicaAtualizar) {
        this.condicaoTelaCadastarMusicaAtualizar = condicaoTelaCadastarMusicaAtualizar;
    }

    public String getFiltroTitulo() {
        return filtroTitulo;
    }

    public void setFiltroTitulo(String filtroTitulo) {
        this.filtroTitulo = filtroTitulo;
    }
}

