package br.com.flordeliz.entidades.jsf;

import br.com.flordeliz.entidades.Itempedido;
import br.com.flordeliz.entidades.jsf.util.JsfUtil;
import br.com.flordeliz.entidades.jsf.util.JsfUtil.PersistAction;
import br.com.flordeliz.entidades.bean.ItempedidoFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("itempedidoController")
@SessionScoped
public class ItempedidoController implements Serializable {

    @EJB
    private br.com.flordeliz.entidades.bean.ItempedidoFacade ejbFacade;
    private List<Itempedido> items = null;
    private Itempedido selected;

    public ItempedidoController() {
    }

    public Itempedido getSelected() {
        return selected;
    }

    public void setSelected(Itempedido selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ItempedidoFacade getFacade() {
        return ejbFacade;
    }

    public Itempedido prepareCreate() {
        selected = new Itempedido();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/resource/Bundle").getString("ItempedidoCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/resource/Bundle").getString("ItempedidoUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/resource/Bundle").getString("ItempedidoDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Itempedido> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/resource/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/resource/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Itempedido getItempedido(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Itempedido> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Itempedido> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Itempedido.class)
    public static class ItempedidoControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ItempedidoController controller = (ItempedidoController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "itempedidoController");
            return controller.getItempedido(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Itempedido) {
                Itempedido o = (Itempedido) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Itempedido.class.getName()});
                return null;
            }
        }

    }

}
