package br.com.projetoweb.controller;

import br.com.projetoweb.dao.MusicaDAO;
import br.com.projetoweb.enums.GenerosMusicais;
import br.com.projetoweb.model.Musica;
import br.com.projetoweb.util.MensagemUtil;
import br.com.projetoweb.util.PagesUtil;
import br.com.projetoweb.util.VerificadorUtil;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static br.com.projetoweb.shared.Constantes.CONDICAO_ATUALIZAR;
import static br.com.projetoweb.shared.Constantes.CONDICAO_CADASTRAR;

@ViewScoped
@ManagedBean(name = "musicaMB")
public class MusicaController {
    private List<GenerosMusicais> listaComGeneros;
    private MusicaDAO musicaDao;
    private Musica musica;
    private ArrayList<Musica> listaComMusicas;
    private String condicaoTelaCadastarMusicaAtualizar;

    public MusicaController() {
        musica = new Musica();
        musicaDao = new MusicaDAO();
        listaComMusicas = new ArrayList<>();
        listaComGeneros = new ArrayList<>();
    }

    private void AdicionarMusica() throws IOException {
        if (musicaDao.AdicionarMusica(musica)) {
            if (VerificadorUtil.estaNulo(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_session"))) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user_session", musica);
            }
            //PagesUtil.redirectPage("principal");
            MensagemUtil.sucesso("Musica adicionada com sucesso");
            musica = new Musica();
        } else {
            MensagemUtil.erro("Erro ao adicionar Musica");
        }
    }

    public void redirecionarMusicaAtualizar() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("music_update", musica);
        PagesUtil.redirectPage("cadastrarmusica");
    }

    private void alterarListaDeMusica() throws IOException {
        if (musicaDao.alterarMusica(musica)) {
            //PagesUtil.redirectPage("principal");
            MensagemUtil.sucesso("alterada com sucesso");
            musica = new Musica();
        } else {
            MensagemUtil.erro("Erro ao alterar ");
        }
    }


    public void excluirMusica() throws IOException {
        if (musicaDao.deletarMusica(musica.getId())) {
            MensagemUtil.sucesso("excluido com sucesso");
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

    public void redirecionarCadastrarMusica() throws IOException {
        PagesUtil.redirectPage("cadastrarmusica");
    }

    private void iniciarListas() {
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
        if (retornarCondicaoTelaCadastarAtualizar().equals(CONDICAO_CADASTRAR))
            AdicionarMusica();

        else
            alterarListaDeMusica();
    }

    public void iniciarDadosPaginaPlaylist() {
        listaComMusicas = musicaDao.retornarPlaylist();

    }


    public List<GenerosMusicais> getListaComGeneros() {
        return listaComGeneros;
    }

    public void setListaComGeneros(List<GenerosMusicais> listaComGeneros) {
        this.listaComGeneros = listaComGeneros;
    }

    public MusicaDAO getMusicaDao() {
        return musicaDao;
    }

    public void setMusicaDao(MusicaDAO musicaDao) {
        this.musicaDao = musicaDao;
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
}

