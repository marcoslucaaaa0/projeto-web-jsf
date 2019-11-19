package br.com.projetoweb.dao;

import br.com.projetoweb.factory.ConnectionFactory;
import br.com.projetoweb.model.Musica;
import br.com.projetoweb.model.Usuario;
import br.com.projetoweb.util.StringUtil;
import br.com.projetoweb.util.VerificadorUtil;

import java.sql.*;
import java.util.ArrayList;

import static br.com.projetoweb.shared.Queries.QueriesMusica.*;



public class MusicaDAO {
    private Connection conexao = null;

    public ArrayList<Musica> retornarPlaylist() {
        ArrayList<Musica> playList = new ArrayList<>();
        conexao = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = conexao.prepareStatement(QUERY_CONSULTAR_RETORNAR_PLAYLIST);
            ResultSet pl = ps.executeQuery();
            while (pl.next()) {
                Musica music = new Musica();
                music.setGenero(pl.getString("genero"));
                music.setTitle(pl.getString("title"));
                music.setUrl_file(pl.getString("url_file"));
                retornarPlaylist().add(music);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                conexao.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return retornarPlaylist();
    }

    public boolean AdicionarMusica(Musica musica) {
        boolean salvou = false;
        conexao = ConnectionFactory.getConnection();
        PreparedStatement ps;
        try {
            ps = conexao.prepareStatement(QUERY_INSERIR_ADICIONAR_MUSICA);
            mapearPrepareStatementAdicionar(musica, ps);
            ps.executeUpdate();
            salvou = true;
            conexao.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                conexao.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return salvou;
    }

    public boolean alterarListaDeMusica(Musica musica) {
        boolean salvou = false;
        conexao = ConnectionFactory.getConnection();
        PreparedStatement ps;
        try {
            ps = conexao.prepareStatement(QUERY_ALTERAR_LISTA_DE_MUSICA);
            mapearPrepareStatementAdicionar(musica, ps);
            ps.executeUpdate();
            salvou = true;
            conexao.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                conexao.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return salvou;
    }
    public boolean deletarMusica(String email) {
        boolean salvou = false;
        conexao = ConnectionFactory.getConnection();
        PreparedStatement ps;
        try {
            ps = conexao.prepareStatement(QUERY_EXCLUIR_MUSICA);
            ps.setString(1, email);
            ps.executeUpdate();
            salvou = true;
            conexao.commit();
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                conexao.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return salvou;
    }

    private void mapearPrepareStatementAdicionar(Musica musica, PreparedStatement ps) throws SQLException {
        if (VerificadorUtil.naoEstaNuloOuVazio(musica.getGenero())) {
            ps.setString(1, musica.getGenero());
        } else {
            ps.setNull(1, java.sql.Types.NULL);
        }
        if (VerificadorUtil.naoEstaNulo(musica.getTitle())) {
            ps.setString(2, musica.getTitle());
        } else {
            ps.setNull(2, java.sql.Types.NULL);
        }
        if (VerificadorUtil.naoEstaNuloOuVazio(musica.getUrl_file())) {
            ps.setString(3, StringUtil.retirarMascara(musica.getUrl_file()));
        } else {
            ps.setNull(3, java.sql.Types.NULL);
        }

    }
}
