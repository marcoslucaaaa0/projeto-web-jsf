package br.com.projetoweb.shared.Queries;

public class QueriesMusica {
    public static final String QUERY_CONSULTAR_RETORNAR_PLAYLIST =
            "SELECT id, title, url_file, group_name, genero " +
                    "FROM public.playlist";

    public static final String QUERY_INSERIR_ADICIONAR_MUSICA =
            "INSERT INTO public.playlist(title, url_file, group_name, genero) " +
                    "VALUES (?, ?, ?, ?)";
    public static final String QUERY_ALTERAR_LISTA_DE_MUSICA =
            "UPDATE public.playlist " +
                    "SET title=?, url_file=?, group_name=?, genero = ? " +
                    "WHERE id = ?";
    public static final String QUERY_EXCLUIR_MUSICA =
            "DELETE FROM public.playlist " +
                    "WHERE id = ?";

}


