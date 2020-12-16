package br.com.projetoweb.dao;

import br.com.projetoweb.factory.ConnectionFactory;
import br.com.projetoweb.model.Musica;
import br.com.projetoweb.model.Usuario;
import br.com.projetoweb.util.StringUtil;
import br.com.projetoweb.util.VerificadorUtil;

import java.sql.*;
import java.util.ArrayList;

import static br.com.projetoweb.shared.Queries.QueriesMusica.*;
import static br.com.projetoweb.shared.Queries.QueriesUsuario.QUERY_INSERIR_GRAVAR_USUARIO;


public class MusicaDAO {
    private Connection conexao = null;

    public ArrayList<Musica> retornarPlaylist() {
        ArrayList<Musica> playList = new ArrayList<>();
        conexao = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = conexao.prepareStatement(QUERY_CONSULTAR_RETORNAR_PLAYLIST);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Musica music = new Musica();
                music.setId(rs.getInt("id"));
                music.setTitle(rs.getString("title"));
                music.setUrl_file(rs.getString("url_file"));
                music.setNome_grupo(rs.getString("group_name"));
                music.setGenero(rs.getString("genero"));

                playList.add(music);
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
        return playList;
    }

    public ArrayList<Musica> retornarPlaylistFiltradaPorTitulo(String titulo) {
        ArrayList<Musica> playList = new ArrayList<>();
        conexao = ConnectionFactory.getConnection();
        try {
            PreparedStatement ps = conexao.prepareStatement(QUERY_CONSULTAR_RETORNAR_PLAYLIST + CONDICAO_FILTRO_MUSICA);
            ps.setString(1, "%"+titulo+"%");
            ps.setString(2, "%"+titulo+"%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Musica music = new Musica();
                music.setId(rs.getInt("id"));
                music.setTitle(rs.getString("title"));
                music.setUrl_file(rs.getString("url_file"));
                music.setNome_grupo(rs.getString("group_name"));
                music.setGenero(rs.getString("genero"));
                playList.add(music);
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
        return playList;
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

    public boolean alterarMusica(Musica musica) {
        boolean salvou = false;
        conexao = ConnectionFactory.getConnection();
        PreparedStatement ps;
        try {
            ps = conexao.prepareStatement(QUERY_ALTERAR_LISTA_DE_MUSICA);
            mapearPrepareStatementAdicionar(musica, ps);
            ps.setInt(5, musica.getId());
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
    public boolean deletarMusica(int idMusica) {
        boolean salvou = false;
        conexao = ConnectionFactory.getConnection();
        PreparedStatement ps;
        try {
            ps = conexao.prepareStatement(QUERY_EXCLUIR_MUSICA);
            ps.setInt(1, idMusica);
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
        if (VerificadorUtil.naoEstaNuloOuVazio(musica.getTitle())) {
            ps.setString(1, musica.getTitle());
        } else {
            ps.setNull(1, java.sql.Types.NULL);
        }
        if (VerificadorUtil.naoEstaNulo(musica.getUrl_file())) {
            ps.setString(2, musica.getUrl_file());
        } else {
            ps.setNull(2, java.sql.Types.NULL);
        }
        if (VerificadorUtil.naoEstaNuloOuVazio(musica.getNome_grupo())) {
            ps.setString(3, musica.getNome_grupo());
        } else {
            ps.setNull(3, java.sql.Types.NULL);
        }
        if (VerificadorUtil.naoEstaNuloOuVazio(musica.getGenero())) {
            ps.setString(4, musica.getGenero());
        } else {
            ps.setNull(4, java.sql.Types.NULL);
        }

    }
}
