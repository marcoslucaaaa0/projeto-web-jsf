package br.com.projetoweb.shared.Queries;

public class QueriesMusica {
    public static final String QUERY_CONSULTAR_RETORNAR_PLAYLIST =
            "SELECT id, title, url_file, genero " +
                    "FROM public.usuarios where ativo = true ORDER BY id";

    public static final String QUERY_INSERIR_ADICIONAR_MUSICA =
            "INSERT INTO public.playlist" +
                    " id, title, url_file, genero)" +
                    "VALUES (?, ?, ?, ?)";
    public static final String QUERY_ALTERAR_LISTA_DE_MUSICA =
            "UPDATE public.playlist " +
                    "SET id=?, title=?, url_file=?, genero=? " +
                    "WHERE <condition>";
    public static final String QUERY_EXCLUIR_MUSICA =
            "DELETE FROM public.playlist" +
                    "WHERE <condition>";

}


