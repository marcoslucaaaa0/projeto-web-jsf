package br.com.projetoweb.util;

import org.primefaces.context.RequestContext;

import javax.faces.context.FacesContext;
import java.io.IOException;

public class PagesUtil {
    public static void redirectPage(String page) throws IOException {
        FacesContext context  = FacesContext.getCurrentInstance();
        String url = context.getExternalContext().getRequestContextPath();
        context.getExternalContext().redirect(url+"/pages/"+page+".xhtml");
    }

    public static void fecharDialog(String dialog) {
        getRequestContext().execute("PF('" + dialog + "').hide();");
    }

    private static RequestContext getRequestContext() {
        return RequestContext.getCurrentInstance();
    }


    public static void atualizarComponente(String componente){
        getRequestContext().update(componente);
    }

    public static void abrirDialog(String dialog) {
        getRequestContext().execute("PF('" + dialog + "').show();");
    }

    public static void abrirDialogAtualizado(String nomeDialog){
        atualizarComponente("form"+nomeDialog);
        abrirDialog("dlg"+nomeDialog);
    }
}
