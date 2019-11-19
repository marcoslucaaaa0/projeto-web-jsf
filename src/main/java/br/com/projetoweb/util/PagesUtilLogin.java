package br.com.projetoweb.util;


import org.primefaces.context.RequestContext;

import javax.faces.context.FacesContext;
import java.io.IOException;

public class PagesUtilLogin {
    public static void redirecionarLogin(String page) throws IOException {
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
}
