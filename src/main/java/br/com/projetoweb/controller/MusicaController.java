package br.com.projetoweb.controller;

import br.com.projetoweb.dao.MusicaDAO;
import br.com.projetoweb.enums.GenerosMusicais;
import br.com.projetoweb.model.Musica;
import br.com.projetoweb.model.Usuario;
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

@ViewScoped
@ManagedBean(name = "musicaMB")
public class MusicaController {
    private List<GenerosMusicais> listaComMusicas;
    private MusicaDAO musicaDao;
    private Musica musica;
    private List<GenerosMusicais> playList;

    public MusicaController(ArrayList<GenerosMusicais> listaComMusicas, MusicaDAO musicaDao, Musica musica, List<GenerosMusicais> playList) {
        this.listaComMusicas = listaComMusicas;
        this.musicaDao = musicaDao;
        this.musica = musica;
        this.playList = playList;
        musicaDao=new MusicaDAO();
        musica = new Musica();
    }
    private void selecionarPlayList() {
       listaComMusicas = Arrays.asList(GenerosMusicais.values());
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

    private void alterarListaDeMusica() throws IOException {
        if (musicaDao.alterarListaDeMusica(musica)) {
            //PagesUtil.redirectPage("principal");
            MensagemUtil.sucesso("alterada com sucesso");
           musica = new Musica();
        } else {
            MensagemUtil.erro("Erro ao alterar ");
        }
    }

    public void excluirMusica() throws IOException {
        if (musicaDao.deletarMusica(musica.getUrl_file())) {
            MensagemUtil.sucesso("excluido com sucesso");
            musica = new Musica();
            PagesUtil.redirectPage("principal");
        } else {
            MensagemUtil.erro("Erro ao excluir !");
        }
    }

  }

